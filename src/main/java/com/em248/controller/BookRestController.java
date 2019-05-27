package com.em248.controller;

import com.em248.model.Book;
import com.em248.service.BookService;
import com.em248.utils.thread.TheadLocalUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/book")
    public ResponseEntity<List<Book>> listAll(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(bookService.listAll());
    }

    @PostMapping("/book")
    public ResponseEntity<?> add(@RequestBody Book book) {
        bookService.save(book);
        return ResponseEntity.ok("success");
    }
}
