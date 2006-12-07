package org.esupportail.lecture.dao.beans;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.domain.model.Editability;

public class ContextDaoBean {

	private String name;
	
	private String id;
	
	private String description;
	
	private List<String> listManagedCategoryProfileId = new ArrayList<String>();
	
	private Editability edit;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getListManagedCategoryProfileId() {
		return listManagedCategoryProfileId;
	}

	public void setListManagedCategoryProfileId(
			List<String> listManagedCategoryProfileId) {
		this.listManagedCategoryProfileId = listManagedCategoryProfileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
