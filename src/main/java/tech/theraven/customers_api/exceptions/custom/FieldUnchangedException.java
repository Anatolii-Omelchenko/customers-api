package tech.theraven.customers_api.exceptions.custom;

import org.springframework.http.HttpStatus;

public class FieldUnchangedException extends BasicApplicationException {

    /**
     * Constructs BasicApplicationException object
     * with the specified message and HTTP status.
     *
     * @param message Error message explaining the reason for the exception.
     */
    public FieldUnchangedException(String message) {
        super(message, HttpStatus.NOT_MODIFIED);
    }
}
