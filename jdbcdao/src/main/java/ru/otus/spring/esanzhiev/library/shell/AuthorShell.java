package ru.otus.spring.esanzhiev.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.esanzhiev.library.domain.Author;
import ru.otus.spring.esanzhiev.library.services.AuthorService;

import java.util.List;

@ShellComponent
public class AuthorShell {
    private final AuthorService authorService;

    @Autowired
    public AuthorShell(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ShellMethod(value = "Add author", key = {"add-author", "aa"})
    public Long addAuthor(@ShellOption("-n") String name) {
        return this.authorService.insert(Author.builder()
                .name(name)
                .build());
    }

    @ShellMethod(value = "Get author by id", key = {"get-author", "ga"})
    public Author getById(@ShellOption({"-id"}) Long id) {
        return this.authorService.getById(id);
    }

    @ShellMethod(value = "Get all authors", key = {"get-authors", "gas"})
    public List<Author> getAllAuthors() {
        return this.authorService.getAll();
    }

    @ShellMethod(value = "Update author", key = {"update-author", "ua"})
    public void updateAuthor(@ShellOption("-id") Long id,
                             @ShellOption("-n") String name) {
        this.authorService.update(Author.builder()
                .id(id)
                .name(name)
                .build());
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public void deleteAuthor(@ShellOption("-id") Long id) {
        this.authorService.delete(id);
    }

}
