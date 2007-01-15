package org.esupportail.lecture.domain.model;


import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;



/**
 * Class where are defined user profile (and customizations ...)
 * @author gbouteil
 *
 */
/**
 * @author gwen
 *
 */
public class UserProfile {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserProfile.class);
	
	/**
	 * Id of the user, get from portlet request by USER_ID, defined in the channel config
	 * @see org.esupportail.lecture.domain.DomainTools#USER_ID
	 * @see ChannelConfig#loadUserId()
	 */
	private String userId;
	
	/**
	 * Hashtable of CustomContexts defined for the user, indexed by contexID.
	 */
	private Map<String,CustomContext> customContexts;

	/**
	 * Hashtable of CustomManagedCategory defined for the user, indexed by ManagedCategoryProfilID.
	 */
	private Map<String,CustomCategory> customCategories;

	/**
	 * Hashtable of CustomSource defined for the user, indexed by SourceProfilID.
	 */
	private Map<String,CustomSource> customSources;
	/**
	 * Database Primary Key
	 */
	private long userProfilePK;

	
	/*
	 ************************** Initialization ************************************/
	

	
	/**
	 * Constructor
	 * @param userId
	 */
	public UserProfile(String userId){
	   	if (log.isDebugEnabled()){
    		log.debug("UserProfile("+userId+")");
    	}
		customContexts = new Hashtable<String,CustomContext>();
		customCategories = new Hashtable<String, CustomCategory>();
		customSources = new Hashtable<String,CustomSource>();
		this.setUserId(userId);
	}

	/**
	 * Constructor
	 */
	public UserProfile(){
		customContexts = new Hashtable<String,CustomContext>();
		customCategories = new Hashtable<String, CustomCategory>();
		customSources = new Hashtable<String,CustomSource>();
	}

	/*
	 *************************** METHODS ************************************/

	
	/**
	 * Return the customContext identified by the context id" 
	 * if exists, else create it.
	 * @param contextId identifier of the context refered by the customContext
	 * @return customContext (or null)
	 * @throws ContextNotFoundException 
	 */
	public CustomContext getCustomContext(String contextId) throws ContextNotFoundException{
	   	if (log.isDebugEnabled()){
    		log.debug("getCustomContext("+contextId+")");
    	}
		CustomContext customContext = customContexts.get(contextId);
		if (customContext == null){
			if (!DomainTools.getChannel().isThereContext(contextId)) {
				String errorMsg = "Context "+contextId+ " is not found in Channel";
				log.error(errorMsg);
				throw new ContextNotFoundException(errorMsg);
			}
			customContext = new CustomContext(contextId,this);
			addCustomContext(customContext);
		}
		
		return customContext;
	}
	
	public boolean containsCustomContext(String contextId) {
		return customContexts.containsKey(contextId);
	}

	/**
	 * Return the customCategory identifed by the category id
	 * if exist,else,create it.
	 * @param categoryId identifier of the category refered by the customCategory
	 * @return customCategory (or null)
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws ElementNotLoadedException 
	 * @throws CustomContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws ElementNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CustomCategoryNotFoundException 
	 * @throws CustomContextNotFoundException 
	 */
	public CustomCategory getCustomCategory(String categoryId,ExternalService ex) 
		throws ManagedCategoryProfileNotFoundException, CategoryNotVisibleException, CustomCategoryNotFoundException {
	   	if (log.isDebugEnabled()){
    		log.debug("getCustomCategory("+categoryId+")");
    	}
		// TODO (GB later) revoir avec customManagedCategory et customPersonalCategory
		CustomCategory customCategory = customCategories.get(categoryId);
		if(customCategory == null){
			updateCustomContextsForOneManagedCategory(categoryId,ex);
			customCategory = customCategories.get(categoryId);
			if (customCategory == null){
				String errorMsg = "CustomCategory associated to category "+categoryId
					+"is not found in user profile "+userId+"whereas an updateCustimContextForOneManagedCategory " +
							"has done and category seems visible to user profile "+userId;
				log.error(errorMsg);
				throw new CustomCategoryNotFoundException(errorMsg);
			}
		}
		return customCategory;
	}
	
	protected void updateCustomContextsForOneManagedCategory(String categoryProfileId,ExternalService ex) 
		throws ManagedCategoryProfileNotFoundException, CategoryNotVisibleException {
		
		ManagedCategoryProfile mcp = DomainTools.getChannel().getManagedCategoryProfile(categoryProfileId);
		Set<Context> contexts = mcp.getContextsSet();
		boolean categoryIsVisible = true;
		// For all contexts refered by the managedCategoryProfile
		for(Context context : contexts){
			String contextId = context.getId();
			// Update on customContexts existing in userProfile
			if (containsCustomContext(contextId)) {
				CustomContext customContext;
				try {
					customContext = getCustomContext(contextId);
				
					if (!mcp.updateCustomContext(customContext, ex)){
						categoryIsVisible = false;
					} else {
						DomainTools.getDaoService().updateCustomContext(customContext);
					}
				} catch (ContextNotFoundException e) {
					log.error("Impossible to get CustomContext associated to context "+ contextId
							+" for managedCategoryProfile "+mcp.getId()+" because context not found",e);
				} catch (ComputeFeaturesException e) {
					log.error("Impossible to update CustomContext associated to context "+ contextId
							+" for managedCategoryProfile "+mcp.getId()+"because an error occured when computing features",e);
				}
			}
		}
		if (!categoryIsVisible){
			String errorMsg = "Category "+categoryProfileId+" is not visible for user profile "+userId;
			log.error(errorMsg);
			throw new CategoryNotVisibleException(errorMsg);
		}
		
	}
	
	/**
	 * Return the customSource identified by the source Id
	 * @param sourceId identifier of the source refered by the customSource
	 * @return customSource
	 * @throws CustomSourceNotFoundException 
	 */
	public CustomSource getCustomSource(String sourceId) throws CustomSourceNotFoundException {
	   	if (log.isDebugEnabled()){
    		log.debug("getCustomSource("+sourceId+")");
    	}
	   	// TODO (GB later) revoir avec customManagedSource et customPersonalSource
		CustomSource customSource = 
			customSources.get(sourceId);
		if(customSource == null){
			String errorMsg = "CustomSource "+sourceId+" is not found in userProfile "+this.userId;
			log.error(errorMsg);
			throw new CustomSourceNotFoundException(errorMsg);
		}
		
		return customSource;
	}
	
	/**
	 * @param customContext
	 */
	public void addCustomContext(CustomContext customContext){
	   	if (log.isDebugEnabled()){
    		log.debug("addCustomContext("+customContext.getElementId()+")");
    	}
		customContexts.put(customContext.getElementId(),customContext);
		DomainTools.getDaoService().updateUserProfile(this);
	}
	
	/**
	 * @param contextId 
	 */
	public void removeCustomContext(String contextId){
	   	if (log.isDebugEnabled()){
    		log.debug("removeCustomContext("+contextId+")");
    	}
	   	CustomContext cctx = customContexts.remove(contextId);
		if( cctx!= null) {
			DomainTools.getDaoService().deleteCustomContext(cctx);
		}
	}
	
	/**
	 * @param customCategory
	 */
	public void addCustomCategory(CustomCategory customCategory){
	   	if (log.isDebugEnabled()){
    		log.debug("addCustomCategory("+customCategory.getElementId()+")");
    	}
		String id = customCategory.getElementId();
		customCategories.put(id,customCategory);
	}
	

	/**
	 * @param categoryId
	 */
	public void removeCustomCategory(String categoryId){
	   	if (log.isDebugEnabled()){
    		log.debug("removeCustomCategory("+categoryId+")");
    	}
	   	CustomCategory ccat = customCategories.remove(categoryId);
		if( ccat!= null) {
			DomainTools.getDaoService().deleteCustomCategory(ccat);
		}
	}
	
	/**
	 * @param customSource
	 */
	public void addCustomSource(CustomSource customSource){
	   	if (log.isDebugEnabled()){
    		log.debug("addCustomSource("+customSource.getElementId()+")");
    	}
		customSources.put(customSource.getElementId(),customSource);
	}
	
	/**
	 * @param sourceId
	 */
	public void removeCustomSource(String sourceId){
	   	if (log.isDebugEnabled()){
    		log.debug("removeCustomSource("+sourceId+")");
    	}
		CustomSource cs = customSources.remove(sourceId);
		if (cs != null) {
			DomainTools.getDaoService().deleteCustomSource(cs);
		}
	}
		
	/* ************************** ACCESSORS ********************************* */



	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return customContexts
	 */
	private Map<String, CustomContext> getCustomContexts() {
		return customContexts;
	}

	/**
	 * @param customContexts
	 */
	private void setCustomContexts(Map<String, CustomContext> customContexts) {
		this.customContexts = customContexts;
	}

	/**
	 * @return customManagedCategories
	 */
	public Map<String, CustomCategory> getCustomCategories() {
		return customCategories;
	}

	/**
	 * @param customCategories 
	 */
	public void setCustomCategories(
			Map<String, CustomCategory> customCategories) {
		this.customCategories = customCategories;
	}

	/**
	 * @return database primary Key
	 */
	public long getUserProfilePK() {
		return userProfilePK;
	}

	/**
	 * @param userProfilePK - database Primary Key
	 */
	public void setUserProfilePK(long userProfilePK) {
		this.userProfilePK = userProfilePK;
	}

	/**
	 * @return custom sources from this userProfile
	 */
	public Map<String, CustomSource> getCustomSources() {
		return customSources;
	}

	/**
	 * @param customSources
	 */
	public void setCustomSources(Map<String, CustomSource> customSources) {
		this.customSources = customSources;
	}




	
}
