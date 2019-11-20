package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.esanzhiev.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы с авторами")
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    private static final int DEFAULT_AUTHOR_COUNT = 1;
    private static final long DEFAULT_AUTHOR_ID = 1;

    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    @Test
    @DisplayName("должен возвращать корректное количество авторов")
    void shouldReturnCorrectAuthorCount() {
        assertThat(this.authorDaoJdbc.count()).isEqualTo(DEFAULT_AUTHOR_COUNT);
    }

    @Test
    @DisplayName("должен возвращать корректного автора по идентификатору")
    void shouldReturnCorrectAuthorById() {
        assertThat(this.authorDaoJdbc.getById(DEFAULT_AUTHOR_ID)).matches(
                authorOptional -> authorOptional.isPresent()
                        && "Pushkin".equals(authorOptional.get().getName())
        );
    }

    @Test
    @DisplayName("должен добавлять автора в БД")
    void shouldInsertAuthor() {
        String newAuthorName = "Shishkin";
        long newAuthorId = this.authorDaoJdbc.insert(Author.builder()
                .name(newAuthorName)
                .build());

        assertThat(this.authorDaoJdbc.getById(newAuthorId)).matches(
                authorOptional -> authorOptional.isPresent() &&
                        newAuthorName.equals(authorOptional.get().getName())
        );
    }

    @Test
    @DisplayName("должен удалять автора")
    void shouldDeleteAuthor() {
        this.authorDaoJdbc.delete(DEFAULT_AUTHOR_ID);

        assertEquals(0, this.authorDaoJdbc.count());
    }

    @Test
    @DisplayName("должен обновлять автора")
    void shouldUpdateAuthor() {
        String newAuthorName = "new author";
        this.authorDaoJdbc.update(Author.builder()
                .id(DEFAULT_AUTHOR_ID)
                .name(newAuthorName).build());

        assertThat(this.authorDaoJdbc.getById(DEFAULT_AUTHOR_ID))
                .matches(authorOptional -> authorOptional.isPresent() && newAuthorName.equals(authorOptional.get().getName()));
    }
}