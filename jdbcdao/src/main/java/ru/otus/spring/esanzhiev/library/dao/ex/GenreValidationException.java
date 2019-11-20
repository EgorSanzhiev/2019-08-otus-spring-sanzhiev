package ru.otus.spring.esanzhiev.library.dao.ex;

public class GenreValidationException extends RuntimeException {
    public GenreValidationException(String message) {
        super(message);
    }
}
