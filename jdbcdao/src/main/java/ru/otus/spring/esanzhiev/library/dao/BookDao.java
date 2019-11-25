package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    int count();

    long insert(Book book);

    void delete(long id);

    void update(Book book);

    Optional<Book> getById(long id);

    List<Book> getAll();
}
