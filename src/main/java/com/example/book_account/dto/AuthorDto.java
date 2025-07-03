package com.example.book_account.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthorDto {
    private Long authorId;

    @NotNull(message = "name must not be null!")
    @NotBlank(message = "name must not be blank!")
    private String name;

    private Integer birthYear;
}
