package org.esupportail.lecture.exceptions.domain;


/**
 * Exception throwed when there is a fatal error
 * @author gbouteil
 *
 */public class FatalException extends InfoDomainException {
	public FatalException(){
		super();
	}
	public FatalException(String msg){
		super(msg);
	}
	public FatalException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}
}
