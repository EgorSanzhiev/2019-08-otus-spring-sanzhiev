package ru.otus.spring.esanzhiev.library.services.ex;

public class AuthorHasBooksException extends RuntimeException {
    public AuthorHasBooksException(long authorId) {
        super(String.format("Cannot delete author with id=%d. Author has books", authorId));
    }
}
