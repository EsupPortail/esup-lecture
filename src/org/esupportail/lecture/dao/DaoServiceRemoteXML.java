package org.esupportail.lecture.dao;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * @author bourges
 * this class is used to get remote XML contents (category, source) and manage cache on this contents
 */
public class DaoServiceRemoteXML {
	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(DaoServiceRemoteXML.class);
	/**
	 * cache Manager (init by Spring)
	 */
	private CacheProviderFacade cacheProviderFacade;
	/**
	 * caching model (init by Spring)
	 */
	private CachingModel cachingModel;
	/**
	 * hash of last last Access in milliseconds by url of managedCategory
	 */
	private Hashtable<String, Long> managedCategoryLastAccess = new Hashtable<String, Long>();
	/**
	 * hash of last last Access in milliseconds by url of Source
	 */
	private Hashtable<String, Long> sourceLastAccess = new Hashtable<String, Long>();
	/**
	 * Default timeout used for http connection (normally, it should be not used)
	 */
	private int defaultTimeout = 3000;

	/**
	 * @param profile 
	 * @return a managedCategory
	 * @throws TimeoutException 
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) throws TimeoutException {

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
		String cacheKey = "CAT:"+profile.getId()+url;
		System.currentTimeMillis();
		Long lastcatAccess = managedCategoryLastAccess.get(cacheKey);
		Long currentTimeMillis = System.currentTimeMillis();
		if (lastcatAccess != null) {
			if (lastcatAccess + (profile.getTtl() * 1000) > currentTimeMillis) {
				ret = (ManagedCategory)cacheProviderFacade.getFromCache(cacheKey, cachingModel);
				if (ret == null) { // not in cache !
					ret = getFreshManagedCategory(profile);
					cacheProviderFacade.putInCache(cacheKey, cachingModel, ret);
					managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("ManagedCategory from url "+url+" can't be found in cahe --> change cache size ?");
					}
				}				
			}
			else{
				ret = getFreshManagedCategory(profile);
				cacheProviderFacade.putInCache(cacheKey, cachingModel, ret);
				managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
			}
		}
		else {
			ret = getFreshManagedCategory(profile);
			cacheProviderFacade.putInCache(cacheKey, cachingModel, ret);
			managedCategoryLastAccess.put(cacheKey, currentTimeMillis);
		}
		return ret;
	}
	
	/**
	 * get a managed category from the web without cache
	 * @param profile ManagedCategoryProfile of Managed category to get
	 * @return Managed category
	 * @throws TimeoutException 
	 */
	@SuppressWarnings("unchecked")
	private ManagedCategory getFreshManagedCategory(ManagedCategoryProfile profile) throws TimeoutException {
		//TODO (RB) refactoring of exceptions
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
//				TODO (RB) --> throw new Exception(msg,e);
			}
			ret = thread.getManagedCategory();
		} catch (InterruptedException e) {
			String msg = "Thread interrupted";
			log.warn(msg);
			//TODO (RB) --> throw new Exception(msg,e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in "+timeOut+" milliseconds";
			log.warn(msg);
			throw new TimeoutException(msg);
        }	
		return ret;
	}

	/**
	 * get a source form cache
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 * @throws TimeoutException 
	 */
	public Source getSource(SourceProfile sourceProfile) throws TimeoutException {
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
			if (lastSrcAccess + (sourceProfile.getTtl() * 1000) > currentTimeMillis) {
				ret = (Source)cacheProviderFacade.getFromCache(urlSource, cachingModel);
				if (ret == null) { // not in cache !
					ret = getFreshSource(sourceProfile);
					cacheProviderFacade.putInCache(urlSource, cachingModel, ret);
					sourceLastAccess.put(urlSource, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("Source from url "+urlSource+" can't be found in cahe --> change cache size ?");
					}
				}				
			}
			else{
				ret = getFreshSource(sourceProfile);
				cacheProviderFacade.putInCache(urlSource, cachingModel, ret);
				sourceLastAccess.put(urlSource, currentTimeMillis);
			}
		}
		else {
			ret = getFreshSource(sourceProfile);
			cacheProviderFacade.putInCache(urlSource, cachingModel, ret);
			sourceLastAccess.put(urlSource, currentTimeMillis);
		}
//		}
		return ret;

	}

	/**
	 * get a source form the Web (without cache)
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 * @throws TimeoutException 
	 */
	private Source getFreshSource(SourceProfile sourceProfile) throws TimeoutException {
		//TODO (RB) refactoring of exceptions
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
			log.warn("getFreshSource : using defaultTimeout because Timeout is not defined for sourceProfile "+sourceProfile.getId());
		}
		try {
			thread.join(timeOut);
			Exception e = thread.getException();
			if (e != null) {
//				TODO (RB) --> throw new Exception(msg,e);
			}			
			ret = thread.getSource();
		} catch (InterruptedException e) {
			String msg = "Thread interrupted";
			log.warn(msg);
			//TODO (RB) --> throw new Exception(msg,e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in "+timeOut+" milliseconds";
			log.warn(msg);
			throw new TimeoutException(msg);
        }	
		return ret;
	}

	
	
	/**
	 * used by Spring at init
	 * @param cacheProviderFacade to set
	 */
	public void setCacheProviderFacade(CacheProviderFacade cacheProviderFacade) {
		this.cacheProviderFacade = cacheProviderFacade;
	}

	/**
	 * used by Spring at init
	 * @param cachingModel to set
	 */
	public void setCachingModel(CachingModel cachingModel) {
		this.cachingModel = cachingModel;
	}

	/**
	 * @param defaultTimeout
	 */
	public void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}
}
