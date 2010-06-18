/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;


import org.esupportail.commons.utils.strings.StringUtils;

// TODO (RB <-- GB : Cette classe est utile ? ne doit-elle pas etre rangee dans un autre package ?
/**
 * The class that represent users.
 * @author bourges
 *
 */
public class User {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * Id of the user.
	 */
	private String id;
	
    /**
	 * Display Name of the user.
	 */
    private String displayName;
    
    /**
	 * True for administrators.
	 */
    private boolean admin;
	
    /**
     * The prefered language.
     */
    private String language;
    
	/*
	 *************************** INIT ************************************** */	

    /**
	 * Bean constructor.
	 */
	public User() {
		super();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		return id.equals(((User) obj).getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User#" + hashCode() + "[id=[" + id + "], displayName=[" + displayName 
		+ "], admin=[" + admin + "], language=[" + language + "]]";
	}

	/**
	 * @param id
	 */
	public void setId(final String id) {
		this.id = StringUtils.nullIfEmpty(id);
	}
	
	/**
	 * @param displayName  The displayName to set.
	 */
    public void setDisplayName(final String displayName) {
        this.displayName = StringUtils.nullIfEmpty(displayName);
    }
    
	/**
	 * @param language the language to set
	 */
	public void setLanguage(final String language) {
		this.language = StringUtils.nullIfEmpty(language);
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	
	/**
	 * @return  the id of the user.
	 */
	public String getId() {
		return id;
	}

    /**
	 * @return  Returns the displayName.
	 */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
	 * @param admin  The admin to set.
	 */
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    /**
	 * @return  Returns the admin.
	 */
    public boolean getAdmin() {
        return this.admin;
    }

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

}
