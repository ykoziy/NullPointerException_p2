package com.modfathers.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.exception.ProductAlreadyExists;
import com.modfathers.exception.ProductDoesNotExist;
import com.modfathers.exception.UserAlreadyExistException;
import com.modfathers.exception.UserAuthenticationException;

@ControllerAdvice
public class HandleExceptions {
	@ExceptionHandler(value = UserAlreadyExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse
	handleException(UserAlreadyExistException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
	
	@ExceptionHandler(value = UserAuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody ErrorResponse
	handleException(UserAuthenticationException ex) {
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
	}
	
	@ExceptionHandler(value = DataNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse
	handleException(DataNotFoundException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
	
	@ExceptionHandler(value = ProductAlreadyExists.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse
	handleException(ProductAlreadyExists ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
	
	@ExceptionHandler(value = ProductDoesNotExist.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse
	handleException(ProductDoesNotExist ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
}
