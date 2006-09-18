package org.esupportail.lecture.utils.exception;

public class FatalException extends RuntimeException {
	public FatalException(){
		super();
	}
	public FatalException(String msg){
		super(msg);
	}
}
