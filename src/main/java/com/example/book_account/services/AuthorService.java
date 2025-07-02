package com.example.book_account.services;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.entities.Author;
import com.example.book_account.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDto getById(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return mapToDto(authorRepository.findById(id).orElseThrow());
    }

    public Collection<AuthorDto> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorService::mapToDto)
                .toList();
    }

    public void create(AuthorDto item){
        authorRepository.save(mapToEntity(item));
    }

    public void update(Long id, AuthorDto item) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            authorRepository.save(mapToEntity(item));
        }
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            authorRepository.deleteById(id);
        }
    }

    public static AuthorDto mapToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(author.getAuthorId());
        authorDto.setName(author.getName());
        authorDto.setBirthYear(authorDto.getBirthYear());
        authorDto.setBooks(author.getBooks()
                .stream()
                .map(BookService::mapToDto)
                .toList());
        return authorDto;
    }

    public static Author mapToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setAuthorId(authorDto.getAuthorId());
        author.setName(authorDto.getName());
        author.setBirthYear(authorDto.getBirthYear());
        author.setBooks(authorDto.getBooks()
                .stream()
                .map(BookService::mapToEntity)
                .toList());
        return author;
    }
}
