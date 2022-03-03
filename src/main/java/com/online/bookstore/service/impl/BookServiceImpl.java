package com.online.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.BillObj;
import com.online.bookstore.json.BookObj;
import com.online.bookstore.json.CartObj;
import com.online.bookstore.model.Book;
import com.online.bookstore.model.Customer;
import com.online.bookstore.repository.BookRepository;
import com.online.bookstore.repository.CustomerRepository;
import com.online.bookstore.service.BookService;
import com.online.bookstore.util.BookStoreExceptionFactory;
import com.online.bookstore.util.Utility;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	public static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BookRepository bookRepository;

	@Override
	public List<BookObj> findAllBooksInStock() throws BookStoreException {
		List<Book> books = bookRepository.findAllBooksInStock();
		if (books.isEmpty()) {
			throw BookStoreExceptionFactory.noBooksAviableForPurchase();
		}
		List<BookObj> booksObj = new ArrayList<>();
		for (Book b : books) {
			BookObj bObj = new BookObj();
			bObj.setId(b.getId());
			bObj.setName(b.getName());
			bObj.setInStock(b.getInStock());
			bObj.setPrice(b.getPrice());
			bObj.setBookType(b.getBookType());
			booksObj.add(bObj);
		}
		return booksObj;
	}

	@Override
	public BillObj buyBooks(List<CartObj> cartsObj, Long customerId) throws BookStoreException {
		// check if Customer exist else throw Exception
		Customer c = customerRepository.findById(customerId).orElseThrow(BookStoreExceptionFactory::customerNotFound);

		Map<CartObj, Book> allBooksToPurchase = new HashMap<>();
		for (CartObj cartObj : cartsObj) {
			// check for the book
			Book b = bookRepository.findById(cartObj.getBookId()).orElseThrow(BookStoreExceptionFactory::bookNotFound);

			if (b.getInStock() < cartObj.getQuantity()) {
				throw BookStoreExceptionFactory.outOfStock(b.getInStock());
			} else {
				// update book quantity
				b.setInStock(b.getInStock() - cartObj.getQuantity());
				bookRepository.save(b);
			}

			allBooksToPurchase.put(cartObj, b);
		}
		// update customer points
		c.setLoyaltyPoints(c.getLoyaltyPoints().add(BigDecimal.valueOf(allBooksToPurchase.size())));
		c = customerRepository.save(c);

		BillObj bill = new BillObj();
		bill.setClientId(customerId);
		bill.setTotalPrice(Utility.calculatePrice(allBooksToPurchase));
		bill.setLoyaltyPoints(c.getLoyaltyPoints());

		return bill;

	}

	@Override
	public BillObj buyWithLoyaltyPoints(CartObj cartObj, Long customerId) throws BookStoreException {
		// check if Customer exist else throw Exception
		Customer c = customerRepository.findById(customerId).orElseThrow(BookStoreExceptionFactory::customerNotFound);
		// check points even if client request more than one book
		if (c.getLoyaltyPoints().intValue() < cartObj.getQuantity() * 10) {
			throw BookStoreExceptionFactory.notEnoughPoints(c.getLoyaltyPoints().intValue());
		}
		// check for the book
		Book b = bookRepository.findById(cartObj.getBookId()).orElseThrow(BookStoreExceptionFactory::bookNotFound);
		if (b.getInStock() < cartObj.getQuantity()) {
			throw BookStoreExceptionFactory.outOfStock(b.getInStock());
		} else {
			// update book quantity
			b.setInStock(b.getInStock() - cartObj.getQuantity());
			bookRepository.save(b);
		}

		// update customer points
		c.setLoyaltyPoints(c.getLoyaltyPoints().subtract(BigDecimal.valueOf(cartObj.getQuantity() * 10)));
		c = customerRepository.save(c);

		BillObj bill = new BillObj();
		bill.setClientId(customerId);
		bill.setTotalPrice(BigDecimal.ZERO);
		bill.setLoyaltyPoints(c.getLoyaltyPoints());

		return bill;
	}

}
