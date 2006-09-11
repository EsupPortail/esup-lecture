package org.esupportail.commons.logging;

/**
 * The interface of logging classes.
 */
public interface Logger {

	/**
	 * This method should be called to prevent from performing expensive operations (such 
	 * as String concatenation) when the logger level is more than debug. 
	 * @return true if the DEBUG level of the service is enabled.
	 */
	boolean isDebugEnabled();

	/**
	 * LoggerImpl a string and a duration as DEBUG.
	 * @param str a string
	 * @param start the start time
	 */
	void debugTime(final String str, final long start);

	/**
	 * LoggerImpl a StringBuffer as DEBUG.
	 * @param sb a StringBuffer
	 */
	void debug(final StringBuffer sb);

	/**
	 * LoggerImpl a string as DEBUG.
	 * @param str a string
	 */
	void debug(final String str);

	/**
	 * LoggerImpl a throwable as DEBUG.
	 * @param t a throwable
	 */
	void debug(final Throwable t);

	/**
	 * LoggerImpl a string and a throwable as DEBUG.
	 * @param str a string
	 * @param t a throwable
	 */
	void debug(final String str, final Throwable t);

	/**
	 * LoggerImpl a string buffer and a throwable as DEBUG.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void debug(final StringBuffer sb, final Throwable t);

	/**
	 * LoggerImpl a string as INFO.
	 * @param str a string
	 */
	void info(final String str);

	/**
	 * LoggerImpl a StringBuffer as INFO.
	 * @param sb a StringBuffer
	 */
	void info(final StringBuffer sb);

	/**
	 * LoggerImpl a throwable as INFO.
	 * @param t a throwable
	 */
	void info(final Throwable t);

	/**
	 * LoggerImpl a string and a throwable as INFO.
	 * @param str a string
	 * @param t a throwable
	 */
	void info(final String str, final Throwable t);

	/**
	 * LoggerImpl a string buffer and a throwable as INFO.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void info(final StringBuffer sb, final Throwable t);

	/**
	 * LoggerImpl a string as WARN.
	 * @param str a string
	 */
	void warn(final String str);

	/**
	 * LoggerImpl a StringBuffer as WARN.
	 * @param sb a StringBuffer
	 */
	void warn(final StringBuffer sb);

	/**
	 * LoggerImpl a throwable as WARN.
	 * @param t a throwable
	 */
	void warn(final Throwable t);

	/**
	 * LoggerImpl a string and a throwable as WARN.
	 * @param str a string
	 * @param t a throwable
	 */
	void warn(final String str, final Throwable t);

	/**
	 * LoggerImpl a string buffer and a throwable as WARN.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void warn(final StringBuffer sb, final Throwable t);

	/**
	 * LoggerImpl a string as ERROR.
	 * @param str a string
	 */
	void error(final String str);

	/**
	 * LoggerImpl a StringBuffer as ERROR.
	 * @param sb a StringBuffer
	 */
	void error(final StringBuffer sb);

	/**
	 * LoggerImpl a throwable as ERROR.
	 * @param t a throwable
	 */
	void error(final Throwable t);

	/**
	 * LoggerImpl a string and a throwable as ERROR.
	 * @param str a string
	 * @param t a throwable
	 */
	void error(final String str, final Throwable t);

	/**
	 * LoggerImpl a string buffer and a throwable as ERROR.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void error(final StringBuffer sb, final Throwable t);

}