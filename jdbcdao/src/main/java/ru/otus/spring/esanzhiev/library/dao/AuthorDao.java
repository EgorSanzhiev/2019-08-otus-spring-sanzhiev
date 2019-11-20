package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    int count();

    long insert(Author author);

    Optional<Author> getById(long id);

    List<Author> getAll();

    void update(Author author);

    void delete(long id);
}
