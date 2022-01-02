package com.rednit.tinder4j.exceptions;

public class ParsingException extends RuntimeException {

    public ParsingException() {
        super();
    }

    public ParsingException(Throwable throwable) {
        super(throwable);
    }

    public ParsingException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
