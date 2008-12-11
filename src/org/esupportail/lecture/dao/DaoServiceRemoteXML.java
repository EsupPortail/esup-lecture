package org.esupportail.lecture.dao;

import java.util.Hashtable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryDummy;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceDummy;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.dao.InternalDaoException;
import org.esupportail.lecture.exceptions.dao.SourceInterruptedException;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
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
	private static final Log LOG = LogFactory.getLog(DaoServiceRemoteXML.class);
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
	 * get a Category form cache.
	 * Synchronized to avoid multiple source get in case of simultaneous first access
	 * @param profile - Category profile of category to get
	 * @param ptCas CAS proxy ticket 
	 * @return the Category
	 * @throws InternalDaoException 
	 */
	public synchronized ManagedCategory getManagedCategory(final ManagedCategoryProfile profile, 
			final String ptCas) throws InternalDaoException {

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
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getManagedCategory");
		}

		ManagedCategory ret = new ManagedCategory(profile);
		String urlCategory = profile.getCategoryURL();
		String cacheKey = "CAT:" + profile.getId() + urlCategory;
		try {
			Element element = cache.get(cacheKey);
			if (element == null) { 
				try {
					ret = getFreshManagedCategory(profile, ptCas);
				} catch (InfoDaoException e) {
					ret = new ManagedCategoryDummy(profile, e);
					String msg = "Create dummy category : "+ cacheKey;
					LOG.warn(msg);
				}
				cache.put(new Element(cacheKey, ret));
				cache.get(cacheKey).setTimeToLive(profile.getTtl());
			} else {
				ret = (ManagedCategory) element.getObjectValue();
				if (ret instanceof ManagedCategoryDummy) {
					ret = (ManagedCategoryDummy) element.getObjectValue();
				}
			}
		} catch (IllegalStateException e) {
			throw new InternalDaoException(e);
		} catch (CacheException e) {
			throw new InternalDaoException(e);
		}
		return ret;
	}

	/**
	 * @param profile 
	 * @return a managedCategory
	 * @throws InternalDaoException 
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(final ManagedCategoryProfile profile) throws InternalDaoException {
		return getManagedCategory(profile, null);
	}
	
	/**
	 * get a managed category from the web without cache.
	 * @param profile ManagedCategoryProfile of Managed category to get
	 * @param ptCas - user and password. null for anonymous access
	 * @return Managed category
	 * @throws TimeoutException 
	 * @throws SourceInterruptedException 
	 * @throws InternalDaoException 
	 * @throws XMLParseException 
	 */
	private ManagedCategory getFreshManagedCategory(final ManagedCategoryProfile profile,
			final String ptCas) throws TimeoutException, SourceInterruptedException, InternalDaoException, XMLParseException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getFreshManagedCategory");
		}
		ManagedCategory ret = new ManagedCategory(profile);
		//start a Thread to get FreshManagedCategory
		FreshManagedCategoryThread thread = new FreshManagedCategoryThread(profile, ptCas);
		
		int timeout = 0;
		try {
			thread.start();
			timeout = defaultTimeout;
			timeout = profile.getTimeOut();
			thread.join(timeout);
			ret = thread.getManagedCategory();
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Source launches XMLParseException";
				LOG.error(msg);
				throw new XMLParseException(msg, e);
			}
		} catch (InterruptedException e) {
			String msg = "Thread getting ManagedCategory interrupted";
			LOG.warn(msg);
			throw new SourceInterruptedException(msg, e);
		} catch (IllegalThreadStateException e) {
			String msg = "Thread getting ManagedCategory interrupted";
			LOG.warn(msg);
			throw new InternalDaoException(e);
		} catch (XMLParseException e) {
			String msg = "Thread getting Source launches XMLParseException";
			LOG.warn(msg);
			throw new XMLParseException(msg, e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in " + timeout + " milliseconds";
			LOG.warn(msg);
			throw new TimeoutException(msg);
        }	
		return ret;
	}

	/**
	 * get a source form cache.
	 * Synchronized to avoid multiple source get in case of simultaneous first access
	 * @param sourceProfile source profile of source to get
	 * @param ptCas CAS proxy ticket 
	 * @return the source
	 * @throws InternalDaoException 
	 */
	public synchronized Source getSource(final SourceProfile sourceProfile, final String ptCas) 
	throws InternalDaoException {
		// TODO (RB <-- GB) Pourquoi ne déclare-tu pas un type Source alors que tu fais un new GlobalSource ?
		// Je comprends que tu n'as pas le droit de faire un new Source car abstract, 
		// mais en ne déclarant pas un GlobalSource, tu limites la potentialité du ret. 
		// Ne crois tu pas ? Tu viens m'en parler ?
		Source ret = new GlobalSource(sourceProfile);
		String urlSource = sourceProfile.getSourceURL();
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getSource");
		}
		try {
			Element element = cache.get(urlSource);
			if (element == null) { 
				try {
					ret = getFreshSource(sourceProfile, ptCas);
				} catch (InfoDaoException e) {
					ret = new SourceDummy(sourceProfile, e);
					String msg = "Create dummy source : "+ urlSource;
					LOG.warn(msg);
				}
				cache.put(new Element(urlSource, ret));
				cache.get(urlSource).setTimeToLive(sourceProfile.getTtl());
				if (LOG.isDebugEnabled()) {
					LOG.debug("Put source in cache : "+urlSource+ " Ttl: "+String.valueOf(sourceProfile.getTtl()));
				}
			} else {
				ret = (Source) element.getObjectValue();
				if (ret instanceof SourceDummy) {
					ret = (SourceDummy) element.getObjectValue();
				}
			}
		} catch (IllegalStateException e) {
			throw new InternalDaoException(e);
		} catch (CacheException e) {
			throw new InternalDaoException(e);
		}
		return ret;

	}

	/**
	 * get a source from cache.
	 * @param sourceProfile source profile of source to get
	 * @return the source 
	 * @throws InternalDaoException 
	 */
	public Source getSource(final SourceProfile sourceProfile) throws InternalDaoException {
		return getSource(sourceProfile, null);
	}

	/**
	 * get a source form the Web (without cache).
	 * @param sourceProfile source profile of source to get
	 * @param ptCas - user and password. null for anonymous access
	 * @return the source
	 * @throws SourceInterruptedException 
	 * @throws TimeoutException 
	 * @throws InternalDaoException 
	 * @throws XMLParseException 
	 */
	private Source getFreshSource(final SourceProfile sourceProfile,
			final String ptCas) throws SourceInterruptedException, TimeoutException, InternalDaoException, XMLParseException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getFreshSource");
		}
		Source ret = new GlobalSource(sourceProfile);
		//start a Thread to get FreshSource
		FreshSourceThread thread = new FreshSourceThread(sourceProfile, ptCas);
		int timeout = 0;
		try {
			thread.start();
			timeout = defaultTimeout;
			timeout = sourceProfile.getTimeOut();
			thread.join(timeout);
			ret = thread.getSource();
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Source launches XMLParseException";
				LOG.error(msg);
				throw new XMLParseException(msg, e);
			}
		} catch (XMLParseException e) {
			String msg = "Thread getting Source launches XMLParseException";
			LOG.warn(msg);
			throw new XMLParseException(msg, e);
		} catch (InterruptedException e) {
			String msg = "Thread getting Source interrupted";
			LOG.warn(msg);
			throw new SourceInterruptedException(msg, e);
		} catch (IllegalThreadStateException e) {
			String msg = "IllegalThreadStateException";
			LOG.error(e.getMessage());
			throw new InternalDaoException(e);
		}
        if (thread.isAlive()) {
    		thread.interrupt();
			String msg = "Category not loaded in " + timeout + " milliseconds";
			LOG.warn(msg);
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
