package ru.otus.spring.esanzhiev.library.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Book {
    private Long id;
    private String name;
    private LocalDate publicationDate;
    private List<Author> authors;
    private List<Genre> genres;
}
