package com.vinceadamo.authproxy.authproxy.exceptions;

public class InvalidAuthHeaderException extends Exception { 
    public InvalidAuthHeaderException(String errorMessage) {
        super(errorMessage);
    }
}