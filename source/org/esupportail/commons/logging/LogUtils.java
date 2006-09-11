/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 */
package org.esupportail.commons.logging; 

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

/**
 * A class that provides static utilities for logging.
 */
public final class LogUtils {
	
	/**
	 * The text separator for the stack trace.
	 */
	private static final String STACK_TRACE_SEPARATOR = 
		"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";

	/**
	 * The "caused by" element.
	 */
	private static final String STACK_TRACE_CAUSED_BY = "caused by: "; 

	/**
	 * Private constructor.
	 */
	private LogUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return A list of strings that correspond to the stack trace of an exception.
	 * @param t
	 */
	public static List<String> getStackTraceStrings(final Throwable t) {
		List<String> result = new ArrayList<String>();
		result.add(t.toString());
		for (StackTraceElement element : t.getStackTrace()) {
			result.add(element.toString());
		}
		Throwable cause;
		if (t instanceof ServletException) {
			cause = ((ServletException) t).getRootCause();
		} else {
			cause = t.getCause();
		}
		if (cause != null) {
			result.add(STACK_TRACE_SEPARATOR + STACK_TRACE_CAUSED_BY);
			result.addAll(getStackTraceStrings(cause));
		}
		return result;
	}

	/**
	 * @return A printable form of the stack trace of a throwable.
	 * @param t
	 */
	public static String getPrintableStackTrace(final Throwable t) {
		StringBuffer sb = new StringBuffer();
		List<String> strings = getStackTraceStrings(t);
		String separator = "";
		for (String string : strings) {
			sb.append(separator).append(string);
			separator = "\n";
		}
		return sb.toString();
	}

	/**
	 * @return A list of strings that correspond to the short stack trace of an exception.
	 * @param t
	 * @param addPrefix true to add the "caused by" prefix
	 */
	private static List<String> getShortStackTraceStrings(final Throwable t, final boolean addPrefix) {
		List<String> result = new ArrayList<String>();
		if (addPrefix) {
			result.add(STACK_TRACE_CAUSED_BY + t.toString());
		} else {
			result.add(t.toString());
		}
		Throwable cause;
		if (t instanceof ServletException) {
			cause = ((ServletException) t).getRootCause();
		} else {
			cause = t.getCause();
		}
		if (cause != null) {
			result.addAll(getShortStackTraceStrings(cause, true));
		}
		return result;
	}

	/**
	 * @return A list of strings that correspond to the short stack trace of an exception.
	 * @param t
	 */
	public static List<String> getShortStackTraceStrings(final Throwable t) {
		return getShortStackTraceStrings(t, false);
	}

	/**
	 * @return A short printable form of the stack trace of a throwable.
	 * @param t
	 */
	public static String getShortPrintableStackTrace(final Throwable t) {
		StringBuffer sb = new StringBuffer();
		List<String> strings = getShortStackTraceStrings(t);
		String separator = "";
		for (String string : strings) {
			sb.append(separator).append(string);
			separator = "\n";
		}
		return sb.toString();
	}

}

