package com.example.book_account.services;

import com.example.book_account.dto.BookDto;
import com.example.book_account.entities.Author;
import com.example.book_account.entities.Book;
import com.example.book_account.repositories.AuthorRepository;
import com.example.book_account.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    public Collection<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(BookService::mapToDto)
                .toList();
    }

    public BookDto getById(Long id) {
        return mapToDto(bookRepository.findById(id).orElseThrow());
    }

    public void create(BookDto item) {
        Book book = mapToEntity(item);
        Long authorId = item.getAuthorId();
        if (!authorRepository.existsById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            book.setAuthor(authorRepository.findById(authorId).orElseThrow());
            bookRepository.save(book);
        }
    }


    public static BookDto mapToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getBookId());
        bookDto.setTitle(book.getTitle());
        bookDto.setGenre(book.getGenre());
        bookDto.setYear(book.getYear());
        bookDto.setAuthorId(book.getAuthor().getAuthorId());
        return bookDto;
    }

    public static Book mapToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setBookId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        book.setYear(bookDto.getYear());
        return book;
    }
}
