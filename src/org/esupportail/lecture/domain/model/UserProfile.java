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
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;



/**
 * Class where are defined user profile (and customizations ...)
 * @author gbouteil
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
	 * Id of the user, get from externalService request by USER_ID, defined in the channel config
	 * @see org.esupportail.lecture.domain.DomainTools#USER_ID
	 * @see ChannelConfig#loadUserId()
	 */
	private String userId;
	
	/**
	 * Hashtable of CustomContexts defined for the user, indexed by contexID.
	 */
	private Map<String,CustomContext> customContexts = new Hashtable<String,CustomContext>();

	/**
	 * Hashtable of CustomManagedCategory defined for the user, indexed by ManagedCategoryProfilID.
	 */
	private Map<String,CustomCategory> customCategories = new Hashtable<String, CustomCategory>();

	/**
	 * Hashtable of CustomSource defined for the user, indexed by SourceProfilID.
	 */
	private Map<String,CustomSource> customSources = new Hashtable<String,CustomSource>();
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
		this.setUserId(userId);
	}

	/**
	 * Default Constructor
	 */
	public UserProfile(){
		if (log.isDebugEnabled()){
    		log.debug("UserProfile("+userId+")");
    	}
	}

	/*
	 *************************** METHODS ************************************/

	
	/**
	 * Return the customContext identified by the contextId 
	 * if exists in userProfile, else create it.
	 * @param contextId identifier of the context refered by the customContext
	 * @return customContext (or null)
	 * @throws ContextNotFoundException 
	 */
	public CustomContext getCustomContext(String contextId) throws ContextNotFoundException{
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - getCustomContext("+contextId+")");
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
	
	/**
	 * @param contextId
	 * @return true if this userProfile contains the customContext identified by contextId
	 */
	public boolean containsCustomContext(String contextId) {
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - containsCustomContext("+contextId+")");
    	}
		return customContexts.containsKey(contextId);
	}

	/**
	 * Return the customCategory identified by the category id
	 * if exist,else,create it.
	 * @param categoryId identifier of the category refered by the customCategory
	 * @param ex access to externalService
	 * @return customCategory (or null)
	 * @throws CategoryNotVisibleException 
	 * @throws CustomCategoryNotFoundException 
	 * @throws InternalDomainException 
	 */
	public CustomCategory getCustomCategory(String categoryId,ExternalService ex) 
		throws  CategoryNotVisibleException, CustomCategoryNotFoundException, InternalDomainException {
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - getCustomCategory("+categoryId+")");
    	}
		// TODO (GB later) revoir avec customManagedCategory et customPersonalCategory
		CustomCategory customCategory = customCategories.get(categoryId);
		if(customCategory == null){
			updateCustomContextsForOneManagedCategory(categoryId,ex);
			customCategory = customCategories.get(categoryId);
			if (customCategory == null){
				String errorMsg = "CustomCategory associated to category "+categoryId
					+" is not found in user profile "+userId+"\nwhereas an updateCustomContextForOneManagedCategory " +
							"has done and category seems visible to user profile "+userId
							+".\nPerhaps this categoryProfile is not defined in current context.";
				log.error(errorMsg);
				throw new CustomCategoryNotFoundException(errorMsg);
			}
		}
		return customCategory;
	}
	
	/**
	 * Update every customContext of this userProfile for (only one)categoryProfile identified by categoryProfileId
	 * @param categoryProfileId
	 * @param ex access to externalService
	 * @throws CategoryNotVisibleException
	 * @throws InternalDomainException 
	 */
	protected void updateCustomContextsForOneManagedCategory(String categoryProfileId,ExternalService ex) 
		throws  CategoryNotVisibleException, InternalDomainException {
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - updateCustomContextsForOneManagedCategory("+categoryProfileId+",ex)");
    	}
		
//		} catch (ManagedCategoryProfileNotFoundException e) {
//			String errorMsg = "Unable to getCustomCategory(categoryId="+categoryId+") because of a ManagedCategoryProfileNotFoundException";
//			log.error(errorMsg);
//			cleanCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg,e);
//		}
	   	boolean categoryIsVisible = true;
		try {
			ManagedCategoryProfile mcp = DomainTools.getChannel().getManagedCategoryProfile(categoryProfileId);
			Set<Context> contexts = mcp.getContextsSet();
			
			// For all contexts refered by the managedCategoryProfile
			for(Context context : contexts){
				String contextId = context.getId();
				// Update on customContexts existing in userProfile
				if (containsCustomContext(contextId)) {
					CustomContext customContext;
					try {
						customContext = getCustomContext(contextId);
				
						VisibilityMode mode = mcp.updateCustomContext(customContext,ex);
						if (mode != VisibilityMode.NOVISIBLE){
							categoryIsVisible = false;
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
		
		} catch (ManagedCategoryProfileNotFoundException e) {
			String errorMsg = "Unable to updateCustomContextsForOneManagedCategory(categoryId="+categoryProfileId+") because of a ManagedCategoryProfileNotFoundException";
			log.error(errorMsg);
			cleanCustomCategoryFromProfile(categoryProfileId);
			throw new InternalDomainException(errorMsg,e);
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
    		log.debug("id="+userId+" - getCustomSource("+sourceId+")");
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
	 * Cleans all the userProfile content for customCategory categoryId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param categoryId customCatgeory ID
	 */
	public void cleanCustomCategoryFromProfile(String categoryId) {
		if (log.isDebugEnabled()){
			log.debug("cleanCustomCategoryFromProfile("+categoryId+")");
		}
		if (true) { // TODO (GB later) remplacer true par la valeur de l'option autoDelCustom
			removeCustomCategoryFromProfile(categoryId);
			log.info("customCatgeory "+categoryId+" has been removed from userProfile "+this.getUserId());
		}else {
			log.error("customCatgeory "+categoryId+" NEEDS TO BE REMOVED from userProfile "+this.getUserId());
		}
	}
	
	/**
	 * Cleans all the userProfile content for customSource sourceId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param sourceId customSource ID
	 */
	public void cleanCustomSourceFromProfile(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("cleanCustomSourceFromProfile("+sourceId+")");
		}
		if (true) { // TODO (GB later) remplacer true par la valeur de l'option autoDelCustom
			removeCustomSourceFromProfile(sourceId);
			log.info("CustomSource "+sourceId+" has been removed from userProfile "+this.getUserId());
		}else {
			log.error("CustomSource "+sourceId+" NEEDS TO BE REMOVED from userProfile "+this.getUserId());
		}
	}
	
	/**
	 * Remove the customSource sourceId in all the profile (this object and in customCategories)
	 * @param sourceId customSource ID
	 */
	public void removeCustomSourceFromProfile(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomSourceFromProfile("+sourceId+")");
		}
		
		for (CustomCategory custom : customCategories.values()){
			if (custom.containsCustomSource(sourceId)){
				custom.removeCustomSource(sourceId);
			}
		}
		removeCustomSource(sourceId);
	}
	
	/**
	 * Remove the customManagedSource sourceId in all the profile (this object and in customCategories)
	 * @param sourceId customManagedSource ID
	 */
	public void removeCustomManagedSourceFromProfile(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedSourceFromProfile("+sourceId+")");
		}
		
		for (CustomCategory custom : customCategories.values()){
			if (custom.containsCustomManagedSource(sourceId)){
			// For all parent customCategories
				custom.removeCustomManagedSource(sourceId);
			}
		}
		removeCustomSource(sourceId); // (GB) pourquoi pas un removeCustomManagedSource ?
	}
	
	/**
	 * Remove the customCategory categoryId in all the profile (this object and in customContexts)
	 * @param categoryId customCategory ID
	 */
	public void removeCustomCategoryFromProfile(String categoryId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomCategoryFromProfile("+categoryId+")");
		}
		
		for (CustomContext custom : customContexts.values()){
			if (custom.containsCustomCategory(categoryId)){
				// For all parent customContexts
				custom.removeCustomCategory(categoryId);
			}
		}
		removeCustomCategory(categoryId);
	}
	
	
	/**
	 * Remove the customManagedCategory categoryId in all the profile (this object and in customContexts)
	 * @param categoryId customManagedCategory ID
	 */
	public void removeCustomManagedCategoryFromProfile(String categoryId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedCategoryFromProfile("+categoryId+")");
		}
		
		for (CustomContext custom : customContexts.values()){
			if (custom.containsCustomManagedCategory(categoryId)){
				// For all parent customContexts
				custom.removeCustomManagedCategory(categoryId);
			}
		}
		removeCustomCategory(categoryId);
	}
	


	/**
	 * Add a customContext to this userProfile
	 * @param customContext
	 */
	protected void addCustomContext(CustomContext customContext){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - addCustomContext("+customContext.getElementId()+")");
    	}
		customContexts.put(customContext.getElementId(),customContext);
		DomainTools.getDaoService().updateUserProfile(this);
	}
	
	/**
	 * Remove a customContext from this userProfile only
	 * @param contextId id of the customContext to add
	 */
	protected void removeCustomContext(String contextId){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - removeCustomContext("+contextId+")");
    	}
	   	CustomContext custom = customContexts.get(contextId);
	   	if (custom!= null){
	   		custom.removeSubscriptions();
	   		customContexts.remove(contextId);
			DomainTools.getDaoService().deleteCustomContext(custom);
			DomainTools.getDaoService().updateUserProfile(this);
	   	}
	}
	
	/**
	 * Add a customCategory to this userProfile
	 * @param customCategory customCategory to add
	 */
	protected void addCustomCategory(CustomCategory customCategory){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - addCustomCategory("+customCategory.getElementId()+")");
    	}
		String id = customCategory.getElementId();
		customCategories.put(id,customCategory);
		DomainTools.getDaoService().updateUserProfile(this);
	}
	

	/**
	 * Remove a customCategory from this userProfile only
	 * @param categoryId
	 */
	protected void removeCustomCategory(String categoryId){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - removeCustomCategory("+categoryId+")");
    	}
	   	
	   	CustomCategory custom = customCategories.get(categoryId);
	   	if (custom != null){
	   		custom.removeSubscriptions();
	   		customCategories.remove(categoryId);
	   		DomainTools.getDaoService().deleteCustomCategory(custom);
			DomainTools.getDaoService().updateUserProfile(this);
	   	}
	}
	
	/**
	 * Add a customSource to this userProfile
	 * @param customSource customSource to add
	 */
	protected void addCustomSource(CustomSource customSource){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - addCustomSource("+customSource.getElementId()+")");
    	}
		customSources.put(customSource.getElementId(),customSource);
		DomainTools.getDaoService().updateUserProfile(this);
	}
	
	/**	 * 
	 * Remove a customCategory from this userProfile only
	 * @param sourceId
	 */
	protected void removeCustomSource(String sourceId){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - removeCustomSource("+sourceId+")");
    	}
		CustomSource cs = customSources.remove(sourceId);
		if (cs != null) {
			DomainTools.getDaoService().deleteCustomSource(cs);
			DomainTools.getDaoService().updateUserProfile(this);
		}
	}
		
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof UserProfile)) return false;
		final UserProfile userprofile = (UserProfile) o;
		if (!userprofile.getUserId().equals(this.getUserId())) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getUserId().hashCode();
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
	private Map<String, CustomCategory> getCustomCategories() {
		return customCategories;
	}

	/**
	 * @param customCategories 
	 */
	private void setCustomCategories(
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
	private Map<String, CustomSource> getCustomSources() {
		return customSources;
	}

	/**
	 * @param customSources
	 */
	private void setCustomSources(Map<String, CustomSource> customSources) {
		this.customSources = customSources;
	}

	

	


	
}
