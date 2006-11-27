package org.esupportail.lecture.domain.model;


import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CustomCategoryNotFoundException;



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
	// TODO why not customCategories ?
	private Map<String,CustomManagedCategory> customManagedCategories;

	/**
	 * Hashtable of CustomSource defined for the user, indexed by SourceProfilID.
	 */
	private Map<String,CustomSource> customSources;

	
	/*
	 ************************** Initialization ************************************/
	
	/**
	 * Constructor
	 */
	public UserProfile(){
		customContexts = new Hashtable<String,CustomContext>();
		customManagedCategories = new Hashtable<String, CustomManagedCategory>();
	}
	
	/**
	 * Constructor
	 * @param userId
	 */
	public UserProfile(String userId){
		customContexts = new Hashtable<String,CustomContext>();
		customManagedCategories = new Hashtable<String, CustomManagedCategory>();
		customSources = new Hashtable<String,CustomSource>();
		this.setUserId(userId);
	}
	/*
	 *************************** METHODS ************************************/

	
	/**
	 * Return the customContext identified by the context id" 
	 * if exists, else create it.
	 * @param contextId identifier of the context refered by the customContext
	 * @return customContext (or null)
	 */
	public CustomContext getCustomContext(String contextId){
		CustomContext customContext = 
				customContexts.get(contextId);
		if (customContext == null){
			customContext = new CustomContext(contextId,this);
			addCustomContext(customContext);
		}
		
		return customContext;
	}
	
	/**
	 * Return the customCategory identifed by the category id
	 * if exist,else,create it.
	 * @param categoryId identifier of the category refered by the customCategory
	 * @return customCategory (or null)
	 * @throws CustomCategoryNotFoundException 
	 */
	public CustomCategory getCustomCategory(String categoryId) throws CustomCategoryNotFoundException{
		// TODO (later) avec customManagedCategory et customPersonalCategory
		CustomManagedCategory customCategory = 
			customManagedCategories.get(categoryId);
		if(customCategory == null){
			throw new CustomCategoryNotFoundException ("CustomCategory "+categoryId+" is not found in userProfile "+this.userId);
		}
		return customCategory;
	}
	
	/**
	 * Return the customSource identified by the source Id
	 * @param sourceId identifier of the source refered by the customSource
	 * @return customSource
	 */
	public CustomSource getCustomSource(String sourceId) {
		CustomSource customSource = 
			customSources.get(sourceId);
		if(customSource == null){
			log.warn("Requested customSource "+sourceId+"is not found in UserProfile "+this.userId);
		}
		
		return customSource;
	}
	
	/**
	 * @param customContext
	 */
	public void addCustomContext(CustomContext customContext){
		customContexts.put(customContext.getContextId(),customContext);
	}
	
	/**
	 * @param customCategory
	 */
	public void addCustomManagedCategory(CustomManagedCategory customCategory){
		customManagedCategories.put(customCategory.getCategoryProfileID(),customCategory);
	}
	

	/**
	 * @param categoryId
	 */
	public void removeCustomManagedCategory(String categoryId){
		customManagedCategories.remove(categoryId);
	}
	
	/**
	 * @param customSource
	 */
	public void addCustomSource(CustomSource customSource){
		customSources.put(customSource.getSourceProfileId(),customSource);
	}
	
	/**
	 * @param sourceId
	 */
	public void removeCustomSource(CustomSource sourceId){
		customSources.remove(sourceId);
	}
		
	/* ************************** ACCESSORS ********************************* */

//	/**
//	 * @return Returns the customContexts.
//	 */
//	public Hashtable<String, CustomContext> getCustomContexts() {
//		return customContexts;
//	}
//
//	/**
//	 * @param customContexts The customContexts to set.
//	 */
//	public void setCustomContexts(Hashtable<String, CustomContext> customContexts) {
//		this.customContexts = customContexts;
//	}

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
	public Map<String, CustomContext> getCustomContexts() {
		return customContexts;
	}

	/**
	 * @param customContexts
	 */
	public void setCustomContexts(Map<String, CustomContext> customContexts) {
		this.customContexts = customContexts;
	}

	/**
	 * @return customManagedCategories
	 */
	public Map<String, CustomManagedCategory> getCustomManagedCategories() {
		return customManagedCategories;
	}

	/**
	 * @param customManagedCategories
	 */
	public void setCustomManagedCategories(
			Map<String, CustomManagedCategory> customManagedCategories) {
		this.customManagedCategories = customManagedCategories;
	}

	
}
