package com.example.book_account.dto;

import lombok.Data;


@Data
public class AuthorDto {
    private Long authorId;
    private String name;
    private Integer birthYear;
}
