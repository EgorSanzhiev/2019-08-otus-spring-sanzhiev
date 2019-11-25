package ru.otus.spring.esanzhiev.library.services.impl;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.dao.BookAuthorRelDao;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.services.AuthorService;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorHasBooksException;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorNotFoundException;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final BookAuthorRelDao bookAuthorRelDao;

    public AuthorServiceImpl(AuthorDao authorDao, BookAuthorRelDao bookAuthorRelDao) {
        this.authorDao = authorDao;
        this.bookAuthorRelDao = bookAuthorRelDao;
    }

    @Override
    public long insert(@NonNull Author author) {
        return this.authorDao.insert(author);
    }

    @Override
    public void update(Author author) {
        getById(author.getId());

        this.authorDao.update(author);
    }

    @Override
    public Author getById(long id) {
        return this.authorDao.getById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public List<Author> getAll() {
        return this.authorDao.getAll();
    }

    @Override
    public void delete(long id) {
        getById(id);

        List<Book> booksByAuthor = this.bookAuthorRelDao.findBooksByAuthorId(id);
        if (booksByAuthor != null && !booksByAuthor.isEmpty()) {
            throw new AuthorHasBooksException(id);
        }

        this.authorDao.delete(id);
    }
}
