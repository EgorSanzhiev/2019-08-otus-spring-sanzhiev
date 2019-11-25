package ru.otus.spring.esanzhiev.library.domain;

import lombok.Data;

@Data
public class BookGenreRel {
    private final long bookId;
    private final long genreId;
}
