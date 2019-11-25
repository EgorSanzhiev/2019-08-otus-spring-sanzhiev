package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.esanzhiev.library.dao.ex.BookValidationException;
import ru.otus.spring.esanzhiev.library.domain.Book;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы с книгами")
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    private static final long DEFAULT_BOOK_ID = 1L;
    private static final long NON_PRESENT_BOOK_ID = 2L;

    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Test
    @DisplayName("должен возвращать корректную книгу по идентификатору")
    void shouldReturnCorrectBookById() {
        assertThat(this.bookDaoJdbc.getById(DEFAULT_BOOK_ID))
                .hasValueSatisfying(book ->
                        assertThat(book.getName()).isEqualTo("Onegin")
                );
    }

    @Test
    void shouldReturnEmptyOptionalWhenNotPresent() {
        assertThat(this.bookDaoJdbc.getById(NON_PRESENT_BOOK_ID)).isNotPresent();
    }

    @Test
    @DisplayName("должен добавлять книгу")
    void shouldInsertBook() {
        String bookName = "Cool book";
        Date publicationDate = Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault())));
        long newBookId = this.bookDaoJdbc.insert(Book.builder()
                .name(bookName)
                .publicationDate(publicationDate)
                .build());
        assertThat(this.bookDaoJdbc.getById(newBookId))
                .hasValueSatisfying(book -> {
                    assertThat(book.getName()).isEqualTo(bookName);
                    assertThat(book.getPublicationDate()).isEqualTo(publicationDate);
                });
    }

    @Test
    @DisplayName("должен удалять книгу")
    void shouldDeleteBook() {
        this.bookDaoJdbc.delete(DEFAULT_BOOK_ID);
        assertThat(this.bookDaoJdbc.getById(DEFAULT_BOOK_ID)).isNotPresent();
    }

    @Test
    @DisplayName("должен обновлять книгу")
    void shouldUpdateBook() {
        String newBookName = "Brand new book";
        this.bookDaoJdbc.update(Book.builder()
                .id(DEFAULT_BOOK_ID)
                .name(newBookName)
                .publicationDate(Date.from(Instant.now()))
                .build());

        assertThat(this.bookDaoJdbc.getById(DEFAULT_BOOK_ID)).hasValueSatisfying(
                book -> assertThat(book.getName()).isEqualTo(newBookName)
        );
    }

    @Test
    @DisplayName("должен выбросить исключение при попытке добавления книги с пустым именем")
    void shouldFailOnInsertWithEmptyName() {
        Date publicationDate = Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault())));
        assertThatThrownBy(() -> this.bookDaoJdbc.insert(Book.builder().publicationDate(publicationDate).build()))
                .isInstanceOf(BookValidationException.class)
                .hasMessage("Book name cannot be empty");
    }

    @Test
    @DisplayName("должен выбросить исключение при попытке добавления книги с незаполненной датой публикации")
    void shouldFailOnInsertWithEmptyPublicationDate() {
        assertThatThrownBy(() -> this.bookDaoJdbc.insert(Book.builder().name("New book").build()))
                .isInstanceOf(BookValidationException.class)
                .hasMessage("Publication date cannot be empty");
    }

    @Test
    @DisplayName("должен выбросить исключение при попытке обновления книги с незаполненным идентификатором")
    void shouldFailOnUpdateWithEmptyId() {
        Date publicationDate = Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault())));
        assertThatThrownBy(() -> this.bookDaoJdbc.update(Book.builder().name("Book").publicationDate(publicationDate).build()))
                .isInstanceOf(BookValidationException.class)
                .hasMessage("Book id cannot be empty");
    }

    @Test
    @DisplayName("должен выбросить исключение при попытке обновления книги с незаполненным наименованием")
    void shouldFailOnUpdateWithEmptyBookName() {
        Date publicationDate = Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault())));
        assertThatThrownBy(() -> this.bookDaoJdbc.update(Book.builder().id(1L).publicationDate(publicationDate).build()))
                .isInstanceOf(BookValidationException.class)
                .hasMessage("Book name cannot be empty");
    }

    @Test
    @DisplayName("должен выбросить исключение при попытке обновления книги с незаполненной датой публикации")
    void shouldFailOnUpdateWithEmptyPublicationDate() {
        assertThatThrownBy(() -> this.bookDaoJdbc.update(Book.builder().id(1L).name("Boook").build()))
                .isInstanceOf(BookValidationException.class)
                .hasMessage("Publication date cannot be empty");
    }
}