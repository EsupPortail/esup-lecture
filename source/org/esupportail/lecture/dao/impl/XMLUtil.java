package org.esupportail.lecture.dao.impl;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.RegularOfSet;

public class XMLUtil {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(XMLUtil.class);
	
	public static DefinitionSets loadDefAndContentSets(XMLConfiguration xmlFile, String fatherPath){
		if (log.isDebugEnabled()){
			log.debug("loadDefAndContentSets("+fatherPath+")");
		}
		DefinitionSets defAndContentSets = new DefinitionSets();
		
		// Definition by group enumeration
		int nbGroups = xmlFile.getMaxIndex(fatherPath + ".group");
		for (int i=0;i<=nbGroups;i++){
			defAndContentSets.addGroup(xmlFile.getString(fatherPath + ".group("+i+")[@name]"));
		}
		// Definition by regular 
		int nbRegulars = xmlFile.getMaxIndex(fatherPath + ".regular");   	
		for (int i=0;i<=nbRegulars;i++){
			RegularOfSet regular = new RegularOfSet();
			regular.setAttribute(xmlFile.getString(fatherPath + ".regular("+i+")[@attribute]"));
			regular.setValue(xmlFile.getString(fatherPath + ".regular("+i+")[@value]"));	
			defAndContentSets.addRegular(regular);
		}
		return defAndContentSets;
	}
}