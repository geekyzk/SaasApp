package com.em248.repository;

import com.em248.model.Book;
import com.em248.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,String> {
}
