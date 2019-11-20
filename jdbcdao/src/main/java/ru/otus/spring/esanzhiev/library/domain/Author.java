package ru.otus.spring.esanzhiev.library.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private long id;

    private String name;
}
