package com.online.bookstore.enums;

public enum BookType {

	NEW_RELEASES("New_Releases"), REGULAR("Regular"), OLD_EDITIONS("Old_Editions");

	private String msg;

	private BookType(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
