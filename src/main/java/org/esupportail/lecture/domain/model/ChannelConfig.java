/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.esupportail.lecture.dao.FreshXmlFileThread;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Channel Config : class used to load and parse XML channel file config.
 * @author gbouteil
 */
public class ChannelConfig  {

	/*
	 ********************** PROPERTIES**************************************/
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(ChannelConfig.class);

	/**
	 * Instance of this class.
	 */
	private static ChannelConfig singleton;

	/**
	 * XML file loaded.
	 */
	private static Document xmlFile;

	/**
	 *  relative classpath of the file to load.
	 */
	private static String filePath;

	/**
	 *  Base path of the file to load.
	 */
	private static String fileBasePath;

	/**
	 * Indicates if file has been modified since last getInstance() calling.
	 */
	private static boolean modified;

	/**
	 * Numbers of category profiles declared in the xml file.
	 */
	private static int nbProfiles;

	/**
	 * Numbers of contexts declared in the xml file.
	 */
	private static int nbContexts;

	/**
	 * timeout of the configFile.
	 */
	private static int xmlFileTimeOut;

	/*
	 ************************** INIT *********************************/

	/**
	 * Private Constructor .
	 */
	private ChannelConfig() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ChannelConfig()");
		}
	}

	/*
	 *********************** METHODS *****************************************/

	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @param configFilePath file path of the channel config
	 * @param defaultTimeOut
	 * @return an instance of the file to load (singleton)
	 * @see ChannelConfig#singleton
	 */
	protected static ChannelConfig getInstance(final String configFilePath, final int defaultTimeOut) {
		filePath = configFilePath;
		xmlFileTimeOut = defaultTimeOut;
		return getInstance();

	}

	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @return an instance of the file to load (singleton)
	 * @see ChannelConfig#singleton
	 */
	protected static synchronized ChannelConfig getInstance() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getInstance()");
		}

		if (singleton == null) {
			singleton = new ChannelConfig();
		}
		return singleton;
	}

	/**
	 * @throws ChannelConfigException
	 */
	protected static synchronized void getConfigFile() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getConfigFile()");
		}

		if (filePath == null) {
			String errorMsg = "Config file path not defined, see in domain.xml file.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}

		URL url = ChannelConfig.class.getResource(filePath);
		if (url == null) {
			String errorMsg = "Config file: " + filePath + " not found.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		File file = new File(url.getFile());
		fileBasePath = file.getAbsolutePath();
		Document xmlFileLoading = null;

		xmlFileLoading = getFreshConfigFile(fileBasePath);

		if (xmlFileLoading != null) {
			xmlFileLoading = checkConfigFile(xmlFileLoading);
		}
		if (xmlFileLoading == null) {
			String errorMsg = "Impossible to load XML Channel config (" + fileBasePath + ")";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		xmlFile = xmlFileLoading;
	}
	/**
	 * Check syntax file that cannot be checked by DTD.
	 * @param xmlFileChecked
	 * @return xmlFileLoading
	 */
	@SuppressWarnings("unchecked")
	private synchronized static Document checkConfigFile(Document xmlFileChecked) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("checkXmlFile()");
		}
		// Merge categoryProfilesUrl and check number of contexts + categories
		Document xmlFileLoading = xmlFileChecked;
		Element channelConfig = xmlFileLoading.getRootElement();
		List<Element> contexts = channelConfig.selectNodes("context");
		nbContexts = contexts.size();
		if (nbContexts == 0) {
			LOG.warn("No context declared in channel config (esup-lecture.xml)");
		}

		// 1. merge categoryProfilesUrls and refCategoryProfile
		for (Element context : contexts) {
			List<Node> nodes = context.selectNodes("categoryProfilesUrl|refCategoryProfile");
			for (Node node : nodes) {
				//Is a refCategoryProfile ?
				if (node.getName().equals("refCategoryProfile")) {
					//remove from context (from its current place in original XML file)
					context.remove(node);
					//add to context (at the end of new constructed context: With merged refCategoryProfile from categoryProfilesUrl)
					context.add(node);
				} else {
					String categoryProfilesUrlPath = node.valueOf("@url");
					//URL url = ChannelConfig.class.getResource(categoryProfilesUrlPath);
					String idPrefix = node.valueOf("@idPrefix");
					if ((categoryProfilesUrlPath == null) || (categoryProfilesUrlPath == "")) {
						String errorMsg = "URL of : categoryProfilesUrl with prefix " + idPrefix + " is null or empty.";
						LOG.warn(errorMsg);
					} else {
						Document categoryProfilesFile = getFreshConfigFile(categoryProfilesUrlPath);
						if (categoryProfilesFile == null) {
							String errorMsg = "Impossible to load categoryProfilesUrl " + categoryProfilesUrlPath;
							LOG.warn(errorMsg);
						} else {
							// merge one categoryProfilesUrl
							// add categoryProfile
							Element rootCategoryProfilesFile = categoryProfilesFile.getRootElement();
							// replace ids with IdPrefix + "-" + id
							List<Element> categoryProfiles = rootCategoryProfilesFile.elements();
							for (Element categoryProfile : categoryProfiles) {
								String categoryProfileId = idPrefix + "-" + categoryProfile.valueOf("@id");
								//String categoryProfileName = categoryProfile.valueOf("@name");
								categoryProfile.addAttribute("id", categoryProfileId);
								Element categoryProfileAdded = categoryProfile.createCopy();
								channelConfig.add(categoryProfileAdded);
								// delete node categoryProfilesUrl ?
								// add refCategoryProfile
								context.addElement("refCategoryProfile").addAttribute("refId", categoryProfileId);
							}
						}
					}
					//remove now unneeded categoryProfilesUrl
					context.remove(node);
				}
			}
		}
		List<Node> categoryProfiles = channelConfig.selectNodes("categoryProfile");
		nbProfiles = categoryProfiles.size();
		if (nbProfiles == 0) {
			LOG.warn("checkXmlConfig :: No managed category profile declared in channel config");
		}
		return xmlFileLoading;

	}

	/**
	 * @param configFilePath
	 * @return ret
	 */
	protected synchronized static Document getFreshConfigFile(final String configFilePath) {
		// Assign null to configFileLoaded during the loading
		Document ret = null;
		// Launch thread
		FreshXmlFileThread thread = new FreshXmlFileThread(configFilePath);

		int timeout = 0;
		try {
			thread.start();
			timeout = xmlFileTimeOut;
			thread.join(timeout);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Source launches XMLParseException";
				LOG.warn(msg);
				throw new XMLParseException(msg, e);
			}
			if (thread.isAlive()) {
				thread.interrupt();
				String msg = "configFile not loaded in " + timeout + " milliseconds";
				LOG.warn(msg);
			}
			ret = thread.getXmlFile();
		} catch (InterruptedException e) {
			String msg = "Thread getting ConfigFile interrupted";
			LOG.warn(msg);
			return null;
		} catch (IllegalThreadStateException e) {
			String msg = "Thread getting ConfigFile launches IllegalThreadStateException";
			LOG.warn(msg);
			return null;
		} catch (XMLParseException e) {
			String msg = "Thread getting Source launches XMLParseException";
			LOG.warn(msg);
			return null;
		}
		return ret;
	}

	/**
	 * Load attribute that identified guest user name (guestUser).
	 */
	protected static synchronized void loadGuestUser() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadGuestUser()");
		}
		Element root = xmlFile.getRootElement();
		String guestUser = root.valueOf("channelConfig/guestUser");
		if (guestUser == null || guestUser.equals("")) {
			guestUser = "guest";
		}
		DomainTools.setGuestUser(guestUser);
	}

	/**
	 * Load the ttl of the config file.
	 */
	protected static synchronized void loadConfigTtl() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadConfigTtl()");
		}
		//int configTtl = xmlFile.getInt("ttl", DomainTools.getConfigTtl());
		Element root = xmlFile.getRootElement();
		String configTtl = root.valueOf("channelConfig/ttl");
		if (!(configTtl == null || configTtl.equals(""))) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("loadConfigTtl() : overriding defaultConfigTtl (" + DomainTools.getConfigTtl()
						+ " with channelConfig/ttl :" + configTtl );
			}
			DomainTools.setConfigTtl(Integer.parseInt(configTtl));
		}
	}

	/**
	 * Load a DefinitionSets that is used to define visibility groups of a managed category profile.
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param categoryProfile
	 * @return the initialized DefinitionSets
	 */
	@SuppressWarnings("unchecked")
	private static synchronized DefinitionSets loadDefAndContentSets(final String fatherName, final Node categoryProfile) {
		DefinitionSets defAndContentSets = new DefinitionSets();
		// pathCategoryProfile = "categoryProfile(" + j + ")";
		// String fatherPath = pathCategoryProfile + ".visibility." + fatherName;

		String fatherPath = "visibility/" + fatherName + "/group";
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadDefAndContentSets(" + fatherName + "," + categoryProfile.valueOf("@id") + ")");
		}

		List<Node> groups = categoryProfile.selectNodes(fatherPath);
		for (Node group : groups) {
			defAndContentSets.addGroup(group.valueOf("@name"));
		}

		fatherPath = "visibility/" + fatherName + "/regular";
		List<Node> regulars = categoryProfile.selectNodes(fatherPath);
		for (Node regular : regulars) {
			RegularOfSet regularOfSet = new RegularOfSet();
			regularOfSet.setAttribute(regular.valueOf("@attribute"));
			regularOfSet.setValue(regular.valueOf("@value"));
			defAndContentSets.addRegular(regularOfSet);
		}

		fatherPath = "visibility/" + fatherName + "/regex";
		List<Node> regexs = categoryProfile.selectNodes(fatherPath);
		for (Node regex : regexs) {
			RegexOfSet regexOfSet = new RegexOfSet();
			regexOfSet.setAttribute(regex.valueOf("@attribute"));
			regexOfSet.setPattern(regex.valueOf("@pattern"));
			defAndContentSets.addRegex(regexOfSet);
		}

		return defAndContentSets;
	}



	/*
	 *********************** ACCESSORS *****************************************/

	/**
	 * Returns the relative classpath file path of the channel config.
	 * @return configFilePath
	 * @see ChannelConfig#filePath
	 */
	protected static String getfilePath() {
		return filePath;
	}

	/**
	 * Set the relative classpath file path of the channel config.
	 * @param filePath
	 * @see ChannelConfig#filePath
	 */
	protected static synchronized void setfilePath(final String filePath) {
		// TODO (GB later) sera utilisé lorsque le file sera externalisé
		LOG.debug("setFilePath(" + filePath + ")");
		ChannelConfig.filePath = filePath;
	}
	/**
	 * @return true if the channel config file has been modified since last "getInstance"
	 */
	protected static boolean isModified() {
		return modified;
	}

	/**
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	public static void loadContextsAndCategoryprofiles(final Channel channel) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadContextsAndCategoryprofiles()");
		}
		String categoryProfileId = "";
		Node channelConfig = xmlFile.getRootElement();
		List<Node> contexts = channelConfig.selectNodes("context");
		for (Node context : contexts) {
			Context c = new Context();
			c.setId(context.valueOf("@id"));
			c.setName(context.valueOf("@name"));
			//treeVisible
			String treeVisible = context.valueOf("@treeVisible");
			if (treeVisible.equals("no")) {
				c.setTreeVisible(TreeDisplayMode.NOTVISIBLE);
			} else if (treeVisible.equals("forceNo")) {
				c.setTreeVisible(TreeDisplayMode.NEVERVISIBLE);
			} else {
				c.setTreeVisible(TreeDisplayMode.VISIBLE);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("loadContextsAndCategoryprofiles() : contextId " + c.getId());
			}
			Node description = context.selectSingleNode("description");
			c.setDescription(description.getStringValue());
			List<Node> refCategoryProfiles = context.selectNodes("refCategoryProfile");

			// Lire les refCategoryProfilesUrl puis :
			// - les transformer en refCategoryProfile ds le context
			// - ajouter les categoryProfile
			// A faire dans checkXmlFile ?

			Map<String, Integer> orderedCategoryIDs =
					Collections.synchronizedMap(new HashMap<String, Integer>());
			int xmlOrder = 1;

			// On parcours les refCategoryProfile de context
			for (Node refCategoryProfile : refCategoryProfiles) {
				String refId;
				// Ajout mcp
				refId = refCategoryProfile.valueOf("@refId");
				if (LOG.isDebugEnabled()) {
					LOG.debug("loadContextsAndCategoryprofiles() : refCategoryProfileId " + refId );
				}
				List<Node> categoryProfiles = channelConfig.selectNodes("categoryProfile");
				// On parcours les categoryProfile de root
				for (Node categoryProfile : categoryProfiles) {
					categoryProfileId = categoryProfile.valueOf("@id");
					if (LOG.isDebugEnabled()) {
						LOG.debug("loadContextsAndCategoryprofiles() : is categoryProfileId " + categoryProfileId + " matching ?");
					}
					if (categoryProfileId.compareTo(refId) == 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + refId + " matches... create mcp");
						}
						ManagedCategoryProfile mcp = new ManagedCategoryProfile();
						// Id = long Id
						String mcpProfileID = categoryProfileId;
						mcp.setFileId(c.getId(), mcpProfileID);
						if (LOG.isDebugEnabled()) {
							LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + mcp.getId() + " matches... create mcp");
						}

						mcp.setEntityName(categoryProfile.valueOf("@entity"));
                        mcp.setGroupCategoryByEntity(getBoolean(categoryProfile.valueOf("@groupCategory"), false));
                        mcp.setName(categoryProfile.valueOf("@name"));
						mcp.setCategoryURL(categoryProfile.valueOf("@urlCategory"));
						mcp.setTrustCategory(getBoolean(categoryProfile.valueOf("@trustCategory"), false));
						mcp.setUserCanMarkRead(getBoolean(categoryProfile.valueOf("@userCanMarkRead"), true));
						String specificUserContentValue = categoryProfile.valueOf("@specificUserContent");
						if (specificUserContentValue.equals("yes")) {
							mcp.setSpecificUserContent(true);
						} else {
							mcp.setSpecificUserContent(false);
						}

						String ttl = categoryProfile.valueOf("@ttl");
						mcp.setTtl(Integer.parseInt(ttl));
						String timeout = categoryProfile.valueOf("@timeout");
						mcp.setTimeOut(Integer.parseInt(timeout));
						mcp.setName(categoryProfile.valueOf("@name"));

						// Accessibility
						String access = categoryProfile.valueOf("@access");
						if (access.equalsIgnoreCase("public")) {
							mcp.setAccess(Accessibility.PUBLIC);
						} else if (access.equalsIgnoreCase("cas")) {
							mcp.setAccess(Accessibility.CAS);
						}
						// Visibility
						VisibilitySets visibilitySets = new VisibilitySets();
						// foreach (allowed / autoSubscribed / Obliged
						visibilitySets.setAllowed(loadDefAndContentSets("allowed", categoryProfile));
						visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed", categoryProfile));
						visibilitySets.setObliged(loadDefAndContentSets("obliged", categoryProfile));
						mcp.setVisibility(visibilitySets);

						channel.addManagedCategoryProfile(mcp);
						c.addRefIdManagedCategoryProfile(mcp.getId());
						orderedCategoryIDs.put(mcp.getId(), xmlOrder);

						break;
					}
				}
				xmlOrder += 1;
			}
			c.setOrderedCategoryIDs(orderedCategoryIDs);
			channel.addContext(c);
		}
	}

	/**
	 * @param valueOf
	 * @param b
	 * @return boolean
	 */
	private static boolean getBoolean(String valueOf, boolean b) {
		boolean ret = b;
		if (valueOf != null) {
			if ((valueOf.equalsIgnoreCase("true")) || (valueOf.equalsIgnoreCase("yes"))) {
				ret = true;
			} else if ((valueOf.equalsIgnoreCase("false")) || (valueOf.equalsIgnoreCase("no"))) {
				ret = false;
			}
		}
		return ret;
	}


}
