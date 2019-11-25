package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.dao.ex.AuthorValidationException;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.AuthorMapper;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.RowMapperBasedResultSetExtractor;
import ru.otus.spring.esanzhiev.library.domain.Author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"ConstantConditions", "SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Author> authorMapper;

    private final ResultSetExtractor<Author> authorResultSetExtractor;

    @Autowired
    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        authorMapper = new AuthorMapper("id", "name");
        authorResultSetExtractor = new RowMapperBasedResultSetExtractor<>(authorMapper);
    }

    @Override
    public int count() {
        return this.jdbc.queryForObject("select count(*) from AUTHORS", Collections.emptyMap(), Integer.class);
    }

    @Override
    public long insert(Author author) {
        validateName(author.getName());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", author.getName());
        KeyHolder kh = new GeneratedKeyHolder();
        this.jdbc.update("insert into AUTHORS (`name`) values (:name)", parameterSource, kh);
        return kh.getKey().longValue();
    }

    @Override
    public Optional<Author> getById(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        Author author = this.jdbc.query("select * from AUTHORS where id=:id", parameterSource, this.authorResultSetExtractor);
        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> getAll() {
        return this.jdbc.query("select * from AUTHORS", this.authorMapper);
    }

    @Override
    public void update(Author author) {
        Long id = author.getId();
        if (id == null) {
            throw new IllegalArgumentException("Author id cannot be null");
        }

        String name = author.getName();
        validateName(name);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("name", name);
        this.jdbc.update("update AUTHORS set `name`=:name where id=:id", parameterSource);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        this.jdbc.update("delete from AUTHORS where id=:id", parameterSource);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new AuthorValidationException("Author name cannot be empty");
        }
    }

}
