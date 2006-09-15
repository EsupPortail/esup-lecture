package org.esupportail.lecture.dao;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.utils.exception.ErrorException;



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
	 * Get a managed category from a remote place
	 * @param urlCategory url of the remote category
	 * @param ttl ttl of the category
	 * @param profileId identifier of the managed category profile referer
	 * @return the managedCategory
	 */
	public ManagedCategory getCategory(String urlCategory,int ttl,String profileId) throws ErrorException;



		
}
