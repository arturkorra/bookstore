package com.online.bookstore.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.online.bookstore.model.Book;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query(value = "SELECT DISTINCT(b) FROM Book b where b.inStock>0")
	public List<Book> findAllBooksInStock();

}
