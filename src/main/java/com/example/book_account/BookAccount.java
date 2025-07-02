package com.example.book_account;

import com.example.book_account.entities.Author;
import com.example.book_account.entities.Book;
import com.example.book_account.repositories.AuthorRepository;
import com.example.book_account.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookAccount {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BookAccount.class, args);
        BookRepository bookRepository = configurableApplicationContext.getBean(BookRepository.class);

        AuthorRepository authorRepository = configurableApplicationContext.getBean(AuthorRepository.class);
        Author author = new Author();
        author.setName("Dimon");
        //author.setBirthYear(1891);
        Book book = new Book();
        book.setTitle("Hello");
        book.setYear(2025);
        book.setGenre("Fantasy");
        book.setAuthor(author);
        authorRepository.save(author);
        bookRepository.save(book);
        Book book2 = new Book();
        book2.setTitle("Hello");
        book2.setYear(2025);
        book2.setGenre("Fantasy");
        book2.setAuthor(author);
        bookRepository.save(book2);
    }


}
