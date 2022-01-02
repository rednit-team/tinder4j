package com.rednit.tinder4j.exceptions;

/**
 * RuntimeException that is thrown when the response parsing fails.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParsingException extends RuntimeException {

    /**
     * Constructs a new ParsingException.
     *
     * @param throwable the cause
     */
    public ParsingException(Throwable throwable) {
        super(throwable);
    }
}
