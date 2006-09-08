package org.esupportail.lecture.domain.model;


import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

/**
 * Customizations on a context for a user profile 
 * @author gbouteil
 *
 */
public class CustomContext {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * The context of this customization refered to
	 */
	Context context;

//	private Collection subscriptions;
//	private Collection creations;
//	private Set foldedCategories;
//	private SortedSet orderCategories;
//	private Collection importations;
//	private int treeWinWidth;
	
	/*
	 ************************** Initialization ************************************/
	/*
	 *************************** METHODS ************************************/

	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @return context
	 * @see CustomContext#context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context : the context to set
	 * @see CustomContext#context
	 */
	public void setContext(Context context) {
		this.context = context;
	}



//	public Collection getSubscriptions() {
//		return subscriptions;
//	}
//
//	public void setSubscriptions(Collection subscriptions) {
//		this.subscriptions = subscriptions;
//	}



//
//	
//	public Collection getCreations() {
//		return creations;
//	}
//
//	public void setCreations(Collection creations) {
//		this.creations = creations;
//	}



//
//	public Set getFoldedCategories() {
//		return foldedCategories;
//	}
//
//	
//	public void setFoldedCategories(Set foldedCategories) {
//		this.foldedCategories = foldedCategories;
//	}


//	
//	public SortedSet getOrderCategories() {
//		return orderCategories;
//	}
//
//	
//	public void setOrderCategories(SortedSet orderCategories) {
//		this.orderCategories = orderCategories;
//	}



//
//	public Collection getImportations() {
//		return importations;
//	}
//
//	
//	public void setImportations(Collection importations) {
//		this.importations = importations;
//	}



//
//	public int getTreeWinWidth() {
//		return treeWinWidth;
//	}
//
//	public void setTreeWinWidth(int treeWinWidth) {
//		this.treeWinWidth = treeWinWidth;
//	}






}
