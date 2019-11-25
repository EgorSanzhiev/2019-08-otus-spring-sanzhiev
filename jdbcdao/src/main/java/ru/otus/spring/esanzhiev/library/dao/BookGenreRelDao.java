package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.BookGenreRel;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.util.List;

public interface BookGenreRelDao {
    void insert(BookGenreRel bookGenreRel);

    void deleteByBookId(long bookId);

    List<Book> findBooksByGenreId(long genreId);

    List<Genre> findGenresByBookId(long bookId);
}
