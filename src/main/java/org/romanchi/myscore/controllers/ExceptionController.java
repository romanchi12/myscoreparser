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
    private class ExceptionDetails{
        String message;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ExceptionDetails exception(NullPointerException e){
        return new ExceptionDetails("Null pointer exception  "
                + e.getStackTrace()[0].getFileName()
                + "(" + e.getStackTrace()[0].getMethodName()
                + ") at line: " + e.getStackTrace()[0].getLineNumber());
    }
}
