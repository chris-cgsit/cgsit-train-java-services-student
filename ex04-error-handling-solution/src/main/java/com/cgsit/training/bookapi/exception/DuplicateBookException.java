package com.cgsit.training.bookapi.exception;

public class DuplicateBookException extends ServiceException {

    public DuplicateBookException(String isbn) {
        super("Book with ISBN '" + isbn + "' already exists", 409);
    }
}
