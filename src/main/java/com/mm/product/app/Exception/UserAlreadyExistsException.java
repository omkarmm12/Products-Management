package com.mm.product.app.Exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message){
        super(message); //constructor call for calling constructor of the RuntimeException(super class)and pass the message
    }
}
