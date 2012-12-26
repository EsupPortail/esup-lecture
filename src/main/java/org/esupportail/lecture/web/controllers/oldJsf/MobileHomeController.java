/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers.oldJsf;


/**
 * @author : Raymond 
 */
public class MobileHomeController extends HomeController {

	private static final long serialVersionUID = 1L;

	/**
	 * @see org.esupportail.lecture.web.controllers.oldJsf.TwoPanesController#selectElement()
	 */
	@Override
	public String selectElement() {
		super.selectElement();
		return "navigationMobileDetail";
	}
	
}
