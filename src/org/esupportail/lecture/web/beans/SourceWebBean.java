package org.esupportail.lecture.web.beans;

import java.util.List;

/**
 * @author bourges
 * used to display source information in view
 */
public class SourceWebBean {
	/**
	 * id of source
	 */
	private String id;
	/**
	 * name of source
	 */
	private String name;
	/**
	 * type of source
	 * "subscribed" --> The source is alloweb and subscribed by the user
	 * "notSubscribed" --> The source is alloweb and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 */
	private String type;
	/**
	 * List of items of source
	 */
	private List<ItemWebBean> items;
	/**
	 * @return name of source
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
	 * @return id of source
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
	 * @return type of source
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return list of items
	 */
	public List<ItemWebBean> getItems() {
		return items;
	}
	/**
	 * @param items
	 */
	public void setItems(List<ItemWebBean> items) {
		this.items = items;
	}
	
}
