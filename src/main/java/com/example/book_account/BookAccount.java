package com.example.book_account;

import com.example.book_account.entities.Book;
import com.example.book_account.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookAccount {
    public static void main(String[] args) {
        SpringApplication.run(BookAccount.class, args);
    }


}
