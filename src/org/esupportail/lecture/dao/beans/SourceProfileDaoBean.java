package org.esupportail.lecture.dao.beans;

import java.util.Hashtable;

import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.DefinitionSets;

public class SourceProfileDaoBean {

	private String id;
	private String name;
	private String url;
	private Accessibility access;
	private boolean specificUserContent;
	private DefinitionSets allowed;
	private DefinitionSets autoSubscribed;
	private DefinitionSets obliged;
	private String xsltFile;
	private String itemXpath;
	/* Couple <prefix,uri> */
	private Hashtable<String,String> xPathNameSpaces = new Hashtable<String, String>();
	public Accessibility getAccess() {
		return access;
	}
	public void setAccess(Accessibility access) {
		this.access = access;
	}
	public DefinitionSets getAllowed() {
		return allowed;
	}
	public void setAllowed(DefinitionSets allowed) {
		this.allowed = allowed;
	}
	public DefinitionSets getAutoSubscribed() {
		return autoSubscribed;
	}
	public void setAutoSubscribed(DefinitionSets autoSubscribed) {
		this.autoSubscribed = autoSubscribed;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemXpath() {
		return itemXpath;
	}
	public void setItemXpath(String itemXpath) {
		this.itemXpath = itemXpath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DefinitionSets getObliged() {
		return obliged;
	}
	public void setObliged(DefinitionSets obliged) {
		this.obliged = obliged;
	}
	public boolean isSpecificUserContent() {
		return specificUserContent;
	}
	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Hashtable<String, String> getXPathNameSpaces() {
		return xPathNameSpaces;
	}
	public void setXPathNameSpaces(Hashtable<String, String> pathNameSpaces) {
		xPathNameSpaces = pathNameSpaces;
	}
	public String getXsltFile() {
		return xsltFile;
	}
	public void setXsltFile(String xsltFile) {
		this.xsltFile = xsltFile;
	}

	
	
}
