package com.org.Shopping_App.exceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler
	public void resourceNotFound(ResourceNotFound e) {
		MainException mainException = new MainException();
		mainException.setStatusCode(HttpStatus.NOT_FOUND.value());
		mainException.setErrMsg(e.getMessage());

	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleGenericException(Exception e) {
		ModelAndView modelAndView = new ModelAndView();
		MainException mainException = new MainException();
		mainException.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		mainException.setErrMsg(e.getMessage());
		modelAndView.addObject("exception", mainException);
		return modelAndView;
	}

//	@ExceptionHandler(ConstraintViolationException.class)
//	public String handleConstraintViolationException(ConstraintViolationException ex, Model m) {
//		m.addAttribute("error", ex.getMessage());
//		return "register";
//	}

}
