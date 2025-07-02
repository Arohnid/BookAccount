package com.example.book_account.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private Long authorId;
    private int year;
    private String genre;
}
