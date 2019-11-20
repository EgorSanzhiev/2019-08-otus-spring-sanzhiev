package ru.otus.spring.esanzhiev.library.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Long id;
    private String name;
}
