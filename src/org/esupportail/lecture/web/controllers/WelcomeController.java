/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.web.beans.PrintableThingSet;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.ldap.LdapService;
import org.esupportail.commons.utils.ExternalContextUtils;
import org.springframework.util.Assert;

/**
 * A visual bean for the welcome page.
 */
public class WelcomeController extends AbstractContextAwareController {

	/**
	 * ldap Service.
	 */
	private LdapService ldapService;

	/**
	 * Bean constructor.
	 */
	public WelcomeController() {
		super();
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(ldapService, 
				"property ldapService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// nothing to reset yet
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return the things the user sees.
	 */
	@SuppressWarnings("unchecked")
	public List<PrintableThingSet> getPrintableThingSets() {
		if (ExternalContextUtils.getRequestVar("printableThingSets") == null) {
			List<PrintableThingSet> result = new ArrayList<PrintableThingSet>();
			User currentUser = getCurrentUser();
			if (currentUser != null) {
				for (Department department : getDomainService().getVisibleDepartments(currentUser)) {
					result.add(new PrintableThingSet(department, getDomainService().getThings(department)));
				}
			}
			ExternalContextUtils.setRequestVar("printableThingSets", result);
		}
		return (List<PrintableThingSet>) ExternalContextUtils.getRequestVar("printableThingSets");
	}

	/**
	 * @return nothing
	 * TODO PA: remove this method when finished.
	 */
	public String throwException() {
		throw new ConfigException("test"); 
	}

	/**
	 * @param ldapService the ldapService to set
	 */
	public void setLdapService(LdapService ldapService) {
		this.ldapService = ldapService;
	}
	
}
