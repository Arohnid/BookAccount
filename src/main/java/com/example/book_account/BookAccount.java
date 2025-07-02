package com.example.book_account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BookAccount {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BookAccount.class, args);
//        BookRepository bookRepository = configurableApplicationContext.getBean(BookRepository.class);
//        AuthorRepository authorRepository = configurableApplicationContext.getBean(AuthorRepository.class);
    }
}
