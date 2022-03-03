package com.online.bookstore.enums;

public enum BookStoreErrors {

	GENERIC_ERROR(9999), UNAUTHORIZED(9900), VALIDATION_ERROR(6000), CUSTOMER_NOT_EXIST(7000), NO_CUSTOMERS(7100),
	NO_BOOKS(8000), BOOK_NOT_EXIST(8100), OUT_OFF_STOCK(8200), NO_POINTS(8300), NEW_RELEASES(8400);

	private int code;

	private BookStoreErrors(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
