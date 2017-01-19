package org.esupportail.lecture.dao;

import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;

public class RubriquesSourceProfile extends ManagedSourceProfile {

	public RubriquesSourceProfile(ManagedCategory mc) {
		super(mc);
	}

	private String color;
	private int uuid;
	private boolean highlight;
	private ManagedCategory category;
	private ManagedCategoryProfile categoryProfile;
	private boolean featuresComputed;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public boolean getHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public ManagedCategory getCategory() {
		return category;
	}

	public void setCategory(ManagedCategory category) {
		this.category = category;
	}

	public ManagedCategoryProfile getCategoryProfile() {
		return categoryProfile;
	}

	public void setCategoryProfile(ManagedCategoryProfile categoryProfile) {
		this.categoryProfile = categoryProfile;
	}

	public boolean isFeaturesComputed() {
		return featuresComputed;
	}

	public void setFeaturesComputed(boolean featuresComputed) {
		this.featuresComputed = featuresComputed;
	}



}
