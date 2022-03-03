package com.online.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.json.CustomerObj;
import com.online.bookstore.model.Customer;
import com.online.bookstore.repository.CustomerRepository;
import com.online.bookstore.service.CustomerService;
import com.online.bookstore.util.BookStoreExceptionFactory;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	public static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public BigDecimal getLoyaltyPoints(Long customerId) throws BookStoreException {
		Optional<Customer> c = customerRepository.findById(customerId);
		if (c.isPresent()) {
			return c.get().getLoyaltyPoints();
		} else {
			throw BookStoreExceptionFactory.customerNotFound();
		}
	}

	@Override
	public List<CustomerObj> findAllCustomers() throws BookStoreException {
		List<Customer> customers = customerRepository.findAll();
		if (customers.isEmpty()) {
			throw BookStoreExceptionFactory.noCustomerAviable();
		}
		List<CustomerObj> customerObj = new ArrayList<>();
		for (Customer c : customers) {
			CustomerObj cObj = new CustomerObj();
			cObj.setId(c.getId());
			cObj.setFirstName(c.getFirstName());
			cObj.setLastName(c.getLastName());
			cObj.setCountry(c.getCountry());
			cObj.setCity(c.getCity());
			cObj.setAddress(c.getAddress());
			cObj.setZip(c.getZip());
			cObj.setLoyaltyPoints(c.getLoyaltyPoints());
			customerObj.add(cObj);
		}
		return customerObj;

	}

}
