package org.esupportail.lecture.domain.model;

/**
 * Visibility mode of a managed element
 * @author gbouteil
 *
 */
public enum VisibilityMode {
	/**
	 * Obliged visibility : automatic subscription with impossible unsubcription 
	 */
	OBLIGED,
	/**
	 * AutoSubscribed visibility : automatic subscription with possible unsubcription 
	 */
	AUTOSUBSCRIBED,
	/**
	 * Allowed visibility : subscription and unsubcription possible 
	 */
	ALLOWED,
	/**
	 * No visibility
	 */
	NOVISIBLE
};