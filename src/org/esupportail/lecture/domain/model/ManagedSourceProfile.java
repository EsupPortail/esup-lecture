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
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;


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
	 * 
	 */
	private ManagedCategoryProfile ownerProfile;
	

	/*
	 ************************** PROPERTIES ******************************** */	
	
	/**
	 * Constructor
	 */
	public ManagedSourceProfile(ManagedCategoryProfile mcp) {
		log.debug("new ManagedSourceProfile(), ownerProfile = "+mcp.getId());
		ownerProfile = mcp;
		computedFeatures = new ComputedManagedSourceFeatures(this);
	}


	/*
	 *************************** METHODS ******************************** */	
	
	public void updateCustomCategory(CustomManagedCategory customManagedCategory, ExternalService externalService) throws ElementNotLoadedException {
		// no loadSource(externalService) is needed here
		setUpCustomCategoryVisibility(externalService,customManagedCategory);
		
	}

	

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#computeFeatures()
	 * Can be called only when source has been realy get. (Not at the instantiation of the object)
	 */
	public void computeFeatures() throws ElementNotLoadedException {
	
		/* Features that can be herited by the managedCategoryProfile */
		Accessibility setAccess;
		VisibilitySets setVisib;
		
		if (ownerProfile.getTrustCategory()) {		
			setAccess = access;
			setVisib = visibility;
			
			if (setAccess == null) {
				setAccess = ownerProfile.getAccess();
			}
			if (setVisib == null) {
				setVisib = ownerProfile.getVisibility();
			}

		}else {
			setAccess = ownerProfile.getAccess();
			setVisib = ownerProfile.getVisibility();
		}
				
		computedFeatures.update(setVisib,setAccess);
		
	}

	protected void loadSource(ExternalService externalService) throws ElementNotLoadedException {
			
		if(getAccess() == Accessibility.PUBLIC) {
			// managed SOurce Profile => single or globalSource
			// TODO (GB) le getSource est il "source" ou "managedSource" ?
			Source source = DomainTools.getDaoService().getSource(this);
			setElement(source);
			
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = externalService.getUserProxyTicketCAS();
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
	 * @param externalService
	 * @param customManagedCategory
	 * @throws ElementNotLoadedException 
	 * @return true if sourceProfile is visible by user (in Obliged or in autoSubscribed, or in Allowed)
	 */
	
	private boolean setUpCustomCategoryVisibility(ExternalService externalService, CustomManagedCategory customManagedCategory) throws ElementNotLoadedException {
			/*
			 * Algo pour gerer les customSourceProfiles :
			 * ------------------------------------
			 * user app. obliged => enregistrer la source dans le user profile + sortir
			 * user app. autoSub => enregistrer la source dans le user profile si c'est la première fois + sortir
			 * user app.allowed => rien à faire + sortir
			 * user n'app. rien => effacer la custom source .
			 */
		log.debug("setUpCustomCategoryVisibility");

			boolean isInObliged = false;
			boolean isInAutoSubscribed = false;
			boolean isInAllowed = false;
						
		/* ---OBLIGED SET--- */
			log.debug("Evaluation DefinitionSets(obliged) de la source profile : "+this.getId()+" pour la cat : "+customManagedCategory.getElementId());
			isInObliged = getVisibilityObliged().evaluateVisibility(externalService);
			log.debug("IsInObliged : "+isInObliged);
			if (isInObliged) {
				log.debug("Is in obliged");
				customManagedCategory.addCustomManagedSource(this);
			
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
					isInAllowed = getVisibilityAllowed().evaluateVisibility(externalService);
					
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

	
	
	
	
/* ************************** ACCESSORS ******************************** */	


	

	/**	 
	 * @return access
	 * @throws ElementNotLoadedException 
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	public Accessibility getAccess() throws ElementNotLoadedException {
		return computedFeatures.getAccess();
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	public void setAccess(Accessibility access) {
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
		return computedFeatures.getVisibility();
	}


	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * Returns ttl
	 * @throws ElementNotLoadedException 
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
	 */
	public int getTtl()  {
		// TODO (GB) retirer le ttl de la dtd de la source
		return ttl;
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
	 */
	public void setTtl(int ttl) {
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
	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}

	/** 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAllowed(DefinitionSets d) {
		visibility.setAllowed(d);
		computedFeatures.setIsComputed(false);
		
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() throws ElementNotLoadedException {
		return computedFeatures.getVisibility().getAllowed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAutoSubcribed(DefinitionSets d) {
		visibility.setAutoSubscribed(d);
		computedFeatures.setIsComputed(false);	
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() throws ElementNotLoadedException {
		return computedFeatures.getVisibility().getAutoSubscribed();
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityObliged(DefinitionSets d) {
		visibility.setObliged(d);
		computedFeatures.setIsComputed(false);	
		
	}

	/**
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() throws ElementNotLoadedException {
		return computedFeatures.getVisibility().getObliged();
	}









	


	









	




	
	
}
