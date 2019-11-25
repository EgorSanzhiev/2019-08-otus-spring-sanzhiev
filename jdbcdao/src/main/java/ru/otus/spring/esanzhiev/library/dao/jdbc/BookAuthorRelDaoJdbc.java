package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.BookAuthorRelDao;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.AuthorMapper;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.BookMapper;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.BookAuthorRel;

import java.util.List;

/**
 * @author Егор Санжиев
 */
@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class BookAuthorRelDaoJdbc implements BookAuthorRelDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Author> authorMapper;

    private final RowMapper<Book> bookMapper;

    @Autowired
    public BookAuthorRelDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        this.authorMapper = new AuthorMapper("author_id", "author_name");
        this.bookMapper = new BookMapper("book_id", "book_name", "publication_date");
    }

    @Override
    public void insert(BookAuthorRel bookAuthorRel) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookAuthorRel.getBookId());
        parameterSource.addValue("author_id", bookAuthorRel.getAuthorId());
        this.jdbc.update("insert into BOOK_AUTHOR_REL (book_id, author_id) values (:book_id, :author_id)", parameterSource);
    }

    @Override
    public void deleteByBookId(long bookId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookId);
        this.jdbc.update("delete from BOOK_AUTHOR_REL where book_id=:book_id", parameterSource);
    }

    @Override
    public List<Book> findBooksByAuthorId(long authorId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author_id", authorId);
        return this.jdbc.query("select b.id as book_id, b.`name` as book_name, b.publication_date as publication_date " +
                "from BOOK_AUTHOR_REL bar " +
                "inner join BOOKS b on bar.book_id=b.id " +
                "where bar.author_id=:author_id",
                parameterSource, this.bookMapper);
    }

    @Override
    public List<Author> findAuthorsByBookId(long bookId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookId);
        return this.jdbc.query("select a.id as author_id, a.`name` as author_name " +
                "from BOOK_AUTHOR_REL bar " +
                "inner join AUTHORS a on bar.author_id=a.id " +
                "where bar.book_id=:book_id",
                parameterSource, this.authorMapper);
    }
}
