package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"ConstantConditions", "SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    private final AuthorMapper authorMapper;

    private final AuthorResultSetExtractor authorResultSetExtractor;

    @Autowired
    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        authorMapper = new AuthorMapper();
        authorResultSetExtractor = new AuthorResultSetExtractor();
    }

    @Override
    public int count() {
        return this.jdbc.queryForObject("select count(*) from AUTHORS", Collections.emptyMap(), Integer.class);
    }

    @Override
    public long insert(Author author) {
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
        Author query = this.jdbc.query("select * from AUTHORS where id=:id", parameterSource, this.authorResultSetExtractor);
        return Optional.ofNullable(query);
    }

    @Override
    public List<Author> getAll() {
        return this.jdbc.query("select * from AUTHORS", this.authorMapper);
    }

    @Override
    public void update(Author author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", author.getId());
        parameterSource.addValue("name", author.getName());
        this.jdbc.update("update AUTHORS set `name`=:name where id=:id", parameterSource);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        this.jdbc.update("delete from AUTHORS where id=:id", parameterSource);
    }

    private static class AuthorResultSetExtractor implements ResultSetExtractor<Author> {
        @Override
        public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                long id1 = rs.getLong("id");
                String name = rs.getString("name");
                return Author.builder()
                        .id(id1)
                        .name(name)
                        .build();
            } else {
                return null;
            }
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");

            return Author.builder()
                    .id(id)
                    .name(name)
                    .build();
        }
    }
}
