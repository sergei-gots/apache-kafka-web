package org.appsdeveloperblog.ws.products.rest;

import lombok.NoArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@NoArgsConstructor
@Getter
public class ErrorMessage {

    private static Throwable getRootCause(Throwable t) {
        if (t.getCause() != null) {
            return getRootCause(t.getCause());
        } else {
            return t;
        }
    }

    String description;
    String cause;
    String resourcePath;
    Instant timestamp;

    public ErrorMessage(Throwable t, String resourcePath) {

        description = t.getMessage();
        cause = getRootCause(t).getMessage();
        this.resourcePath = resourcePath;
        timestamp = Instant.now();
    }
}
