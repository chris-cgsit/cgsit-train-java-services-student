package com.cgsit.training.bookapi.mapper;

public record ErrorResponse(
    int status,
    String title,
    String detail,
    String timestamp
) {
    public ErrorResponse(int status, String title, String detail) {
        this(status, title, detail, java.time.Instant.now().toString());
    }
}
