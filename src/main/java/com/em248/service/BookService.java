package com.em248.service;


import com.em248.configuration.database.ChangeDataSource;
import com.em248.model.Book;
import com.em248.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {


    private BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @ChangeDataSource
    public List<Book> listAll() {
        return bookRepository.findAll();
    }


    @ChangeDataSource
    public void save(Book book) {
        bookRepository.save(book);
    }
}
