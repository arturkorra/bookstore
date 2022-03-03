package com.online.bookstore.util;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.enums.BookStoreErrors;

public class BookStoreExceptionFactory {

	private BookStoreExceptionFactory() {
		throw new IllegalStateException("Utility class");
	}

	public static BookStoreException noBooksAviableForPurchase() {
		return new BookStoreException("No books found available for purchase", BookStoreErrors.NO_BOOKS.getCode());
	}

	public static BookStoreException bookNotFound() {
		return new BookStoreException("Book does not exist", BookStoreErrors.BOOK_NOT_EXIST.getCode());
	}

	public static BookStoreException customerNotFound() {
		return new BookStoreException("Customer does not exist", BookStoreErrors.CUSTOMER_NOT_EXIST.getCode());
	}

	public static BookStoreException noCustomerAviable() {
		return new BookStoreException("No available customer", BookStoreErrors.NO_CUSTOMERS.getCode());
	}

	public static BookStoreException outOfStock(int q) {
		return new BookStoreException("Out of stock Max quantity:" + q, BookStoreErrors.OUT_OFF_STOCK.getCode());
	}

	public static BookStoreException notEnoughPoints(int p) {
		return new BookStoreException("Not Enough Points Your points:" + p + "Min points:10",
				BookStoreErrors.NO_POINTS.getCode());
	}

}