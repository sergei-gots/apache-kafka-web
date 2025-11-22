package org.appsdeveloperblog.ws.products.rest;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {
    Instant timestamp;
    String message;
    Exception e;
}
