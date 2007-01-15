/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;


/**
 * Managed source profile element. It refers a source and is defined in a managedCategory
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.SourceProfile
 * @see org.esupportail.lecture.domain.model.ManagedElementProfile
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
	 * Resolve feature values (access, visibility) from :
	 * - managedSourceProfile 
	 * - source 
	 * - trustCategory parameter 
	 */
	private ManagedSourceFeatures features;

	/**
	 * profile of the parent category of this managed source profile 
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/**
	 * source profile Id defined in the xml file : interne Id of the source Profile
	 */
	private String fileId;

	/*
	 ************************** INIT ******************************** */	
	
	/**
	 * Constructor
	 * @param mcp profile of the managedCategory parent of this ManagedSourceProfile
	 */
	public ManagedSourceProfile(ManagedCategoryProfile mcp) {
		if (log.isDebugEnabled()){
			log.debug("ManagedSourceProfile("+mcp.getId()+")");
		}
		categoryProfile = mcp;
		features = new ManagedSourceFeatures(this);
	}


	/*
	 *************************** METHODS ******************************** */	
	
	/** 
	 * Update CustomCategory with this ManagedSourceProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customCategory.
	 * @param customManagedCategory the customManagedCategory to update
	 * @param ex access to externalService
	 * @return true if the source is visible by the userProfile
	 * @throws ComputeFeaturesException
	 */
	synchronized public boolean updateCustomCategory(CustomManagedCategory customManagedCategory, ExternalService ex) 
		throws ComputeFeaturesException {
		if (log.isDebugEnabled()){
			log.debug("updateCustomCategory("+customManagedCategory.getElementId()+"externalService)");
		}
		// no loadSource(ex) is needed here
		return setUpCustomCategoryVisibility(customManagedCategory,ex);
		
	}

	

	/**
	 * Computes rights on parameters shared between parent ManagedCategory and managedSourceProfile
	 * ManagedCategory (edit, visibility,access)
	 * @throws ComputeFeaturesException 
	 */
	synchronized public void computeFeatures() throws ComputeFeaturesException {
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
				
		features.update(setVisib,setAccess);
		
	}

	/**
	 * Load the source referenced by this ManagedSourceProfile
	 * @param ex access to externalService
	 * @throws ComputeFeaturesException
	 * @see org.esupportail.lecture.domain.model.SourceProfile#loadSource(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	synchronized protected void loadSource(ExternalService ex) throws ComputeFeaturesException {
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
		
		//features.compute();
		//features.setIsComputed(false); // TODO (GB later) à optimiser
	}

	
	/**
	 * Evaluate visibility of current user for this managed source profile.
	 * Update customManagedCategory (belongs to user) if needed :
	 * add or remove customManagedSources associated with this ManagedSourceProfile
	 * @param ex access to externalService to evaluate visibility
	 * @param customManagedCategory customManagedCategory to set up
	 * @return true if sourceProfile is visible by user (in Obliged or in autoSubscribed, or in Allowed)
	 * @throws ComputeFeaturesException 
	 */
	
	synchronized private boolean setUpCustomCategoryVisibility(CustomManagedCategory customManagedCategory,ExternalService ex) 
	throws ComputeFeaturesException {
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
		
		VisibilityMode mode = getVisibility().whichVisibility(ex);
		
		if (mode == VisibilityMode.OBLIGED){
			if (log.isTraceEnabled()){
				log.trace("IsInObliged : "+mode);
			}
			customManagedCategory.addSubscription(this);
			return true;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED){
			if (log.isTraceEnabled()){
				log.trace("IsInAutoSubscribed : "+mode);
			}
			// TODO (GB later) l'ajouter dans le custom category si c'est la premiere fois
			//customManagedCategory.addSubscription(this);
			return true;
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (log.isTraceEnabled()){
				log.trace("IsInAllowed : "+mode);
			}
			// Nothing to do
			return true;
		} 
		// TODO (GB later) retirer les customSource du user profile qui correspondent à des profiles 
		// de sources  disparus	
		
		// ELSE not Visible
		customManagedCategory.removeCustomManagedSource(this);
		return false;
		
//
//			boolean isInObliged = false;
//			boolean isInAutoSubscribed = false;
//			boolean isInAllowed = false;
//						
//		/* ---OBLIGED SET--- */
//			log.debug("Evaluation DefinitionSets(obliged) de la source profile : "+this.getId()+" pour la cat : "+customManagedCategory.getElementId());
//			isInObliged = getVisibilityObliged().evaluateVisibility(ex);
//			log.debug("IsInObliged : "+isInObliged);
//			if (isInObliged) {
//				log.debug("Is in obliged");
//				customManagedCategory.addSubscription(this);
//			
//			} else {
//		/* ---AUTOSUBSCRIBED SET--- */	
//				// en attendant : isInAutoSubscribed = false 
//				
//				if(isInAutoSubscribed) {
//					// customManagedCategory.addManagedCustomSource(this);
//				
//				} else {
//		/* ---ALLOWED SET--- */
//					log.debug("Evaluation DefinitionSets(allowed) de la source profile : "+this.getId()+" pour la cat : "+customManagedCategory.getElementId());
//					isInAllowed = getVisibilityAllowed().evaluateVisibility(ex);
//					
//					if (!isInAllowed) { // If isInAllowed : nothing to do
//		/* ---CATEGORY NOT VISIBLE FOR USER--- */
//						customManagedCategory.removeCustomManagedSource(this);
//						return false;
//					}
//					
//				}	
//			}
//			
//			return true;
//		}
	}

	/**	
	 * Compute access value from feature and returns it
	 * @return access
	 * @throws ComputeFeaturesException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	synchronized public Accessibility getAccess() throws ComputeFeaturesException {
		if (log.isDebugEnabled()){
			log.debug("getAccess()");
		}
		return features.getAccess();
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
		features.setIsComputed(false);
	}

	/**
	 * Compute visibility value from features and returns it
	 * @return visibility
	 * @throws ComputeFeaturesException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws ComputeFeaturesException  {
		if (log.isDebugEnabled()){
			log.debug("getVisibility()");
		}
		return features.getVisibility();
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
		features.setIsComputed(false);
	}
	
//	/** 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityAllowed(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityAllowed(definitionSets)");
//		}
//		visibility.setAllowed(d);
//		features.setIsComputed(false);
//		
//	}
//
//	/**
//	 * @throws ComputeFeaturesException 
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAllowed()
//	 */
//	public DefinitionSets getVisibilityAllowed() throws ComputeFeaturesException  {
//		if (log.isDebugEnabled()){
//			log.debug("getVisibilityAllowed()");
//		}
//		return features.getVisibility().getAllowed();
//	}
//
//	/**
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityAutoSubcribed(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityAutoSubcribed(definitionSets)");
//		}
//		visibility.setAutoSubscribed(d);
//		features.setIsComputed(false);	
//	}
//
//	/**
//	 * @throws ComputeFeaturesException 
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
//	 */
//	public DefinitionSets getVisibilityAutoSubscribed() throws ComputeFeaturesException {
//		if (log.isDebugEnabled()){
//			log.debug("getVisibilityAutoSubscribed()");
//		}
//		return features.getVisibility().getAutoSubscribed();
//	}
//
//	/**
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityObliged(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityObliged(definitionSets)");
//		}
//		visibility.setObliged(d);
//		features.setIsComputed(false);	
//		
//	}
//
//	/**
//	 * @throws ComputeFeaturesException 
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
//	 */
//	public DefinitionSets getVisibilityObliged() throws ComputeFeaturesException  {
//		if (log.isDebugEnabled()){
//			log.debug("getVisibilityObliged()");
//		}
//		return features.getVisibility().getObliged();
//	}

	
	/* 
	 *************************** ACCESSORS ******************************** */	

	
	/**
	 * Returns ttl
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
	 */
	@Override
	public int getTtl()  {
		return ttl;
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
	 */
	@Override
	synchronized public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	@Override
	public boolean isSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Sets specificUserContent
	 * @param specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	@Override
	synchronized public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}


	/**
	 * @return fileId : sourceProfileId defined in xml file category
	 */
	public String getFileId() {
		return fileId;
	}


	/**
	 * @param fileId sourceProfileId defiend in xml category file
	 */
	synchronized public void setFileId(String fileId) {
		this.fileId = fileId;
		super.setId(super.makeId("m",categoryProfile.getId(),fileId));
	}
	
}
