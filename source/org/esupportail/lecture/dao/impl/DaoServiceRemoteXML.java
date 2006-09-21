package org.esupportail.lecture.dao.impl;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.utils.exception.ErrorException;

public class DaoServiceRemoteXML {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DaoServiceRemoteXML.class);
	
	/**
	 * @throws ErrorException 
	 * @see org.esupportail.lecture.dao.DaoService#getCategory(java.lang.String, int, java.lang.String)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) {
		URL url;
		ManagedCategory ret = new ManagedCategory();
		//TODO cache !
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
			HashSet<ManagedSourceProfile> sourceProfiles = new HashSet<ManagedSourceProfile>();
			int max = xml.getMaxIndex("sourceProfiles(0).sourceProfile");
			for(int i = 0; i <= max;i++ ){
				Configuration subxml = xml.subset("sourceProfiles(0).sourceProfile("+i+")");
				// SourceProfile properties
				ManagedSourceProfile sp = new ManagedSourceProfile();
				sp.init();
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
				sourceProfiles.add(sp);
			}
			ret.setManagedSourceProfilesSet(sourceProfiles);
			// Category visibility
			VisibilitySets visibilitySets = new VisibilitySets();  
			// foreach (allowed / autoSubscribed / Obliged
			visibilitySets.setAllowed(XMLUtil.loadDefAndContentSets(xml, "visibility(0).allowed(0)"));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "visibility(0).obliged(0)"));
			visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(xml, "visibility(0).autoSubscribed(0)"));
			ret.setVisibility(visibilitySets);
		} catch (MalformedURLException e) {
			log.error("DaoServiceRemoteXML :: getCategory, "+e.getMessage());
			throw new ErrorException();
		} catch (ConfigurationException e) {
			log.error("DaoServiceRemoteXML :: getCategory, "+e.getMessage());
			throw new ErrorException();
		} 
		return ret;
	}
	
	public Source getSource(String urlSource, int ttl, String profileId, boolean specificUserContent) {
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
			//TODO get xmltype (xsd def)
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
}
