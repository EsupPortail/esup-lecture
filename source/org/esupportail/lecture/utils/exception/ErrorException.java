package org.esupportail.lecture.utils.exception;

public class ErrorException extends  RuntimeException{
	public ErrorException(String msg){
		super(msg);
	}
	public ErrorException(){
		super();
	}
}
