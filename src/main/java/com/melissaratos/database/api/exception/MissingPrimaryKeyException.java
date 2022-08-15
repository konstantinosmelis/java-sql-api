package com.melissaratos.database.api.exception;

public class MissingPrimaryKeyException extends Exception {

    public MissingPrimaryKeyException() {
        super();
    }

    public MissingPrimaryKeyException(String message) {
        super(message);
    }
}
