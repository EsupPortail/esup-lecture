package org.esupportail.lecture.exceptions;

/**
 * Exception tthrowed when there is an error
 * @author gbouteil
 *
 */
public class ErrorExceptionAVirer extends  RuntimeException{
	
	public ErrorExceptionAVirer(String msg){
		super(msg);
	}
	public ErrorExceptionAVirer(){
		super();
	}
}
