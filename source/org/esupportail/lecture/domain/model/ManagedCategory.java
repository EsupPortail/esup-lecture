package org.esupportail.lecture.domain.model;


import java.util.Collection;


/**
 * Managed category element : loaded from a remote definition, transfered by an XML file
 * @author gbouteil
 *
 */
public class ManagedCategory extends Category {

/* ********************** PROPERTIES**************************************/ 

	/**
	 * Visibility sets of this category (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding 
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of this object reloading from remote definition (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding
	 */
	private int ttl;

	/**
	 * Managed category edit mode : not used for the moment (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding
	 */
	private Editability edit;


/* ********************** ACCESSORS**************************************/ 

	
	/**
	 * Returns visibility sets of this managed category (if defined)
	 * @return visibility
	 */
	protected VisibilitySets getVisibility() {
		return visibility;
	}
// TODO faire des method pour ltester l'existance de ces param optionnels

	/**
	 * Sets visibility sets of this managed category
	 * @param visibility
	 */
	protected void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}


	/**
	 * Returns ttl of this managed category (if defined)
	 * @return ttl
	 */
	protected int getTtl() {
		return ttl;
	}


	/**
	 * Sets ttl of this managed category
	 * @param ttl
	 */
	protected void setTtl(int ttl) {
		this.ttl = ttl;
	}


//	protected Editability getEdit() {
//		return edit;
//	}


//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	}
//	
// TODO : peut etre eutre chose que des collections ?
//	/** 
//	 * @uml.property name="managedChildren"
//	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite" inverse="managedCategory:org.esupportail.lecture.domain.model.ManagedComposantProfile"
//	 */
//	private Collection managedComposantProfile;
//
//	/** 
//	 * Getter of the property <tt>managedChildren</tt>
//	 * @return  Returns the managedComposantProfile.
//	 * @uml.property  name="managedChildren"
//	 */
//	protected Collection getManagedChildren() {
//		return managedComposantProfile;
//	}
//
//	/** 
//	 * Setter of the property <tt>managedChildren</tt>
//	 * @param managedChildren  The managedComposantProfile to set.
//	 * @uml.property  name="managedChildren"
//	 */
//	protected void setManagedChildren(Collection managedChildren) {
//		managedComposantProfile = managedChildren;
//	}

		
	

}
