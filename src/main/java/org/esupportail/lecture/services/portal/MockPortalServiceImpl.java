/**
 * ESUP-Portail Commons - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-commons
 */
package org.esupportail.lecture.services.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalGroupHierarchy;
import org.esupportail.portal.ws.client.PortalUser;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.esupportail.portal.ws.client.exceptions.PortalGroupNotFoundException;
import org.esupportail.portal.ws.client.exceptions.PortalUserNotFoundException;
import org.esupportail.portal.ws.client.support.AbstractPortalService;

/** 
 * A void implementation of PortalService (for application that do not 
 * use portal services, for instance portlet installations).
 */
public class MockPortalServiceImpl extends AbstractPortalService implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7399929419364841341L;
	/**
	 * 
	 */
	private static final PortalGroup portalGroup = new PortalGroup("admin", "admin");
	/**
	 * 
	 */
	private List<String> values = new ArrayList<String>();
	/**
	 * 
	 */
	private static final Map<String, List<String>> attributes = new HashMap<String, List<String>>();
	/**
	 * 
	 */
	private PortalUser portalUser;
	/**
	 * 
	 */
	private static final PortalGroupHierarchy portalGroupHierarchy = new PortalGroupHierarchy(portalGroup, null);

	/**
	 * Bean constructor.
	 */
	public MockPortalServiceImpl() {
		super();
		values.add("val1");
		attributes.put("att1", values);
		portalUser = new PortalUser("admin", attributes);
	}

	//////////////////////////////////////////////////////////
	// user methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getUser(java.lang.String)
	 */
	public PortalUser getUser(
			final String userId) {
		return portalUser;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#searchUsers(java.lang.String)
	 */
	public List<PortalUser> searchUsers(
			final String token) 
	throws PortalErrorException, PortalUserNotFoundException {
		List<PortalUser> ret = new ArrayList<PortalUser>();
		ret.add(portalUser);
		return ret;
	}

	//////////////////////////////////////////////////////////
	// group methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupById(java.lang.String)
	 */
	public PortalGroup getGroupById(
			final String groupId) {
		return portalGroup;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupByName(java.lang.String)
	 */
	public PortalGroup getGroupByName(
			final String groupName) {
		return portalGroup;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#searchGroupsByName(java.lang.String)
	 */
	public List<PortalGroup> searchGroupsByName(
			final String token) {
		List<PortalGroup> ret = new ArrayList<PortalGroup>();
		ret.add(portalGroup);
		return ret;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getSubGroupsById(java.lang.String)
	 */
	public List<PortalGroup> getSubGroupsById(
			final String arg0) 
			throws PortalErrorException, PortalGroupNotFoundException {
		List<PortalGroup> ret = new ArrayList<PortalGroup>();
		ret.add(portalGroup);
		return ret;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getSubGroupsByName(java.lang.String)
	 */
	public List<PortalGroup> getSubGroupsByName(
			final String arg0) 
			throws PortalErrorException, PortalGroupNotFoundException {
		List<PortalGroup> ret = new ArrayList<PortalGroup>();
		ret.add(portalGroup);
		return ret;
	}

	//////////////////////////////////////////////////////////
	// group hierarchy methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchyById(java.lang.String)
	 */
	public PortalGroupHierarchy getGroupHierarchyById(
			final String arg0) 
	throws PortalErrorException, PortalGroupNotFoundException {
		return portalGroupHierarchy;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchyByName(java.lang.String)
	 */
	public PortalGroupHierarchy getGroupHierarchyByName(
			final String arg0) 
	throws PortalErrorException, PortalGroupNotFoundException {
		return portalGroupHierarchy;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getRootGroup()
	 */
	public PortalGroup getRootGroup() {
		return portalGroup;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchy()
	 */
	public PortalGroupHierarchy getGroupHierarchy() {
		return portalGroupHierarchy;
	}

	//////////////////////////////////////////////////////////
	// group membership methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getUserGroups(java.lang.String)
	 */
	public List<PortalGroup> getUserGroups(
			final String userId) {
		List<PortalGroup> ret = new ArrayList<PortalGroup>();
		ret.add(portalGroup);
		return ret;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupUsers(java.lang.String)
	 */
	public List<PortalUser> getGroupUsers(
			final String groupId) {
		List<PortalUser> ret = new ArrayList<PortalUser>();
		ret.add(portalUser);
		return ret;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#isUserMemberOfGroup(
	 * java.lang.String, java.lang.String)
	 */
	public boolean isUserMemberOfGroup(
			final String userId, 
			final String groupId) {
		return true;
	}

	
	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getContainingGroupsById(java.lang.String)
	 */
	public List<PortalGroup> getContainingGroupsById(String arg0)
			throws PortalErrorException, PortalGroupNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getContainingGroupsByName(java.lang.String)
	 */
	public List<PortalGroup> getContainingGroupsByName(String arg0)
			throws PortalErrorException, PortalGroupNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
