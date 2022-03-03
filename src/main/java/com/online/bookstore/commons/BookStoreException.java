package com.online.bookstore.commons;

import com.online.bookstore.enums.BookStoreErrors;

public class BookStoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1109781229672784810L;

	private final Integer code;

	public BookStoreException(Exception e) {
		super(e);
		this.code = BookStoreErrors.GENERIC_ERROR.getCode();
	}

	public BookStoreException(Integer code) {
		super();
		this.code = code;
	}

	public BookStoreException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public BookStoreException(Throwable cause, Integer code) {
		super(cause);
		this.code = code;
	}

	public Integer getCode() {
		return this.code;
	}

}
