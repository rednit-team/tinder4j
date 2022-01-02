package com.rednit.tinder4j.exceptions;

/**
 * RuntimeException thrown when a non-ok http status code occurs. Non-ok means <em>200 &lt;= code &lt; 300</em> except for
 * <em>429</em>.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpStatusCodeException extends RuntimeException {

    /**
     * Constructs a new HttpStatusCodeException.
     *
     * @param message the detail message
     */
    public HttpStatusCodeException(String message) {
        super(message);
    }

}
