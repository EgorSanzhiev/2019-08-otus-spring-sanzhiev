package ru.otus.spring.esanzhiev.library.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.esanzhiev.library.dao.GenreDao;
import ru.otus.spring.esanzhiev.library.domain.Genre;
import ru.otus.spring.esanzhiev.library.services.ex.GenreNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис для работы с жанрами")
class GenreServiceImplTest {
    @Mock
    private GenreDao genreDaoMock;

    @Test
    @DisplayName("должен возвращать идентификатор добавленного жанра")
    void shouldInsertGenre() {
        long genreIdToBeAdded = 1L;
        doReturn(genreIdToBeAdded)
                .when(genreDaoMock)
                .insert(any(Genre.class));

        GenreServiceImpl genreService = new GenreServiceImpl(genreDaoMock);

        long newGenreId = genreService.insert(Genre.builder().name("andrey").build());
        assertThat(genreIdToBeAdded).isEqualTo(newGenreId);
    }

    @Test
    @DisplayName("должен возвращать жанр по идентификатору")
    void shouldReturnGenreById() {
        long mockGenreId = 1L;
        String mockGenreName = "mock genre";

        Genre mockGenre = Genre.builder()
                .id(mockGenreId)
                .name(mockGenreName)
                .build();

        doReturn(Optional.of(mockGenre))
                .when(genreDaoMock)
                .getById(eq(mockGenreId));

        GenreServiceImpl genreService = new GenreServiceImpl(genreDaoMock);
        assertThat(genreService.getById(mockGenreId))
                .matches(genre -> mockGenreId == genre.getId() && mockGenreName.equals(genre.getName()));
    }

    @Test
    @DisplayName("должен выбрасывать исключение, если жанр не найден по идентфиикатору")
    void shouldThrowExceptionIfGenreNotFoundById() {
        long nonPresentGenreId = 1L;
        doReturn(Optional.empty())
                .when(genreDaoMock)
                .getById(nonPresentGenreId);

        GenreServiceImpl genreService = new GenreServiceImpl(genreDaoMock);
        assertThatThrownBy(() -> genreService.getById(nonPresentGenreId))
                .isInstanceOf(GenreNotFoundException.class);
    }
}