package ru.otus.spring.esanzhiev.library.services.ex;

public class AuthorValidationException extends RuntimeException {
    public AuthorValidationException(String message) {
        super(message);
    }
}
