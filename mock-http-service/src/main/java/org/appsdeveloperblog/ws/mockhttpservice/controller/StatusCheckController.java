package org.appsdeveloperblog.ws.mockhttpservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response")
@Slf4j
public class StatusCheckController {

    //curl -i -X GET 'http://localhost:8082/response/200'
    @GetMapping("/200")
    ResponseEntity<@NotNull String> response200String() {
        log.info("Return OK/200");
        return ResponseEntity.ok().body("200");
    }

    //curl -i -X GET 'http://localhost:8082/response/500'
    @GetMapping("/500")
    ResponseEntity<@NotNull Void> response500String() {
        log.info("Return ISE/500");
        return ResponseEntity.internalServerError().build();
    }
}
