package org.esupportail.lecture.dao;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
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
	 * Default timeout used for http connection
	 */
	private int defaultTimeout = 3000;

	/**
	 * @param profile 
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
	@SuppressWarnings("unchecked")
	private ManagedCategory getFreshManagedCategory(ManagedCategoryProfile profile) {
		//TODO (RB) refactoring of exceptions
		if (log.isDebugEnabled()) {
			log.debug("in getFreshManagedCategory");
		}
		ManagedCategory ret = new ManagedCategory();
		try {
			//get the XML
			SAXReader reader = new SAXReader();
			String categoryURL = profile.getUrlCategory();
			Document document = reader.read(categoryURL);
			Element root = document.getRootElement();
			// Category properties
			ret.setName(root.valueOf("@name"));
			ret.setDescription(root.valueOf("/category/description"));
			ret.setProfileId(profile.getId());
			// SourceProfiles loop
			Hashtable<String, SourceProfile> sourceProfiles = new Hashtable<String,SourceProfile>();
			List<Node> srcProfiles = root.selectNodes("/category/sourceProfiles/sourceProfile");
			for (Node srcProfile : srcProfiles) {
				ManagedSourceProfile sp = new ManagedSourceProfile(profile);
				sp.setFileId(srcProfile.valueOf("@id"));
				sp.setName(srcProfile.valueOf("@name"));
				sp.setSourceURL(srcProfile.valueOf("@url"));
				sp.setTtl(Integer.parseInt(srcProfile.valueOf("@ttl")));
				String specificUserContentValue = srcProfile.valueOf("@specificUserContent");
				if (specificUserContentValue.equals("yes")) {
					sp.setSpecificUserContent(true);
				}
				else {
					sp.setSpecificUserContent(false);
				}
				sp.setXsltURL(srcProfile.valueOf("@xsltFile"));
				sp.setItemXPath(srcProfile.valueOf("@itemXPath"));
				String access = srcProfile.valueOf("@access");
				if (access.equalsIgnoreCase("public")) {
					sp.setAccess(Accessibility.PUBLIC);
				} else if (access.equalsIgnoreCase("cas")) {
					sp.setAccess(Accessibility.CAS);
				}
				// SourceProfile visibility
				VisibilitySets visibilitySets = new VisibilitySets();  
				// foreach (allowed / autoSubscribed / Obliged)
				visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/allowed")));
				visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/obliged")));
				visibilitySets.setAutoSubscribed(XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/autoSubscribed")));
				sp.setVisibility(visibilitySets);
				sp.setTtl(profile.getTtl());
				sourceProfiles.put(sp.getId(),sp);				
			}
			ret.setSourceProfilesHash(sourceProfiles);
			// Category visibility
			VisibilitySets visibilitySets = new VisibilitySets();  
			// foreach (allowed / autoSubscribed / Obliged)
			visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/allowed")));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/obliged")));
			visibilitySets.setAutoSubscribed(XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/autoSubscribed")));
			ret.setVisibility(visibilitySets);
		} catch (DocumentException e) {
			String profileId = (profile != null ? profile.getId() : "null");
			String msg = "getFreshManagedCategory("+profileId+"). Can't read configuration file.";
			log.error(msg);
			throw new XMLParseException(msg ,e);
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
	 */
	private Source getFreshSource(SourceProfile sourceProfile) {
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
			String msg = "getSource with url="+sourceProfile.getSourceURL()+". Is it a valid XML Source ?";
			log.error(msg);
			throw new XMLParseException(msg ,e);
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
