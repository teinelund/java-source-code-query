package org.teinelund.application;

public class ApplicationException extends RuntimeException {
    public ApplicationException() {
        super();
    }

    public ApplicationException(final String message) {
        super(message);
    }
}