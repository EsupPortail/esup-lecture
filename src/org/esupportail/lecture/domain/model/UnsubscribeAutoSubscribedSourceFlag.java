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
public final class UnsubscribeAutoSubscribedSourceFlag extends Flag {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Constructor.
	 */
	protected UnsubscribeAutoSubscribedSourceFlag() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("UnsubscribeAutoSubscribedSourceFlag()");
		}
	}

	/**
	 * @param customManagedCategory
	 * @param elementId
	 */
	protected UnsubscribeAutoSubscribedSourceFlag(
			CustomManagedCategory customManagedCategory, String elementId) {
		this.customManagedCategory = customManagedCategory;
		this.elementId = elementId;
	}

	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(UnsubscribeAutoSubscribedSourceFlag.class);
	/**
	 *  UnsubscribeAutoSubscribedSourceFlag unsubscribedSourcePK.
	 */
	private long unsubscribedSourcePK;

	/**
	 *  CustomCategory.
	 */
	private CustomManagedCategory customManagedCategory;

	/**
	 *  Flag elementId.
	 */
	private String elementId;

	/**
	 * @return id
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @param elementId
	 */
	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the unsubscribedSourcePK
	 */
	public long getUnsubscribedSourcePK() {
		return unsubscribedSourcePK;
	}

	/**
	 * @param unsubscribedSourcePK the unsubscribedSourcePK to set
	 */
	public void setUnsubscribedSourcePK(long unsubscribedSourcePK) {
		this.unsubscribedSourcePK = unsubscribedSourcePK;
	}

	/**
	 * @return the customCategory
	 */
	public CustomManagedCategory getCustomManagedCategory() {
		return customManagedCategory;
	}

	/**
	 * @param customManagedCategory the customCategory to set
	 */
	public void setCustomManagedCategory(CustomManagedCategory customManagedCategory) {
		this.customManagedCategory = customManagedCategory;
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
		string += "	id : " + getUnsubscribedSourcePK() + "\n";
			
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
		UnsubscribeAutoSubscribedSourceFlag other = (UnsubscribeAutoSubscribedSourceFlag) obj;
		if (elementId == null) {
			if (other.elementId != null) {
				return false;
			}
		} else if (!elementId.equals(other.elementId)) {
			return false;
		}
		if (customManagedCategory == null) {
			if (other.customManagedCategory != null) {
				return false;
			}
		} else if (!customManagedCategory.equals(other.customManagedCategory)) {
			return false;
		}
		return true;
	}

}
