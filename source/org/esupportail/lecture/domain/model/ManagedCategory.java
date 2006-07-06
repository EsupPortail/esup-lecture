package org.esupportail.lecture.domain.model;


import java.util.Collection;


public class ManagedCategory extends Category {

/* ********************** PROPERTIES**************************************/ 

	/**
	 * @uml.property  name="visibility"
	 */
	private VisibilitySets visibility;

	/**
	 * @uml.property  name="ttl"
	 */
	private int ttl;

	/**
	 * @uml.property  name="edit"
	 */
	private Editability edit;


/* ********************** ACCESSORS**************************************/ 
	/**
	 * Getter of the property <tt>visibility</tt>
	 * @return  Returns the visibility.
	 * @uml.property  name="visibility"
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}

	/**
	 * Setter of the property <tt>visibility</tt>
	 * @param visibility  The visibility to set.
	 * @uml.property  name="visibility"
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}

	/**
	 * Getter of the property <tt>ttl</tt>
	 * @return  Returns the ttl.
	 * @uml.property  name="ttl"
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * Setter of the property <tt>ttl</tt>
	 * @param ttl  The ttl to set.
	 * @uml.property  name="ttl"
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * Getter of the property <tt>edit</tt>
	 * @return  Returns the edit.
	 * @uml.property  name="edit"
	 */
	public Editability getEdit() {
		return edit;
	}

	/**
	 * Setter of the property <tt>edit</tt>
	 * @param edit  The edit to set.
	 * @uml.property  name="edit"
	 */
	public void setEdit(Editability edit) {
		this.edit = edit;
	}
	
// TODO : peut etre eutre chose que des collections ?
	/** 
	 * @uml.property name="managedChildren"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite" inverse="managedCategory:org.esupportail.lecture.domain.model.ManagedComposantProfile"
	 */
	private Collection managedComposantProfile;

	/** 
	 * Getter of the property <tt>managedChildren</tt>
	 * @return  Returns the managedComposantProfile.
	 * @uml.property  name="managedChildren"
	 */
	public Collection getManagedChildren() {
		return managedComposantProfile;
	}

	/** 
	 * Setter of the property <tt>managedChildren</tt>
	 * @param managedChildren  The managedComposantProfile to set.
	 * @uml.property  name="managedChildren"
	 */
	public void setManagedChildren(Collection managedChildren) {
		managedComposantProfile = managedChildren;
	}

		
	

}
