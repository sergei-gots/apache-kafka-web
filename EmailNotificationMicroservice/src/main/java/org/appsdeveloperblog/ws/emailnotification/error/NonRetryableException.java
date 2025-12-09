package org.appsdeveloperblog.ws.emailnotification.error;

public class NonRetryableException extends RuntimeException {

    public NonRetryableException(Throwable cause) {
        super(cause);
    }

    public NonRetryableException(String message) {
        super(message);
    }
}
