package org.esupportail.lecture.web.beans;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author bourges
 * used to display category informations in view
 */
public class CategoryWebBean implements Comparable<CategoryWebBean> {
	/**
	 * id of categery
	 */
	private String id;
	/**
	 * name of category
	 */
	private String name;
	/**
	 * description of category
	 */
	private String description;
	/**
	 * store if category is folded or not
	 */
	private boolean folded=false;
	/**
	 * selected source of category
	 */
	private SourceWebBean selectedSource;
	/**
	 * list of sources of category
	 */
	private List<SourceWebBean> sources;
	/**
	 * @return if category is folded or not
	 */
	public boolean isFolded() {
		return folded;
	}
	/**
	 * @param folded
	 */
	public void setFolded(boolean folded) {
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
	public void setId(String id) {
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
	public void setName(String name) {
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
	public void setSelectedSource(SourceWebBean selectedSource) {
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
	public void setSources(List<SourceWebBean> sources) {
		Collections.sort(sources);
		this.sources = sources;
	}
	/**
	 * @return descrition
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(CategoryWebBean o) {
		return name.compareTo(o.name);
	}

}
