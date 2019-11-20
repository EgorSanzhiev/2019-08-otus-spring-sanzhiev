package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    int count();

    long insert(Genre genre);

    Optional<Genre> getById(long id);

    List<Genre> getAll();

    void update(Genre genre);

    void delete(long id);
}
