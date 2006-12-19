package org.esupportail.lecture.exceptions.web;

public class PrivateException extends Exception {
	public PrivateException(Exception e) {
		super(e);
	}
	
	public PrivateException(String message) {
		super(message);
	}
}
