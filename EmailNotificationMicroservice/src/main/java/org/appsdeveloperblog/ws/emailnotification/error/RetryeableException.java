package org.appsdeveloperblog.ws.emailnotification.error;

public class RetryeableException extends RuntimeException{

    public RetryeableException(Throwable cause) {
        super(cause);
    }

    public RetryeableException(String message) {
        super(message);
    }
}
