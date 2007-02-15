package org.esupportail.lecture.dao;

import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Node;
import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.RegularOfSet;

/**
 * Collection of statis method to parse xml file used by ESUp-Lecture
 * @author bourges
 */
public class XMLUtil {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(XMLUtil.class);
	
	/**
	 * return DefinitionSets from a dom4j node
	 * @param node 
	 * @return DefinitionSets the this part of XML
	 */
	@SuppressWarnings("unchecked")
	public static DefinitionSets loadDefAndContentSets(Node node){
		if (log.isDebugEnabled()){
			String path = "null";
			if (node != null) path= node.getPath();
			log.debug("loadDefAndContentSets("+path+")");
		}
		DefinitionSets defAndContentSets = new DefinitionSets();
		if (node != null) {
			// Definition by group enumeration
			List<Node> groups = node.selectNodes("group");
			for (Node group : groups) {
				String name = group.valueOf("@name");
				if (log.isDebugEnabled()) log.debug("name = "+name);
				defAndContentSets.addGroup(name);
			}
			// Definition by regular 
			List<Node> regulars = node.selectNodes("regular");
			for (Node regular : regulars) {
				String attribute = regular.valueOf("@attribute");
				if (log.isDebugEnabled()) log.debug("attribute = "+attribute);
				String value = regular.valueOf("@value");
				if (log.isDebugEnabled()) log.debug("value = "+value);
				RegularOfSet regularOfSet = new RegularOfSet();
				regularOfSet.setAttribute(attribute);
				regularOfSet.setValue(value);	
				defAndContentSets.addRegular(regularOfSet);
			}
		}
		return defAndContentSets;
	}
}