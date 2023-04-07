package one.design.exception;

import lombok.extern.slf4j.Slf4j;
import one.design.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity duplicateUserId(IllegalArgumentException e){
        log.error("duplicate userId", e);
        return new ExceptionEntity("duplicate", e.getMessage());
    }



}
