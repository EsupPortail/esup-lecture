package org.esupportail.lecture.domain.model;

public abstract class SourceProfile {

	/**
	 * @uml.property  name="id"
	 */
	private String id;

	/**
	 * Getter of the property <tt>id</tt>
	 * @return  Returns the id.
	 * @uml.property  name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter of the property <tt>id</tt>
	 * @param id  The id to set.
	 * @uml.property  name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @uml.property  name="name"
	 */
	private String name = "";

	/**
	 * Getter of the property <tt>name</tt>
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @uml.property  name="sourceURL"
	 */
	private String sourceURL = "";

	/**
	 * Getter of the property <tt>sourceURL</tt>
	 * @return  Returns the sourceURL.
	 * @uml.property  name="sourceURL"
	 */
	public String getSourceURL() {
		return sourceURL;
	}

	/**
	 * Setter of the property <tt>sourceURL</tt>
	 * @param sourceURL  The sourceURL to set.
	 * @uml.property  name="sourceURL"
	 */
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

}
