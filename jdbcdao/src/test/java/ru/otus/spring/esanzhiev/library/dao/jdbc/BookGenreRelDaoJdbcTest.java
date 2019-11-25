package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы со связью книг и жанров")
@Import(BookGenreRelDaoJdbc.class)
class BookGenreRelDaoJdbcTest {

    private static final long DEFAULT_GENRE_ID = 2L;
    private static final long DEFAULT_BOOK_ID = 1L;

    @Autowired
    private BookGenreRelDaoJdbc bookGenreRelDaoJdbc;

    @Test
    @DisplayName("должен возвращать книги по идентификатору жанра")
    void shouldReturnBooksByGenreID() {
        Condition<Book> hasCorrectName = new Condition<>(book -> "Onegin".equals(book.getName()), "Book should have correct name");

        assertThat(this.bookGenreRelDaoJdbc.findBooksByGenreId(DEFAULT_GENRE_ID))
                .hasSize(1)
                .haveExactly(1, hasCorrectName);

    }

    @Test
    @DisplayName("должен возвращать жанры по идентификатору книги")
    void shouldReturnGenresByBookID() {
        Condition<Genre> hasCorrectName = new Condition<>(genre -> "Novel".equals(genre.getName()), "Genre should have correct name");

        assertThat(this.bookGenreRelDaoJdbc.findGenresByBookId(DEFAULT_BOOK_ID))
                .hasSize(1)
                .haveExactly(1, hasCorrectName);
    }
}