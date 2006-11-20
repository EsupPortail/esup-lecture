package org.esupportail.lecture.dao;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.UserProfile;



/**
 * Interface Service to Data Access Object
 * @author gbouteil
 *
 */
public interface DaoService {
	
	/**
	 * Returns the userProfile that is identified with "userId" 
	 * and null if no user profile exists with this userId 
	 * @param userId : user identifient provided by portlet request
	 * @return user profile 
	 */
	public UserProfile getUserProfile(String userId) ;

	/**
	 * Add a user profile to persistent data.
	 * @param userProfile : user to add
	 */
	public void addUserProfile(UserProfile userProfile);

	/**
	 * Add a customContext to persistent data.
	 * @param customContext : customContext to add
	 */
	public void addCustomContext(CustomContext customContext);

	/**
	 * Get a managed category from a remote place
	 * @param profile of the category to get
	 * @return the managedCategory
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile);

	/**
	 * Delete userProfile that is identified with "userId" 
	 * @param userProfile : userProfile to delete
	 */
	public void deleteUserProfile(UserProfile userProfile);

	public Category getManagedCategory(ManagedCategoryProfile profile, String ptCas);

	/**
	 * get a source from a remote place
	 * @param profile of the source to get
	 * @param ptCas proxy ticket CAS used in case of CAS protected source
	 * @return the source
	 */
	public Source getSource(ManagedSourceProfile profile, String ptCas);

	/**
	 * get a source from a remote place
	 * @param profile of the source to get
	 * @return the source
	 */
	public Source getSource(ManagedSourceProfile profile);

	public void updateCustomContext(CustomContext customContext);

	public void updateUserProfile(UserProfile userProfile);

	
	
		
}
