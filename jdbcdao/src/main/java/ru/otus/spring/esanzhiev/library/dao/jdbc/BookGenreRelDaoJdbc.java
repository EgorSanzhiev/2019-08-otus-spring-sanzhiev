package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.BookGenreRelDao;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.BookMapper;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.GenreMapper;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.BookGenreRel;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.util.List;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class BookGenreRelDaoJdbc implements BookGenreRelDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Genre> genreMapper;

    private final RowMapper<Book> bookMapper;

    @Autowired
    public BookGenreRelDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        this.genreMapper = new GenreMapper("genre_id", "genre_name");
        this.bookMapper = new BookMapper("book_id", "book_name", "publication_date");
    }


    @Override
    public void insert(BookGenreRel bookGenreRel) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookGenreRel.getBookId());
        parameterSource.addValue("genre_id", bookGenreRel.getGenreId());
        this.jdbc.update("insert into BOOK_GENRE_REL (book_id, genre_id) values (:book_id, :genre_id)", parameterSource);
    }

    @Override
    public void deleteByBookId(long bookId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookId);
        this.jdbc.update("delete from BOOK_GENRE_REL where book_id=:book_id", parameterSource);
    }

    @Override
    public List<Book> findBooksByGenreId(long genreId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("genre_id", genreId);
        return this.jdbc.query("select b.id as book_id, b.`name` as book_name, b.publication_date as publication_date" +
                        " from BOOK_GENRE_REL bgr" +
                        " inner join BOOKS b on bgr.book_id=b.id" +
                        " where bgr.genre_id=:genre_id",
                parameterSource, bookMapper);
    }

    @Override
    public List<Genre> findGenresByBookId(long bookId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("book_id", bookId);
        return this.jdbc.query("select g.id as genre_id, g.`name` as genre_name" +
                        " from BOOK_GENRE_REL bgr" +
                        " inner join GENRES g on bgr.genre_id=g.id" +
                        " where bgr.book_id=:book_id",
                parameterSource, genreMapper);
    }
}
