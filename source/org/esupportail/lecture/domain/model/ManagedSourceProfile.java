/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


/**
 * Managed source profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.SourceProfile
 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile
 *
 */
/**
 * @author gbouteil
 *
 */
public class ManagedSourceProfile extends org.esupportail.lecture.domain.model.SourceProfile implements org.esupportail.lecture.domain.model.ManagedComposantProfile {
	
/* ************************** PROPERTIES ******************************** */	

	/**
	 * Remote source loaded : a managed source profile has a global or a single source
	 * Thats depends on "specificUserContent" parameter.
	 * @see ManagedSourceProfile#specificUserContent
	 */
	private Source source;
	/**
	 * Access mode on the remote source
	 */
	private Accessibility access;
	
	/**
	 * Visibility rights for groups on the remote source
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of the remote source reloading
	 * Using depends on trustCategory parameter
	 */
	private int ttl;
	
	/**
	 * Specific user content parameter : indicates source multiplicity :
	 * - true : source is specific to a user, it is loaded in user profile => source is a SingleSource
	 * - false : source is global to users, it is loaded in channel environnement => source is a GlobalSource
	 */
	private boolean specificUserContent;
	
	
	/**
	 * URL of the xslt file to display remote source
	 */
	private String xsltURL = "";
	
	/**
	 * Xpath to access item in the XML source file correspoding to this source profile
	 */
	private String itemXPath = "";

// utile ou pas ?
//	/**
//	 * Optionnal : xmlns of the source (one of these parameter is required : xmlns, xmlType, dtd)
//	 */
	private String xmlns = "";
//	
//	/**
//	 * Optionnal : xmlType of the source (one of these parameter is required : xmlns, xmlType, dtd)
//	 */
	private String xmlType = "";
//	
//	/**
//	 * Opitionnal : DTD of the source (one of these parameter is required : xmlns, xmlType, dtd)
//	 */
	private String dtd = "";


/* ************************** METHODS ******************************** */	
	
/* ************************** ACCESSORS ******************************** */	


	/**
	 * Returns source of this managed source profile (if loaded)
	 * @return source
	 */
	protected Source getSource() {
		return source;
	}
	
	/**
	 * Sets source on the profile
	 * @param source
	 */
	protected void setSource(Source source) {
		this.source = source;
	}

	/**	 
	 * @return access
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getAccess()
	 */
	public Accessibility getAccess() {
		return access;
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	public void setAccess(Accessibility access) {
		this.access = access;
	}

	/**
	 * @return visibility
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}


	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * Returns ttl
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getTtl()
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setTtl(int)
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	protected boolean getSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Sets specificUserContent
	 * @param specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}


	/**
	 * Returns URL of xslt file
	 * @return xsltURL
	 * @see ManagedSourceProfile#xsltURL
	 */
	protected String getXsltURL() {
		return xsltURL;
	}


	/**
	 * Sets URL of xslt file
	 * @param xsltURL
	 * @see ManagedSourceProfile#xsltURL
	 */
	public void setXsltURL(String xsltURL) {
		this.xsltURL = xsltURL;
	}


	/**
	 * Returns Xpath to access to item in source XML file
	 * @return itemXPath
	 * @see ManagedSourceProfile#xsltURL
	 */
	protected String getItemXPath() {
		return itemXPath;
	}

	/**
	 * Sets Xpath to access to item in source XML file
	 * @param itemXPath
	 * @see ManagedSourceProfile#xsltURL
	 */
	public void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}
	
	/**
	 * Returns the dtd.
	 * @return dtd
	 * @see ManagedSourceProfile#dtd
	 */
	protected String getDtd() {
		return dtd;
	}

	/**
	 * Sets dtd
	 * @param dtd 
	 * @see ManagedSourceProfile#dtd
	 */
	protected void setDtd(String dtd) {
		this.dtd = dtd;
	}

	/**
	 * Returns the xmlns.
	 * @return xmlns
	 * @see ManagedSourceProfile#xmlns
	 */
	protected String getXmlns() {
		return xmlns;
	}

	/**
	 * Sets xmlns
	 * @param xmlns
	 * @see ManagedSourceProfile#xmlns
	 */
	protected void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Returns the xmlType.
	 * @return xmlType.
	 * @see ManagedSourceProfile#xmlType
	 */
	protected String getXmlType() {
		return xmlType;
	}

	/**
	 * Sets the xmlType.
	 * @param xmlType 
	 * @see ManagedSourceProfile#xmlType
	 */
	protected void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

	/** 
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAllowed(DefinitionSets d) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAutoSubcribed(DefinitionSets d) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityObliged(DefinitionSets d) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() {
		// TODO Auto-generated method stub
		return null;
	}

}
