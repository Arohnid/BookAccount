package com.example.book_account.controllers;

import com.example.book_account.entities.Book;
import com.example.book_account.repositories.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Book> findAll() {
        return repository.findAll();
    }

    public void addBook() {

    }
}
