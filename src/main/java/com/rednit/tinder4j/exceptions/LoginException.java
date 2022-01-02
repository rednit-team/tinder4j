package com.rednit.tinder4j.exceptions;

/**
 * RuntimeException indicating the provided X-Auth-Token is invalid.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginException extends RuntimeException {

    /**
     * Constructs a new LoginException.
     *
     * @param message the detail message
     */
    public LoginException(String message) {
        super(message);
    }

}
