/**
 * @author caijianqing, 2013-6-29 上午11:28:23
 */
package com.zcdh.mobile.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件分割工具<br>
 * 用于分割敏感的文件，将敏感文件生成无后缀名的片段，使其不能直接读取文件<br>
 * 使用顺序：<br>
 * 1. FileSplitOutputStream os = FileSplit.getOutputStream(...); 创建一个写入流<br>
 * 2. os.write(...); 使用常规方法按顺序写入被分割文件的字节<br>
 * 3. os.close(); 写入最后片段并关闭释放资源<br>
 * 4. FileSplitInputStream is = FileSplit.getInputStream(...); 创建一个读取流<br>
 * 5. is.read(...); 使用常规方法按顺序读取字节流<br>
 * 6. is.close(); 使用完毕释放资源<br>
 * 
 * @author caijianqing, 2013-6-29 下午4:18:36
 */
public class FileSplit {

	/**
	 * 文件片段大小（同时用作读写缓冲大小）<br>
	 * 一般为文件分区的分配单元的倍数，这样可以保证不产生额外的磁盘浪费
	 */
	static int blockSize = 64 * 1024;

	/**
	 * 用于生成片段文件名<br>
	 * 可以重写此方法，保证（至少利用index参数）生成唯一的文件名
	 */
	private static String getName(String arg1, String arg2, String arg3, int index) {
		int h1 = Math.abs((index + arg1 + index + arg2 + index + arg3).hashCode());
		int h2 = Math.abs((index + arg2 + index + arg1 + index + arg3).hashCode());
		int h3 = Math.abs((index + arg3 + index + arg2 + index + arg1).hashCode());
		String str = "" + h1 + h2 + h3;
		return str;
	}

	/**
	 * 创建一个分割写入流<br>
	 * 
	 * @author caijianqing, 2013-6-29 下午3:08:11
	 * @param dir 储存目录
	 * @param arg1 用于生成文件片段名称的参数1，通常为用户名
	 * @param arg2 用于生成文件片段名称的参数2，通常为用户密码
	 * @param arg3 用于生成文件片段名称的参数3，通常为文件的相对路径
	 * @return
	 */
	public static FileSplitOutputStream getOutputStream(File dir, String arg1, String arg2, String arg3) {
		return new FileSplitOutputStream(dir, arg1, arg2, arg3, null);
	}

	/**
	 * 创建一个分割写入流<br>
	 * 
	 * @author caijianqing, 2013-6-29 下午3:08:11
	 * @param dir 储存目录
	 * @param arg1 用于生成文件片段名称的参数1，通常为用户名
	 * @param arg2 用于生成文件片段名称的参数2，通常为用户密码
	 * @param arg3 用于生成文件片段名称的参数3，通常为文件的相对路径
	 * @param blockSize 分割大小（缓冲大小），读取和写入必须保持一致
	 * @return
	 */
	public static FileSplitOutputStream getOutputStream(File dir, String arg1, String arg2, String arg3, int newBlockSize) {
		return new FileSplitOutputStream(dir, arg1, arg2, arg3, newBlockSize);
	}

	/**
	 * 创建一个分割读取流<br>
	 * 
	 * @author caijianqing, 2013-6-29 下午3:08:11
	 * @param dir 储存目录
	 * @param arg1 用于生成文件片段名称的参数1，通常为用户名
	 * @param arg2 用于生成文件片段名称的参数2，通常为用户密码
	 * @param arg3 用于生成文件片段名称的参数3，通常为文件的相对路径
	 * @return
	 * @throws IOException 文件不存在时抛出IOException，注意：参数1，2，3组合不正常也会抛出该异常
	 */
	public static FileSplitInputStream getInputStream(File dir, String arg1, String arg2, String arg3) throws IOException {
		return new FileSplitInputStream(dir, arg1, arg2, arg3, null);
	}

	/**
	 * 创建一个分割读取流<br>
	 * 
	 * @author caijianqing, 2013-6-29 下午3:08:11
	 * @param dir 储存目录
	 * @param arg1 用于生成文件片段名称的参数1，通常为用户名
	 * @param arg2 用于生成文件片段名称的参数2，通常为用户密码
	 * @param arg3 用于生成文件片段名称的参数3，通常为文件的相对路径
	 * @param blockSize 分割大小（缓冲大小），读取和写入必须保持一致
	 * @return
	 * @throws IOException 文件不存在时抛出IOException，注意：参数1，2，3组合不正常也会抛出该异常
	 */
	public static FileSplitInputStream getInputStream(File dir, String arg1, String arg2, String arg3, int newBlockSize) throws IOException {
		return new FileSplitInputStream(dir, arg1, arg2, arg3, newBlockSize);
	}

	/**
	 * 设置全局的分割大小
	 * 
	 * @param blockSize 应该为磁盘格式单元大小的倍数
	 */
	public static void setBlockSize(int blockSize) {
		FileSplit.blockSize = blockSize;
	}

	/**
	 * 文件分割写入流
	 */
	public static class FileSplitOutputStream extends OutputStream {

		File dir; // 保存目录
		String arg1, arg2, arg3; // 加密参数1，参数2，文件路径标识path
		ByteBuffer buf; // 缓冲
		int blockSize = FileSplit.blockSize, count = -1; // 字节计数器
		List<FileChannel> channels = new ArrayList<FileChannel>();// 文件片段通道集合

		private FileSplitOutputStream(File dir, String arg1, String arg2, String arg3, Integer newBlockSize) {
			this.dir = dir;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			if (newBlockSize != null)
				blockSize = newBlockSize;
			buf = ByteBuffer.allocate(blockSize + 4); // 缓冲
		}

		/**
		 * 写入一个缓冲
		 * 
		 * @param buf
		 * @throws IOException
		 */
		public void write(ByteBuffer buf) throws IOException {
			while (buf.hasRemaining()) {
				write(buf.get());
			}
		}

		@Override
		public void write(int b) throws IOException {
			b = -b; // 翻转byte
			count++;
			int index = count / blockSize;
			int pos = count % blockSize;
			if (pos == 0) {
				// 分割文件
				String name = getName(arg1, arg2, arg3, index);
				File file = new File(dir, name);
				file.createNewFile();
				FileOutputStream fis = new FileOutputStream(file);
				channels.add(fis.getChannel());
				fis.close();
				// 在第一个文件头4个字节创建文件长度占位符
				if (index == 0) {
					buf.putInt(0); // 文件长度占位
					count += 4;
				}
				buf.put((byte) b);
			} else if (pos == blockSize - 1) {
				// 缓冲区已满准备写入
				buf.put((byte) b);
				flush(index);
			} else {
				// 累加到缓冲区
				buf.put((byte) b);
			}
		}

		/**
		 * 将缓冲区文件写入
		 * 
		 * @param index 文件索引号
		 * @throws IOException
		 */
		private void flush(int index) throws IOException {
			if (buf.position() > 0) {
				buf.flip();
				FileChannel fc = channels.get(index);
				fc.write(buf);
				buf.clear();
			}
		}

		/**
		 * 完成文件分割，写入最后文件的字节，关闭所有资源
		 */
		@Override
		public void close() throws IOException {
			if (count > 0) {
				// 写入最后一个片段
				flush(count / blockSize);
				// 写入文件长度
				FileChannel first = channels.get(0);
				buf.putInt(count - 3).flip();
				first.write(buf, 0);
				buf.clear();
				count = -1;
			}
			// 关闭所有通道
			Iterator<FileChannel> iter = channels.iterator();
			while (iter.hasNext()) {
				iter.next().close();
				iter.remove();
			}
		}

		/**
		 * 快捷操作：将一个输入流写入并分割
		 * 
		 * @param in
		 * @throws IOException
		 */
		public void split(InputStream in) throws IOException {
			try {
				byte[] buf = new byte[4096];
				int len;
				while ((len = in.read(buf)) != -1) {
					this.write(buf, 0, len);
				}
			} finally {
				this.close();
			}
		}

		/**
		 * 快捷操作：将一个文件分割
		 * 
		 * @param file
		 * @throws IOException
		 */
		public void split(File file) throws IOException {
			FileChannel fc = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				fc = fis.getChannel();
				ByteBuffer buf = ByteBuffer.allocate(4096);
				while ((fc.read(buf)) != -1) {
					buf.flip();
					this.write(buf); // 直接写入分割流，将自动分割文件
					buf.clear();
				}
			} finally {
				// 释放资源
				if (fc != null)
					fc.close();
				if (fis != null)
					fis.close();
				this.close();
			}
		}

		/**
		 * 快捷操作：获得一个读取流
		 * 
		 * @return
		 * @throws IOException
		 */
		public FileSplitInputStream getInputStream() throws IOException {
			return FileSplit.getInputStream(dir, arg1, arg2, arg3, blockSize);
		}

	}

	/**
	 * 文件分割读取流
	 */
	public static class FileSplitInputStream extends InputStream {

		File dir; // 保存目录
		String arg1, arg2, arg3; // 加密参数1，参数2，文件路径标识path
		ByteBuffer buf; // 缓冲
		int blockSize = FileSplit.blockSize, len = -1, count = -1; // 原文件长度，字节计数器
		List<FileChannel> channels = new ArrayList<FileChannel>();// 文件片段通道集合

		private FileSplitInputStream(File dir, String arg1, String arg2, String arg3, Integer newBlockSize) throws IOException {
			this.dir = dir;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			if (newBlockSize != null)
				blockSize = newBlockSize;
			buf = ByteBuffer.allocate(blockSize + 4); // 缓冲
			try {
				// 在第一个文件片段读取文件长度
				FileChannel firstFc = openFileChannel(0);
				firstFc.read(buf);
				buf.flip();
				len = buf.getInt() + 4;
				count = 4;
				// 打开所有文件片段
				channels.add(firstFc);
				int sum = len / blockSize + (len % blockSize == 0 ? 0 : 1);
				for (int i = 1; i < sum; i++) {
					FileChannel channel = openFileChannel(i);
					channels.add(channel);
				}
			} catch (IOException e) {
				this.close();
				throw e;
			}
		}

		/**
		 * 打开指定序号的文件片段通道
		 * @throws IOException 
		 */
		private FileChannel openFileChannel(int index) throws IOException {
			String name = getName(arg1, arg2, arg3, index);
			File file = new File(dir, name);
			if (file.isDirectory())
				throw new FileNotFoundException(dir.getAbsolutePath() + "/" + name + " 不是一个文件");
			if (!file.exists() || file.length() < 4)
				throw new FileNotFoundException(dir.getAbsolutePath() + "/" + name + " 文件小于4个字节");
			FileInputStream fis = new FileInputStream(file);
			FileChannel channel = fis.getChannel();
			fis.close();
			return channel;
		}

		/**
		 * 读取数据到字节缓冲
		 * 
		 * @param buf
		 * @return 返回读取到的字节数量，-1时为结尾
		 * @throws IOException
		 */
		public int read(ByteBuffer buf) throws IOException {
			int pos = buf.position();
			for (int i = pos; i < buf.limit(); i++) {
				int b = read();
				if (b == -1)
					break;
				buf.put((byte) b);
			}
			pos = buf.position();
			return pos == 0 ? -1 : pos;
		}

		@Override
		public int read() throws IOException {
			count++;
			if (count <= len) {
				int index = count / blockSize;
				if (buf.position() >= buf.limit()) { // 读取下一个片段
					FileChannel fc = channels.get(index);
					buf.clear();
					fc.read(buf);
					buf.flip();
				}
				// 读取缓冲字节
				int b = buf.get();
				b = -b; // 翻转byte
				if (b < 0)
					b += 256;
				return b;
			} else {
				return -1;
			}
		}

		/**
		 * 关闭所有文件片段
		 */
		@Override
		public void close() throws IOException {
			// 关闭所有通道
			Iterator<FileChannel> iter = channels.iterator();
			while (iter.hasNext()) {
				iter.next().close();
				iter.remove();
			}
		}

		/**
		 * 快捷操作：讲分割片段合并到一个文件中
		 * 
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public File saveAs(File file) throws IOException {
			FileChannel fc = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fc = fos.getChannel();
				ByteBuffer buf = ByteBuffer.allocate(4096);
				while (true) {
					// 读取到缓冲区
					if ((this.read(buf)) == -1)
						break;
					// 写入文件
					buf.flip();
					fc.write(buf);
					buf.clear();
				}
			} finally {
				// 释放资源
				if (fc != null)
					fc.close();
				if (fos != null) {
					fos.close();
				}
				this.close();
			}
			return file;

		}

	}

	public static void main(String[] args) throws Exception {
		testWrite();
		testReadAsFile();
		testReadInputStream();
	}

	/**
	 * 读取（还原）例子
	 */
	private static void testReadAsFile() throws IOException {
		File out = new File("d:/2.png"); // 还原并保存到这个文件
		// 重要提示：这里如果3个标识符不正确或者还没有分割此文件，将会抛出IOException，此时需要重新下载文件等操作
		FileSplitInputStream is = FileSplit.getInputStream(new File("d:/"), "admin", "fuckyou", "d:/1.png");
		is.saveAs(out);

	}

	/**
	 * 读取（还原）例子
	 */
	private static void testReadInputStream() throws IOException {
		FileChannel fc = null; // 输出到这个文件通道
		FileSplitInputStream is = null; // 分割读取流
		FileInputStream fis = null;
		try {
			File out = new File("D:/2.png"); // 还原并保存到这个文件
			// 还原这个文件
			// 重要提示：这里如果3个标识符不正确或者还没有分割此文件，将会抛出IOException，此时需要重新下载文件等操作
			is = FileSplit.getInputStream(new File("d:/"), "admin", "fuckyou", "d:/1.png");
			fis = new FileInputStream(out);
			fc = fis.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			while (true) {
				// 读取到缓冲区
				if ((is.read(buf)) == -1)
					break;
				// 写入文件
				buf.flip();
				fc.write(buf);
				buf.clear();
			}
		} finally {
			// 释放资源
			if (fc != null)
				fc.close();
			if (is != null)
				is.close();
			if (fis != null)
				fis.close();
		}

	}

	/**
	 * 写入（分割）例子
	 * 
	 * @throws IOException
	 */
	private static void testWrite() throws IOException {
		// 分割这个文件
		File file = new File("D:/1.png");
		// 创建分割写入流
		FileSplitOutputStream os = FileSplit.getOutputStream(new File("d:/"), "admin", "fuckyou", "d:/1.png");
		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			fis = new FileInputStream(file);
			fc = fis.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			while ((fc.read(buf)) != -1) {
				buf.flip();
				os.write(buf); // 直接写入分割流，将自动分割文件
				buf.clear();
			}
		} finally {
			// 释放资源
			if (fc != null)
				fc.close();
			if (os != null)
				os.close();
			if (fis != null)	
				fis.close();
		}
	}
}
