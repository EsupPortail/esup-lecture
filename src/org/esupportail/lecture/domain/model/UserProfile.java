package org.esupportail.lecture.domain.model;


import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
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

	
	/* GETTING CUSTOM ELEMENTS */
	
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
	 * Return the customCategory identified by the category id
	 * if exist,else,create it.
	 * @param categoryId identifier of the category refered by the customCategory
	 * @param ex access to externalService
	 * @return customCategory (or null)
	 * @throws CategoryNotVisibleException 
	 * @throws CustomCategoryNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 */
	public CustomCategory getCustomCategory(String categoryId,ExternalService ex) 
		throws  CategoryNotVisibleException, CustomCategoryNotFoundException, InternalDomainException, CategoryTimeOutException, CategoryOutOfReachException {
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - getCustomCategory("+categoryId+")");
    	}
		// TODO (GB later) revoir avec customManagedCategory et customPersonalCategory
		CustomCategory customCategory = customCategories.get(categoryId);
		if (customCategory == null){
			try {
				updateCustomContextsForOneManagedCategory(categoryId,ex);
			} catch (CategoryNotLoadedException e) {
				// Dans ce cas : la managedCategory n'est pointé par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				removeCustomManagedCategoryIfOrphan(categoryId);
				throw new CategoryOutOfReachException("ManagedCategory "+categoryId+
						"is not refered by any customContext in userProfile "+getUserId());
			}
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
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotLoadedException 
	 */
	protected void updateCustomContextsForOneManagedCategory(String categoryProfileId,ExternalService ex) 
		throws  CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException, CategoryNotLoadedException {
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
					} 
				}
			}
		
		} catch (ManagedCategoryProfileNotFoundException e) {
			String errorMsg = "Unable to updateCustomContextsForOneManagedCategory(categoryId="+categoryProfileId+") because of a ManagedCategoryProfileNotFoundException";
			log.error(errorMsg);
			//cleanCustomCategoryFromProfile(categoryProfileId);
			removeCustomCategoryFromProfile(categoryProfileId);
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
		CustomSource customSource = customSources.get(sourceId);
		if (customSource == null){
			String errorMsg = "CustomSource "+sourceId+" is not found in userProfile "+this.userId;
			log.error(errorMsg);
			throw new CustomSourceNotFoundException(errorMsg);
		}
		
		return customSource;
	}

	/* ADD CUSTOM ELEMENTS */
	
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
	
	/* REMOVE/CLEAN CUSTOM ELEMENTS FROM PROFILE */
	
	/**
	 * Cleans the userProfile content for customCategory categoryId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param categoryId customCategory ID
	 */
	public void cleanCustomCategory(String categoryId) {
		if (log.isDebugEnabled()){
			log.debug("cleanCustomCategory("+categoryId+")");
		}
		if (true) { // TODO (GB later) remplacer true par la valeur de l'option autoDelCustom (sinon : marquer l'objet)
			removeCustomCategory(categoryId);
			log.info("customCatgeory "+categoryId+" has been removed from userProfile "+this.getUserId());
		}else {
			log.info("customCatgeory "+categoryId+" NEEDS TO BE REMOVED from userProfile "+this.getUserId());
		}
	}
	
	/**
	 * Cleans the userProfile content for customSource sourceId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param sourceId customSource ID
	 */
	public void cleanCustomSource(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("cleanCustomSource("+sourceId+")");
		}
		// TODO (GB later) : ici ou dans removeCustomSource le 
		if (true) { // TODO (GB later) remplacer true par la valeur de l'option autoDelCustom (sinon : marquer l'objet)
			removeCustomSource(sourceId);
			log.info("CustomSource "+sourceId+" has been removed from userProfile "+this.getUserId());
		}else {
			log.info("CustomSource "+sourceId+" NEEDS TO BE REMOVED from userProfile "+this.getUserId());
		}
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
		cleanCustomCategory(categoryId);
	}
	
	/**
	 * Remove the customManagedCategory categoryId in all the profile (this object and in customContexts)
	 * @param categoryId customManagedCategory ID
	 */
	public void removeCustomManagedCategoryFromProfile(String categoryId) {
		//TODO (GB) A qoui sert il celui ci ?
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedCategoryFromProfile("+categoryId+")");
		}
		
		for (CustomContext custom : customContexts.values()){
			if (custom.containsCustomManagedCategory(categoryId)){
				// For all parent customContexts
				custom.removeCustomManagedCategory(categoryId);
			}
		}
		cleanCustomCategory(categoryId); 
	}
	
	/**
	 * Remove the customManagedCategory categoryId only if it is not refered in any customContext
	 * @param categoryId customManagedCategory ID
	 */
	public void removeCustomManagedCategoryIfOrphan(String categoryId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedCategoryIfOrphan("+categoryId+")");
		}
		boolean isOrphan = true;
		for (CustomContext custom : customContexts.values()){
			if (custom.containsCustomManagedCategory(categoryId)){
				isOrphan = false;
				break;
			}
		}
		if (isOrphan){
			cleanCustomCategory(categoryId);
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
		cleanCustomSource(sourceId);
	}
	
	/**
	 * Remove the customManagedSource sourceId in all the profile (this object and in customCategories)
	 * @param sourceId customManagedSource ID
	 */
	public void removeCustomManagedSourceFromProfile(String sourceId) {
		//TODO (GB) A qoui sert il celui ci ?
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedSourceFromProfile("+sourceId+")");
		}
		
		for (CustomCategory custom : customCategories.values()){
			if (custom.containsCustomManagedSource(sourceId)){
			// For all parent customCategories
				custom.removeCustomManagedSource(sourceId);
			}
		}
		cleanCustomSource(sourceId); // TODO (GB) pourquoi pas un removeCustomManagedSource ?
	}
	
	
	/**
	 * Remove the customManagedSource sourceId only if it is not refered in any customCategory
	 * @param sourceId customManagedSource ID
	 */
	public void removeCustomManagedSourceIfOrphan(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedSourceIfOrphan("+sourceId+")");
		}
		boolean isOrphan = true;
		for (CustomCategory custom : customCategories.values()){
			if (custom.containsCustomManagedSource(sourceId)){
				isOrphan = false;
				break;
			}
		}
		if (isOrphan){
			cleanCustomSource(sourceId);
		}
	}
	
	/* REMOVE CUSTOM ELEMENTS : ATOMIC METHODS */
	
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
	 * Remove a customCategory from this userProfile only
	 * @param categoryId
	 */
	private void removeCustomCategory(String categoryId){
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
	
	/**	 * 
	 * Remove a customCategory from this userProfile only
	 * @param sourceId
	 */
	private void removeCustomSource(String sourceId){
	   	if (log.isDebugEnabled()){
    		log.debug("id="+userId+" - removeCustomSource("+sourceId+")");
    	}
		CustomSource cs = customSources.remove(sourceId);
		if (cs != null) {
			DomainTools.getDaoService().deleteCustomSource(cs);
			DomainTools.getDaoService().updateUserProfile(this);
		}
	}
	
	/* MISCELLANEOUS */
	
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer(getClass().getSimpleName() + "#" + hashCode() 
			+ "[userId=[" + userId + "], userProfilePK=[" + userProfilePK + "], ");
		// customContexts
		string.append("\n customContexts=[");
		for (String key : customContexts.keySet()) {
			string.append("\n  ");
			string.append(customContexts.get(key)).append(", ");
		}
		string.append("], ");
		// customCategories
		string.append("\n customCategories=[");
		for (String key : customCategories.keySet()) {
			string.append("\n  ");
			string.append(customCategories.get(key)).append(", ");
		}
		string.append("], ");
		// customSources
		string.append("\n customSources=[");
		for (String key : customSources.keySet()) {
			string.append("\n  ");
			string.append(customSources.get(key)).append(", ");
		}
		string.append("]\n]");
		return string.toString();
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

//	/**
//	 * @return customContexts
//	 */
//	private Map<String, CustomContext> getCustomContexts() {
//		return customContexts;
//	}

	
//	/**
//	 * @param customContexts
//	 */
//	private void setCustomContexts(Map<String, CustomContext> customContexts) {
//		this.customContexts = customContexts;
//	}

//	/**
//	 * @return customManagedCategories
//	 */
//	private Map<String, CustomCategory> getCustomCategories() {
//		return customCategories;
//	}

//	/**
//	 * @param customCategories 
//	 */
//	private void setCustomCategories(
//			Map<String, CustomCategory> customCategories) {
//		this.customCategories = customCategories;
//	}

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

//	/**
//	 * @return custom sources from this userProfile
//	 */
//	private Map<String, CustomSource> getCustomSources() {
//		return customSources;
//	}

//	/**
//	 * @param customSources
//	 */
//	private void setCustomSources(Map<String, CustomSource> customSources) {
//		this.customSources = customSources;
//	}


	

	


	
}
