package org.esupportail.lecture.dao;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.exceptions.ErrorException;
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
	 * @throws ErrorException 
	 * @return a managedCategory
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(ManagedCategoryProfile)
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
	 */
	private ManagedCategory getFreshManagedCategory(ManagedCategoryProfile profile) {
		//TODO (RB) refactoring of exceptions
		if (log.isDebugEnabled()) {
			log.debug("in getFreshManagedCategory");
		}
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
			ret.setProfileId(profile.getId());
			//ret.setTtl(profile.getTtl());
			// SourceProfiles loop

			Hashtable<String, SourceProfile> sourceProfiles = new Hashtable<String,SourceProfile>();

			int max = xml.getMaxIndex("sourceProfiles(0).sourceProfile");
			for(int i = 0; i <= max;i++ ){
				Configuration subxml = xml.subset("sourceProfiles(0).sourceProfile("+i+")");
				// SourceProfile properties

				ManagedSourceProfile sp = new ManagedSourceProfile(profile);

				sp.setFileId(subxml.getString("[@id]"));
				sp.setName(subxml.getString("[@name]"));
				sp.setSourceURL(subxml.getString("[@url]"));
				sp.setTtl(subxml.getInt("[@ttl]"));
				sp.setSpecificUserContent(subxml.getBoolean("[@specificUserContent]"));
				sp.setXsltURL(subxml.getString("[@xsltFile]"));
				sp.setItemXPath(subxml.getString("[@itemXPath]"));
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
				visibilitySets.setAutoSubscribed(XMLUtil.loadDefAndContentSets(xml, "sourceProfiles(0).sourceProfile("+i+").visibility(0).autoSubscribed(0)"));
				sp.setVisibility(visibilitySets);
				sp.setTtl(profile.getTtl());
				sourceProfiles.put(sp.getId(),sp);
			}
			ret.setSourceProfilesHash(sourceProfiles);
			// Category visibility
			VisibilitySets visibilitySets = new VisibilitySets();  
			// foreach (allowed / autoSubscribed / Obliged
			visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(xml, "visibility(0).allowed(0)"));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "visibility(0).obliged(0)"));
			visibilitySets.setAutoSubscribed(XMLUtil.loadDefAndContentSets(xml, "visibility(0).autoSubscribed(0)"));
			ret.setVisibility(visibilitySets);
		} catch (MalformedURLException e) {
			log.error("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
			throw new ErrorException("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
		} catch (ConfigurationException e) {
			log.error("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
			throw new ErrorException("DaoServiceRemoteXML :: getFreshManagedCategory, "+e.getMessage());
		} 
		return ret;
	}
	
	/**
	 * get a source form cache
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 */
	public Source getSource(SourceProfile sourceProfile) {
		Source ret = new GlobalSource();
// not yet implemented
//		if (sourceProfile.isSpecificUserContent()) { 
//			ret = getFreshSource(sourceProfile);
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
	 * get a source from the web without Web
	 * @see DaoServiceRemoteXML#getSource(String, int, String, boolean)
	 * @return the source
	 */
	private Source getFreshSource(SourceProfile sourceProfile) {
		//TODO (RB) refactoring of exceptions
		//log.debug("URL de la source : "+urlSource);
		Source ret = new GlobalSource();
		try {
			String dtd = null;
			String root = null;
			String rootNamespace = null;
			String xmltype = null;
			String xml = null;
			
			//get the XML
			SAXReader reader = new SAXReader();
			String sourceURL = sourceProfile.getSourceURL();
			Document document = reader.read(sourceURL);
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
			//find XML Schema URL
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
			ret.setURL(sourceURL);
//			ret.setItemXPath(sourceProfile.getItemXPath());
//			ret.setXsltURL(sourceProfile.getXsltURL());
		} catch (DocumentException e) {
			log.error("DaoServiceRemoteXML :: getSource, "+e.getMessage());
			throw new ErrorException("DaoServiceRemoteXML :: getSource, url="+sourceProfile.getSourceURL()+", message="+e.getMessage());
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
}
