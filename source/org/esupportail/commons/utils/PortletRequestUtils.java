/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 */
package org.esupportail.commons.utils; 

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;


import org.esupportail.commons.logging.LoggerImpl;

/**
 * A class that provides facilities with portlet requests.
 */
public class PortletRequestUtils {
// TODO tout revoir ...

	/**
	 * A logger.
	 */
	private static final LoggerImpl LOG = new LoggerImpl(PortletRequestUtils.class);

	
	private static FacesContext getFacesContext(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			LOG.warn("FacesContext.getCurrentInstance() returns null, " 
					+ "can not get the current HTTP servlet request.");
			return null;
		}
		return facesContext;
	}

	private static ExternalContext getExternalContext(){
		FacesContext facesContext = getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext(); 
		if (externalContext == null) {
			LOG.warn("facesContext.getExternalContext() returns null, " 
					+ "can not get the current HTTP servlet request.");
			return  null;
		}
		return externalContext;
	}
	
	private static PortletRequest getPortletRequest() {

		ExternalContext externalContext = getExternalContext();
		Object requestObject = externalContext.getRequest();
		
		if (requestObject == null) {
			LOG.warn("externalContext.getRequest() returns null, " 
				+ "can not get the current HTTP servlet request.");
			return null;
		}
		if (!(requestObject instanceof PortletRequest)) {
			LOG.warn("requestObject is not a PortletRequest, " 
				+ "can not get the current portlet request.");
			return null;
		}
		
		PortletRequest portletRequest = (PortletRequest) requestObject;
		
		return portletRequest;
	}
	
	
	/** 
	 * Return Portlet preferences by the request
	 * @return the portletPreference
	 */
	private static PortletPreferences getPortletPreferences(){
		
		PortletRequest portletRequest = getPortletRequest();
		return portletRequest.getPreferences();
		
	}
	
	
	
	
	
	
	
	/**
	 * @param request
	 * @return The id of the current user.
	 */
	/**
	 * @param request
	 * @param attributeName name of the attribute
	 * @return a string containing the user attribute value of
	 */
	public static String getUserAttribute(PortletRequest request,String attributeName) {
		
		if (request == null) {
			request = getPortletRequest();
			if (request == null) {
				LOG.warn("no request, can not get the user ID");
				return null;
			}
		}
		
		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String attributeValue = (String)userInfo.get(attributeName);
		//TODO gerer attribute non trouvé ???
		return attributeValue;
	}
	
	/**
	 * @return the current user's id.
	 */
	public static String getUserAttribute(String attributeName) {
		return getUserAttribute(null,attributeName);
	}
	
	/**
	 * Tests portlet container if current user is in role "role" 
	 * @param role
	 * @return true if the user is in the role, esle false
	 */
	public static boolean isUserInRole(String role) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		PortletRequest request = (PortletRequest) externalContext.getRequest();
		if (request.isUserInRole(role)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * Return the preference named by "name"
	 * @param name
	 * @return the preference value
	 */
	public static String getPreferences(String name){
		
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    LOG.error(" external context : "+externalContext.toString() );
        PortletRequest request = (PortletRequest) externalContext.getRequest();
        PortletPreferences portletPreferences = request.getPreferences();
        String value = portletPreferences.getValue(name, name+" not define !");
        return value;
//		String value = getPortletPreferences().getValue(name, "Preference '"+name+"' not define !");
//		return value;
	}
//	/**
//	 * Private constructor.
//	 */
//	private PortletRequestUtils() {
////		throw new UnsupportedOperationException();
//	}
//
//	/**
//	 * @param portletRequest
//	 * @return The HttpServletRequest instance that corresponds to a PortletRequest, or null if not possible.
//	 */
//	public static HttpServletRequest getHttpServletRequest(final PortletRequest portletRequest) {
//		if (!(portletRequest instanceof ServletRequestWrapper)) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("portletRequest ('" + portletRequest.getClass().getName() 
//						+ "') is not a ServletRequestWrapper");
//			}
//			return null;
//		}
//		ServletRequestWrapper requestWrapper = (ServletRequestWrapper) portletRequest;
//		ServletRequest servletRequest = requestWrapper.getRequest();
//		if (servletRequest == null) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("servletRequest is null");
//			}
//			return null;
//		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("retrieved a ServletRequest instance from portletRequest");
//		}
//		if (!(servletRequest instanceof HttpServletRequest)) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("servletRequest ('" + servletRequest.getClass().getName() 
//						+ "') is not a HttpServletRequest");
//			}
//			return null;
//		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("servletRequest ('" + servletRequest.getClass().getName() 
//					+ "') casted to HttpServletRequest");
//		}
//		return (HttpServletRequest) servletRequest;
//	}
//
//	/**
//	 * @param request
//	 * @return The portlet session attributes, as a printable string.
//	 */
//	public static String getPrintablePortletSessionAttributes(final PortletRequest request) {
//		Map<String, Object> sessionAttributes = getSessionAttributes(request);
//		Set<String> keys = sessionAttributes.keySet();
//		if (keys.isEmpty()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		for (String key : keys) {
//			Object obj = sessionAttributes.get(key);
//			if (obj != null) {
//				sortedStrings.add(key + " = [" + obj.toString() + "]");
//			}
//		}
//		StringBuffer printableSessionAttributes = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableSessionAttributes.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableSessionAttributes.toString();
//	}
//	
//	/**
//	 * @param request
//	 * @return The parameters of the request, as a printable string.
//	 */
//	@SuppressWarnings({ "cast", "unchecked" })
//	public static String getPrintablePortletRequestParameters(final PortletRequest request) {
//		Enumeration<String> names = (Enumeration<String>) request.getParameterNames();
//		if (!names.hasMoreElements()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			String [] values = request.getParameterValues(name);
//			for (String value : values) {
//				sortedStrings.add(name + " = [" + value + "]");
//			}
//		}
//		StringBuffer printableRequestParameters = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableRequestParameters.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableRequestParameters.toString();
//	}
//
//	/**
//	 * @param request
//	 * @return The properties of the request, as a printable string.
//	 */
//	@SuppressWarnings("unchecked")
//	public static String getPrintablePortletRequestProperties(final PortletRequest request) {
//		Enumeration<String> names = request.getPropertyNames();
//		if (!names.hasMoreElements()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			Enumeration<String> properties = request.getProperties(name);
//			while (properties.hasMoreElements()) {
//				String property = properties.nextElement();
//				sortedStrings.add(name + ": " + property);
//			}
//		}
//		StringBuffer printableRequestProperties = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableRequestProperties.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableRequestProperties.toString();
//	}
//
//	/**
//	 * @param request
//	 * @return The HTTP session attributes, as a printable string.
//	 */
//	public static String getPrintableHttpSessionAttributes(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return ServletRequestUtils.getPrintableSessionAttributes(httpServletRequest);
//	}
//	
//	/**
//	 * @param request
//	 * @return The cookies, as a printable string.
//	 */
//	public static String getPrintableCookies(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return ServletRequestUtils.getPrintableCookies(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The headers of the request, as a printable string.
//	 */
//	public static String getPrintableHttpRequestHeaders(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return ServletRequestUtils.getPrintableRequestHeaders(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The HTTP session attributes, as a printable string.
//	 */
//	public static String getPrintableHttpRequestParameters(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return ServletRequestUtils.getPrintableRequestParameters(httpServletRequest);
//	}
//	
//	/**
//	 * @param request
//	 * @return The client as a printable string.
//	 */
//	public static String getPrintableClient(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			LOG.debug("client address can not be retrieved from PortletRequest");
//			return null;
//		}
//		return ServletRequestUtils.getPrintableClient(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The server as a raw String.
//	 */
//	private static String getRawServerString(final PortletRequest request) {
//		return request.getServerName();
//	}
//
//	/**
//	 * @param request
//	 * @return The server as a printable string.
//	 */
//	public static String getPrintableServer(final PortletRequest request) {
//		String rawServerString = getRawServerString(request);
//		try {
//			InetAddress server = InetAddress.getByName(rawServerString);
//			return new StringBuffer(server.getHostAddress())
//			.append(" (").append(server.getHostName()).append(")").toString();
//		} catch (UnknownHostException e) {
//			return rawServerString;
//		}
//	}
//
//	/**
//	 * @param request
//	 * @return The user agent.
//	 */
//	public static String getUserAgent(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			// no servlet request available, try to get it from the properties of the request
//			return request.getProperty("user-agent");
//		}
//		return ServletRequestUtils.getUserAgent(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The query string.
//	 */
//	public static String getQueryString(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return ServletRequestUtils.getQueryString(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The user's action.
//	 */
//	public static String getUserAction(final PortletRequest request) {
//		return request.getParameter("action");
//	}
//
//	/**
//	 * @param request
//	 * @return All the attributes of the current session.
//	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object> getSessionAttributes(final PortletRequest request) {
//		PortletSession session = request.getPortletSession();
//		Map<String, Object> attributes = new Hashtable<String, Object>();
//		if (session != null) {
//			Enumeration<String> names = session.getAttributeNames();
//			while (names.hasMoreElements()) {
//				String name = names.nextElement();
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("name -> " + name);
//				}
//				Object obj = session.getAttribute(name);
//				if (obj != null) {
//					attributes.put(name, obj);
//				}
//			}
//		}
//		return attributes;
//	}
//

	
	
//	/**
//	 * @param request
//	 * @param name
//	 * @return The attribute that corresponds to a name.
//	 */
//	private static Object getSessionAttribute(
//			final PortletRequest request, 
//			final String name) {
//		
//		PortletRequest theRequest = request;
//		if (theRequest == null) {
//			theRequest = getPortletRequest();
//			if (theRequest == null) {
//				LOG.warn("no request, can not get session attribute '" + name + "'");
//				return null;
//			}
//		}
//		
//		PortletSession session = request.getPortletSession();
//		Object obj = null;
//		
//		
//		if (session != null) {
//			obj = session.getAttribute(name);
//		}
//		return obj;
//	}

	
		
	
	
	
	
	
	
	
	
	
	
	
	

//
//	/**
//	 * @param request
//	 * @return The state of the application.
//	 */
//	public static String getApplicationState(final PortletRequest request) {
//		return (String) getSessionAttribute(request, APPLICATION_STATE);
//	}
//	
}
