package com.example.book_account.services;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.dto.BookDto;
import com.example.book_account.entities.Author;
import com.example.book_account.entities.Book;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@DataJpaTest
@ComponentScan(basePackages = "com.example.book_account.services")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookService bookService;

    private Author author;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void init() {
        author = new Author();
        author.setName("Test Author");
        author.setBirthYear(1989);
        book1 = new Book();
        book1.setTitle("Test Book 1");
        book1.setAuthor(author);
        book1.setYear(2011);
        book1.setGenre("Test Genre 1");
        book2 = new Book();
        book2.setTitle("Test Book 2");
        book2.setAuthor(author);
        book2.setYear(2012);
        book2.setGenre("Test Genre 2");
        book3 = new Book();
        book3.setTitle("Test Book 3");
        book3.setAuthor(author);
        book3.setYear(2013);
        book3.setGenre("Test Genre 3");

        entityManager.persist(author);
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Test method getById")
    public void getByIdTest() {
        Long bookId = book1.getBookId();

        BookDto bookDto = bookService.getById(bookId);

        assertThat(bookDto.getId()).isEqualTo(bookId);
        assertThat(bookDto.getTitle()).isEqualTo(book1.getTitle());
        assertThat(bookDto.getAuthorId()).isEqualTo(book1.getAuthor().getAuthorId());
        assertThat(bookDto.getYear()).isEqualTo(book1.getYear());
        assertThat(bookDto.getGenre()).isEqualTo(book1.getGenre());
    }

    @Test
    @DisplayName("Test getById method with non-existent id")
    public void getByIdException() {
        Long id = 500L;

        assertThatThrownBy(() -> bookService.getById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(NOT_FOUND.name());
    }

    @Test
    @DisplayName("Test getAll method")
    public void getAllTest() {
        Collection<BookDto> books = bookService.getAll();

        assertThat(books).hasSize(3);
        assertThat(books).extracting("title")
                .containsExactlyInAnyOrder("Test Book 1", "Test Book 2", "Test Book 3");
    }

    @Test
    @DisplayName("Test getAll method with empty DB")
    public void getAllEmptyTest() {
        entityManager.remove(book1);
        entityManager.remove(book2);
        entityManager.remove(book3);
        entityManager.flush();

        Collection<BookDto> books = bookService.getAll();

        assertThat(books).isEmpty();
    }

    @Test
    @DisplayName("Test create method")
    public void createTest() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 4");
        bookDto.setAuthorId(author.getAuthorId());
        bookDto.setYear(2013);
        bookDto.setGenre("Test Genre 4");

        bookService.create(bookDto);

        Query query = entityManager
                .getEntityManager()
                .createNativeQuery("SELECT * FROM book ORDER BY ID DESC LIMIT 1", Book.class);
        Book createdBook = (Book) query.getSingleResult();

        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(createdBook.getAuthor().getAuthorId()).isEqualTo(bookDto.getAuthorId());
        assertThat(createdBook.getYear()).isEqualTo(bookDto.getYear());
        assertThat(createdBook.getGenre()).isEqualTo(bookDto.getGenre());
    }


    @Test
    @DisplayName("Test update method")
    public void update() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 4");
        bookDto.setAuthorId(author.getAuthorId());
        bookDto.setYear(2013);
        bookDto.setGenre("Test Genre 4");
        Long updatedId = book3.getBookId();

        bookService.update(updatedId, bookDto);

        Query query = entityManager
                .getEntityManager()
                .createNativeQuery("SELECT * FROM book ORDER BY ID DESC LIMIT 1", Book.class);
        Book updatedBook = (Book) query.getSingleResult();

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getBookId()).isEqualTo(updatedId);
        assertThat(updatedBook.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(updatedBook.getAuthor().getAuthorId()).isEqualTo(bookDto.getAuthorId());
        assertThat(updatedBook.getYear()).isEqualTo(bookDto.getYear());
        assertThat(updatedBook.getGenre()).isEqualTo(bookDto.getGenre());

    }

    @Test
    @DisplayName("Test update method with non-existent id")
    public void updateIdException() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book 4");
        bookDto.setAuthorId(author.getAuthorId());
        bookDto.setYear(2013);
        bookDto.setGenre("Test Genre 4");
        Long id = 500L;

        assertThatThrownBy(() -> bookService.update(id, bookDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(NOT_FOUND.name());
    }

    @Test
    @DisplayName("Test delete method")
    public void delete() {
        Long deleteId = book3.getBookId();

        bookService.delete(deleteId);

        Query query = entityManager
                .getEntityManager()
                .createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> books = query.getResultList();

        assertThat(books.size()).isEqualTo(2);
        assertThat(books).doesNotContain(book3);

    }

    @Test
    @DisplayName("Test delete method with non-existent id")
    public void deleteIdException() {
        Long deleteId = 500L;

        assertThatThrownBy(() -> bookService.delete(deleteId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(NOT_FOUND.name());
    }
}