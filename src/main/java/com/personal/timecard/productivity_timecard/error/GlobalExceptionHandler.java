package com.personal.timecard.productivity_timecard.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

import static com.personal.timecard.productivity_timecard.constant.ApplicationConstants.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimecardAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<Map<String, Object>> handleTimecardAlreadyExists(
            TimecardAlreadyExistsException ex,
            ServerWebExchange exchange
    ) {

        return Mono.just(Map.of(
                STATUS, HttpStatus.CONFLICT.value(),
                MESSAGE, ex.getMessage(),
                PATH, exchange.getRequest().getPath().value(),
                TIMESTAMP, LocalDateTime.now()
        ));
    }

}