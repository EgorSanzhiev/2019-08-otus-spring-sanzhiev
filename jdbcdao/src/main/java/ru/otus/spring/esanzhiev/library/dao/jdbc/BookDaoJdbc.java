package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.BookDao;
import ru.otus.spring.esanzhiev.library.dao.ex.BookValidationException;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.BookMapper;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.RowMapperBasedResultSetExtractor;
import ru.otus.spring.esanzhiev.library.domain.Book;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"ConstantConditions", "SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class BookDaoJdbc implements BookDao {
    
    private final NamedParameterJdbcOperations jbdc;

    private final ResultSetExtractor<Book> bookResultSetExtractor;

    private final RowMapper<Book> bookMapper;

    @Autowired
    public BookDaoJdbc(NamedParameterJdbcOperations jbdc) {
        this.jbdc = jbdc;
        bookMapper = new BookMapper("id", "name", "publication_date");
        bookResultSetExtractor = new RowMapperBasedResultSetExtractor<>(bookMapper);
    }

    @Override
    public int count() {
        return this.jbdc.queryForObject("select count(*) from BOOKS", Collections.emptyMap(), Integer.class);
    }

    @Override
    public long insert(Book book) {
        String bookName = book.getName();
        java.util.Date publicationDate = book.getPublicationDate();
        if (bookName == null || bookName.isEmpty()) {
            throw new BookValidationException("Book name cannot be empty");
        }
        if (publicationDate == null) {
            throw new BookValidationException("Publication date cannot be empty");
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", bookName);
        parameterSource.addValue("publication_date", publicationDate);
        KeyHolder kh = new GeneratedKeyHolder();
        this.jbdc.update("insert into BOOKS(`name`, publication_date) values (:name, :publication_date)", parameterSource, kh);
        return kh.getKey().longValue();
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        this.jbdc.update("delete from BOOKS where id=:id", parameterSource);
    }

    @Override
    public void update(Book book) {
        Long bookId = book.getId();
        String bookName = book.getName();
        Date publicationDate = book.getPublicationDate();
        if (bookId == null) {
            throw new BookValidationException("Book id cannot be empty");
        }
        if (bookName == null || bookName.isEmpty()) {
            throw new BookValidationException("Book name cannot be empty");
        }
        if (publicationDate == null) {
            throw new BookValidationException("Publication date cannot be empty");
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", bookName);
        parameterSource.addValue("publication_date", publicationDate);
        parameterSource.addValue("id", bookId);
        this.jbdc.update("update BOOKS set `name`=:name, publication_date=:publication_date where id=:id", parameterSource);
    }

    @Override
    public Optional<Book> getById(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);

        Book book = this.jbdc.query("select * from BOOKS where id=:id", parameterSource, this.bookResultSetExtractor);
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> getAll() {
        return this.jbdc.query("select * from BOOKS", this.bookMapper);
    }

}
