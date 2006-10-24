package org.esupportail.lecture.web.beans;

import java.util.List;

/**
 * @author bourges
 * used to display category informations in view
 */
public class CategoryWebBean {
	/**
	 * id of categery
	 */
	private String id;
	/**
	 * name of category
	 */
	private String name;
	/**
	 * store if category is folded or not
	 */
	private boolean folded;
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
		this.sources = sources;
	}

}
