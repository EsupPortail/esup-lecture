package org.esupportail.lecture.web.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.esupportail.lecture.domain.model.AvailabilityMode;

/**
 * @author bourges
 * used to display category informations in view
 */
public class CategoryWebBean implements Comparable<CategoryWebBean> {

	/**
	 * id of category.
	 */
	private String id;
	/**
	 * name of category.
	 */
	private String name;
	/**
	 * type of category.
	 * "subscribed" --> The source is allowed and subscribed by the user
	 * "notSubscribed" --> The source is allowed and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> The source is personal
	 */
	private AvailabilityMode availabilityMode;
	/**
	 * description of category.
	 */
	private String description;
	/**
	 * store if category is folded or not.
	 */
	private boolean folded;
	/**
	 * Can a user mark items of this managed category as read / not read ?
	 */
	private boolean userCanMarkRead;
	/**
	 * list of sources of category.
	 */
	private List<SourceWebBean> sources;
	/**
	 * xmlOrder is used to store the order of the corresponding categoryProfile in an Context XML file.
	 */
	private int xmlOrder = Integer.MAX_VALUE;

	private boolean fromPublisher;
	
	/**
	 * Default constructor.
	 */
	public CategoryWebBean() {
		super();
		folded = false;
	}
	
	/**
	 * @return if category is folded or not
	 */
	public boolean isFolded() {
		return folded;
	}
	/**
	 * @param folded
	 */
	public void setFolded(final boolean folded) {
		this.folded = folded;
	}
	/**
	 * @return id of category
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(final String id) {
		this.id = id;
	}
	/**
	 * @return name of category
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * @return list of sources
	 */
	public List<SourceWebBean> getSources() {
		if (sources != null) {
			Collections.sort(sources);			
		}
		return sources;
	}
	/**
	 * @param sources
	 */
	public void setSources(final List<SourceWebBean> sources) {
		this.sources = sources;
	}
	
	/**
	 * get a source from category
	 * @param srcID searched source ID
	 * @return
	 */
	public SourceWebBean getSource(String srcID) {
		if (srcID != null) {
			List<SourceWebBean> sourceWebBeans = getSources();
			for (SourceWebBean sourceWebBean : sourceWebBeans) {
				if (sourceWebBean.getId().equals(srcID)) {
					return sourceWebBean;
				}
			}
		}
		return null;
	}
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @return the availabilityMode
	 */
	public AvailabilityMode getAvailabilityMode() {
		return availabilityMode;
	}
	/**
	 * @param type
	 */
	public void setAvailabilityMode(final AvailabilityMode type) {
		this.availabilityMode = type;
	}

	/**
	 * @return the xmlOrder
	 */
	public int getXmlOrder() {
		return xmlOrder;
	}

	/**
	 * @param xmlOrder the xmlOrder to set
	 */
	public void setXmlOrder(final int xmlOrder) {
		this.xmlOrder = xmlOrder;
	}

	/**
	 * @return if source is obliged or not
	 */
	public boolean isObliged() {
		boolean ret = false;
		if (availabilityMode == AvailabilityMode.OBLIGED) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return if source is macked as subcribed
	 */
	public boolean isSubscribed() {
		boolean ret = false;
		if (availabilityMode == AvailabilityMode.SUBSCRIBED) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return if source is macked as unsubcribed
	 */
	public boolean isNotSubscribed() {
		boolean ret = false;
		if (availabilityMode == AvailabilityMode.NOTSUBSCRIBED) {
			ret = true;
		}
		return ret;
	}

	/**
	 * @param o 
	 * @return int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final CategoryWebBean o) {
		int ret = xmlOrder - o.getXmlOrder();
		return ret;
	}
	/**
	 * @return the userCanMarkRead
	 */
	public boolean isUserCanMarkRead() {
		return userCanMarkRead;
	}

	/**
	 * @param userCanMarkRead the userCanMarkRead to set
	 */
	public void setUserCanMarkRead(final boolean userCanMarkRead) {
		this.userCanMarkRead = userCanMarkRead;
	}

	public boolean isFromPublisher() {
		return fromPublisher;
	}

	public void setFromPublisher(boolean fromPublisher) {
		this.fromPublisher = fromPublisher;
	}

}
