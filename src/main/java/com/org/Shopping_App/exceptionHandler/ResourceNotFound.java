package com.org.Shopping_App.exceptionHandler;


public class ResourceNotFound extends RuntimeException{
	
	public ResourceNotFound(String msg) {
		super(msg);
	}
}
