package ru.otus.spring.esanzhiev.library.dao;

import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.BookAuthorRel;

import java.util.List;

/**
 * @author Егор Санжиев
 */
public interface BookAuthorRelDao {
    void insert(BookAuthorRel bookAuthorRel);

    void deleteByBookId(long bookId);

    void deleteByAuthorId(long authorId);

    List<Book> findBooksByAuthorId(long authorId);

    List<Author> findAuthorsByBookId(long bookId);
}
