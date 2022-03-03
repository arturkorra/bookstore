package com.online.bookstore.service;

import java.util.List;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.BillObj;
import com.online.bookstore.json.BookObj;
import com.online.bookstore.json.CartObj;

public interface BookService {

	public List<BookObj> findAllBooksInStock() throws BookStoreException;

	public BillObj buyBooks(List<CartObj> cartsObj, Long customerId) throws BookStoreException;

	public BillObj buyWithLoyaltyPoints(CartObj cartObj, Long customerId) throws BookStoreException;

}
