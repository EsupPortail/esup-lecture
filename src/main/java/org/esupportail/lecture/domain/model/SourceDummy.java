/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.utils.DummyInterface;

/**
 * SourceDummy element : a source that cannot be created well.
 * @author gbouteil
 *
 */
@SuppressWarnings("serial")
public class SourceDummy extends Source implements DummyInterface {

	/* 
	 *************************** PROPERTIES ******************************** */
	/**
	 * log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(Source.class); 
	/**
	 * Cause of the Dummy Bean.
	 */
	private Exception cause;

	/*
	 *************************** INIT ************************************** */
	/**
	 * Constructor.
	 * @param sp sourceProfile associated to this source
	 * @param e 
	 */
	public SourceDummy(final SourceProfile sp, final Exception e) {
		super(sp);
		cause = e;
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("SourceDummy(" + sp.getId() + ")");
    	}
	   	
	}
	/*
	 *************************** METHODS *********************************** */
	
	/**
	 * @return the ttl of a dummy : declared in config
	 */
	@Override
	public int getTtl() {
		return DomainTools.getDummyTtl();
	}
	
	/**
	 * get Items list of this source.
	 * @return an empty items list
	 */
	@Override
	protected List<Item> getItems(ItemParser parser, final String fname) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + super.getProfileId() + " - getItems()");
    	}
		return new ArrayList<Item>();
	}
	/*
	 *************************** ACCESSORS ********************************* */

	/**
	 * @see org.esupportail.lecture.domain.utils.DummyInterface#getCause()
	 */
	public Exception getCause() {
		return cause;
	}

}
