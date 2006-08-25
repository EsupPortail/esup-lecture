package org.esupportail.lecture.domain.model.tmp;

public class SourceRB extends org.esupportail.lecture.domain.model.Source {
	private boolean selected=false;
	private boolean withUnread=true;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isWithUnread() {
		return withUnread;
	}

	public void setWithUnread(boolean withunread) {
		this.withUnread = withunread;
	}

}
