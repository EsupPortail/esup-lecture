/**
 * 
 */
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author vrepain
 *
 */
public final class UnsubscribeAutoSubscribedCategoryFlag extends Flag {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Constructor.
	 */
	protected UnsubscribeAutoSubscribedCategoryFlag() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("UnsubscribeAutoSubscribedCategoryFlag()");
		}
	}

	/**
	 * @param customContext
	 * @param elementId
	 */
	protected UnsubscribeAutoSubscribedCategoryFlag(
			CustomContext customContext, String elementId) {
		this.customContext = customContext;
		this.elementId = elementId;
	}

	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(UnsubscribeAutoSubscribedCategoryFlag.class);
	/**
	 *  UnsubscribeAutoSubscribedCategoryFlag unsubscribedCategoryPK.
	 */
	private long unsubscribedCategoryPK;

	/**
	 *  customSource.
	 */
	private CustomContext customContext;

	/**
	 *  Flag elementId.
	 */
	private String elementId;

	/**
	 * @return id
	 * @see Flag#id
	 * @see ElementProfile#getId()
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @param id
	 * @see Flag#id
	 */
	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the unsubscribedCategoryPK
	 */
	public long getUnsubscribedCategoryPK() {
		return unsubscribedCategoryPK;
	}

	/**
	 * @param unsubscribedCategoryPK the unsubscribedCategoryPK to set
	 */
	public void setUnsubscribedCategoryPK(long unsubscribedCategoryPK) {
		this.unsubscribedCategoryPK = unsubscribedCategoryPK;
	}

	/**
	 * @return the customSource
	 */
	public CustomContext getCustomContext() {
		return customContext;
	}

	/**
	 * @param customSource the customSource to set
	 */
	public void setCustomContext(CustomContext customContext) {
		this.customContext = customContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementId == null) ? 0 : elementId.hashCode());
		return result;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String string = "";
		
		/* The Flag Id */
		string += "	id : " + getUnsubscribedCategoryPK() + "\n";
			
		/* The Flag date */
		//string += "	id : " + getId() + "\n";
		
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnsubscribeAutoSubscribedCategoryFlag other = (UnsubscribeAutoSubscribedCategoryFlag) obj;
		if (elementId == null) {
			if (other.elementId != null) {
				return false;
			}
		} else if (!elementId.equals(other.elementId)) {
			return false;
		}
		if (customContext == null) {
			if (other.customContext != null) {
				return false;
			}
		} else if (!customContext.equals(other.customContext)) {
			return false;
		}
		return true;
	}

}
