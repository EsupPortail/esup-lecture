package org.esupportail.lecture.domain.beans;
 

import org.esupportail.commons.utils.strings.StringUtils;

/**
 * The class that represents departments.
 */
public class Department {

    /**
	 * The unique id.
	 */
    private long id;
	
    /**
	 * The label.
	 */
	private String label;

	/**
	 * The long label.
	 */
	private String xlabel;
	
	/**
	 * The LDAP filter of the department.
	 */
	private String ldapFilter;
	
	/**
     * Bean constructor.
     */
    public Department() {
    	super();
    }

    /**
     * Clone an object.
     * @param department 
	 */
	public Department(final Department department) {
		this();
		id = department.getId();
		label = department.getLabel();
		xlabel = department.getXlabel();
		ldapFilter = department.getLdapFilter();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Department)) {
			return false;
		}
		return ((Department) obj).getId() == id;
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
		return "Department#" + hashCode() + "[id=[" + id + "], label=[" + label + "], xlabel=[" 
		+ xlabel + "], ldapFilter=[" + ldapFilter + "]]";
	}

	/**
	 * @return  Returns the id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id  The id to set.
	 */
    public void setId(final long id) {
        this.id = id;
    }

    /**
	 * @return  Returns the label.
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * @param label  The label to set.
	 */
    public void setLabel(final String label) {
        this.label = StringUtils.nullIfEmpty(label);
    }

	/**
	 * @return the xlabel
	 */
	public String getXlabel() {
		return xlabel;
	}

	/**
	 * @param xlabel the xlabel to set
	 */
	public void setXlabel(final String xlabel) {
		this.xlabel = StringUtils.nullIfEmpty(xlabel);
	}

	/**
	 * @return the ldapFilter
	 */
	public String getLdapFilter() {
		return ldapFilter;
	}

	/**
	 * @param ldapFilter the ldapFilter to set
	 */
	public void setLdapFilter(final String ldapFilter) {
		this.ldapFilter = StringUtils.nullIfEmpty(ldapFilter);
	}

}
