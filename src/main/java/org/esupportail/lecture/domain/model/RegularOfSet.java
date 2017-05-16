/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * Regular definition of a group :
 * Every user that "attribute x" has "value Y".
 * @author gbouteil
 *
 */
public class RegularOfSet {

	/*
	 *************************** PROPERTIES ******************************** */
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(RegularOfSet.class);

	/**
	 * attribute required value.
	 */
	private String attribute = "";

	/**
	 * Value required by the attribute to be in the group that is defined.
	 */
	private String value = "";

	/*
	 ************************** INIT *********************************/
	/**
	 * Constructor.
	 */
	public RegularOfSet() {
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
		if (value != null && value.length() > 0) {
			found =  userAttributeValues.contains(value);
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
		string += ", value : " + value;

		return string;
	}

	/*
	 *************************** ACCESSORS ******************************** */

	/**
	 * Returns attribute name.
	 * @return attribute
	 * @see RegularOfSet#attribute
	 */
	protected String getAttribute() {
		return attribute;
	}
	/**
	 * Sets attribute name.
	 * @param attribute
	 * @see RegularOfSet#attribute
	 */
	public void setAttribute(final String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns attribute required value.
	 * @return value
	 * @see RegularOfSet#value
	 */
	protected String getValue() {
		return value;
	}
	/**
	 * Sets attribute required value.
	 * @param value
	 * @see RegularOfSet#value
	 */
	 public void setValue(final String value) {
		this.value = value;
	}



}
