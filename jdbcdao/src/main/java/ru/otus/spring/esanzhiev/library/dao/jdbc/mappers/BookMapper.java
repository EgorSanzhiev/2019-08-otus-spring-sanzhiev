package ru.otus.spring.esanzhiev.library.dao.jdbc.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.esanzhiev.library.domain.Book;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Егор Санжиев
 */
@RequiredArgsConstructor
public class BookMapper implements RowMapper<Book> {

    private final String idParameter;
    private final String nameParameter;
    private final String publicationDateParameter;

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(idParameter);
        String name = rs.getString(nameParameter);
        Date publicationDate = rs.getDate(publicationDateParameter);
        return Book.builder()
                .id(id)
                .name(name)
                .publicationDate(publicationDate)
                .build();
    }
}
