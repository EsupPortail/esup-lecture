/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import java.util.Collection;

import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.lecture.domain.utils.DummyInterface;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.UnknownException;
import org.springframework.web.util.HtmlUtils;

/**
 * A DummyBean for a ItemBean.
 * @author bourges
 *
 */
public class ItemDummyBean extends ItemBean implements DummyInterface {
	// TODO (RB/GB) Revoir le traitement des Dummy (autre que par des ifs) 
	// pour faire du vrai objet
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Cause of the Dummy Bean.
	 */
	private DomainServiceException cause;

	/*
	 *************************** INIT ************************************** */	

	/**
	 * default constructor.
	 */
	public ItemDummyBean() {
		cause = new UnknownException();
		super.setHtmlContent(null);
		super.setId(null);
		super.setRead(false);
	}
	/**
	 * Constructor.
	 * @param e exception cause of dummyBean creation
	 */
	public ItemDummyBean(final DomainServiceException e) {
		cause = e;
	}

	/*
	 *************************** ACCESSORS ********************************* */	
	/**
	 * @see org.esupportail.lecture.domain.utils.DummyInterface#getCause()
	 */
	public DomainServiceException getCause() {
		return cause;
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.ItemBean#getHtmlContent()
	 */
	@Override
	public String getHtmlContent() {
		StringBuffer buffer = new StringBuffer(cause.getMessage()).append("<br/><hr/>");
		buffer.append(html(ExceptionUtils.getShortStackTraceStrings(cause)));
		return buffer.toString();
	}
	/**
	 * @see org.esupportail.lecture.domain.beans.ItemBean#getId()
	 */
	@Override
	public String getId() {
		return "foo";
	}
	/**
	 * @see org.esupportail.lecture.domain.beans.ItemBean#isRead()
	 */
	@Override
	public boolean isRead() {
		return false;
	}
	/**
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
			result.append(separator).append(HtmlUtils.htmlEscape(string));
			separator = "<br/>";
		}
		return result.toString();
	}
	
	
	// TODO (RB/VC <-- GB : hum hum ...)
	/**
	 * @see org.esupportail.lecture.domain.beans.ItemBean#isDummy()
	 * return true because this a dummy bean
	 */
	@Override
	public boolean isDummy() {
		return true;
	}
	
}
