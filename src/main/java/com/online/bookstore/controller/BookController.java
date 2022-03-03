package com.online.bookstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.BillObj;
import com.online.bookstore.json.BookObj;
import com.online.bookstore.json.CartObj;
import com.online.bookstore.service.BookService;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {

	public static final Logger logger = LoggerFactory.getLogger(BookController.class);
	@Autowired
	private BookService bookService;

	@GetMapping
	@CrossOrigin
	public ResponseEntity<List<BookObj>> getAvailableBooksForPurchase(HttpServletRequest request)
			throws BookStoreException {
		logger.info("All Available Books For Purchase START");
		List<BookObj> books = bookService.findAllBooksInStock();
		logger.info("All Available Books For Purchase END {}", books);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@PostMapping(value = { "/{customerId}" })
	public ResponseEntity<BillObj> buyBooks(@RequestBody @Valid List<CartObj> cartsObj,
			@PathVariable("customerId") @NotNull Long customerId, HttpServletRequest request)
			throws BookStoreException {
		logger.info("Purchase books {} cutomer {} START", cartsObj, customerId);
		BillObj billObj = bookService.buyBooks(cartsObj, customerId);
		logger.info("Purchase bill {} cutomer {} END", billObj, customerId);
		return new ResponseEntity<>(billObj, HttpStatus.OK);
	}

	@PostMapping(value = { "buyWithLoyaltyPoints/{customerId}" })
	public ResponseEntity<BillObj> buyWithLoyaltyPoints(@RequestBody @Valid CartObj cartObj,
			@PathVariable("customerId") @NotNull Long customerId, HttpServletRequest request)
			throws BookStoreException {
		logger.info("Purchase With LoyaltyPoints {} cutomer {} START", cartObj, customerId);
		BillObj billObj = bookService.buyWithLoyaltyPoints(cartObj, customerId);
		logger.info("Purchase bill {} cutomer {} END", billObj, customerId);
		return new ResponseEntity<>(billObj, HttpStatus.OK);
	}

}
