package com.mm.product.app.controllerAdviser;

import com.mm.product.app.Exception.UserAlreadyExistsException;
import com.mm.product.app.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdviser {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?>userNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>invalidRequestException(MethodArgumentNotValidException e){
        Map<String,String>errormap=new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errormap.put(fieldError.getField(),fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormap);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StringBuilder>constraintViolations(ConstraintViolationException e){
        StringBuilder errormessage=new StringBuilder();
        for(ConstraintViolation<?> violation:e.getConstraintViolations()){
            errormessage
                    .append(violation.getPropertyPath())
                    .append(" : ")
                    .append(violation.getMessage())
                    .append(".");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormessage);
    }
    //accuse when spring will not handle exceptions & get this when Hibernate handle validation
}
