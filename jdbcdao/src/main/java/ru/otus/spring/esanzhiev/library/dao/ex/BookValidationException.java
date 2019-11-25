package ru.otus.spring.esanzhiev.library.dao.ex;

/**
 * @author Егор Санжиев
 */
public class BookValidationException extends RuntimeException {
    public BookValidationException(String message) {
        super(message);
    }
}
