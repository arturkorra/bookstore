package com.online.bookstore.service;

import java.math.BigDecimal;
import java.util.List;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.CustomerObj;

public interface CustomerService {

	BigDecimal getLoyaltyPoints(Long customerId) throws BookStoreException;

	List<CustomerObj> findAllCustomers() throws BookStoreException;

}
