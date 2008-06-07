/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import java.util.Collection;

import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

/**
 * A DummyBean for a ItemBean : every methods are overriden from SourceBean and throw an ElementDummyBeanException
 * @author gbouteil
 *
 */
public class ItemDummyBean extends ItemBean implements DummyBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Cause of the Dummy Bean
	 */
	DomainServiceException cause;

	/*
	 *************************** INIT ************************************** */	

	/**
	 * default constructor
	 */
	public ItemDummyBean() {
		cause = new UnknownException();
	}
	/**
	 * Constructor
	 * @param e exception cause of dummyBean creation
	 */
	public ItemDummyBean(DomainServiceException e) {
		cause = e;
	}

	/*
	 *************************** ACCESSORS ********************************* */	
	/**
	 * @see org.esupportail.lecture.domain.beans.DummyBean#getCause()
	 */
	public DomainServiceException getCause() {
		return cause;
	}

	/*
	 * @see org.esupportail.lecture.domain.beans.ItemBean#getHtmlContent()
	 */
	@Override
	public String getHtmlContent() {
		StringBuffer buffer = new StringBuffer(cause.getMessage()).append("<br/><hr/>");
		buffer.append(html(ExceptionUtils.getShortStackTraceStrings(cause)));
		return buffer.toString();
	}
	/*
	 * @see org.esupportail.lecture.domain.beans.ItemBean#getId()
	 */
	@Override
	public String getId() {
		return "foo";
	}
	/*
	 * @see org.esupportail.lecture.domain.beans.ItemBean#isRead()
	 */
	@Override
	public boolean isRead() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.esupportail.lecture.domain.beans.ItemBean#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	/**
	 * @param strings
	 * @return a printable String for text outputs.
	 */
	private String html(final Collection<String> strings) {
		if (strings == null || strings.isEmpty()) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		String separator = "";
		for (String string : strings) {
			result.append(separator).append(StringUtils.escapeHtml(string));
			separator = "<br/>";
		}
		return result.toString();
	}
	
}
