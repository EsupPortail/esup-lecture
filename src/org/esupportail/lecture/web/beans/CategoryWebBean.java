package org.esupportail.lecture.web.beans;

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
	private AvailabilityMode type;
	/**
	 * description of category.
	 */
	private String description;
	/**
	 * store if category is folded or not.
	 */
	private boolean folded;
	/**
	 * selected source of category.
	 */
	private SourceWebBean selectedSource;
	/**
	 * list of sources of category.
	 */
	private List<SourceWebBean> sources;

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
	 * @return selected source of category
	 */
	public SourceWebBean getSelectedSource() {
		return selectedSource;
	}
	/**
	 * @param selectedSource
	 */
	public void setSelectedSource(final SourceWebBean selectedSource) {
		this.selectedSource = selectedSource;
	}
	/**
	 * @return list of sources
	 */
	public List<SourceWebBean> getSources() {
		return sources;
	}
	/**
	 * @param sources
	 */
	public void setSources(final List<SourceWebBean> sources) {
		Collections.sort(sources);
		this.sources = sources;
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
	 * @param type
	 */
	public void setType(final AvailabilityMode type) {
		this.type = type;
	}

	/**
	 * @return if source is obliged or not
	 */
	public boolean isObliged() {
		boolean ret = false;
		if (type == AvailabilityMode.OBLIGED) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return if source is macked as subcribed
	 */
	public boolean isSubscribed() {
		boolean ret = false;
		if (type == AvailabilityMode.SUBSCRIBED) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return if source is macked as unsubcribed
	 */
	public boolean isNotSubscribed() {
		boolean ret = false;
		if (type == AvailabilityMode.NOTSUBSCRIBED) {
			ret = true;
		}
		return ret;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final CategoryWebBean o) {
		return name.compareTo(o.name);
	}

}
