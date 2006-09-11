/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 */
package org.esupportail.commons.logging; 

import org.apache.commons.logging.LogFactory;

/**
 * A class based on Jakarta commons-logging that provides logging features.
 */
public class LoggerImpl implements Logger {

	/**
	 * a static physical logger.
	 */
	private static org.apache.commons.logging.Log logger;

	/**
	 * Constructor.
	 * @param logClass the class the logger will be used by.
	 */
	@SuppressWarnings("unchecked")
	public LoggerImpl(final Class logClass) {
		logger = LogFactory.getLog(logClass);
	}

	/**
	 * @see org.esupportail.commons.logging.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * @see org.esupportail.commons.logging.Logger#debugTime(java.lang.String, long)
	 */
	public void debugTime(final String str, final long start) {
		long duration = System.currentTimeMillis() - start;
		StringBuffer sb = new StringBuffer();
		sb.append("duration: ").append(duration).append(" -> ").append(str);
		debug(sb);
	}

	/**
	 * @see org.esupportail.commons.logging.Logger#debug(java.lang.StringBuffer)
	 */
	public void debug(final StringBuffer sb) {
		debug(sb.toString());
	}

	/**
	 * @see org.esupportail.commons.logging.Logger#debug(java.lang.String)
	 */
	public void debug(final String str) {
		if (str == null) {
			logger.debug("null");
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.debug(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#debug(java.lang.Throwable)
	 */
	public void debug(final Throwable t) {
		debug(LogUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	public void debug(final String str, final Throwable t) {
		debug(str);
		debug(t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#debug(java.lang.StringBuffer, java.lang.Throwable)
	 */
	public void debug(final StringBuffer sb, final Throwable t) {
		debug(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#info(java.lang.String)
	 */
	public void info(final String str) {
		if (str == null) {
			logger.info("null");
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.info(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#info(java.lang.StringBuffer)
	 */
	public void info(final StringBuffer sb) {
		info(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#info(java.lang.Throwable)
	 */
	public void info(final Throwable t) {
		info(LogUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	public void info(final String str, final Throwable t) {
		info(str);
		info(t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#info(java.lang.StringBuffer, java.lang.Throwable)
	 */
	public void info(final StringBuffer sb, final Throwable t) {
		info(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#warn(java.lang.String)
	 */
	public void warn(final String str) {
		if (str == null) {
			logger.warn("null");
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.warn(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#warn(java.lang.StringBuffer)
	 */
	public void warn(final StringBuffer sb) {
		warn(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#warn(java.lang.Throwable)
	 */
	public void warn(final Throwable t) {
		warn(LogUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	public void warn(final String str, final Throwable t) {
		warn(str);
		warn(t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#warn(java.lang.StringBuffer, java.lang.Throwable)
	 */
	public void warn(final StringBuffer sb, final Throwable t) {
		warn(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#error(java.lang.String)
	 */
	public void error(final String str) {
		if (str == null) {
			logger.error("null");
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.error(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#error(java.lang.StringBuffer)
	 */
	public void error(final StringBuffer sb) {
		error(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#error(java.lang.Throwable)
	 */
	public void error(final Throwable t) {
		error(LogUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	public void error(final String str, final Throwable t) {
		error(str);
		error(t);
	}
	
	/**
	 * @see org.esupportail.commons.logging.Logger#error(java.lang.StringBuffer, java.lang.Throwable)
	 */
	public void error(final StringBuffer sb, final Throwable t) {
		error(sb.toString(), t);
	}
	
}

