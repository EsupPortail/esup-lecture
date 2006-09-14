package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


//import java.util.Collection;
//import java.util.Set;
//import java.util.SortedSet;

/**
 * Customizations on a context for a user profile 
 * @author gbouteil
 *
 */
public class CustomContext {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomContext.class);
	
	/**
	 * Used for tests
	 */
	public String test = "CustomCharge";
	
	
	/**
	 * The context Id of this customization refered to
	 */
	String contextId;

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
	 * @see CustomContext#contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId : the contextId to set
	 * @see CustomContext#contextId
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
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
