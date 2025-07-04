package com.example.book_account.controllers;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.dto.BookDto;
import com.example.book_account.entities.Book;
import com.example.book_account.repositories.BookRepository;
import com.example.book_account.services.AuthorService;
import com.example.book_account.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    private Long authorId;

    @BeforeEach
    void init() {
        if (authorId == null) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setName("Test Author");
            authorDto.setBirthYear(1989);

            authorService.create(authorDto);

            authorId = authorService.getAll(0, 10).iterator().next().getAuthorId();
        }
        bookService.deleteAll();
    }

    @Test
    @DisplayName("Test getById method")
    public void getByIdTest() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 1");
        bookDto.setAuthorId(authorId);
        bookDto.setYear(2011);
        bookDto.setGenre("Test Genre 1");

        bookService.create(bookDto);

        Long bookId = bookService.getAll().iterator().next().getId();

        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Test Book 1"));
    }

    @Test
    @DisplayName("Test getById method with non-existent id")
    public void getByIdExceptionTest() throws Exception {
        mockMvc.perform(get("/books/{id}", 500L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getAll method")
    public void getAllTest() throws Exception {
        BookDto bookDto1 = new BookDto();
        bookDto1.setTitle("Test Book 1");
        bookDto1.setAuthorId(authorId);
        bookDto1.setYear(2011);
        bookDto1.setGenre("Test Genre 1");

        BookDto bookDto2 = new BookDto();
        bookDto2.setTitle("Test Book 2");
        bookDto2.setAuthorId(authorId);
        bookDto2.setYear(2012);
        bookDto2.setGenre("Test Genre 2");

        BookDto bookDto3 = new BookDto();
        bookDto3.setTitle("Test Book 3");
        bookDto3.setAuthorId(authorId);
        bookDto3.setYear(2013);
        bookDto3.setGenre("Test Genre 3");

        bookService.create(bookDto1);
        bookService.create(bookDto2);
        bookService.create(bookDto3);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title",
                        hasItems("Test Book 1", "Test Book 2", "Test Book 3")));
    }

    @Test
    @DisplayName("Test getAll method with empty DB")
    public void getAllEmptyTest() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Test create method")
    public void createTest() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 1");
        bookDto.setAuthorId(authorId);
        bookDto.setYear(2011);
        bookDto.setGenre("Test Genre 1");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated());


        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);

        Book book = books.get(0);

        assertThat(book.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(book.getAuthor().getAuthorId()).isEqualTo(bookDto.getAuthorId());
        assertThat(book.getYear()).isEqualTo(bookDto.getYear());
        assertThat(book.getGenre()).isEqualTo(bookDto.getGenre());
    }

    @Test
    @DisplayName("Test update method")
    public void updateTest() throws Exception {
        BookDto createdBook = new BookDto();
        createdBook.setTitle("Test Book 1");
        createdBook.setAuthorId(authorId);
        createdBook.setYear(2011);
        createdBook.setGenre("Test Genre 1");

        BookDto updatedBook = new BookDto();
        updatedBook.setTitle("Updated Book 1");
        updatedBook.setAuthorId(authorId);
        updatedBook.setYear(2012);
        updatedBook.setGenre("Updated Genre 1");

        bookService.create(createdBook);

        Long createdBookId = bookService.getAll().iterator().next().getId();

        mockMvc.perform(put("/books/{id}", createdBookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());

        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);

        Book book = books.get(0);

        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor().getAuthorId()).isEqualTo(updatedBook.getAuthorId());
        assertThat(book.getYear()).isEqualTo(updatedBook.getYear());
        assertThat(book.getGenre()).isEqualTo(updatedBook.getGenre());
    }

    @Test
    @DisplayName("Test update method with non-existent id")
    public void updateIdExceptionTest() throws Exception {
        BookDto createdBook = new BookDto();
        createdBook.setTitle("Test Book 1");
        createdBook.setAuthorId(authorId);
        createdBook.setYear(2011);
        createdBook.setGenre("Test Genre 1");

        bookService.create(createdBook);

        mockMvc.perform(put("/books/{id}", 500L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete method")
    public void deleteTest() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 1");
        bookDto.setAuthorId(authorId);
        bookDto.setYear(2011);
        bookDto.setGenre("Test Genre 1");

        bookService.create(bookDto);

        Long bookId = bookService.getAll().iterator().next().getId();

        List<Book> booksBeforeDeletion = bookRepository.findAll();

        assertThat(booksBeforeDeletion).isNotEmpty();
        assertThat(booksBeforeDeletion).hasSize(1);

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isOk());

        List<Book> booksAfterDeletion = bookRepository.findAll();

        assertThat(booksAfterDeletion).isEmpty();
    }

    @Test
    @DisplayName("Test delete method with non-existent id")
    public void deleteIdExceptionTest() throws Exception {
        mockMvc.perform(delete("/books/{id}", 500L))
                .andExpect(status().isNotFound());
    }
}