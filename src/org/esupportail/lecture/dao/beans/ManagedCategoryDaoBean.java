package org.esupportail.lecture.dao.beans;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.Editability;

public class ManagedCategoryDaoBean {
	
	private String description;
	private String name;
	private Editability edit;
	private DefinitionSets allowed;
	private DefinitionSets autoSubscribed;
	private DefinitionSets obliged;
	private List<SourceProfileDaoBean> listSourceProfiles = new ArrayList<SourceProfileDaoBean>();
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Editability getEdit() {
		return edit;
	}
	public void setEdit(Editability edit) {
		this.edit = edit;
	}
	public List<SourceProfileDaoBean> getListSourceProfiles() {
		return listSourceProfiles;
	}
	public void setListSourceProfiles(List<SourceProfileDaoBean> listSourceProfiles) {
		this.listSourceProfiles = listSourceProfiles;
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
	
	

}
