package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.GenreDao;
import ru.otus.spring.esanzhiev.library.dao.ex.GenreValidationException;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.GenreMapper;
import ru.otus.spring.esanzhiev.library.dao.jdbc.mappers.RowMapperBasedResultSetExtractor;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"ConstantConditions", "SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Genre> genreMapper;

    private final ResultSetExtractor<Genre> genreResultSetExtractor;

    @Autowired
    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        genreMapper = new GenreMapper("id", "name");
        genreResultSetExtractor = new RowMapperBasedResultSetExtractor<>(genreMapper);
    }

    @Override
    public int count() {
        return this.jdbc.queryForObject("select count(*) from GENRES", Collections.emptyMap(), Integer.class);
    }

    @Override
    public long insert(Genre genre) {
        String name = genre.getName();
        validateName(name);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", name);

        KeyHolder kh = new GeneratedKeyHolder();
        this.jdbc.update("insert into GENRES(`name`) values (:name)", parameterSource, kh);
        return kh.getKey().longValue();
    }

    @Override
    public Optional<Genre> getById(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        Genre genre = this.jdbc.query("select * from GENRES where id=:id", parameterSource, genreResultSetExtractor);
        return Optional.ofNullable(genre);
    }

    @Override
    public List<Genre> getAll() {
        return this.jdbc.query("select * from GENRES", genreMapper);
    }

    @Override
    public void update(Genre genre) {
        Long id = genre.getId();
        if (id == null) {
            throw new IllegalArgumentException("Genre id cannot be null");
        }

        String name = genre.getName();
        validateName(name);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("name", name);
        this.jdbc.update("update GENRES set `name`=:name where id=:id", parameterSource);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        this.jdbc.update("delete from GENRES where id=:id", parameterSource);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new GenreValidationException("Genre name cannot be empty");
        }
    }
}
