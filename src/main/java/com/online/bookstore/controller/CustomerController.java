package com.online.bookstore.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.CustomerObj;
import com.online.bookstore.service.CustomerService;

@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {

	public static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	private CustomerService customerService;

	@GetMapping(value = { "/loyaltyPoints/{id}" })
	public ResponseEntity<BigDecimal> getLoyaltyPointsByCustomerId(HttpServletRequest request,
			@PathVariable("id") @NotNull Long id) throws BookStoreException {
		logger.info("Get Customer loyalty points by ID START {}", id);
		BigDecimal loyaltyPoints = customerService.getLoyaltyPoints(id);
		logger.info("Get Customer loyalty points by ID END {} {}", id, loyaltyPoints);
		return new ResponseEntity<>(loyaltyPoints, HttpStatus.OK);
	}

	@GetMapping
	@CrossOrigin
	public ResponseEntity<List<CustomerObj>> getAllCustomer(HttpServletRequest request) throws BookStoreException {
		logger.info("Get All Customer START");
		List<CustomerObj> customers = customerService.findAllCustomers();
		logger.info("Get All Customer END {}", customers);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

}
