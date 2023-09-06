package com.ratz.wemanage.exception;

public class ApiException extends RuntimeException{

    public ApiException(String message){
        super(message);
    }
}
