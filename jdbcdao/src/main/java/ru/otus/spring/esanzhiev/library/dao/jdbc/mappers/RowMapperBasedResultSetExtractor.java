package ru.otus.spring.esanzhiev.library.dao.jdbc.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Егор Санжиев
 */
@RequiredArgsConstructor
public class RowMapperBasedResultSetExtractor<T> implements ResultSetExtractor<T> {

    private final RowMapper<T> rowMapper;

    @Override
    public T extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.next()) {
            return rowMapper.mapRow(rs, 0);
        } else {
            return null;
        }
    }
}
