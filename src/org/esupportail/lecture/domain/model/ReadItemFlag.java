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
public final class ReadItemFlag extends Flag {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ReadItemFlag.class);
	/**
	 *  ReadItemFlag readItemPK.
	 */
	private long readItemPK;

	/**
	 *  ReadItemFlag customSource.
	 */
	private CustomSource customSource;

	/**
	 *  ReadItemFlag elementId.
	 */
	private String elementId;

	/**
	 * @param customSource
	 * @param elementId
	 */
	protected ReadItemFlag(CustomSource customSource, String elementId) {
		this.customSource = customSource;
		this.elementId = elementId;
	}

	/**
	 * Constructor.
	 */
	protected ReadItemFlag() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ReadItemFlag()");
		}
	}

	/**
	 * @return hash
	 * @see Flag#hash
	 * @see ElementProfile#getId()
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @param id
	 * @see Flag#hash
	 */
	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the readItemPK
	 */
	public long getReadItemPK() {
		return readItemPK;
	}

	/**
	 * @param readItemPK the readItemPK to set
	 */
	public void setReadItemPK(long readItemPK) {
		this.readItemPK = readItemPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementId == null) ? 0 : elementId.hashCode());
		return result;
	}

	/**
	 * @return the customSource
	 */
	public CustomSource getCustomSource() {
		return customSource;
	}

	/**
	 * @param customSource the customSource to set
	 */
	public void setCustomSource(CustomSource customSource) {
		this.customSource = customSource;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String string = "";
		
		/* The Flag Id */
		string += "	id : " + getReadItemPK() + "\n";
			
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
		ReadItemFlag other = (ReadItemFlag) obj;
		if (elementId == null) {
			if (other.elementId != null) {
				return false;
			}
		} else if (!elementId.equals(other.elementId)) {
			return false;
		}
		if (customSource == null) {
			if (other.customSource != null) {
				return false;
			}
		} else if (!customSource.equals(other.customSource)) {
			return false;
		}
		return true;
	}

}
