package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Егор Санжиев
 */
@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы со связью книг и авторов")
@Import(BookAuthorRelDaoJdbc.class)
class BookAuthorRelDaoJdbcTest {

    private static final long DEFAULT_AUTHOR_ID = 1L;
    private static final long DEFAULT_BOOK_ID = 1L;

    @Autowired
    private BookAuthorRelDaoJdbc bookAuthorRelDaoJdbc;

    @Test
    void shouldReturnBooksByAuthorId() {
        Condition<Book> hasCorrectName = new Condition<>(book -> "Onegin".equals(book.getName()), "Book should have correct name");
        assertThat(this.bookAuthorRelDaoJdbc.findBooksByAuthorId(DEFAULT_AUTHOR_ID))
                .hasSize(1)
                .haveExactly(1, hasCorrectName);
    }

    @Test
    void shouldReturnCorrectAuthorsByBookId() {
        Condition<Author> hasCorrectName = new Condition<>(author -> "Pushkin".equals(author.getName()), "Author should have correct name");
        assertThat(this.bookAuthorRelDaoJdbc.findAuthorsByBookId(DEFAULT_BOOK_ID))
                .hasSize(1)
                .haveExactly(1, hasCorrectName);
    }
}
