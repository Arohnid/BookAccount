package com.example.book_account.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthorDto {
    private Long authorId;
    private String name;
    private int birthYear;
    private List<BookDto> books;
}
