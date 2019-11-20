package ru.otus.spring.esanzhiev.library.services;

import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.util.List;

public interface GenreService {
    long insert(Genre genre);

    void update(Genre genre);

    void delete(long id);

    Genre getById(long id);

    List<Genre> getAll();
}
