package com.online.bookstore.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.online.bookstore.enums.BookType;
import com.online.bookstore.enums.CurrencyType;
import com.online.bookstore.model.Book;
import com.online.bookstore.model.Customer;
import com.online.bookstore.repository.BookRepository;
import com.online.bookstore.repository.CustomerRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BookRepository bookRepository;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		if (customerRepository.findAll().isEmpty()) {
			List<Customer> customers = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				Customer c = new Customer();
				c.setFirstName("FirstName" + i);
				c.setLastName("LastName" + i);
				c.setCountry("Country" + i);
				c.setCity("City" + i);
				c.setAddress("Address" + i);
				c.setZip("Zip" + i);
				c.setLoyaltyPoints(BigDecimal.ZERO);
				customers.add(c);
			}
			customerRepository.saveAll(customers);
		}

		if (bookRepository.findAll().isEmpty()) {
			List<Book> books = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				Book b = new Book();
				b.setName("Name" + i);
				b.setInStock(i);
				b.setPrice(BigDecimal.valueOf(i));
				b.setCurrencyType(CurrencyType.USD);
				if (i <= 3) {
					b.setBookType(BookType.NEW_RELEASES);
				} else if (i > 3 && i <= 7) {
					b.setBookType(BookType.REGULAR);
				} else {
					b.setBookType(BookType.OLD_EDITIONS);
				}

				books.add(b);
			}
			bookRepository.saveAll(books);
		}
	}

}