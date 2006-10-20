/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.web.controllers.AbstractDomainAwareBean;

/**
 * A JSF converter to pass Department instances.
 */
public class DepartmentConverter extends AbstractDomainAwareBean implements Converter {

	/**
	 * Bean constructor.
	 */
	public DepartmentConverter() {
		super();
		reset();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// nothing to reset for this bean 
	}

	/**
	 * @see javax.faces.convert.Converter#getAsObject(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(
			@SuppressWarnings("unused") final FacesContext context, 
			@SuppressWarnings("unused") final UIComponent component, 
			final String value) {
		if ("".equals(value)) {
			return null;
		}
		try {
			long longValue = Long.valueOf(value);
			return getDomainService().getDepartment(longValue);
		} catch (NumberFormatException e) {
			throw new UnsupportedOperationException(
					"could not convert String [" + value + "] to a Department.", e);
		}
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(
			@SuppressWarnings("unused") final FacesContext context, 
			@SuppressWarnings("unused") final UIComponent component, 
			final Object value) {
		if (value == null || "".equals(value)) {
			return "";
		}
		if (!(value instanceof Department)) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a Department.");
		}
		Department department = (Department) value;
		return Long.toString(department.getId());
	}

}
