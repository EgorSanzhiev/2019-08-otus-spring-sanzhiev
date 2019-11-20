package ru.otus.spring.esanzhiev.library.dao.ex;

public class AuthorValidationException extends RuntimeException {
    public AuthorValidationException(String message) {
        super(message);
    }
}
