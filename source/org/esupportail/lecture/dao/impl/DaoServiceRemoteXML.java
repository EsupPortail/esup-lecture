package org.esupportail.lecture.dao.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
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
	public ManagedCategory getManagedCategory(String urlCategory, int ttl, String profileId) {
		URL url;
		ManagedCategory ret = new ManagedCategory();
		//TODO cache !
		try {
			url = new URL(urlCategory);
			XMLConfiguration xml = new XMLConfiguration(url);
			//TODO use and test Validating
			//xml.setValidating(true);
			xml.load();
			// Category properties
			ret.setName(xml.getString("[@name]"));
			ret.setDescription(xml.getString("description"));
			ret.setProfilId(profileId);
			ret.setTtl(ttl);
			// SourceProfiles loop
			Hashtable<String,SourceProfile> sourceProfiles = new Hashtable<String, SourceProfile>();
			int max = xml.getMaxIndex("sourceProfiles(0).sourceProfile");
			for(int i = 0; i <= max;i++ ){
				Configuration subxml = xml.subset("sourceProfiles(0).sourceProfile("+i+")");
				// SourceProfile properties
				ManagedSourceProfile sp = new ManagedSourceProfile();
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
				sourceProfiles.put(sp.getId(), sp);
			}
			ret.setSourceProfiles(sourceProfiles);
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
}
