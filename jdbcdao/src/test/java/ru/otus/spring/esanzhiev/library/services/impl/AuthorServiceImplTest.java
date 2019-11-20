package ru.otus.spring.esanzhiev.library.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис для работы с авторами")
class AuthorServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    @Test
    @DisplayName("должен возвращать идентификатор добавленного автора")
    void shouldInsertAuthor() {
        long authorIdToBeAdded = 1L;
        doReturn(authorIdToBeAdded)
                .when(authorDao)
                .insert(any(Author.class));

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao);

        long newAuthorId = authorService.insert(Author.builder().name("andrey").build());
        assertEquals(authorIdToBeAdded, newAuthorId);
    }

    @Test
    @DisplayName("должен возвращать автора по идентификатору")
    void shouldReturnAuthorById() {
        long mockAuthorId = 1L;
        String mockAuthorName = "mock author";

        Author mockAuthor = Author.builder()
                .id(mockAuthorId)
                .name(mockAuthorName)
                .build();

        doReturn(Optional.of(mockAuthor))
                .when(authorDao)
                .getById(eq(mockAuthorId));

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao);
        assertThat(authorService.getById(mockAuthorId))
                .matches(author -> mockAuthorId == author.getId() && mockAuthorName.equals(author.getName()));
    }

    @Test
    @DisplayName("должен выбрасывать исключение, если автор не найден по идентфиикатору")
    void shouldThrowExceptionIfAuthorNotFoundById() {
        long nonPresentAuthorId = 1L;
        doReturn(Optional.empty())
                .when(authorDao)
                .getById(nonPresentAuthorId);

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao);
        assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.getById(nonPresentAuthorId)
        );
    }
}