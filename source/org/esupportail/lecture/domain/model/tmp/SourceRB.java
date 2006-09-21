package org.esupportail.lecture.domain.model.tmp;

import java.util.List;

public class SourceRB extends org.esupportail.lecture.domain.model.Source {
	private boolean selected=false;
	private boolean withUnread=true;
	private int id;
	private String name;
	private List<Item> items;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
