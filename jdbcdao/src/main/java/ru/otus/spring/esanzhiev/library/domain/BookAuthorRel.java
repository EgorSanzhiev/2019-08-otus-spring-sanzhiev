package ru.otus.spring.esanzhiev.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookAuthorRel {
    private long bookId;
    private long authorId;
}
