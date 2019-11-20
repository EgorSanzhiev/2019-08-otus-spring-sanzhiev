package ru.otus.spring.esanzhiev.library.services.impl;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.spring.esanzhiev.library.dao.AuthorDao;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.services.AuthorService;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorNotFoundException;
import ru.otus.spring.esanzhiev.library.services.ex.AuthorValidationException;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public long insert(@NonNull Author author) {
        validateName(author.getName());
        return this.authorDao.insert(author);
    }

    @Override
    public void update(Author author) {
        validateName(author.getName());

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

        this.authorDao.delete(id);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new AuthorValidationException("Author name cannot be empty");
        }
    }
}
