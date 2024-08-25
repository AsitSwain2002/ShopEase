package com.org.Shopping_App.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler
	public void resourceNotFound(ResourceNotFound e) {
		MainException mainException = new MainException();
		mainException.setStatusCode(HttpStatus.NOT_FOUND.value());
		mainException.setErrMsg(e.getMessage());

	}
}
