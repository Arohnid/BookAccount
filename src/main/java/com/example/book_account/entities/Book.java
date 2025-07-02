package com.example.book_account.entities;

import jakarta.persistence.*;

import lombok.Data;


@Entity
@Table(name = "book")
@Data
public class Book {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "publ_year", nullable = false)
    private int year;

    @Column(name = "genre", nullable = false)
    private String genre;

}
