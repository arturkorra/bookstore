package com.online.bookstore.enums;

public enum CurrencyType {

	USD("USD"), EUR("EUR");

	private String msg;

	private CurrencyType(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
