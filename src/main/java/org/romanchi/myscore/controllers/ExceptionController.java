package org.romanchi.myscore.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController {

    @Data
    @AllArgsConstructor
    @Builder
    private class ExceptionDetails{
        String message;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ExceptionDetails exception(NullPointerException e){
        ExceptionDetails es = null;
        return es;
    }
}
