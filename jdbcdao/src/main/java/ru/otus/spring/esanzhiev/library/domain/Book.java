package ru.otus.spring.esanzhiev.library.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Book {
    private Long id;
    private String name;
    private Date publicationDate;
}
