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
import org.esupportail.lecture.web.controllers.HomeController;

/**
 * A JSF converter to pass Department instances.
 */
public class ItemDisplayModeConverter implements Converter {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ItemDisplayModeConverter.class);

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) throws ConverterException {
		if (log.isDebugEnabled()) log.debug("getAsObject("+value+")");
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
	public String getAsString(FacesContext facesContext, UIComponent component, Object obj) throws ConverterException {
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
		if (log.isDebugEnabled()) log.debug("getAsString()="+ret);		
		return ret;
	}
	
}
