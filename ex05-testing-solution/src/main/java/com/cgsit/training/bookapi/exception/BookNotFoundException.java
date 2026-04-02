package com.cgsit.training.bookapi.exception;

public class BookNotFoundException extends ServiceException {

    public BookNotFoundException(long id) {
        super("Book with id " + id + " not found", 404);
    }
}
