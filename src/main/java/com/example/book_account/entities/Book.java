package com.example.book_account.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Book {

    @Id
    private long id;

    private String title;

    private long author_id;

    private int year;

    private String genre;

}
