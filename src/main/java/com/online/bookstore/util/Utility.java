package com.online.bookstore.util;

import java.math.BigDecimal;
import java.util.Map;

import com.online.bookstore.enums.BookType;
import com.online.bookstore.json.CartObj;
import com.online.bookstore.model.Book;

public class Utility {

	private Utility() {
		throw new IllegalStateException("Utility class");
	}

	public static BigDecimal calculatePrice(Map<CartObj, Book> allBooksToPurchase) {

		BigDecimal totalPrice = BigDecimal.ZERO;
		// calculate total price and discounts for all products
		for (CartObj cart : allBooksToPurchase.keySet()) {
			Book b = allBooksToPurchase.get(cart);
			if (allBooksToPurchase.size() >= 3 && b.getBookType().equals(BookType.REGULAR)) {
				// cart size>=3 and Book type regular discount 10%
				totalPrice = totalPrice.add(b.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))
						.multiply(BigDecimal.valueOf(0.9)));
			} else if (allBooksToPurchase.size() >= 3 && b.getBookType().equals(BookType.OLD_EDITIONS)) {
				// cart size>=3 and Book type old discount 25%
				totalPrice = totalPrice.add(b.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))
						.multiply(BigDecimal.valueOf(0.75)));
			} else if (b.getBookType().equals(BookType.REGULAR)) {
				// cart size<3 and Book type regular discount 0%
				totalPrice = totalPrice.add(b.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
			} else if (b.getBookType().equals(BookType.OLD_EDITIONS)) {
				// cart size<3 and Book type old discount 20%
				totalPrice = totalPrice.add(b.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))
						.multiply(BigDecimal.valueOf(0.80)));
			} else if (b.getBookType().equals(BookType.NEW_RELEASES)) {
				// cart size<3 and Book type new discount 0%
				totalPrice = totalPrice.add(b.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
			}

		}
		return totalPrice;
	}

}
