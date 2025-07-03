package com.example.book_account.services;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.entities.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@DataJpaTest
@ComponentScan(basePackages = "com.example.book_account.services")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorService authorService;

    private Author author1;
    private Author author2;
    private Author author3;

    @BeforeEach
    void authorSetUp() {
        // Создаем тестового автора
        author1 = new Author();
        author1.setName("Test Author 1");
        author1.setBirthYear(1991);

        author2 = new Author();
        author2.setName("Test Author 2");
        author2.setBirthYear(1964);

        author3 = new Author();
        author3.setName("Test Author 3");
        author3.setBirthYear(1971);

        // Сохраняем в тестовую базу данных
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(author3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Test getById")
    public void getByIdTest() {
        Long authorId = author1.getAuthorId();

        AuthorDto authorDto = authorService.getById(authorId);

        assertThat(authorDto.getAuthorId()).isEqualTo(authorId);
        assertThat(authorDto.getName()).isEqualTo(author1.getName());
        assertThat(authorDto.getBirthYear()).isEqualTo(author1.getBirthYear());
    }

    @Test
    @DisplayName("Test if getById throws exception")
    public void getByIdException() {
        Long id = 500L;

        assertThatThrownBy(() -> authorService.getById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(NOT_FOUND.name());
    }

    @Test
    @DisplayName("Test getAll")
    public void getAllTest(){
        Collection<AuthorDto> authors = authorService.getAll(0, 5);

        assertThat(authors).hasSize(3);
        assertThat(authors).extracting("name")
                .containsExactlyInAnyOrder("Test Author 1", "Test Author 2", "Test Author 3");
    }

    @Test
    @DisplayName("Test getAll with empty DB")
    public void getAllEmptyTest(){
        entityManager.remove(author1);
        entityManager.remove(author2);
        entityManager.remove(author3);
        entityManager.flush();

        Collection<AuthorDto> authors = authorService.getAll(0, 5);

        assertThat(authors).isEmpty();
    }
}