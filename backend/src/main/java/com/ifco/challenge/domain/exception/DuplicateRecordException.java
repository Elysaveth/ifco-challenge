package com.ifco.challenge.domain.exception;

public class DuplicateRecordException extends Exception {

    private static String MESSAGE = "There was a duplicate entry saved on database";

    public DuplicateRecordException() {
        super(MESSAGE);
    }
}
