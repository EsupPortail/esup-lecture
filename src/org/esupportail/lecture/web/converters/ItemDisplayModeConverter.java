/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.ItemDisplayMode;

/**
 * A JSF converter to pass ItemDisplayMode instances.
 */
public class ItemDisplayModeConverter implements Converter {
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ItemDisplayModeConverter.class);

	/**
	 * Constructor.
	 */
	public ItemDisplayModeConverter() {
		super();
	}

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(@SuppressWarnings("unused")
	final FacesContext facesContext, 
			@SuppressWarnings("unused")
			final UIComponent component, final String value) throws ConverterException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAsObject(" + value + ")");
		}
		ItemDisplayMode ret = ItemDisplayMode.ALL;
		if (value.equals("notRead")) {
			ret = ItemDisplayMode.UNREAD;
		}
		if (value.equals("unreadFirst")) {
			ret = ItemDisplayMode.UNREADFIRST;
		}
		return ret;
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(@SuppressWarnings("unused")
	final FacesContext facesContext, 
			@SuppressWarnings("unused")
			final UIComponent component, final Object obj) throws ConverterException {
		String ret = "";
		if (obj instanceof ItemDisplayMode) {
			ItemDisplayMode itemDisplayMode = (ItemDisplayMode) obj;
			if (itemDisplayMode == ItemDisplayMode.ALL) {
				ret = "all";
			}
			if (itemDisplayMode == ItemDisplayMode.UNREAD) {
				ret = "notRead";
			}
			if (itemDisplayMode == ItemDisplayMode.UNREADFIRST) {
				ret = "unreadFirst";
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAsString()="+ret);		
		}
		return ret;
	}
	
}
