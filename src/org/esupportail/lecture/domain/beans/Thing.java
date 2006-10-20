package org.esupportail.lecture.domain.beans;
 

import java.sql.Timestamp;

import org.esupportail.commons.services.i18n.I18nUtils;
import org.esupportail.commons.utils.strings.StringUtils;

/**
 * The class that represents things.
 */
public class Thing {

	/**
	 * The unique id.
	 */
	private long id;

	/**
	 * The value.
	 */
	private String value;

	/**
	 * A comment that describes the thing.
	 */
	private String comment;

    /**
     * The department of the thing.
     */
    private Department department; 

    /**
     * The time the thing was modified for the last time.
     */
    private Timestamp date; 

    /**
     * The user that modified the thing for the last time.
     */
    private User user; 

	/**
	 * The order.
	 */
	private int order; 

	/**
	 * Bean constructor.
	 */
	public Thing() {
		super();
	}

	/**
	 * Clone.
	 * @param thing 
	 */
	public Thing(final Thing thing) {
		this();
		id = thing.getId();
		value = thing.getValue();
		comment = thing.getComment();
		department = thing.getDepartment();
		order = thing.getOrder();
		date = thing.getDate();
		user = thing.getUser();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Thing)) {
			return false;
		}
		return ((Thing) obj).getId() == id;
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
		return "Thing#" + hashCode() + "[id=[" + id + "], order=[" + order + "], value=[" 
		+ value + "], comment=[" + comment + "], date=[" + date + "], user= " + user + ", department=[" 
		+ department + "]]";
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
	 * @return  Returns the comment.
	 */
    public String getComment() {
		return comment;
	}

	/**
	 * @param comment  The comment to set.
	 */
    public void setComment(final String comment) {
		this.comment = StringUtils.nullIfEmpty(comment);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}
	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @return the date in a printable form.
	 */
	public String getPrintableRelativeDate() {
		return I18nUtils.printableRelativeDate(date.getTime());
	}

	/**
	 * @return the date in a printable form.
	 */
	public String getPrintableDate() {
		return I18nUtils.printableDate(date.getTime());
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(final Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final int order) {
		this.order = order;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

}
