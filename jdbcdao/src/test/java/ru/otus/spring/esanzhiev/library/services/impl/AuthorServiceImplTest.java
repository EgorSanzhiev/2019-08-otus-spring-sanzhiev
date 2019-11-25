package ru.otus.spring.esanzhiev.library.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.dao.BookAuthorRelDao;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorHasBooksException;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис для работы с авторами")
class AuthorServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private BookAuthorRelDao bookAuthorRelDao;

    @Test
    @DisplayName("должен возвращать идентификатор добавленного автора")
    void shouldInsertAuthor() {
        long authorIdToBeAdded = 1L;
        doReturn(authorIdToBeAdded)
                .when(authorDao)
                .insert(any(Author.class));

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao, bookAuthorRelDao);

        assertThat(authorService.insert(Author.builder().name("andrey").build()))
                .isEqualTo(authorIdToBeAdded);
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

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao, bookAuthorRelDao);
        assertThat(authorService.getById(mockAuthorId))
                .matches(author -> mockAuthorId == author.getId() && mockAuthorName.equals(author.getName()));
    }

    @Test
    @DisplayName("должен выбрасывать исключение, если автор не найден по идентфиикатору")
    void shouldThrowExceptionIfAuthorNotFoundById() {
        long nonPresentAuthorId = 1L;
        doReturn(Optional.empty())
                .when(authorDao)
                .getById(eq(nonPresentAuthorId));

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao, bookAuthorRelDao);
        assertThatThrownBy(() -> authorService.getById(nonPresentAuthorId))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    @DisplayName("должен выбрасывать исключение при попытке удаления автора, у которого есть связанные с ним книги")
    void shouldThrowExceptionWhenDeletingAuthorWithRelatedBooks() {
        long authorId = 1L;
        doReturn(Optional.of(Author.builder().build()))
                .when(authorDao)
                .getById(eq(authorId));

        doReturn(Collections.singletonList(Book.builder().build()))
                .when(bookAuthorRelDao)
                .findBooksByAuthorId(eq(authorId));

        AuthorServiceImpl authorService = new AuthorServiceImpl(authorDao, bookAuthorRelDao);
        assertThatThrownBy(() -> authorService.delete(authorId))
                .isInstanceOf(AuthorHasBooksException.class);
    }
}