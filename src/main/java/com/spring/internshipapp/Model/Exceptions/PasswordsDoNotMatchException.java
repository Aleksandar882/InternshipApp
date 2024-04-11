package com.spring.internshipapp.Model.Exceptions;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("PasswordsDoNotMatchException");
    }
}
