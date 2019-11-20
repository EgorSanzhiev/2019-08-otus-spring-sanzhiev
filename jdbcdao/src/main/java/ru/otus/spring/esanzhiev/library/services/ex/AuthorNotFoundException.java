package ru.otus.spring.esanzhiev.library.services.ex;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super(String.format("Author not found by id=%d", id));
    }
}
