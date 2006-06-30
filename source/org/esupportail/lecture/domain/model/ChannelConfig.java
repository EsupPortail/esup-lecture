package org.esupportail.lecture.domain.model;


import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * 26/06/2006
 * Channel Config : class for loading and parsing chanel file config.
 * @author gbouteil
 *
 */
public class ChannelConfig {
	
	/* ********************** PROPERTIES**************************************/ 

	/**
	 * The instance of the channel configuration
	 */
	private static ChannelConfig singleton = null;
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * The channel of the config
	 */
	private static Channel channel;
	
	XMLConfiguration config;
	
	
	/* Objects to create */
//	 List<Context> listContexts;
//	 List<ManagedCategoryProfile> listManagedCategoryProfiles;
//	private List<MappingFile> listMappingFile;
//	List<Element> contextElements = new ArrayList();
//	List<Element> profileElements = new ArrayList();
//	Attribute mappingFile;
	
	/*path of the config file*/
	private static String configFilePath ="/properties/channelConfig.xml";
	private static long configFileLastModified;
		
	/* ********************** ACCESSORS *****************************************/
	
	public void setChannel(Channel c){
		channel = c;
	}
	
	public Channel getChannel(){
		return channel;
	}
	/* ********************** METHODS *****************************************/

	/**
	* Get an instance of the chanel configuration
	* @return an instance of the channel configuration
	* @throws Exception
	*/
	public static ChannelConfig getInstance(Channel c) throws Exception  {
		if (singleton == null) {
			singleton = new ChannelConfig(c);
			log.error("ChannelConfig créé");
		}else {
			// TODO : voir le chargt de fichier modifie + cf. new ChannelConfig()
//			URL url = ChannelConfig.class.getResource(configFilePath);
//			File configFile = new File(url.getFile());
//			
//			long newDate = configFile.lastModified();
//			if (configFileLastModified < newDate) {
//				log.debug("getInstance :: "+"Configuration reloaded");
//				configFileLastModified = newDate;
//				config = new ChannelConfig(c);
		}		
		return singleton;
	}
			
	/**
	 * Constructor
	 * @throws Exception
	 */
	private ChannelConfig(Channel c) throws Exception {
		this.channel = c;
		
		try {
			// TODO voir le /
			config = new XMLConfiguration("properties/test.xml");
//			 TODO faire la check de la DTD
//			config.setValidating(true);
			// TODO automatic reloading
//			config.setReloadingStrategy(new FileChangedReloadingStrategy());
//			config.load();

			/* Loading UrlMappingFile */
			loadUrlMappingFile();

			/* Loading managed category profiles */
			loadManagedCategoryProfiles();
			log.error("Categories chargées");
//			
//			/* Loading Contexts */
//			loadContexts();
//			

		} catch (ConfigurationException cex) {
			log.error(" Erreur config");
		
		}
		
//		/* Parsing */
//		Document doc = getDocumentFromConfigFile();
//		
//		/* Loading data in config file */
//		loadDataConfig(doc);
//		

//		/* Loading Contexts */
//		loadContexts();
//		

	}

	/**
	 * Load Mapping file location 
	 *
	 */
	private void loadUrlMappingFile() {
	   	MappingFile mappingFile = new MappingFile();
	   	mappingFile.setLocation(config.getString("[@mappingFile]"));
	   	channel.setMappingFile(mappingFile);
    }	
	
	private void loadManagedCategoryProfiles() throws Exception {
		// Code pour la version commons-configuration 1.3
//		List configCategoryProfiles = config.configurationsAt("categoryProfiles");
//		for(Iterator it = configCategoryProfiles.iterator(); it.hasNext();) {
//		    HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
//		    ManagedCategoryProfile mcp = new ManagedCategoryProfile();
//		    mcp.setId(sub.getInt("id"));
//		    mcp.setName(sub.getString("name"));
//		    mcp.setUrlCategory(sub.getString("urlCategory"));
//		    mcp.setTrustCategory(sub.getBoolean("trustCategory"));
//		    mcp.setTtl(sub.getInt("ttl"));
//		    
//		    // Accessibility
//		    String access = sub.getString("access");
//		    if (access == "PUBLIC") {
//				mcp.setAccess(Accessibility.PUBLIC);
//			} else if (access == "CAS"){
//				mcp.setAccess(Accessibility.CAS);
//			} else {
//				throw new Exception();
//			}
//		   // Visibility
		
		

		int nbProfiles = config.getMaxIndex("categoryProfile") + 1;
		for(int i = 0; i<nbProfiles;i++ ){
			String pathCategoryProfile = "categoryProfile(" + i + ")";
			ManagedCategoryProfile mcp = new ManagedCategoryProfile();
			mcp.setId(config.getInt(pathCategoryProfile+ "[@id]"));
			mcp.setName(config.getString(pathCategoryProfile+ "[@name]"));
			mcp.setUrlCategory(config.getString(pathCategoryProfile+ "[@urlCategory]"));
			mcp.setTrustCategory(config.getBoolean(pathCategoryProfile+ "[@trustCategory]"));
			mcp.setTtl(config.getInt(pathCategoryProfile+ "[@ttl]"));
			
			//Description
			mcp.setDescription(config.getString(pathCategoryProfile+".description"));
			
		    // Accessibility
		    String access = config.getString(pathCategoryProfile+"[@access]");
		    if (access.equalsIgnoreCase("PUBLIC")) {
				mcp.setAccess(Accessibility.PUBLIC);
			} else if (access.equalsIgnoreCase("CAS")) {
				mcp.setAccess(Accessibility.CAS);
			} else {
				throw new Exception();
			}
			
		    // Visibility
		    VisibilitySets visibilitySets = new VisibilitySets();
		    
		    // foreach (allowed / autoSubscribed / Obliged
		    visibilitySets.setAllowed(loadDefAndContentSets("allowed",i));
		    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed",i));
		   	visibilitySets.setObliged(loadDefAndContentSets("obliged",i));
		    mcp.setVisibility(visibilitySets);
		    
			log.error("chargement futur de mcp");
		    channel.setManagedCategoryProfile(mcp);
	    
		}
		
	}
	
	private DefAndContentSets loadDefAndContentSets(String fatherName,int index){
		DefAndContentSets defAndContentSets = new DefAndContentSets();
		String fatherPath = "categoryProfile("+index+ ").visibility." + fatherName;
		
//		Definition by group enumeration
		int nbGroups = config.getMaxIndex(fatherPath + ".group") +1;
		for (int i=0;i<nbGroups;i++){
			defAndContentSets.addGroup(config.getString(fatherPath + ".group("+i+")[@name]"));
		}
   		// Definition by regular 
   		int nbRegulars = config.getMaxIndex(fatherPath + ".regular") +1;   	
   		for (int i=0;i<nbRegulars;i++){
   			RegularOfSet regular = new RegularOfSet();
   			regular.setAttribute(config.getString(fatherPath + ".regular("+i+")[@attribute]"));
   			regular.setValue(config.getString(fatherPath + ".regular("+i+")[@value]"));	
   			defAndContentSets.addRegular(regular);
   		}
   		return defAndContentSets;
	}


//	private void loadCategoryProfiles() throws Exception{
//	    for (Iterator iterator = profileElements.iterator(); iterator.hasNext();) {
//	    	Element element = (Element) iterator.next();
//	    	// TODO c'est le new qui attribut directement le id ?
//	    	ManagedCategoryProfile m = new ManagedCategoryProfile();
//	    	
//	    	// xml attributes
//	    	for(Iterator it = element.attributeIterator(); it.hasNext();){
//	    		Attribute a = (Attribute) it.next();
//	    		String aName = a.getQualifiedName();
//	    		String aValue = a.getValue();
//	    		if (aName == "name"){
//	    			m.setName(aValue);
//	    		}else if (aName == "id"){
//	    			m.setId(Integer.decode(aValue).intValue()); 
//	    		}else if (aName == "urlCategory"){
//	    			m.setUrlCategory(aValue);
//	    		}else if (aName == "trustCategory") {
//	    			Boolean b = Boolean.getBoolean(aValue);
//	    			m.setTrustCategory(b.booleanValue()); 
//	    		}else if (aName == "access") {
//	    			if (aValue == "PUBLIC") {
//	    				m.setAccess(Accessibility.PUBLIC);
//	    			} else if (aValue == "CAS"){
//	    				m.setAccess(Accessibility.CAS);
//	    			} else {
//	    				throw new Exception();
//	    			}
//	    		}else if (aName == "ttl") {
//	    			m.setTtl(Integer.decode(aValue).intValue());
//	    		}else {
//	    			throw new Exception();
//	    		}
//	    	}
//	
//	    	// Element visibility (e1)
//	    	int nbChild1 = 0;
//	    	for(Iterator i1 = element.elementIterator(); i1.hasNext();) {
//	    		nbChild1++;
//	    		if (nbChild1 > 1){
//	    			throw new Exception();
//	    		}
//	    		
//	    		Element e1 = (Element)i1.next();
//	    		String eName1 = e1.getQualifiedName();
//	    		if (eName1 != "visibility"){
//	    			throw new Exception();
//	    		}
//	    		
//	    		// Elements allowed, autoSubscribed, obliged (e2)
//	    		int nbChild2 = 0;
//	    		for (Iterator i2 = e1.elementIterator(); i2.hasNext();) {
//	    			nbChild2++;
//	    			if (nbChild2 > 3) {
//	    				throw new Exception();
//	    			}
//	    			
//	    			DefAndContentSets mySet = new DefAndContentSets();
//	    			
//	    			Element e2 = (Element)i2.next();
//	    			String eName2 = e2.getQualifiedName();
//	    			if ((eName2!="allowed")||(eName2!="autoSubscribed")||(eName2!="obliged")){ 
//	    				throw new Exception();
//		    		}
//	    				    				    			
//	    			
//		    				
//			    			
//			    		
//			    			
//			    			
//		    			}
//	    					
//	    			}
//	    			
//	    		}
//	    		
//	    		
//	    	}
//	    	
//	    	
//	    }
//	}
	
	/**
	 * Load "regular" and "group" of a DefAndCOntentSets object
	 * @param XML element containing elements  "regular" and/or "group"
	 */
//	private DefAndContentSets loadDefAndContentSets (Element e) {
//		//TODO : revoir en fonction de la definition des groups ...
//		
//		for (Iterator i1 = e.elementIterator(); i1.hasNext();) {
//			Element e = (Element)i1.next();
//			String eName = e.getQualifiedName();
//			if ((eName!="regular")||(eName!="group")){ 
//				throw new Exception();
//    		}
//			
//			// Attributes
//			int nbChild = 0;
//			EnumSet<String> group = new EnumSet();
//			EnumSet<RegularOfSets> regulars = new EnumSet();
//			RegularOfSets regular;
//			if (eName == "regular"){
//				regular = new RegularOfSet();
//			}
//			
//			for (Iterator i2 = e.attributeIterator(); i2.hasNext();){
//				nbChild++;
//				
//				Attribute a = (Attribute) i2.next();
//	    		String aName = a.getQualifiedName();
//	    		String aValue = a.getValue();
//	    		
//	    		
//				if (eName == "regular"){
//					if (nbChild > 2 ){
//	    				throw new Exception();
//	    			}
//		    		if (aName == "attribute"){
//		    			regular.setAttribute(aValue);
//		    		}else if (aName == "value"){
//		    			regular.setValue(aValue);
//		    		}else{
//	    				throw new Exception();
//	    			}			
//				}else if (eName == "group"){
//					if (nbChild > 1 ){
//	    				throw new Exception();
//	    			}
//					if (aName == "name"){
//						group.add(aValue);
//					}
//					else {
//						throw new Exception();
//	    			}
//				}else{ 
//    				throw new Exception();
//	    		}
//			}
//			
//			if (eName == "regular"){
//				regulars.add(regular);
//			}
//			
//		}
//		
//			
//			defAndContentSets.setRegulars()
//				
//	}
//	    
	/**
	 * Parse the config and create a Document
	 */	
//	private Document getDocumentFromConfigFile() throws Exception {
//		URL url = ChannelConfig.class.getResource(configFilePath);
//			
//		if (url != null) {
//			InputSource inSource = new InputSource(url.toExternalForm());	
//			// get the file date
//			File configFile = new File(url.getFile());
//			configFileLastModified = configFile.lastModified();
//		             
//			try {
//				//parse config file
//				SAXReader reader = new SAXReader();
//			    Document document = reader.read(url);
//			    return document;
//						
//			}catch (Exception e) {					
//				log.error("ChannelConfig :: Unable to open config file" + configFilePath	+" "+ e.toString());
//				throw new Exception();
//			}
//		}else { 
//			log.error("ChannelConfig :: "	+ configFilePath + " does not exist.\n");
//			throw new Exception();
//		}
//	}
			
	/**
	 * Load data config in Channel
	 * @throws Exception 
	 */
//	private void loadDataConfig(Document doc) throws Exception{
//		
//		Element root = doc.getRootElement();
//		
//		// iterate through child elements of root
//        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
//            Element element = (Element) i.next();
//            String qualifiedName = element.getQualifiedName();
//            if (qualifiedName == "context") {
//            	contextElements.add(element);
//            }else if (qualifiedName == "categoryProfile"){
//            	profileElements.add(element);
//            }else {
//            	throw new Exception();
//            }         	
//        }
//        // iterate through attributes of root 
//        for ( Iterator i = root.attributeIterator(); i.hasNext(); ) {
//            Attribute attribute = (Attribute) i.next();
//            String qualifiedName = attribute.getQualifiedName();
//            if (qualifiedName == "mappingFile") {
//            	mappingFile = attribute;
//            } else {
//            	throw new Exception();
//            }
//        }
//	}		
   
	    
    private void loadContexts(){
    	
    }
    
 
    
 
        
        
	
}
