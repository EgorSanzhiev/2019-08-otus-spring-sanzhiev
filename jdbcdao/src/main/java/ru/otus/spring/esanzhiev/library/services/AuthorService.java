package ru.otus.spring.esanzhiev.library.services;

import ru.otus.spring.esanzhiev.library.domain.Author;

import java.util.List;

public interface AuthorService {
    long insert(Author author);

    void update(Author author);

    void delete(long id);

    Author getById(long id);

    List<Author> getAll();
}
