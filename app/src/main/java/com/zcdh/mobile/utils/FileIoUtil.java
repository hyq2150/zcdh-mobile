package com.zcdh.mobile.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;

import android.os.Environment;

import com.zcdh.mobile.framework.exceptions.FileException;



public class FileIoUtil {

	public static String write(String path, InputStream inputStream) throws IOException{
		File storeFile = new File(path);
		FileOutputStream output = new FileOutputStream(storeFile);
        byte b[] = new byte[1024];  
        int j = 0;  
        while( (j = inputStream.read(b))!=-1){  
            output.write(b,0,j);  
        }  
        output.flush();  
        output.close();
        if(storeFile.exists()){
        	return storeFile.getPath();
        }else{
        	return null;
        }
	}
	public static File writeExt(InputStream inputStream, String savePath) throws FileException{
        
			File targetFile = new File(savePath);
//			OutputStreamWriter osw;

			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					FileOutputStream output = new FileOutputStream(targetFile);
			        byte b[] = new byte[1024];  
			        int j = 0;  
			        while( (j = inputStream.read(b))!=-1){  
			            output.write(b,0,j);  
			        }  
			        output.flush();  
			        output.close();
			        return targetFile;
					
				} else {
//					osw = new OutputStreamWriter(new FileOutputStream(targetFile));
					FileOutputStream output = new FileOutputStream(targetFile);
			        byte b[] = new byte[1024];  
			        int j = 0;  
			        while( (j = inputStream.read(b))!=-1){  
			            output.write(b,0,j);  
			        }  
			        output.flush();  
			        output.close();
			        return targetFile;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new FileException("文件写入错误！");
			}
	}
	
	public static String readfile(String filename) throws FileException{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断内存卡是否加载
		    File targetFile=new File(filename);
		    String readedStr="";
		    readedStr = readfile(targetFile);
		    return readedStr;
		}else{
			throw new FileException("未发现SD卡！");
		}
		
	}
	
	public static Object readfileToObject(String filename) throws FileException, OptionalDataException, ClassNotFoundException, IOException{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断内存卡是否加载
		    File targetFile=new File(filename);
		    
		    Object object=readfileToObject(targetFile);
		    return object;
		}else{
			throw new FileException("未发现SD卡！");
		}
		
	}
	
	public  static String readfile(File targetFile) throws FileException{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
		    String readedStr="";
			 try{
				if(!targetFile.exists()){
					throw new FileException("文件不存在");
				}else{
					 InputStream in = new BufferedInputStream(new FileInputStream(targetFile));
					 BufferedReader br= new BufferedReader(new InputStreamReader(in, "UTF-8"));
					 String tmp;
					 
					 while((tmp=br.readLine())!=null){
						 readedStr+=tmp;
					 }
					 br.close();
					 in.close();
					 
					 return readedStr;
				}
			 } catch (Exception e) {
				 	throw new FileException("文件读取错误！"); 	
			 }
		}else{
			throw new FileException("未发现SD卡！");
		}
		
	}

	public  static Object readfileToObject(File targetFile) throws FileException{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
		    Object object="";
			 try{
				if(!targetFile.exists()){
					throw new FileException("文件不存在");
				}else{
					ObjectInputStream  in = new ObjectInputStream (new FileInputStream(targetFile));
					object=in.readObject();
					in.close();
					return object;
				}
			 } catch (Exception e) {
				 	throw new FileException("文件读取错误！"); 	
			 }
		}else{
			throw new FileException("未发现SD卡！");
		}
		
	}
	
	public  static File writefile(String foldername, String fileName,
			Object filecontent) throws FileException {

			File folder = new File(foldername);

			if (folder == null || !folder.exists()) {
				folder.mkdirs();
			}

			File targetFile = new File(foldername + fileName);
			ObjectOutputStream  osw;

			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					osw = new ObjectOutputStream (new FileOutputStream(
							targetFile));
					osw.writeObject(filecontent);
					osw.close();
				} else {
					osw = new ObjectOutputStream (new FileOutputStream(
							targetFile));
					osw.writeObject(filecontent);
					osw.close();
				}
				return targetFile;
			} catch (Exception e) {

				throw new FileException("文件写入错误！");
			}
		

	}
	
	public  static File writefile(String foldername, String fileName,
			String filecontent) throws FileException {
			File targetFile=null;
		
			File folder = new File(foldername);

			if (folder == null || !folder.exists()) {
				folder.mkdirs();
			}

			 targetFile = new File(foldername + fileName);
			OutputStreamWriter osw;

			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile), "utf-8");
					osw.write(filecontent);
					osw.close();
				} else {
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile), "utf-8");
					osw.write(filecontent);
					osw.close();
				}
			} catch (Exception e) {

				throw new FileException("文件写入错误！");
			}
		return targetFile;
		
	}
	
	public  static void writefileExt(String foldername, String fileName,
			String filecontent) throws FileException {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File folder = new File(foldername);

			if (folder == null || !folder.exists()) {
				folder.mkdirs();
			}

			File targetFile = new File(foldername + fileName);
			OutputStreamWriter osw;

			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile), "utf-8");
					osw.write(filecontent);
					osw.close();
				} else {
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile, true), "utf-8");
					osw.write("\n" + filecontent);
					osw.flush();
					osw.close();
				}
			} catch (Exception e) {

				throw new FileException("文件写入错误！");
			}
		} else {

			throw new FileException("未发现SD卡！");
		}

	}
	
	public  static File[] listFiles (String filepath) throws FileException {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File folder = new File(filepath);
			if (folder == null || !folder.exists()) {
				throw new FileException(filepath+"目录不存在！");
			}
			return folder.listFiles();
			
		} else {

			throw new FileException("未发现SD卡！");
		}


	}
	
	public  static boolean delFile(String filename ) { 
		File file = new File( filename); 
		return file.delete(); 
	} 
	
	public  static boolean delFile(File file ) { 
		return file.delete(); 
	} 
	
	/**
	 * @param filename
	 * @return
	 */
	public  static boolean isFileExist(String filename ) { 
		File file = new File( filename); 
		return file.exists(); 
	} 
	
	public  static boolean isFileExist(File file ) { 
		return file.exists(); 
	} 
 
	
	/**
	 * 获取文件大小
	 * @param f 指定文件
	 * @return
	 * @throws Exception
	 * @author peisonghai, 2013-5-28 下午5:13:19
	 */
	public static long getFileSizes(File f) throws Exception{
        long s=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
           s= fis.available();
           fis.close();
        } 
        return s;
    }
	
	public static String createDir(String filepath){
		try{
			File dir = new File(filepath);
			if(!dir.exists()){
				dir.mkdirs();
			}
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return filepath;
	}	
	
	public static byte[] read(File file) throws IOException{

	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if ( ous != null ) 
	                 ous.close();
	        } catch ( IOException e) {
	        }

	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}

}
