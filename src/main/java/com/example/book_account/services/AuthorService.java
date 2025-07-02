package com.example.book_account.services;

import com.example.book_account.dto.AuthorDto;
import com.example.book_account.entities.Author;
import com.example.book_account.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Collection<AuthorDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return authorRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(AuthorService::mapToDto)
                .toList();
    }

    public void create(AuthorDto item) {
        authorRepository.save(mapToEntity(item));
    }

    public static AuthorDto mapToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(author.getAuthorId());
        authorDto.setName(author.getName());
        if (author.getBirthYear() != null) {
            authorDto.setBirthYear(author.getBirthYear());
        }
        return authorDto;
    }


    public static Author mapToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setAuthorId(authorDto.getAuthorId());
        author.setName(authorDto.getName());
        if (authorDto.getBirthYear() != null) {
            author.setBirthYear(authorDto.getBirthYear());
        }
        return author;
    }
}
