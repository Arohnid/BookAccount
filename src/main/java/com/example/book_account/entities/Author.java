package com.example.book_account.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_year")
    private Integer birthYear;

}
