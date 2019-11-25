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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DisplayName("DAO для работы с жанрами")
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    private static final int DEFAULT_GENRE_COUNT = 2;
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
        assertThat(this.genreDaoJdbc.getById(DEFAULT_GENRE_ID)).hasValueSatisfying(
                genre -> assertThat(genre.getName()).isEqualTo("Sci-Fi")
        );
    }

    @Test
    @DisplayName("должен добавлять жанр в БД")
    void shouldInsertGenre() {
        String newGenreName = "Detective";
        long newGenreId = this.genreDaoJdbc.insert(Genre.builder()
                .name(newGenreName)
                .build());

        assertThat(this.genreDaoJdbc.getById(newGenreId))
                .hasValueSatisfying(genre -> assertThat(genre.getName()).isEqualTo(newGenreName));
    }

    @Test
    @DisplayName("должен выбросить исключение при добавлении жанра с пустым наименованием")
    void shouldFailOnInsertWithEmptyName() {
        assertThatThrownBy(() -> this.genreDaoJdbc.insert(Genre.builder().build()))
                .isInstanceOf(GenreValidationException.class);
    }

    @Test
    @DisplayName("должен удалять жанр")
    void shouldDeleteGenre() {
        this.genreDaoJdbc.delete(DEFAULT_GENRE_ID);

        assertThat(this.genreDaoJdbc.getById(DEFAULT_GENRE_ID)).isNotPresent();
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
                .hasValueSatisfying(genre -> assertThat(genre.getName()).isEqualTo(newGenreName));
    }

    @Test
    @DisplayName("должен выбросить исключение при передаче пустого идентификатора в метод обновления")
    void shouldFailOnUpdateWithNullId() {
        assertThatThrownBy(() -> this.genreDaoJdbc.update(Genre.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}