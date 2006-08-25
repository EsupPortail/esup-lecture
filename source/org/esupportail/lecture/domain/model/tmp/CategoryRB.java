package org.esupportail.lecture.domain.model.tmp;

import java.util.List;

public class CategoryRB extends org.esupportail.lecture.domain.model.Category {
	private List<SourceRB> sources;
	private boolean selected;
	private boolean expanded;

	public List<SourceRB> getSources() {
		return sources;
	}

	public void setSources(List<SourceRB> sources) {
		this.sources = sources;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
