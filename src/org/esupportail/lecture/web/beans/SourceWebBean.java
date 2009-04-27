package org.esupportail.lecture.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.FacadeService;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.controllers.HomeController;

/**
 * @author bourges
 * used to display source information in view
 */
public class SourceWebBean  implements Comparable<SourceWebBean> {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(SourceWebBean.class);
	/**
	 * id of source.
	 */
	private String id;
	/**
	 * name of source.
	 */
	private String name;
	/**
	 * number of Items in the source
	 */
	private int itemsNumber;
	/**
	 * number of unread Items in the source
	 */
	private int unreadItemsNumber;
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
	 * xmlOrder is used to store the order of the corresponding sourceProfile in an Category XML file.
	 */
	private int xmlOrder = Integer.MAX_VALUE;

	/**
	 * Default constructor.
	 */
	public SourceWebBean(final List<ItemBean> itemsBeans) {
		super();
		items = new ArrayList<ItemWebBean>();
		if (itemsBeans != null) {
			for (ItemBean itemBean : itemsBeans) {
				ItemWebBean itemWebBean = new ItemWebBean();
				itemWebBean.setId(itemBean.getId());
				itemWebBean.setHtmlContent(itemBean.getHtmlContent());
				itemWebBean.setRead(itemBean.isRead());
				itemWebBean.setDummy(itemBean.isDummy());
				items.add(itemWebBean);				
			}			
		}
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
	 * sort items list in function of itemDisplayMode.
	 * @return Sorted items list
	 */
	public List<ItemWebBean> getSortedItems() {
		List<ItemWebBean> list = getItems();
		List<ItemWebBean> ret = new ArrayList<ItemWebBean>();
		if (itemDisplayMode == ItemDisplayMode.ALL) {
			ret = list;
		} else if (itemDisplayMode == ItemDisplayMode.UNREAD) {
			if (list != null) {
				for (ItemWebBean itemWebBean : list) {
					if (!itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}					
				}
			}
		} else if (itemDisplayMode == ItemDisplayMode.UNREADFIRST) {
			if (list != null) {
				// find unread
				for (ItemWebBean itemWebBean : list) {
					if (!itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}					
				}
				// and read
				for (ItemWebBean itemWebBean : list) {
					if (itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}					
				}
			}
		} else {
			LOG.warn("Unknown itemDisplayMode value \"" + itemDisplayMode + "\" in sortedItems function");
		}
		return ret;
	}
	
	
//	/**
//	 * @param items
//	 */
//	public void setItems(final List<ItemWebBean> items) {
//		this.items = items;
//	}
	
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
		int ret = xmlOrder - o.getXmlOrder();
		return ret;
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
	 * @return number of Items in the source
	 */
	public int getItemsNumber() {
		return getItems().size();
	}
	
	/**
	 * @return the unreadItemsNumber
	 */
	public int getUnreadItemsNumber() {
		return unreadItemsNumber;
	}
	/**
	 */
	public void setUnreadItemsNumber() {
		int ret = 0;
		for (ItemWebBean item : getItems()) {
			if (!item.isRead()) {
				ret += 1;
			}
		}
		this.unreadItemsNumber = ret;
	}
	
	/**
	 * @param unreadItemsNumber the unreadItemsNumber to set
	 */
	public void setUnreadItemsNumber(int unreadItemsNumber) {
		this.unreadItemsNumber = unreadItemsNumber;
	}
	/**
	 * @param itemsNumber the itemsNumber to set
	 */
	public void setItemsNumber() {
		this.itemsNumber = getItems().size();
	}
	/**
	 * @return if source contains Items to display or not
	 */
	public boolean isWithDisplayedItems() {
		if (itemDisplayMode == ItemDisplayMode.UNREAD) {
			if (unreadItemsNumber > 0) {
				return true;
			}
		} else {
			if (itemsNumber > 0) {
				return true;
			}
		}
		return false;
	}
}
