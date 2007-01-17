package org.esupportail.lecture.dao;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
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
	
	/* Remote data */
	
	/**
	 * Get a managed category from a remote place
	 * @param profile of the category to get
	 * @return the managedCategory
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile);

	/**
	 * Get a managed category from a remote place
	 * @param profile of the category to get
	 * @param ptCas proxy ticket CAS used in case of CAS protected source
	 * @return the managedCategory
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile,String ptCas);

	/**
	 * get a source from a remote place
	 * @param profile of the source to get
	 * @return the source
	 */
	public Source getSource(ManagedSourceProfile profile);
	
	/**
	 * get a source from a remote place
	 * @param profile of the source to get
	 * @param ptCas proxy ticket CAS used in case of CAS protected source
	 * @return the source
	 */
	public Source getSource(ManagedSourceProfile profile, String ptCas);

	/* User Profile */
	
	/**
	 * Returns the userProfile that is identified with "userId" 
	 * and null if no user profile exists with this userId 
	 * @param userId : user identifient provided by portlet request
	 * @return user profile 
	 */
	public UserProfile getUserProfile(String userId) ;

	/**
	 * Return a "fresh" userProfile from data base 
	 * @param userProfile
	 * @return a "fresh" userProfile
	 */
	public UserProfile refreshUserProfile(UserProfile userProfile);

	/**
	 * Add a user profile to persistent data.
	 * @param userProfile : user to add
	 */
	public void saveUserProfile(UserProfile userProfile);

	/**
	 * Delete userProfile that is identified with "userId" 
	 * @param userProfile : userProfile to delete
	 */
	// TODO (GB later) creer un service pour supprimer définitivement un userProfile par l'admin ?
	public void deleteUserProfile(UserProfile userProfile);

	/**
	 * update userProfile
	 * @param userProfile
	 */
	public void updateUserProfile(UserProfile userProfile);
	
	/* CustomContext */

	/**
	 * update custom context
	 * @param customContext
	 */
	public void updateCustomContext(CustomContext customContext);

	/**
	 * delete custom context
	 * @param cco
	 */
	// TODO (GB later) creer un service pour supprimer définitivement un contexte par l'admin ?
	public void deleteCustomContext(CustomContext cco);
	
	/* CustomCategory */
	
	/**
	 * delete custom category
	 * @param cca
	 */
	public void deleteCustomCategory(CustomCategory cca);

	/**
	 * update custom category
	 * @param cca
	 */
	public void updateCustomCategory(CustomCategory cca);

	/* CustomSource */
	
	/**
	 * delete custom source
	 * @param cs
	 */
	public void deleteCustomSource(CustomSource cs);

	/**
	 * update custom source
	 * @param source
	 */
	public void updateCustomSource(CustomSource source);

		
}
