package com.example.book_account.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.OptionalInt;

@Entity
@Table(name = "author")
@Data
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_year", nullable = true)
    int birthYear;

    @OneToMany(mappedBy = "author", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;

}
