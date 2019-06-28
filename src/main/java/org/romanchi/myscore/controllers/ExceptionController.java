package org.romanchi.myscore.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@ControllerAdvice
@RestController
public class ExceptionController {

    @Data
    @AllArgsConstructor
    private class ExceptionDetails{
        String message;
    }

    @ExceptionHandler(value = ParseException.class)
    public ExceptionDetails exception(ParseException e){
        return new ExceptionDetails("Parse exception  "
                + e.getStackTrace()[0].getFileName()
                + "(" + e.getStackTrace()[0].getMethodName()
                + ") at line: " + e.getStackTrace()[0].getLineNumber());
    }
}
