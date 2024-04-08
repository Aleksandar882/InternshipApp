package com.spring.internshipapp.Model.Exceptions;

public class InvalidUserCredentialsException extends RuntimeException{

    public InvalidUserCredentialsException() {
        super("Invalid user credentials exception");
    }

}
