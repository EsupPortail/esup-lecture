package org.esupportail.lecture.exceptions;

/**
 * Exception tthrowed when there is a warning
 * @author gbouteil
 *
 */
public class WarningException extends RuntimeException {

	public WarningException(String msg){
		super(msg);
	}
}
