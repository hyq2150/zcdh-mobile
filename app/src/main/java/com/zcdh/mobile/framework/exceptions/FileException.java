package com.zcdh.mobile.framework.exceptions;

/**
 * @author danny
 *文件错误的异常类
 */
public class FileException extends Exception {

	private static final long serialVersionUID = 1420536791936124535L;

	public FileException() {
		super();
	}

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FileException(Throwable cause) {
		super(cause);
	}

}
