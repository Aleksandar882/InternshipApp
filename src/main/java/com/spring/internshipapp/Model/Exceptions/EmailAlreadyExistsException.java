package com.spring.internshipapp.Model.Exceptions;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException() {
        super("EmailAlreadyExistsException");
    }

}
