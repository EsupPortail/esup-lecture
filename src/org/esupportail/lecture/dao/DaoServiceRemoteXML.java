package org.esupportail.lecture.dao;

import java.util.Hashtable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * @author bourges
 * this class is used to get remote XML contents (category, source) and manage cache on this contents
 */
public class DaoServiceRemoteXML implements InitializingBean {
	
	/**
	 * number of milliseconds per second.
	 */
	private static final int MILLIS_PER_SECOND = 1000;
	/**
	 * Default timeout value.
	 */
	private static final int DEFAULT_TIMEOUT_VALUE = 3000;
	/**
	 * Log instance.
	 */
	private static final Log log = LogFactory.getLog(DaoServiceRemoteXML.class);
	/**
	 * The default name for the cache.
	 */
	private static final String DEFAULT_CACHE_NAME = DaoServiceRemoteXML.class.getName();
	/**
	 * hash of last last Access in milliseconds by url of managedCategory.
	 */
	private Hashtable<String, Long> managedCategoryLastAccess = new Hashtable<String, Long>();
	/**
	 * hash of last last Access in milliseconds by url of Source.
	 */
	private Hashtable<String, Long> sourceLastAccess = new Hashtable<String, Long>();
	/**
	 * Default timeout used for http connection (normally, it should be not used).
	 */
	private int defaultTimeout = DEFAULT_TIMEOUT_VALUE;
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
	private Cache cache;

	/**
	 * @param profile 
	 * @return a managedCategory
	 * @throws TimeoutException 
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(final ManagedCategoryProfile profile) throws TimeoutException {

		/* *************************************
		 * Cache logic :
		 * hash of (url, lastDate)
		 * search url in cache
		 * if find
		 * 	if lastDate + ttl > date
		 *   if steel in cache
		 *    get from cache
		 *   else
		 *    warning (cache to small)
		 *   fi
		 *  else
		 *   get url
		 *   send in cache
		 *   update lastdate
		 *  fi
		 * else
		 *  get url
		 *  send in cache
		 *  update lastdate
		 * fi 
		 * *************************************
		 */		
		if (log.isDebugEnabled()) {
			log.debug("in getManagedCategory");
		}
		ManagedCategory ret = new ManagedCategory();
		String url = profile.getUrlCategory();
		String cacheKey = "CAT:" + profile.getId() + url;
		System.currentTimeMillis();
		Long lastcatAccess = managedCategoryLastAccess.get(cacheKey);
		Long currentTimeMillis = System.currentTimeMillis();
		if (lastcatAccess != null) {
			if (lastcatAccess + (profile.getTtl() * MILLIS_PER_SECOND) > currentTimeMillis) {
				ret = (ManagedCategory) cache.get(cacheKey).getObjectValue();
				if (ret == null) { 
					// not in cache !
					ret = getFreshManagedCategory(profile);
					cache.put(new Element(cacheKey, ret));
					managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("ManagedCategory from url " + url 
							+ " can't be found in cahe --> change cache size ?");
					}
				}				
			} else {
				ret = getFreshManagedCategory(profile);
				cache.put(new Element(cacheKey, ret));
				managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
			}
		} else {
			ret = getFreshManagedCategory(profile);
			cache.put(new Element(cacheKey, ret));
			managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
		}
		return ret;
	}
	
	/**
	 * get a managed category from the web without cache.
	 * @param profile ManagedCategoryProfile of Managed category to get
	 * @return Managed category
	 * @throws TimeoutException 
	 */
	@SuppressWarnings("unchecked")
	private ManagedCategory getFreshManagedCategory(final ManagedCategoryProfile profile) throws TimeoutException {
		if (log.isDebugEnabled()) {
			log.debug("in getFreshManagedCategory");
		}
		ManagedCategory ret = new ManagedCategory();
		//start a Thread to get FreshManagedCategory
		FreshManagedCategoryThread thread = new FreshManagedCategoryThread(profile);
		thread.start();
		int timeOut = profile.getTimeOut();
		try {
			thread.join(timeOut);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Error during execution of Thread getting Category";
				log.error(msg);
				throw new RuntimeException(msg, e);
			}
			ret = thread.getManagedCategory();
		} catch (InterruptedException e) {
			String msg = "Thread getting Category interrupted";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in " + timeOut + " milliseconds";
			log.warn(msg);
			throw new TimeoutException(msg);
        }	
		return ret;
	}

	/**
	 * get a source form cache.
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 * @throws TimeoutException 
	 */
	/**
	 * @param sourceProfile
	 * @return
	 * @throws TimeoutException
	 */
	public Source getSource(final SourceProfile sourceProfile) throws TimeoutException {
		Source ret = new GlobalSource();
//		not yet implemented
//		if (sourceProfile.isSpecificUserContent()) { 
//		ret = getFreshSource(sourceProfile);
//		}
//		else {
		System.currentTimeMillis();
		String urlSource = sourceProfile.getSourceURL();
		Long lastSrcAccess = sourceLastAccess.get(urlSource);
		Long currentTimeMillis = System.currentTimeMillis();
		if (lastSrcAccess != null) {
			if (lastSrcAccess + (sourceProfile.getTtl() * MILLIS_PER_SECOND) > currentTimeMillis) {
				ret = (Source) cache.get(urlSource).getObjectValue();
				if (ret == null) { 
					// not in cache !
					ret = getFreshSource(sourceProfile);
					cache.put(new Element(urlSource, ret));
					sourceLastAccess.put(urlSource, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("Source from url " + urlSource 
							+ " can't be found in cahe --> change cache size ?");
					}
				}				
			} else {
				ret = getFreshSource(sourceProfile);
				cache.put(new Element(urlSource, ret));
				sourceLastAccess.put(urlSource, currentTimeMillis);
			}
		} else {
			ret = getFreshSource(sourceProfile);
			cache.put(new Element(urlSource, ret));
			sourceLastAccess.put(urlSource, currentTimeMillis);
		}
		return ret;
	}

	/**
	 * get a source form the Web (without cache).
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 * @throws TimeoutException 
	 */
	private Source getFreshSource(final SourceProfile sourceProfile) throws TimeoutException {
		if (log.isDebugEnabled()) {
			log.debug("in getFreshSource");
		}
		Source ret = new GlobalSource();
		//start a Thread to get FreshSource
		FreshSourceThread thread = new FreshSourceThread(sourceProfile);
		thread.start();
		int timeOut = defaultTimeout;
		try {
			timeOut = sourceProfile.getTimeOut();
		} catch (ComputeFeaturesException e1) {
			log.warn("getFreshSource : using defaultTimeout because "
				+ "Timeout is not defined for sourceProfile "
				+ sourceProfile.getId());
		}
		try {
			thread.join(timeOut);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Error during execution of Thread getting Source";
				log.error(msg);
				throw new RuntimeException(msg, e);
			}			
			ret = thread.getSource();
		} catch (InterruptedException e) {
			String msg = "Thread getting Source interrupted";
			log.warn(msg);
			throw new RuntimeException(msg, e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in " + timeOut + " milliseconds";
			log.warn(msg);
			throw new TimeoutException(msg);
        }	
		return ret;
	}

	/**
	 * @param defaultTimeout
	 */
	public void setDefaultTimeout(final int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(cacheName)) {
			setDefaultCacheName();
			log.warn(getClass() + ": no cacheName attribute set, '" 
					+ cacheName + "' will be used");
		}
		Assert.notNull(cacheManager, 
				"property cacheManager of class " + getClass().getName() 
				+ " can not be null");
		if (!cacheManager.cacheExists(cacheName)) {
			cacheManager.addCache(cacheName);
		}
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

}
