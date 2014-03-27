package org.esupportail.lecture.domain.model;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;



/**
 * Class where are defined user profile (and customizations ...).
 * @author gbouteil
 *
 */
public class UserProfile {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * ID string for log.
	 */
	private static final String ID = "id=";

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(UserProfile.class);
	
	/**
	 * Id of the user, get from externalService.
	 */
	private String userId;
	
	/**
	 * Hashtable of CustomContexts defined for the user, indexed by contexID.
	 */
	private Map<String, CustomContext> customContexts = new Hashtable<String, CustomContext>();

	/**
	 * Hashtable of CustomManagedCategory defined for the user, indexed by ManagedCategoryProfilID.
	 */
	private Map<String, CustomCategory> customCategories = new Hashtable<String, CustomCategory>();

	/**
	 * Hashtable of CustomSource defined for the user, indexed by SourceProfilID.
	 */
	private Map<String, CustomSource> customSources = new Hashtable<String, CustomSource>();
	
	/**
	 * Database Primary Key.
	 */
	private Long userProfilePK;

	
	/*
	 ************************** Initialization ************************************/

	/**
	 * Constructor.
	 * @param userId
	 */
	public UserProfile(final String userId) {
	   if (LOG.isDebugEnabled()) {
    		LOG.debug("UserProfile(" + userId + ")");
            }
            this.setUserId(userId);
	}

	/**
	 * Default Constructor.
	 */
	public UserProfile() {
            if (LOG.isDebugEnabled()) {
    		LOG.debug("UserProfile(" + userId + ")");
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
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public CustomContext getCustomContext(final String contextId)  {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - getCustomContext(" + contextId + ")");
    	}
		CustomContext customContext = customContexts.get(contextId);
		if (customContext == null) {
			if (!DomainTools.getChannel().isThereContext(contextId)) {
				String errorMsg = "Context " + contextId + " is not found in Channel";
				LOG.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}
			customContext = new CustomContext(contextId, this);
			addCustomContext(customContext);
		}		
		return customContext;
	}
	
	/**
	 * Return the customCategory identified by the category id
	 * if exist,else,create it.
	 * @param categoryId identifier of the category refered by the customCategory
	 * @return customCategory (or null)
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CustomCategoryNotFoundException 
	 */
	public CustomCategory getCustomCategory(final String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException, CustomCategoryNotFoundException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - getCustomCategory(" + categoryId + ")");
    	}
		// TODO (GB later) revoir avec customManagedCategory et customPersonalCategory
		CustomCategory customCategory = customCategories.get(categoryId);
		if (customCategory == null) {
			try {
				updateCustomContextsForOneManagedCategory(categoryId);
			} catch (ManagedCategoryNotLoadedException e) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				removeCustomManagedCategoryIfOrphan(categoryId);
				String errorMsg = "ManagedCategory " + categoryId 
					+ "is not refered by any customContext in userProfile " 
					+ getUserId() + ".";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e);
			}
			customCategory = customCategories.get(categoryId);
			if (customCategory == null) {
				String errorMsg = "CustomCategory associated to category " + categoryId
					+ " is not found in user profile " + userId
					+ "\nwhereas an updateCustomContextForOneManagedCategory " 
					+ "has done and category seems visible to user profile " + userId
					+ ".\nPerhaps this categoryProfile is not defined in current context.";
				LOG.error(errorMsg);
				throw new CustomCategoryNotFoundException(errorMsg);
			}
		}
		return customCategory;
	}
	
	/**
	 * Update every customContext of this userProfile for (only one)categoryProfile identified by categoryProfileId.
	 * @param categoryProfileId
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 */
	protected void updateCustomContextsForOneManagedCategory(final String categoryProfileId) 
	throws ManagedCategoryNotLoadedException, InternalDomainException, CategoryNotVisibleException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - updateCustomContextsForOneManagedCategory(" + categoryProfileId + ",ex)");
    	}
		
	   	boolean categoryIsVisible = true;
		try {
			ManagedCategoryProfile mcp = 
				DomainTools.getChannel().getManagedCategoryProfile(categoryProfileId);
			Set<Context> contexts = mcp.getContextsSet();
			
			// For all contexts refered by the managedCategoryProfile
			for (Context context : contexts) {
				String contextId = context.getId();
				// Update on customContexts existing in userProfile
				if (containsCustomContext(contextId)) {
					CustomContext customContext;
					customContext = getCustomContext(contextId);
					VisibilityMode mode = mcp.updateCustomContext(customContext);
					if (mode == VisibilityMode.NOVISIBLE) {
						categoryIsVisible = false;
					} 
				}
			}		
		} catch (ManagedCategoryProfileNotFoundException e) {
			String errorMsg = "Unable to updateCustomContextsForOneManagedCategory(categoryId="
				+ categoryProfileId + ") because of a ManagedCategoryProfileNotFoundException";
			LOG.error(errorMsg);
			//cleanCustomCategoryFromProfile(categoryProfileId);
			removeCustomCategoryFromProfile(categoryProfileId);
			throw new InternalDomainException(errorMsg, e);
		}
		if (!categoryIsVisible) {
			String errorMsg = "Category " + categoryProfileId 
				+ " is not visible for user profile " + userId;
			LOG.error(errorMsg);
			throw new CategoryNotVisibleException(errorMsg);
		}
		
	}
	
	/**
	 * Return the customSource identified by the source Id.
	 * @param sourceId identifier of the source refered by the customSource
	 * @return customSource
	 * @throws CustomSourceNotFoundException 
	 */
	public CustomSource getCustomSource(final String sourceId) throws CustomSourceNotFoundException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - getCustomSource(" + sourceId + ")");
    	}
	   	// TODO (GB later) revoir avec customManagedSource et customPersonalSource
		CustomSource customSource = customSources.get(sourceId);
		if (customSource == null) {
			String errorMsg = "CustomSource " + sourceId + " is not found in userProfile " + this.userId;
			LOG.error(errorMsg);
			throw new CustomSourceNotFoundException(errorMsg);
		}		
		return customSource;
	}

	/* ADD CUSTOM ELEMENTS */
	
	/**
	 * Add a customContext to this userProfile.
	 * @param customContext
	 */
	protected void addCustomContext(final CustomContext customContext) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - addCustomContext(" + customContext.getElementId() + ")");
    	}
		customContexts.put(customContext.getElementId(), customContext);
		customContext.setUserProfile(this);
	}
	
	/**
	 * Add a customCategory to this userProfile.
	 * @param customCategory customCategory to add
	 */
	protected void addCustomCategory(final CustomCategory customCategory) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - addCustomCategory(" + customCategory.getElementId() + ")");
    	}
		String id = customCategory.getElementId();
		customCategories.put(id, customCategory);			
		customCategory.setUserProfile(this);
	}
	
	/**
	 * Add a customSource to this userProfile.
	 * @param customSource customSource to add
	 */
	protected void addCustomSource(final CustomSource customSource) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - addCustomSource(" + customSource.getElementId() + ")");
    	}
	   	String id = customSource.getElementId();
		customSources.put(id, customSource);			
		customSource.setUserProfile(this);
	}
	
	/* REMOVE/CLEAN CUSTOM ELEMENTS FROM PROFILE */
	
	/**
	 * Cleans the userProfile content for customCategory categoryId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param categoryId customCategory ID
	 */
	public void cleanCustomCategory(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("cleanCustomCategory(" + categoryId + ")");
		}
		if (true) { 
			// TODO (GB later) remplacer true par la valeur de l'option 
			// autoDelCustom (sinon : marquer l'objet)
			removeCustomCategory(categoryId);
			LOG.info("customCatgeory " + categoryId + " has been removed from userProfile " 
				+ this.getUserId());
		} 
	}
	
	/**
	 * Cleans the userProfile content for customSource sourceId. It means, if option
	 * "autoDelCustom" in channel config is ( no option for now : it is yes all the time)
	 * - yes => it removes it
	 * - no => it not removes it, the admin will have to remove it manually with command tools
	 * @param sourceId customSource ID
	 */
	public void cleanCustomSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("cleanCustomSource(" + sourceId + ")");
		}
		// TODO (GB later) : ici ou dans removeCustomSource le 
		if (true) { 
			// TODO (GB later) remplacer true par la valeur de l'option 
			// autoDelCustom (sinon : marquer l'objet)
			removeCustomSource(sourceId);
			LOG.info("CustomSource " + sourceId + " has been removed from userProfile " + this.getUserId());
		} 
	}
	
	/**
	 * Remove the customCategory categoryId in all the user profile (this object and in customContexts).
	 * @param categoryId customCategory ID
	 */
	public void removeCustomCategoryFromProfile(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomCategoryFromProfile(" + categoryId + ")");
		}
		
		for (CustomContext custom : customContexts.values()) {
			if (custom.containsCustomCategory(categoryId)) {
				// For all parent customContexts
				custom.removeCustomCategory(categoryId);
			}
		}
		cleanCustomCategory(categoryId);
	}
	
	/**
	 * Remove the customManagedCategory categoryId in all the profile (this object and in customContexts).
	 * @param categoryId customManagedCategory ID
	 */
	public void removeCustomManagedCategoryFromProfile(final String categoryId) {
		//TODO (GB later) A qoui sert il celui ci ?
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomManagedCategoryFromProfile(" + categoryId + ")");
		}
		
		for (CustomContext custom : customContexts.values()) {
			if (custom.containsCustomManagedCategory(categoryId)) {
				// For all parent customContexts
				custom.removeCustomManagedCategory(categoryId);
			}
		}
		cleanCustomCategory(categoryId); 
	}
	
	/**
	 * Remove the customManagedCategory categoryId only if it is not refered in any customContext.
	 * @param categoryId customManagedCategory ID
	 */
	public void removeCustomManagedCategoryIfOrphan(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomManagedCategoryIfOrphan(" + categoryId + ")");
		}
		boolean isOrphan = true;
		for (CustomContext custom : customContexts.values()) {
			if (custom.containsCustomManagedCategory(categoryId)) {
				isOrphan = false;
				break;
			}
		}
		if (isOrphan) {
			cleanCustomCategory(categoryId);
		}
	}	
	
	/**
	 * Remove the customSource sourceId in all the profile (this object and in customCategories).
	 * @param sourceId customSource ID
	 */
	public void removeCustomSourceFromProfile(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomSourceFromProfile(" + sourceId + ")");
		}
		
		for (CustomCategory custom : customCategories.values()) {
			if (custom.containsCustomSource(sourceId)) {
				custom.removeCustomSource(sourceId);
			}
		}
		cleanCustomSource(sourceId);
	}
	
	/**
	 * Remove the customManagedSource sourceId in all the profile (this object and in customCategories).
	 * @param sourceId customManagedSource ID
	 */
	public void removeCustomManagedSourceFromProfile(final String sourceId) {
		//TODO (GB later) A qoui sert il celui ci ?
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomManagedSourceFromProfile(" + sourceId + ")");
		}
		
		for (CustomCategory custom : customCategories.values()) {
			if (custom.containsCustomManagedSource(sourceId)) {
			// For all parent customCategories
				custom.removeCustomManagedSource(sourceId);
			}
		}
		cleanCustomSource(sourceId); 
		// TODO (GB later) pourquoi pas un removeCustomManagedSource ?
	}
	
	
	/**
	 * Remove the customManagedSource sourceId only if it is not refered in any customCategory.
	 * @param sourceId customManagedSource ID
	 */
	public void removeCustomManagedSourceIfOrphan(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("removeCustomManagedSourceIfOrphan(" + sourceId + ")");
		}
		boolean isOrphan = true;
		for (CustomCategory custom : customCategories.values()) {
			if (custom.containsCustomManagedSource(sourceId)) {
				isOrphan = false;
				break;
			}
		}
		if (isOrphan) {
			cleanCustomSource(sourceId);
		}
	}
	
	/* REMOVE CUSTOM ELEMENTS : ATOMIC METHODS */
	
	/**
	 * Remove a customContext from this userProfile only.
	 * @param contextId id of the customContext to add
	 */
	protected void removeCustomContext(final String contextId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - removeCustomContext(" + contextId + ")");
    	}
	   	CustomContext custom = customContexts.get(contextId);
	   	if (custom != null) {
	   		custom.removeSubscriptions();
	   		customContexts.remove(contextId);
	   	}
	}
	

	/**
	 * Remove a customCategory from this userProfile only.
	 * @param categoryId
	 */
	protected void removeCustomCategory(final String categoryId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - removeCustomCategory(" + categoryId + ")");
    	}
	   	
	   	CustomCategory custom = customCategories.get(categoryId);
	   	if (custom != null) {
	   		custom.removeSubscriptions();
	   		customCategories.remove(categoryId);
	   	}
	}
	
	/**
	 * Remove a customCategory from this userProfile only.
	 * @param sourceId
	 */
	protected void removeCustomSource(final String sourceId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - removeCustomSource(" + sourceId + ")");
    	}
	   	//TODO (RB --> GB): I add this line to avoid a ClassCastException 
	   	// due to a hibernate bug when map is not yet loaded!
	   	//TODO (RB <-- GB): je ne comprends pas ce que tu veux faire
	   	//boolean foo = customSources.containsKey(sourceId);
		CustomSource cs = customSources.remove(sourceId);
	}
	
	/* MISCELLANEOUS */
	
	/**
	 * @param contextId
	 * @return true if this userProfile contains the customContext identified by contextId
	 */
	public boolean containsCustomContext(final String contextId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug(ID + userId + " - containsCustomContext(" + contextId + ")");
    	}
		return customContexts.containsKey(contextId);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer(getClass().getSimpleName() + "#" + hashCode() 
			+ "\n [userId=[" + userId + "], userProfilePK=[" + userProfilePK + "], ");
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
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * @return customContexts
	 */
	@SuppressWarnings("unused")
	private Map<String, CustomContext> getCustomContexts() {
		return customContexts;
		//Needed by Hibernate
	}

	
	/**
	 * @param customContexts
	 */
	@SuppressWarnings("unused")
	private void setCustomContexts(final Map<String, CustomContext> customContexts) {
		this.customContexts = customContexts;
		//Needed by Hibernate
	}

	/**
	 * @return database primary Key
	 */
	public Long getUserProfilePK() {
		return userProfilePK;
	}

	/**
	 * @param userProfilePK - database Primary Key
	 */
	public void setUserProfilePK(final Long userProfilePK) {
		this.userProfilePK = userProfilePK;
	}

	/**
	 * @return the customCategories
	 */
	public Map<String, CustomCategory> getCustomCategories() {
		return customCategories;
	}

	/**
	 * @param customCategories the customCategories to set
	 */
	@SuppressWarnings("unused")
	private void setCustomCategories(final Map<String, CustomCategory> customCategories) {
		this.customCategories = customCategories;
		//Needed by Hibernate
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
	@SuppressWarnings("unused")
	private void setCustomSources(final Map<String, CustomSource> customSources) {
		this.customSources = customSources;
		//Needed by Hibernate
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return userId.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserProfile other = (UserProfile) obj;
		if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}
	
}
