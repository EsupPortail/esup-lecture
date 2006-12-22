package org.esupportail.lecture.domain.model;

/**
 * Visibility mode of a managed element
 * @author gbouteil
 *
 */
public enum VisibilityMode {
	/**
	 * Obliged visibility : automatic subscription with no unsubcription possible 
	 */
	OBLIGED,
	/**
	 * AutoSubscribed visibility : automatic subscription with unsubcription possible 
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