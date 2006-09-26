package org.esupportail.lecture.beans;

import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomElement;

public class SelectedBean {
	
	private String name;
// TODO je ne sais pas
	private String content;
	
	public void init(CustomContext customContext) {
		CustomElement customElement = customContext.getSelectedElement();
		name = customElement.getName();
		
		content = customElement.getContent();
		
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	protected void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	protected void setName(String name) {
		this.name = name;
	}

}
