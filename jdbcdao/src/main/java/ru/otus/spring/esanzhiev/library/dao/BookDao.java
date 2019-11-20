package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Book;

import java.util.List;

public interface BookDao {
    int count();

    void insert(Book book);

    Book getById(long id);

    List<Book> getAll();
}
