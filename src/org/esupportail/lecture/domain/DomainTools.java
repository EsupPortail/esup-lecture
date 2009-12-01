/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.info.AuthInfo;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
/**
 * Provide various tools :
 * - constants defintion for user attributes provided by externalService. 
 * - single access to DaoService, externalService, channel for domain layer
 * @author gbouteil
 */
public class DomainTools implements InitializingBean {
	
	/*
	 ************************** PROPERTIES *********************************/	


	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(DomainTools.class);

	/**
	 * The default name for the cache.
	 */
	private static final String DEFAULT_CACHE_NAME = DomainTools.class.getName();
	/**
	 * the name of the cache.
	 */
	
	private String cacheName;
	/**
	 * the cacheManager.
	 */
	
	private CacheManager cacheManager;
	/**
	 * the cache.
	 */
	
	private static Cache cache;
	/* 
	 * Single Access to layers or principal classes */
	
	/**
	 * Current DaoService initialised during portlet init.
	 */
	private static DaoService daoService;

	/**
	 * current External Service during portlet init.
	 */
	private static ExternalService externalService;

	/**
	 * Current Channel initialised during portlet init.
	 */
	private static Channel channel;

	/*
	 * Constants definition */
	
	/**
	 * Attribute name used to identified the guset user.
	 * It is defined in the channel config
	 */
	private static String guestUser;
	/**
	 * Name of the portlet preference that set a context to an instance of the channel.
	 */
	private static String context;
	/**
	 * ttl for dummy elements.
	 */
	private static int dummyTtl; 
	/**
	 * default TTL.
	 */
	private static int defaultTtl;
	/**
	 * default TIME OUT.
	 */
	private static int defaultTimeOut;
	/**
	 * Max tree size in context.
	 */
	private static int maxTreeSize;
	/**
	 * Default tree size in context.
	 */
	private static int defaultTreeSize;
	/**
	 * default TTL of config file
	 */
	private static int configTtl;
	

	
	
	/*
	 ************************** INIT ******************************** */	

	/**
	 * Dfault constructor.
	 */

	private DomainTools() {
		super();
	}
	

	
	
	/*
	 *************************** METHODS ******************************** */	

	/**
	 * replace user attibute in a String. Example a user with name "Toto" and age "25" 
	 * if param="{name} is  {age} years old" then return value is "Toto is 25 years old" 
	 * @param value - string where we try to find user attribute to replace
	 * @return value with {attribute} replaced by this attribute according to current connected user
	 */
	public static String replaceWithUserAttributes(final String value) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("replaceWithUserAttributes(" + value + ")");
		}
		String ret = null;
		if (value != null) {
			// find and replace {...}
			int firstIndex = value.indexOf("{");
			int lastIndex = value.indexOf("}");
			//is { and } in value
			if (firstIndex != -1 && lastIndex != -1) {
				String begin = value.substring(0, firstIndex);
				//med = attribute to replace
				String med = value.substring(firstIndex + 1, lastIndex);
				String end = value.substring(lastIndex + 1, value.length());
				//replacing med value
				String realMedValue;
				try {
					realMedValue = externalService.getUserAttribute(med);
				} catch (NoExternalValueException e) {
					LOG.warn("replaceWithUserAttributes(" + value + ") generate " + e.getMessage());
				} catch (InternalExternalException e) {
					LOG.warn("replaceWithUserAttributes(" + value + ") generate " + e.getMessage());
				} finally {
					realMedValue = med;
				}
				//generate return value
				ret = begin + realMedValue + end;
				if (LOG.isDebugEnabled()) {
					LOG.debug("replaceWithUserAttributes" + " :: replace " + value + " by " + ret);
				}
				//is there some other { } ?
				int firstIndex2 = ret.indexOf("{");
				int lastIndex2 = ret.indexOf("}");
				//is { and } in value
				if (firstIndex2 != -1 && lastIndex2 != -1) {
					ret = replaceWithUserAttributes(ret);
				}
			} else {
				ret = value;
			}
		}
		return ret;
	}
	
	/*
	 ************************** ACCESSORS *********************************/	
	/**
	 * Return an instance of current DaoService initialised by Spring.
	 * @return current DomainService
	 */
	public static DaoService getDaoService() {
		return daoService;
	}

	/**
	 * set current DaoService (used by Spring).
	 * @param daoService 
	 */
	public static void setDaoService(final DaoService daoService) {
		DomainTools.daoService = daoService;
	}

	/**
	 * Returns current channel instance (main class).
	 * @return current channel
	 */
	public static Channel getChannel() {
		return channel;
	}

	/**
	 * set current channel instance (used by Spring).
	 * @param channel
	 */
	public static void setChannel(final Channel channel) {
		DomainTools.channel = channel;
	}
	

	/**
	 * @return external service
	 */
	public static ExternalService getExternalService() {
		return externalService;
	}
	
	/**
	 * @param externalService
	 */
	public static void setExternalService(final ExternalService externalService) {
		DomainTools.externalService = externalService;
	}

	/**
	 * @return the guestUser
	 */
	public static String getGuestUser() {
		return guestUser;
	}

	/**
	 * @param guestUser the guestUser to set
	 */
	public static void setGuestUser(final String guestUser) {
		DomainTools.guestUser = guestUser;
	}


	/**
	 * @return dummyTtl
	 */
	public static int getDummyTtl() {
		return dummyTtl;
	}


	/**
	 * @param dummyTtl
	 */
	public static void setDummyTtl(final int dummyTtl) {
		DomainTools.dummyTtl = dummyTtl;
	}


	/**
	 * @return context
	 */
	public static String getContext() {
		return context;
	}


	/**
	 * @param context
	 */
	public static void setContext(final String context) {
		DomainTools.context = context;
	}


	/**
	 * @return defaultTtl
	 */
	public static int getDefaultTtl() {
		return defaultTtl;
	}


	/**
	 * @param defaultTtl
	 */
	public static void setDefaultTtl(int defaultTtl) {
		DomainTools.defaultTtl = defaultTtl;
	}


	/**
	 * @return defaultTimeOut
	 */
	public static int getDefaultTimeOut() {
		return defaultTimeOut;
	}


	/**
	 * @param defaultTimeOut
	 */
	public static void setDefaultTimeOut(int defaultTimeOut) {
		DomainTools.defaultTimeOut = defaultTimeOut;
	}


	/**
	 * @return maxTreeSize
	 */
	public static int getMaxTreeSize() {
		return maxTreeSize;
	}


	/**
	 * @param maxTreeSize
	 */
	public static void setMaxTreeSize(int maxTreeSize) {
		DomainTools.maxTreeSize = maxTreeSize;
	}


	/**
	 * @return defaultTreeSize
	 */
	public static int getDefaultTreeSize() {
		return defaultTreeSize;
	}


	/**
	 * @param defaultTreeSize
	 */
	public static void setDefaultTreeSize(final int defaultTreeSize) {
		DomainTools.defaultTreeSize = defaultTreeSize;
	}

	/**
	 * @param xsltFileURL
	 * @return cached xsltFile as a streamSource
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws TransformerConfigurationException 
	 */
	public static String getXsltFile(final String xsltFileURL)
			throws IOException, TransformerConfigurationException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("getXsltFile(" + xsltFileURL + ")");
		}
		String inputXslt;
		StreamSource streamSource;
		String cacheKey = "XSLT:" + xsltFileURL;
		Element element = cache.get(cacheKey);
		if (element == null) { 
			URL url2 = new URL(xsltFileURL);
//			streamSource = new StreamSource(url2.openStream());
			InputStream is =  url2.openStream();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(is));

			//inputXslt = in.toString();
		    String line;
		    inputXslt = "";
		    while ((line = in.readLine()) != null)
		    {
		    	inputXslt += line;
		    }
		    in.close();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Put xslt in cache : " + cacheKey);
				LOG.debug("inputXslt : " + inputXslt);
			}
			Element cacheElement = new Element(cacheKey, inputXslt);
			cache.put(cacheElement);

		}
		else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("xslt already in cache : " + cacheKey);
			}
			inputXslt = (String) element.getObjectValue();
			LOG.debug("inputXslt : " + inputXslt);
		}
		return inputXslt;
	}


	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		if (!StringUtils.hasText(cacheName)) {
			setDefaultCacheName();
			LOG.warn(getClass() + ": no cacheName attribute set, '" 
					+ cacheName + "' will be used");
		}
		Assert.notNull(cacheManager, 
				"property cacheManager of class " + getClass().getName() 
				+ " can not be null");
		if (!cacheManager.cacheExists(cacheName)) {
			cacheManager.addCache(cacheName);
		}
		LOG.debug("DaoServiceRemoteXML cache : " + cacheName);
		cache = cacheManager.getCache(cacheName);
	}
	
	/**
	 * set the default cacheName.
	 */
	protected void setDefaultCacheName() {
		this.cacheName = DEFAULT_CACHE_NAME;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}




	/**
	 * @param authenticationService
	 * @return Current User Id
	 */
	public static String getCurrentUserId(
			AuthenticationService authenticationService) {
		String ret;
		AuthInfo authInfo = authenticationService.getAuthInfo();
		if (authInfo == null) {
			ret = getGuestUser();
		}
		else {
			ret = authenticationService.getAuthInfo().getId();
		}
		return ret;
	}



	/**
	 * @return defaultTtl
	 */
	public static int getConfigTtl() {
		return configTtl;
	}


	/**
	 * @param defaultTtl
	 */
	public static void setConfigTtl(int configTtl) {
		DomainTools.configTtl = configTtl;
	}




}
