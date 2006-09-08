package org.esupportail.lecture.dao;
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
		
}
