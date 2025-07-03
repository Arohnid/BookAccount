package com.example.book_account.controllers;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.entities.Author;
import com.example.book_account.repositories.AuthorRepository;
import com.example.book_account.services.AuthorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;


    @BeforeEach
    void init() {
        authorService.deleteAll();
    }

    @Test
    @DisplayName("Test getById method")
    public void getByIdTest() throws Exception {
        AuthorDto author1 = new AuthorDto();
        author1.setName("Test Author 1");
        author1.setBirthYear(1991);
        authorService.create(author1);

        Long authorId = authorService.getAll(0, 5).iterator().next().getAuthorId();

        mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Test Author 1"));
    }

    @Test
    @DisplayName("Test getById method with non-existent id")
    public void getByNullIdTest() throws Exception {
        mockMvc.perform(get("/authors/{id}", 500L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getAll method")
    public void getAllTest() throws Exception {
        AuthorDto author1 = new AuthorDto();
        author1.setName("Test Author 1");
        author1.setBirthYear(1991);

        AuthorDto author2 = new AuthorDto();
        author2.setName("Test Author 2");
        author2.setBirthYear(1964);

        AuthorDto author3 = new AuthorDto();
        author3.setName("Test Author 3");
        author3.setBirthYear(1971);

        authorService.create(author1);
        authorService.create(author2);
        authorService.create(author3);

        mockMvc.perform(get("/authors")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name",
                        hasItems("Test Author 1", "Test Author 2", "Test Author 3")));
    }

    @Test
    @DisplayName("Test getAll method with empty DB")
    public void getAllEmptyTest() throws Exception {
        mockMvc.perform(get("/authors")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    @DisplayName("Test create method")
    public void createTest() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Test Author 4");
        authorDto.setBirthYear(1989);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated());


        List<Author> authors = authorRepository.findAll();

        assertThat(authors).isNotEmpty();
        assertThat(authors).hasSize(1);

        Author author = authors.get(0);

        assertThat(author.getName()).isEqualTo(authorDto.getName());
        assertThat(author.getBirthYear()).isEqualTo(authorDto.getBirthYear());
    }

    @Test
    @DisplayName("Test create method with null birthYear")
    public void createNullYearTest() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Test Author 4");

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated());

        List<Author> authors = authorRepository.findAll();

        assertThat(authors).isNotEmpty();
        assertThat(authors).hasSize(1);

        Author author = authors.get(0);

        assertThat(author.getName()).isEqualTo(authorDto.getName());
        assertThat(author.getBirthYear()).isNull();
    }
}