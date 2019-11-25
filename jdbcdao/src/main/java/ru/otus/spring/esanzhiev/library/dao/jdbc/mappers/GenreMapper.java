package ru.otus.spring.esanzhiev.library.dao.jdbc.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Егор Санжиев
 */
@RequiredArgsConstructor
public class GenreMapper implements RowMapper<Genre> {

    private final String idParameter;
    private final String nameParameter;

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(idParameter);
        String name = rs.getString(nameParameter);

        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}
