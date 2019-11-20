package ru.otus.spring.esanzhiev.library.dao.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.esanzhiev.library.dao.ex.GenreValidationException;
import ru.otus.spring.esanzhiev.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы с жанрами")
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    private static final int DEFAULT_GENRE_COUNT = 1;
    private static final long DEFAULT_GENRE_ID = 1L;


    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @Test
    @DisplayName("должен возвращать корректное количество жанров")
    void shouldReturnCorrectGenreCount() {
        assertThat(this.genreDaoJdbc.count()).isEqualTo(DEFAULT_GENRE_COUNT);
    }

    @Test
    @DisplayName("должен возвращать корректный жанр по идентификатору")
    void shouldReturnCorrectGenreById() {
        assertThat(this.genreDaoJdbc.getById(DEFAULT_GENRE_ID)).matches(
                GenreOptional -> GenreOptional.isPresent()
                        && "Sci-Fi".equals(GenreOptional.get().getName())
        );
    }

    @Test
    @DisplayName("должен добавлять жанр в БД")
    void shouldInsertGenre() {
        String newGenreName = "Detective";
        long newGenreId = this.genreDaoJdbc.insert(Genre.builder()
                .name(newGenreName)
                .build());

        assertThat(this.genreDaoJdbc.getById(newGenreId)).matches(
                GenreOptional -> GenreOptional.isPresent() &&
                        newGenreName.equals(GenreOptional.get().getName())
        );
    }

    @Test
    @DisplayName("должен выбросить исключение при добавлении жанра с пустым наименованием")
    void shouldFailOnInsertWithEmptyName() {
        assertThrows(GenreValidationException.class, () -> this.genreDaoJdbc.insert(Genre.builder().build()));
    }

    @Test
    @DisplayName("должен удалять жанр")
    void shouldDeleteGenre() {
        this.genreDaoJdbc.delete(DEFAULT_GENRE_ID);

        assertEquals(0, this.genreDaoJdbc.count());
    }

    @Test
    @DisplayName("должен обновлять жанр")
    void shouldUpdateGenre() {
        String newGenreName = "Classic";
        this.genreDaoJdbc.update(Genre.builder()
                .id(DEFAULT_GENRE_ID)
                .name(newGenreName)
                .build());

        assertThat(this.genreDaoJdbc.getById(DEFAULT_GENRE_ID))
                .matches(genreOptional -> genreOptional.isPresent() && newGenreName.equals(genreOptional.get().getName()));
    }

    @Test
    @DisplayName("должен выбросить исключение при передаче пустого идентификатора в метод обновления")
    void shouldFailOnUpdateWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> this.genreDaoJdbc.update(Genre.builder().build()));
    }
}