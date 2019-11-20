package ru.otus.spring.esanzhiev.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.esanzhiev.library.domain.Genre;
import ru.otus.spring.esanzhiev.library.services.GenreService;

import java.util.List;

@ShellComponent
public class GenreShell {
    private final GenreService genreService;

    @Autowired
    public GenreShell(GenreService genreService) {
        this.genreService = genreService;
    }

    @ShellMethod(value = "Add genre", key = {"add-genre", "ag"})
    public Long addGenre(@ShellOption("-n") String name) {
        return this.genreService.insert(Genre.builder()
                .name(name)
                .build());
    }

    @ShellMethod(value = "Get genre by id", key = {"get-genre", "gg"})
    public Genre getById(@ShellOption({"-id"}) Long id) {
        return this.genreService.getById(id);
    }

    @ShellMethod(value = "Get all genres", key = {"get-genres", "ggs"})
    public List<Genre> getAllGenres() {
        return this.genreService.getAll();
    }

    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public void updateGenre(@ShellOption("-id") Long id,
                             @ShellOption("-n") String name) {
        this.genreService.update(Genre.builder()
                .id(id)
                .name(name)
                .build());
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public void deleteGenre(@ShellOption("-id") Long id) {
        this.genreService.delete(id);
    }
}
