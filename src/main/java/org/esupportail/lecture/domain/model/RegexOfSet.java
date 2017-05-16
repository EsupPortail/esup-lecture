/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * Regex definition of a group :
 * Every user that "attribute x" match with "pattern Y".
 * @author jgribonvald
 *
 */
public class RegexOfSet {

	/*
	 *************************** PROPERTIES ******************************** */
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(RegexOfSet.class);

	/**
	 * attribute required value.
	 */
	private String attribute = "";

	/**
	 * Value required by the attribute to be in the group that is defined.
	 */
	private String pattern = "";

	/*
	 ************************** INIT *********************************/
	/**
	 * Constructor.
	 */
	public RegexOfSet() {
		// Nothing to do
	}

	/*
	 *************************** METHODS ******************************** */

	/**
	 * Return true if user checks this regular, else returns false
	 * (returns false when no answer or error from externalService).
	 * @return boolean
	 */
	protected boolean evaluate() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("evaluate()");
		}

		List<String> userAttributeValues;
		try {
			ExternalService ex = DomainTools.getExternalService();
			userAttributeValues = ex.getUserAttribute(attribute);
		} catch (NoExternalValueException e) {
			LOG.warn("User attribute evaluation impossible (NoExternalValueException) : " + e.getMessage());
			return false;
		} catch (InternalExternalException e) {
			LOG.error("User attribute evaluation impossible (external service unavailable) : "
					+ e.getMessage());
			// TODO (GB later) revoir ?
			return false;
		}
		// TODO (GB later) voir le cas ou il y est mais que le portail ne connait pas

		boolean found = false;
		Iterator<String> itAttrs = userAttributeValues.iterator();
		if (pattern != null && pattern.length() > 0) {
			Pattern patrn = Pattern.compile(pattern);
			while (!found && itAttrs.hasNext()){
				final String userAttrVal = itAttrs.next();
				found = patrn.matcher(userAttrVal).matches();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Try to match pattern '" + pattern + "' on user attribute value '" + userAttrVal + "' return : " + found);
				}
			}
		}
		return found;
	}


	/**
	 * Check existence of attributes names used in regular definition
	 * Not used for the moment : see later
	 * Not ready to use without modification.
	 * @deprecated
	 */
	@Deprecated
	protected void checkNamesExistence() {
	   	if (LOG.isDebugEnabled()) {
	   		LOG.debug("checkNamesExistence()");
    	}
		// TODO (GB later) vérification de l'existence de l'attribut dans le portail :impossible
	   	// on ne peut verifier que sa declaration dans le portlet.xml ? + log.warn si pb
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String string = "";
		string += "attribute : " + attribute;
		string += ", pattern : " + pattern;

		return string;
	}

	/*
	 *************************** ACCESSORS ******************************** */

	/**
	 * Returns attribute name.
	 * @return attribute
	 * @see RegexOfSet#attribute
	 */
	protected String getAttribute() {
		return attribute;
	}
	/**
	 * Sets attribute name.
	 * @param attribute
	 * @see RegexOfSet#attribute
	 */
	public void setAttribute(final String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns attribute required value.
	 * @return value
	 * @see RegexOfSet#pattern
	 */
	protected String getPattern() {
		return pattern;
	}
	/**
	 * Sets attribute required value.
	 * @param pattern
	 * @see RegexOfSet#pattern
	 */
	 public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
