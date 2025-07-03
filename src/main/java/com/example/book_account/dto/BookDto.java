package com.example.book_account.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookDto {

    private Long id;

    @NotNull(message = "title must not be null!")
    @NotBlank(message = "title must not be blank!")
    private String title;

    @NotNull(message = "authorId must not be null!")
    @Positive
    private Long authorId;

    @NotNull(message = "year must not be null!")
    @Min(0)
    @Max(2025)
    private Integer year;

    @NotNull(message = "genre must not be null!")
    @NotBlank(message = "genre must not be blank!")
    private String genre;
}
