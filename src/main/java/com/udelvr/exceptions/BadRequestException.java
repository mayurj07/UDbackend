package com.udelvr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Exception
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class BadRequestException extends IllegalArgumentException {

    public BadRequestException(String message) { super(message);}

}