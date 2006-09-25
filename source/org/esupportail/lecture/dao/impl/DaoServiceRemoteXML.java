package org.esupportail.lecture.dao.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.CustomManagedSource;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.utils.exception.ErrorException;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.provider.CacheProviderFacade;

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
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(org.esupportail.lecture.domain.model.ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) {
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
		ManagedCategory ret = new ManagedCategory();
		String url = profile.getUrlCategory();
		System.currentTimeMillis();
		Long lastcatAccess = managedCategoryLastAccess.get(url);
		Long currentTimeMillis = System.currentTimeMillis();
		if (lastcatAccess != null) {
			if (lastcatAccess + (profile.getTtl() * 1000) > currentTimeMillis) {
				ret = (ManagedCategory)cacheProviderFacade.getFromCache(url, cachingModel);
				if (ret == null) { // not in cache !
					ret = getFreshManagedCategory(profile);
					cacheProviderFacade.putInCache(url, cachingModel, ret);
					managedCategoryLastAccess.put(url, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("ManagedCategory from url "+url+" can't be found in cahe --> change cache size ?");
					}
				}				
			}
			else{
				ret = getFreshManagedCategory(profile);
				cacheProviderFacade.putInCache(url, cachingModel, ret);
				managedCategoryLastAccess.put(url, currentTimeMillis);
			}
		}
		else {
			ret = getFreshManagedCategory(profile);
			cacheProviderFacade.putInCache(url, cachingModel, ret);
			managedCategoryLastAccess.put(url, currentTimeMillis);
		}
		return ret;
	}
	
		/**
	 * get a managed category from the web without cache
	 * @param profile ManagedCategoryProfile of Managed category to get
	 * @return Managed category
	 */
	private ManagedCategory getFreshManagedCategory(ManagedCategoryProfile profile) {
		URL url;
		ManagedCategory ret = new ManagedCategory();
		try {
			url = new URL(profile.getUrlCategory());
			XMLConfiguration xml = new XMLConfiguration(url);
			xml.setValidating(true);
			xml.load();
			// Category properties
			ret.setName(xml.getString("[@name]"));
			ret.setDescription(xml.getString("description"));
			ret.setProfilId(profile.getId());
			ret.setTtl(profile.getTtl());
			// SourceProfiles loop
			Hashtable<String,SourceProfile> sourceProfiles = new Hashtable<String,SourceProfile>();
			int max = xml.getMaxIndex("sourceProfiles(0).sourceProfile");
			for(int i = 0; i <= max;i++ ){
				Configuration subxml = xml.subset("sourceProfiles(0).sourceProfile("+i+")");
				// SourceProfile properties
				ManagedSourceProfile sp = new ManagedSourceProfile(profile);
				sp.setManagedCategoryProfile(profile);
				sp.setId(subxml.getString("[@id]"));
				sp.setName(subxml.getString("[@name]"));
				sp.setSourceURL(subxml.getString("[@url]"));
				sp.setTtl(subxml.getInt("[@ttl]"));
				sp.setSpecificUserContent(subxml.getBoolean("[@specificUserContent]"));
				sp.setXsltURL(subxml.getString("[@xslt]"));
				sp.setItemXPath(subxml.getString("[@xpath]"));
				String access = subxml.getString("[@access]");
				if (access.equalsIgnoreCase("public")) {
					sp.setAccess(Accessibility.PUBLIC);
				} else if (access.equalsIgnoreCase("cas")) {
					sp.setAccess(Accessibility.CAS);
				}
				// SourceProfile visibility
				VisibilitySets visibilitySets = new VisibilitySets();  
				// foreach (allowed / autoSubscribed / Obliged
				visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(xml, "sourceProfiles(0).sourceProfile("+i+").visibility(0).allowed(0)"));
				visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "sourceProfiles(0).sourceProfile("+i+").visibility(0).obliged(0)"));
				visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "sourceProfiles(0).sourceProfile("+i+").visibility(0).autoSubscribed(0)"));
				sp.setVisibility(visibilitySets);
				sourceProfiles.put(sp.getId(),sp);
			}
			ret.setSourceProfilesHash(sourceProfiles);
			// Category visibility
			VisibilitySets visibilitySets = new VisibilitySets();  
			// foreach (allowed / autoSubscribed / Obliged
			visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(xml, "visibility(0).allowed(0)"));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "visibility(0).obliged(0)"));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "visibility(0).autoSubscribed(0)"));
			ret.setVisibility(visibilitySets);
		} catch (MalformedURLException e) {
			log.error("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
			throw new ErrorException();
		} catch (ConfigurationException e) {
			log.error("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
			throw new ErrorException();
		} 
		return ret;
	}
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public Source getSource(ManagedSourceProfile profile) {
		Source ret = new GlobalSource();
		String url = profile.getSourceURL();
		String profileId = profile.getId();
		if (profile.isSpecificUserContent()) { // no cache if the content can be different for a specific user
			return getFreshSource(url, profileId);
		}
		System.currentTimeMillis();
		Long lastcatAccess = sourceLastAccess.get(url);
		Long currentTimeMillis = System.currentTimeMillis();
		if (lastcatAccess != null) {
			if (lastcatAccess + (profile.getTtl() * 1000) > currentTimeMillis) {
				ret = (GlobalSource)cacheProviderFacade.getFromCache(url, cachingModel);
				if (ret == null) { // not in cache !
					ret = getFreshSource(url, profileId);
					cacheProviderFacade.putInCache(url, cachingModel, ret);
					sourceLastAccess.put(url, currentTimeMillis);
					if (log.isWarnEnabled()) {
						log.warn("Source from url "+url+" can't be found in cahe --> change cache size ?");
					}
				}				
			}
			else{
				ret = getFreshSource(url, profileId);
				cacheProviderFacade.putInCache(url, cachingModel, ret);
				sourceLastAccess.put(url, currentTimeMillis);
			}
		}
		else {
			ret = getFreshSource(url, profileId);
			cacheProviderFacade.putInCache(url, cachingModel, ret);
			sourceLastAccess.put(url, currentTimeMillis);
		}
		return ret;
	}

	/**
	 * get a source from the web without cache
	 * @param urlSource url of the source
	 * @param profileId of the profile of the source
	 * @return the source
	 */
	private Source getFreshSource(String urlSource, String profileId) {
		Source ret = new GlobalSource();
		try {
			String dtd = null;
			String root = null;
			String rootNamespace = null;
			String xmltype = null;
			String xml = null;
			
			//get the XML
			SAXReader reader = new SAXReader();
			Document document = reader.read(urlSource);
			//find the dtd
			DocumentType doctype = document.getDocType();
			if (doctype != null) {
				dtd = doctype.getSystemID();				
			}
			//find root Element
			Element rootElement = document.getRootElement();
			root = rootElement.getName();
			//find xmlns on root element
			Namespace ns = rootElement.getNamespace();
			if (!ns.getURI().equals("")) {
				rootNamespace = ns.getURI();				
			}
			Namespace xmlSchemaNameSpace = rootElement.getNamespaceForURI("http://www.w3.org/2001/XMLSchema-instance");
			if (xmlSchemaNameSpace != null) {
				String xmlSchemaNameSpacePrefix = xmlSchemaNameSpace.getPrefix();
				for ( Iterator i = rootElement.attributeIterator(); i.hasNext(); ) {
					Attribute attribute = (Attribute) i.next();
					if (attribute.getQName().getNamespacePrefix().equals(xmlSchemaNameSpacePrefix) && attribute.getName().equals("schemaLocation")) {
						xmltype = attribute.getValue();
					}
				}				
			}
			//get XML as String
			xml = document.asXML();
			ret.setDtd(dtd);
			ret.setRootElement(root);
			ret.setXmlns(rootNamespace);
			ret.setXmlStream(xml);
			ret.setXmlType(xmltype);
		} catch (DocumentException e) {
			log.error("DaoServiceRemoteXML :: getSource, "+e.getMessage());
			throw new ErrorException();
		}
		return ret;
	}


	public CacheProviderFacade getCacheProviderFacade() {
		return cacheProviderFacade;
	}


	public void setCacheProviderFacade(CacheProviderFacade cacheProviderFacade) {
		this.cacheProviderFacade = cacheProviderFacade;
	}


	public CachingModel getCachingModel() {
		return cachingModel;
	}


	public void setCachingModel(CachingModel cachingModel) {
		this.cachingModel = cachingModel;
	}

}
