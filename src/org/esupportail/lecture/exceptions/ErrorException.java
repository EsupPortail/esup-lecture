package org.esupportail.lecture.exceptions;

/**
 * Exception tthrowed when there is an error
 * @author gbouteil
 *
 */
public class ErrorException extends  RuntimeException{
	
	public ErrorException(String msg){
		super(msg);
	}
	public ErrorException(){
		super();
	}
}
