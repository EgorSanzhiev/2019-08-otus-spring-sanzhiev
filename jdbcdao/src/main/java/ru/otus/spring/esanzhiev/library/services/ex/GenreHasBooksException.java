package ru.otus.spring.esanzhiev.library.services.ex;

public class GenreHasBooksException extends RuntimeException {
    public GenreHasBooksException(long genreId) {
        super(String.format("Cannot delete genre with id=%d. Genre has related books", genreId));
    }
}
