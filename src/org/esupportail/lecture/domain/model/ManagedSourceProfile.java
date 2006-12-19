/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import javax.swing.text.DefaultEditorKit.CutAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;


/**
 * Managed source profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.SourceProfile
 * @see org.esupportail.lecture.domain.model.ManagedElementProfile
 *
 */
/**
 * @author gbouteil
 *
 */
public class ManagedSourceProfile extends SourceProfile implements ManagedElementProfile {
	
	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedSourceProfile.class); 
	
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

	/**
	 * profile of the owner category of this managed source profile 
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/**
	 * source profile Id defined in the xml file : interne Id of hte source Profile
	 */
	private String fileId;

	/*
	 ************************** PROPERTIES ******************************** */	
	
	/**
	 * Constructor
	 */
	public ManagedSourceProfile(ManagedCategoryProfile mcp) {
		if (log.isDebugEnabled()){
			log.debug("ManagedSourceProfile("+mcp.getId()+")");
		}
		categoryProfile = mcp;
		computedFeatures = new ComputedManagedSourceFeatures(this);
	}


	/*
	 *************************** METHODS ******************************** */	
	
	synchronized public void updateCustomCategory(CustomManagedCategory customManagedCategory, ExternalService ex) 
		throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("updateCustomCategory("+customManagedCategory.getElementId()+"externalService)");
		}
		// no loadSource(ex) is needed here
		setUpCustomCategoryVisibility(customManagedCategory,ex);
		
	}

	

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#computeFeatures()
	 * Can be called only when source has been realy get. (Not at the instantiation of the object)
	 */
	synchronized public void computeFeatures() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("computeFeatures()");
		}
	
		/* Features that can be herited by the managedCategoryProfile */
		Accessibility setAccess;
		VisibilitySets setVisib;
		
		if (categoryProfile.getTrustCategory()) {		
			setAccess = access;
			setVisib = visibility;
			
			if (setAccess == null) {
				setAccess = categoryProfile.getAccess();
			}
			if (setVisib == null) {
				setVisib = categoryProfile.getVisibility();
			}

		}else {
			setAccess = categoryProfile.getAccess();
			setVisib = categoryProfile.getVisibility();
		}
				
		computedFeatures.update(setVisib,setAccess);
		
	}

	synchronized protected void loadSource(ExternalService ex) throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("loadSource(externalService)");
		}
			
		if(getAccess() == Accessibility.PUBLIC) {
			// managed Source Profile => single or globalSource
			Source source = DomainTools.getDaoService().getSource(this);
			setElement(source);
			
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = ex.getUserProxyTicketCAS();
			Source source = DomainTools.getDaoService().getSource(this,ptCas);
			setElement(source);
			
		}
		//computedFeatures.compute();
		//computedFeatures.setIsComputed(false); // TODO (GB later) à optimiser
	}

	
	/**
	 * Evaluate visibility of current user for this managed source profile.
	 * Update customManagedCategory (belongs to user) if needed :
	 * add or remove customManagedSources associated with
	 * @param ex
	 * @param customManagedCategory
	 * @throws ElementNotLoadedException 
	 * @return true if sourceProfile is visible by user (in Obliged or in autoSubscribed, or in Allowed)
	 */
	
	synchronized private boolean setUpCustomCategoryVisibility(CustomManagedCategory customManagedCategory,ExternalService ex) 
		throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("setUpCustomCategoryVisibility("+customManagedCategory.getElementId()+",externalService)");
		}
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
			log.debug("Evaluation DefinitionSets(obliged) de la source profile : "+this.getId()+" pour la cat : "+customManagedCategory.getElementId());
			isInObliged = getVisibilityObliged().evaluateVisibility(ex);
			log.debug("IsInObliged : "+isInObliged);
			if (isInObliged) {
				log.debug("Is in obliged");
				customManagedCategory.addSubscription(this);
			
			} else {
		/* ---AUTOSUBSCRIBED SET--- */	
				// TODO (GB later) isInAutoSubscribed =  getVisibilitySubscribed().evaluateVisibility(portletService);
				// en attendant : isInAutoSubscribed = false 
				
				if(isInAutoSubscribed) {
					// TODO (GB later) l'ajouter dans le custom category si c'est la preniere fois
					// customManagedCategory.addManagedCustomSource(this);
				
				} else {
		/* ---ALLOWED SET--- */
					log.debug("Evaluation DefinitionSets(allowed) de la source profile : "+this.getId()+" pour la cat : "+customManagedCategory.getElementId());
					isInAllowed = getVisibilityAllowed().evaluateVisibility(ex);
					
					if (!isInAllowed) { // If isInAllowed : nothing to do
		/* ---CATEGORY NOT VISIBLE FOR USER--- */
						customManagedCategory.removeCustomManagedSource(this);
						return false;
					}
					
				}	
			}
			// TODO (GB later) retirer les customSource du user profile qui correspondent à des profiles 
			// de sources  disparus	
			return true;
		}
	

	/**	 
	 * @return access
	 * @throws ElementNotLoadedException 
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	synchronized public Accessibility getAccess() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getAccess()");
		}
		return computedFeatures.getAccess();
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	synchronized public void setAccess(Accessibility access) {
		if (log.isDebugEnabled()){
			log.debug("setAccess()");
		}
		this.access = access;
		computedFeatures.setIsComputed(false);
	}

	/**
	 * @return visibility
	 * @throws ElementNotLoadedException 
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getVisibility()");
		}
		return computedFeatures.getVisibility();
	}


	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	synchronized public void setVisibility(VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("setVisibility(visibility)");
		}
		this.visibility = visibility;
		computedFeatures.setIsComputed(false);
	}
	
	/** 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityAllowed(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityAllowed(definitionSets)");
		}
		visibility.setAllowed(d);
		computedFeatures.setIsComputed(false);
		
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getVisibilityAllowed()");
		}
		return computedFeatures.getVisibility().getAllowed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityAutoSubcribed(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityAutoSubcribed(definitionSets)");
		}
		visibility.setAutoSubscribed(d);
		computedFeatures.setIsComputed(false);	
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getVisibilityAutoSubscribed()");
		}
		return computedFeatures.getVisibility().getAutoSubscribed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityObliged(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityObliged(definitionSets)");
		}
		visibility.setObliged(d);
		computedFeatures.setIsComputed(false);	
		
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getVisibilityObliged()");
		}
		return computedFeatures.getVisibility().getObliged();
	}

	
	/* ************************** ACCESSORS ******************************** */	

	
	/**
	 * Returns ttl
	 * @throws ElementNotLoadedException 
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
	 */
	public int getTtl()  {
		return ttl;
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
	 */
	synchronized public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	public boolean isSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Sets specificUserContent
	 * @param specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	synchronized public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}


	public String getFileId() {
		return fileId;
	}


	synchronized public void setFileId(String fileId) {
		this.fileId = fileId;
		super.setId(super.makeId("m",categoryProfile.getId(),fileId));
	}









	


	









	




	
	
}
