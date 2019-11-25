package ru.otus.spring.esanzhiev.library.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.esanzhiev.library.dao.BookGenreRelDao;
import ru.otus.spring.esanzhiev.library.dao.GenreDao;
import ru.otus.spring.esanzhiev.library.domain.Book;
import ru.otus.spring.esanzhiev.library.domain.Genre;
import ru.otus.spring.esanzhiev.library.services.GenreService;
import ru.otus.spring.esanzhiev.library.services.ex.GenreHasBooksException;
import ru.otus.spring.esanzhiev.library.services.ex.GenreNotFoundException;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final BookGenreRelDao bookGenreRelDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao, BookGenreRelDao bookGenreRelDao) {
        this.genreDao = genreDao;
        this.bookGenreRelDao = bookGenreRelDao;
    }

    @Override
    public long insert(Genre genre) {
        return this.genreDao.insert(genre);
    }

    @Override
    public void update(Genre genre) {
        getById(genre.getId());
        this.genreDao.update(genre);
    }

    @Override
    public void delete(long id) {
        getById(id);

        List<Book> books = this.bookGenreRelDao.findBooksByGenreId(id);
        if (books != null && !books.isEmpty()) {
            throw new GenreHasBooksException(id);
        }

        this.genreDao.delete(id);
    }

    @Override
    public Genre getById(long id) {
        return this.genreDao.getById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }

    @Override
    public List<Genre> getAll() {
        return this.genreDao.getAll();
    }
}
