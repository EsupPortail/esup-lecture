package org.esupportail.lecture.web.beans;

import java.util.List;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.ItemDisplayMode;

/**
 * @author bourges
 * used to display source information in view
 */
public class SourceWebBean  implements Comparable<SourceWebBean> {

	/**
	 * id of source.
	 */
	private String id;
	/**
	 * name of source.
	 */
	private String name;
	/**
	 * type of source.
	 * "subscribed" --> The source is alloweb and subscribed by the user
	 * "notSubscribed" --> The source is alloweb and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> The source is personal
	 */
	private AvailabilityMode type;
	/**
	 * the display form source Items.
	 */
	private ItemDisplayMode itemDisplayMode;
	/**
	 * List of items of source.
	 */
	private List<ItemWebBean> items;

	/**
	 * Default constructor.
	 */
	public SourceWebBean() {
		super();
	}
	/**
	 * @return name of source
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
	 * @return id of source
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
	 * @return type of source
	 */
	public AvailabilityMode getType() {
		return type;
	}
	/**
	 * @param type
	 */
	public void setType(final AvailabilityMode type) {
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
	public void setItems(final List<ItemWebBean> items) {
		this.items = items;
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
	public int compareTo(final SourceWebBean o) {
		return name.compareTo(o.name);
	}
	
	/**
	 * @return ItemDisplayMode
	 */
	public ItemDisplayMode getItemDisplayMode() {
		return itemDisplayMode;
	}
	
	/**
	 * @param itemDisplayMode
	 */
	public void setItemDisplayMode(final ItemDisplayMode itemDisplayMode) {
		this.itemDisplayMode = itemDisplayMode;
	}
	
}
