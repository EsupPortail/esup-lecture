/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.beans.FeatureDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.service.PortletService;


/**
 * Managed source profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.SourceProfile
 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile
 *
 */
/**
 * @author gbouteil
 *
 */
public class ManagedSourceProfile extends SourceProfile implements ManagedComposantProfile {
	
	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedSourceProfile.class); 
	
	/**
	 * Remote source loaded : a managed source profile has a global or a single source
	 * Thats depends on "specificUserContent" parameter.
	 * @see ManagedSourceProfile#specificUserContent
	 */
	
	private Source source;
	/**
	 * Access mode on the remote source
	 */
	private Accessibility access;
	
	/**
	 * Visibility rights for groups on the remote source
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of the remote source reloading
	 * Using depends on trustCategory parameter
	 */
	private int ttl;
	
	/**
	 * Specific user content parameter : indicates source multiplicity :
	 * - true : source is specific to a user, it is loaded in user profile => source is a SingleSource
	 * - false : source is global to users, it is loaded in channel environnement => source is a GlobalSource
	 */
	private boolean specificUserContent;
	
	/**
	 * Resolve feature values (access, visibility,tll,ItemXpath,xsltURL) from :
	 * - managedSourceProfile features
	 * - source features
	 * - trustCategory parameter 
	 */
	private ComputedManagedSourceFeatures computedFeatures;

	private ManagedCategoryProfile managedCategoryProfile;
	

	

	
	/*
	 ************************** PROPERTIES ******************************** */	
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#init()
	 */
	public void init() {
		computedFeatures = new ComputedManagedSourceFeatures(this);
	}


	/*
	 *************************** METHODS ******************************** */	
	

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#computeFeatures()
	 * Can be called only when source has been realy get. (Not at the instantiation of the object)
	 */
	public void computeFeatures() {
	
		/* Features that can be herited by the managedCategoryProfile */
		Accessibility setAccess;
		VisibilitySets setVisib;
		int setTtl;
		
		if (managedCategoryProfile.getTrustCategory()) {		
			setAccess = access;
			setVisib = visibility;
			setTtl = ttl;
			
			if (setAccess == null) {
				setAccess = managedCategoryProfile.getAccess();
			}
			if (setVisib == null) {
				setVisib = managedCategoryProfile.getVisibility();
			}
			if (setTtl == -1) {
				setTtl = managedCategoryProfile.getTtl();
			}	
		}else {
			setAccess = managedCategoryProfile.getAccess();
			setVisib = managedCategoryProfile.getVisibility();
			setTtl = managedCategoryProfile.getTtl();
		}
		
		/* Features that can be get from the mappingFile */
		
		Channel channel = DomainTools.getChannel();
		String setXsltURL = getXsltURL();
		String setItemXPath = getItemXPath();
			
		String dtd = source.getDtd();
		String xmlType = source.getXmlType();
		String xmlns = source.getXmlns();
		//TODO faire le root element
		//String rootElement = source.getRootElement();
			
		Mapping m = new Mapping();
		
		if (setXsltURL == null || setItemXPath == null) {
			if (dtd != null) {
				m = channel.getMappingByDtd(dtd);
			} else {
			if (xmlType != null) {
				m = channel.getMappingByXmlType(xmlType);
			} else {
			if (xmlns != null) {
				m = channel.getMappingByXmlns(xmlns);
			}}}
		
			if (setXsltURL == null) {
				setXsltURL = m.getXsltUrl();
			}
			if (setItemXPath == null) {
				setItemXPath = m.getItemXPath();
			}
		}
		
		computedFeatures.update(setVisib,setTtl,setAccess,setItemXPath,setXsltURL);
		
	}

	
	/**
	 * Evaluate visibility of current user for this managed source profile.
	 * Update customManagedCategory (belongs to user) if needed :
	 * add or remove customManagedSources associated with
	 * @param portletService
	 * @param customManagedCategory
	 */
	
	public void evaluateVisibilityAndUpdateCustomCategory(PortletService portletService, CustomManagedCategory customManagedCategory) {
			/*
			 * Algo pour gerer les customSourceProfiles :
			 * ------------------------------------
			 * user app. obliged => enregistrer la source dans le user profile + sortir
			 * user app. autoSub => enregistrer la source dans le user profile si c'est la première fois + sortir
			 * user app.allowed => rien à faire + sortir
			 * user n'app. rien => effacer la custom source .
			 */

			boolean isInObliged = false;
			boolean isInAutoSubscribed = false;
			boolean isInAllowed = false;
						
		/* ---OBLIGED SET--- */
			log.debug("Appel de evaluate sur DefenitionSets(obliged) de la cat : "+this.getName());
			isInObliged = visibility.getObliged().evaluateVisibility(portletService);
			log.debug("IsInObliged : "+isInObliged);
			if (isInObliged) {
				customManagedCategory.addManagedCustomSource(this);
			
			} else {
		/* ---AUTOSUBSCRIBED SET--- */	
				// TODO isInAutoSubscribed = visibility.getAutoSubscribed().evaluateVisibility(portletService);
				// en attendant : isInAutoSubscribed = false 
				
				if(isInAutoSubscribed) {
					// TODO l'ajouter dans le custom category si c'est la preniere fois
					// customManagedCategory.addManagedCustomSource(this);
				
				} else {
		/* ---ALLOWED SET--- */
					log.debug("Appel de evaluate sur DefenitionSets(allowed) de la source profile : "+this.getName());
					isInAllowed = visibility.getAllowed().evaluateVisibility(portletService);
					
					if (!isInAllowed) { // If isInAllowed : nothing to do
		/* ---CATEGORY NOT VISIBLE FOR USER--- */
						customManagedCategory.removeManagedCustomSource(this);
					}
					
				}	
			}
			// TODO retirer les customSource du user profile qui correspondent à des profiles 
			// de sources  disparus	
		}

	
	
	
	
/* ************************** ACCESSORS ******************************** */	


	/**
	 * Returns source of this managed source profile (if loaded)
	 * @return source
	 */
	protected Source getSource() {
		return source;
	}
	
	/**
	 * Sets source on the profile
	 * @param source
	 */
	protected void setSource(Source source) {
		this.source = source;
	}

	/**	 
	 * @return access
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getAccess()
	 */
	public Accessibility getAccess() {
		return computedFeatures.getAccess();
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	public void setAccess(Accessibility access) {
		this.access = access;
		computedFeatures.setIsComputed(false);
	}

	/**
	 * @return visibility
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() {
		return computedFeatures.getVisibility();
	}


	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * Returns ttl
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getTtl()
	 */
	public int getTtl() {
		return computedFeatures.getTtl();
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setTtl(int)
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
		computedFeatures.setIsComputed(false);
	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	protected boolean getSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Sets specificUserContent
	 * @param specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}


	/**
	 * Returns URL of xslt file
	 * @return xsltURL
	 * @see ManagedSourceProfile#xsltURL
	 */
	protected String getXsltURL() {
		return computedFeatures.getXsltUrl();
	}


	/**
	 * Sets URL of xslt file
	 * @param xsltURL
	 * @see ManagedSourceProfile#xsltURL
	 */
	public void setXsltURL(String xsltURL) {
		setXsltURL(xsltURL);
		computedFeatures.setIsComputed(false);
	}


	/**
	 * Returns Xpath to access to item in source XML file
	 * @return itemXPath
	 * @see ManagedSourceProfile#xsltURL
	 */
	protected String getItemXPath() {
		return  computedFeatures.getItemXPath();
	}

	/**
	 * Sets Xpath to access to item in source XML file
	 * @param itemXPath
	 * @see ManagedSourceProfile#xsltURL
	 */
	public void setItemXPath(String itemXPath) {
		setItemXPath(itemXPath);
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * Returns the dtd.
	 * @return dtd
	 * @see ManagedSourceProfile#dtd
	 */
	protected String getDtd() {
		return getDtd();
	}

	/**
	 * Sets dtd
	 * @param dtd 
	 * @see ManagedSourceProfile#dtd
	 */
	protected void setDtd(String dtd) {
		setDtd(dtd);
	}



	/** 
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAllowed(DefinitionSets d) {
		visibility.setAllowed(d);
		computedFeatures.setIsComputed(false);
		
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() {
		return computedFeatures.getVisibility().getAllowed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAutoSubcribed(DefinitionSets d) {
		visibility.setAutoSubscribed(d);
		computedFeatures.setIsComputed(false);	
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() {
		return computedFeatures.getVisibility().getAutoSubscribed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityObliged(DefinitionSets d) {
		visibility.setObliged(d);
		computedFeatures.setIsComputed(false);	
		
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() {
		return computedFeatures.getVisibility().getObliged();
	}


	public void setManagedCategoryProfile(ManagedCategoryProfile categoryProfile) {
		this.managedCategoryProfile = categoryProfile;
		
	}







	




	
	
}
