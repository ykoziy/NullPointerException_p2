package com.modfathers.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.modfathers.exception.UserAlreadyExistException;

@ControllerAdvice
public class HandleExceptions {
	@ExceptionHandler(value = UserAlreadyExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse
	handleException(UserAlreadyExistException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
}
