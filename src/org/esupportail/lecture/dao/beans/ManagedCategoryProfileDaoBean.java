package org.esupportail.lecture.dao.beans;

import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.Editability;

public class ManagedCategoryProfileDaoBean {

	private String id;
	private String name;
	private String urlCategory;
	private boolean trustCategory;
	private Editability edit;
	private Accessibility access;
	private int ttl;
	private DefinitionSets allowed;
	private DefinitionSets autoSubscribed;
	private DefinitionSets obliged;
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
	public Editability getEdit() {
		return edit;
	}
	public void setEdit(Editability edit) {
		this.edit = edit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public boolean isTrustCategory() {
		return trustCategory;
	}
	public void setTrustCategory(boolean trustCategory) {
		this.trustCategory = trustCategory;
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	public String getUrlCategory() {
		return urlCategory;
	}
	public void setUrlCategory(String urlCategory) {
		this.urlCategory = urlCategory;
	}
	
}
