package org.esupportail.lecture.domain.model;


import java.util.Collection;

public class ManagedSourceProfile extends org.esupportail.lecture.domain.model.SourceProfile implements org.esupportail.lecture.domain.model.ManagedComposantProfile {

	/**
	 * @uml.property  name="globalSource"
	 * @uml.associationEnd  aggregation="composite" inverse="managedSourceProfile:org.esupportail.lecture.domain.model.GlobalSource"
	 */
	private GlobalSource globalSource;

	/** 
	 * Getter of the property <tt>globalSource</tt>
	 * @return  Returns the globalSource.
	 * @uml.property  name="globalSource"
	 */
	public GlobalSource getGlobalSource() {
		return globalSource;
	}

	/** 
	 * Setter of the property <tt>globalSource</tt>
	 * @param globalSource  The globalSource to set.
	 * @uml.property  name="globalSource"
	 */
	public void setGlobalSource(GlobalSource globalSource) {
		this.globalSource = globalSource;
	}

	/**
	 * @uml.property  name="access"
	 */
	private Accessibility access;

	/** 
	 * Getter of the property <tt>access</tt>
	 * @return  Returns the access.
	 * @uml.property  name="access"
	 */
	public Accessibility getAccess() {
		return access;
	}

	/** 
	 * Setter of the property <tt>access</tt>
	 * @param access  The access to set.
	 * @uml.property  name="access"
	 */
	public void setAccess(Accessibility access) {
		this.access = access;
	}

	/**
	 * @uml.property  name="visibility"
	 */
	private VisibilitySets visibility;

	/**
	 * Getter of the property <tt>visibility</tt>
	 * @return  Returns the visibility.
	 * @uml.property  name="visibility"
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}

	/**
	 * Setter of the property <tt>visibility</tt>
	 * @param visibility  The visibility to set.
	 * @uml.property  name="visibility"
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}

	/**
	 * @uml.property  name="ttl"
	 */
	private int ttl;

	/**
	 * Getter of the property <tt>ttl</tt>
	 * @return  Returns the ttl.
	 * @uml.property  name="ttl"
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * Setter of the property <tt>ttl</tt>
	 * @param ttl  The ttl to set.
	 * @uml.property  name="ttl"
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @uml.property  name="specificUserContent"
	 */
	private boolean specificUserContent;

	/**
	 * Getter of the property <tt>specificUserContent</tt>
	 * @return  Returns the specificUserContent.
	 * @uml.property  name="specificUserContent"
	 */
	public boolean getSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Setter of the property <tt>specificUserContent</tt>
	 * @param specificUserContent  The specificUserContent to set.
	 * @uml.property  name="specificUserContent"
	 */
	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}

	/**
	 * @uml.property  name="xsltURL"
	 */
	private String xsltURL = "";

	/**
	 * Getter of the property <tt>xsltURL</tt>
	 * @return  Returns the xsltURL.
	 * @uml.property  name="xsltURL"
	 */
	public String getXsltURL() {
		return xsltURL;
	}

	/**
	 * Setter of the property <tt>xsltURL</tt>
	 * @param xsltURL  The xsltURL to set.
	 * @uml.property  name="xsltURL"
	 */
	public void setXsltURL(String xsltURL) {
		this.xsltURL = xsltURL;
	}

	/**
	 * @uml.property  name="itemXPath"
	 */
	private String itemXPath = "";

	/** 
	 * Getter of the property <tt>xpath</tt>
	 * @return  Returns the xpath.
	 * @uml.property  name="itemXPath"
	 */
	public String getItemXPath() {
		return itemXPath;
	}

	/** 
	 * Setter of the property <tt>xpath</tt>
	 * @param xpath  The xpath to set.
	 * @uml.property  name="itemXPath"
	 */
	public void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}

}
