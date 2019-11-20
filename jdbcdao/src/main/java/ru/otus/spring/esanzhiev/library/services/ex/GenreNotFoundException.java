package ru.otus.spring.esanzhiev.library.services.ex;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(long id) {
        super(String.format("Genre not found by id=%d", id));
    }
}
