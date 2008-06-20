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
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;


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
	 * @throws CategoryNotLoadedException 
	 */
	synchronized public VisibilityMode updateCustomCategory(CustomManagedCategory customManagedCategory, ExternalService ex) 
		throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - updateCustomCategory("+customManagedCategory.getElementId()+"externalService)");
		}
		// no loadSource(ex) is needed here
		return setUpCustomCategoryVisibility(customManagedCategory,ex);
		
	}

	

	/**
	 * Computes rights on parameters shared between parent ManagedCategory and managedSourceProfile
	 * ManagedCategory (edit, visibility,access)
	 * @throws CategoryNotLoadedException 
	 */
	synchronized public void computeFeatures() throws CategoryNotLoadedException   {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - computeFeatures()");
		}
	
		/* Features that can be herited by the managedCategoryProfile */
		Accessibility setAccess;
		VisibilitySets setVisib;
		int setTimeOut;
		
		if (categoryProfile.getTrustCategory()) {		
			setAccess = access;
			setVisib = visibility;
			setTimeOut = super.timeOut;
			
			if (setAccess == null) {
				setAccess = categoryProfile.getAccess();
			}
			if (setVisib == null) {
				setVisib = categoryProfile.getVisibility();
			}
			if (setTimeOut == 0) {
				setTimeOut = categoryProfile.getTimeOut();
			}

		}else {
			setAccess = categoryProfile.getAccess();
			setVisib = categoryProfile.getVisibility();
			setTimeOut = categoryProfile.getTimeOut();
			
		}
				
		features.update(setVisib,setAccess,setTimeOut);
		
	}

	/**
	 * Load the source referenced by this ManagedSourceProfile.
	 * @param ex access to externalService
	 * @throws SourceTimeOutException 
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.SourceProfile#loadSource(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	protected synchronized void loadSource(final ExternalService ex) throws InfoDomainException {
		if (log.isDebugEnabled()) {
			log.debug("id=" + this.getId() + " - loadSource(externalService)");
		}
			
		if (getAccess() == Accessibility.PUBLIC) {
			// managed Source Profile => single or globalSource
			Source source;
			try {
				source = DomainTools.getDaoService().getSource(this);
			} catch (InfoDaoException e) {
				String errorMsg = "Impossible to load source with ID: " + this.getId();
				log.error(errorMsg);
				throw new InfoDomainException(errorMsg, e);
			}
			setElement(source);
				
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = ex.getUserProxyTicketCAS();
			Source source = DomainTools.getDaoService().getSource(this, ptCas);
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
	 * @throws CategoryNotLoadedException 
	 */
	
	synchronized private VisibilityMode setUpCustomCategoryVisibility(CustomManagedCategory customManagedCategory,ExternalService ex) 
	throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setUpCustomCategoryVisibility("+customManagedCategory.getElementId()+",externalService)");
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
			return mode;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED){
			if (log.isTraceEnabled()){
				log.trace("IsInAutoSubscribed : "+mode);
			}
			// TODO (GB later) l'ajouter dans le custom category si c'est la premiere fois
			//customManagedCategory.addSubscription(this);
			return mode;
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (log.isTraceEnabled()){
				log.trace("IsInAllowed : "+mode);
			}
			// Nothing to do
			return mode;
		} 

		// ELSE not Visible
		customManagedCategory.removeCustomManagedSourceFromProfile(this.getId());
		mode = VisibilityMode.NOVISIBLE;
		return mode;
	}

	/**	
	 * Compute access value from feature and returns it
	 * @return access
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	synchronized public Accessibility getAccess() throws CategoryNotLoadedException  {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getAccess()");
		}
		return features.getAccess();
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	synchronized public void setAccess(Accessibility access) {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setAccess()");
		}
		this.access = access;
		features.setIsComputed(false);
	}

	/**
	 * Compute visibility value from features and returns it
	 * @return visibility
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getVisibility()");
		}
		return features.getVisibility();
	}


	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	synchronized public void setVisibility(VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setVisibility(visibility)");
		}
		this.visibility = visibility;
		features.setIsComputed(false);
	}
	
	/**
	 * Compute timeOut value from features and returns it
	 * @return timeOut
	 * @throws CategoryNotLoadedException 
	 */
	@Override
	public int getTimeOut() throws CategoryNotLoadedException  {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getTimeOut()");
		}
		if (log.isTraceEnabled()){
			log.trace("timeOut : "+features.getTimeOut());
		}
		return features.getTimeOut();
	}


	
	/**
	 * @see org.esupportail.lecture.domain.model.SourceProfile#setTimeOut(int)
	 */
	@Override
	synchronized public void setTimeOut(int timeOut) {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setTimeOut("+timeOut+")");
		}
		super.timeOut = timeOut;
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
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAllowed()
//	 */
//	public DefinitionSets getVisibilityAllowed() {
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
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
//	 */
//	public DefinitionSets getVisibilityAutoSubscribed(){
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
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
//	 */
//	public DefinitionSets getVisibilityObliged() {
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
