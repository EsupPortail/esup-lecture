package org.esupportail.lecture.domain.model;

/**
 * An xslt and xpath mapping to source view and item acess
 * @author gbouteil
 *
 */

/**
 * @author gbouteil
 *
 */
public class Mapping {
/* ************************** PROPERTIES ******************************** */	

	/**
	 * Name of the dtd
	 */
	private String dtd = "";
	
	/**
	 * Xmlns of the source xml stream
	 */
	private String xmlns = "";
	
	/**
	 * Xml type 
	 */
	private String xmlType;

	/**
	 * Path of the xslt file 
	 */
	private String xsltFile = "";
	
	/**
	 * Xpath to get an item in the source xml stream
	 */
	private String itemXPath = "";
	
/* ************************** Methods ******************************** */	

	/**
	 * Return a string containing mapping content : dtd, xsltfile, itemXPath, xmlns and xmlType 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
	
		String string="";
		
		/* The dtd name*/
		string += "	dtd : "+ dtd +"\n";

		/* The path of the xslt file */
		string += "	xsltFile : "+ xsltFile +"\n";
	
		/* The Xpath to get an item in the source xml stream */
		string += "	itemXPath : "+ itemXPath +"\n";
	
		/* The xmlns of the source xml stream */
		string += "	xmlns : "+ xmlns +"\n";
	
		/* The xml type ??? */
		string += "	xmlType : "+ xmlType +"\n";
	
		return string;
	}

/* ************************** ACCESSORS ******************************** */	

	/**
	 * Returns the dtd of the mapping
	 * @return dtd
	 * @see Mapping#dtd
	 */
	protected String getDtd() {
		return dtd;
	}
	/**
	 * Sets the dtd
	 * @param dtd
	 * @see Mapping#dtd
	 */
	protected void setDtd(String dtd) {
		this.dtd = dtd;
	}

	/**
	 * Returns the xslt file
	 * @return xsltFile
	 * @see Mapping#xsltFile
	 */
	protected String getXsltFile() {
		return xsltFile;
	}
	
	/**
	 * Sets the xsltFile
	 * @param xsltFile
	 * @see Mapping#xsltFile
	 */
	protected void setXsltFile(String xsltFile) {
		this.xsltFile = xsltFile;
	}

	/**
	 * Returns the item XPath
	 * @return itemXPath
	 * @see Mapping#itemXPath
	 */
	protected String getItemXPath() {
		return itemXPath;
	}
	
	/**
	 * Sets the item XPath
	 * @param itemXPath
	 * @see Mapping#itemXPath
	 */
	protected void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}

	/**
	 * Returns the xmlns
	 * @return xmlns
	 * @see Mapping#xmlns
	 */
	protected String getXmlns() {
		return xmlns;
	}
	/**
	 * Sets the xmlns
	 * @param xmlns
	 * @see Mapping#xmlns
	 */
	protected void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Returns the XML type
	 * @return xmlType
	 * @see Mapping#xmlType
	 */
	protected String getXmlType() {
		return xmlType;
	}
	/**
	 * Sets the XML type
	 * @param xmlType
	 * @see Mapping#xmlType
	 */
	protected void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
	
	
}
