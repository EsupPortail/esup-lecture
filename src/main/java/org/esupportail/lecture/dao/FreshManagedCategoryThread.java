package org.esupportail.lecture.dao;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.exceptions.dao.XMLParseException;

/**
 * Get a Freash Managed Category from a distinct Thread.
 * 
 * @author bourges
 */
public class FreshManagedCategoryThread extends Thread {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(FreshManagedCategoryThread.class);
	/**
	 * ManagedCategory to return by this Thread.
	 */
	private ManagedCategory managedCategory;
	/**
	 * ManagedCategoryProfile used to return a ManagedCategory.
	 */
	private ManagedCategoryProfile managedCategoryProfile;
	/**
	 * Exception generated in this Thread.
	 */
	private Exception exception;
	/**
	 * Proxy ticket CAS null for anonymous access.
	 */
	private String ptCas;

	/**
	 * HttpClient to make request
	 */
	private HttpClient httpClient;

	/**
	 * Constructor.
	 * 
	 * @param profile
	 *            used to return a ManagedCategory
	 * @param ptCas
	 *            - user and password. null for anonymous access
	 */
	public FreshManagedCategoryThread(final ManagedCategoryProfile profile, final String ptCas, final HttpClient httpClient) {
		this.managedCategoryProfile = profile;
		this.exception = null;
		this.ptCas = ptCas;
		this.httpClient = httpClient;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.managedCategory = getFreshManagedCategory(managedCategoryProfile, ptCas);
		} catch (XMLParseException e) {
			this.exception = e;
		}
	}

	/**
	 * get a managed category from the web without cache.
	 * 
	 * @param profile
	 *            ManagedCategoryProfile of Managed category to get
	 * @param ptCasGet
	 *            - user and password. null for anonymous access
	 * @return Managed category
	 * @throws XMLParseException
	 */
	@SuppressWarnings("unchecked")
	private ManagedCategory getFreshManagedCategory(final ManagedCategoryProfile profile, final String ptCasGet)
			throws XMLParseException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getFreshManagedCategory");
		}
		// pour gerer le nouveau paramétrage
		if (profile.isFromPublisher()) {
			return this.getFreshManagedCategoryActus(profile, ptCasGet);
		}
		// TODO (RB <-- GB) gestion des attributs xml IMPLIED
		ManagedCategory ret = new ManagedCategory(profile);
		String categoryURL = null;
		try {
			// get the XML
			categoryURL = profile.getCategoryURL();
			if (ptCasGet != null) {
				if (categoryURL.contains("?")) {
					categoryURL = categoryURL + "&ticket=" + ptCasGet;
				} else {
					categoryURL = categoryURL + "?ticket=" + ptCasGet;
				}
			}

			SAXReader reader = new SAXReader();
			Document document;
			if (categoryURL.startsWith("http")) {
				HttpGet getRequest = new HttpGet(categoryURL);
				getRequest.setHeader("accept", "application/xml");
				HttpResponse response = httpClient.execute(getRequest);
				document = reader.read(response.getEntity().getContent());
			} else {
				 document = reader.read(categoryURL);
			}
			Element root = document.getRootElement();
			// Category properties
			ret.setName(root.valueOf("@name"));
			ret.setDescription(root.valueOf("/category/description"));
			// GB : devenu inutile
			// ret.setProfileId(profile.getId());
			// SourceProfiles loop
			Hashtable<String, SourceProfile> sourceProfiles = new Hashtable<String, SourceProfile>();
			Map<String, Integer> orderedSourceIDs = Collections.synchronizedMap(new HashMap<String, Integer>());
			List<Node> srcProfiles = root.selectNodes("/category/sourceProfiles/sourceProfile");
			int xmlOrder = 1;
			for (Node srcProfile : srcProfiles) {
				ManagedSourceProfile sp = new ManagedSourceProfile(ret);
				String srcProfileID = srcProfile.valueOf("@id");
				sp.setFileId(srcProfileID);
				sp.setName(srcProfile.valueOf("@name"));
				sp.setSourceURL(srcProfile.valueOf("@url"));
				String srcTtl = srcProfile.valueOf("@ttl");
				if (!(srcTtl.equals(""))) {
					sp.setTtl(Integer.parseInt(srcTtl));
				}
				// use of caculated sp.getId() as key for orderedSourceIDs
				orderedSourceIDs.put(sp.getId(), xmlOrder);
				xmlOrder += 1;
				String timeout = srcProfile.valueOf("@timeout");
				if (!(timeout.equals(""))) {
					sp.setTimeOut(Integer.parseInt(timeout));
					if (LOG.isTraceEnabled()) {
						LOG.trace("1 getFreshManagedCategory : " + "first vcalue of timeout (string) : " + timeout);
						LOG.trace("2 getFreshManagedCategory : " + "value of timeout in xml :"
								+ srcProfile.valueOf("@timeout"));
						LOG.trace("3 getFreshManagedCategory : " + "value of timeout in Integer :"
								+ Integer.parseInt(srcProfile.valueOf("@timeout")));
					}
				} else {
					LOG.trace("4 getFreshManagedCategory : timeout (string) is empty");
				}
				String specificUserContentValue = srcProfile.valueOf("@specificUserContent");
				if (profile.isSpecificUserContent() || specificUserContentValue.equals("yes")) {
					sp.setSpecificUserContent(true);
				} else {
					sp.setSpecificUserContent(false);
				}
				sp.setXsltURL(srcProfile.valueOf("@xslt"));
				sp.setMobileXsltURL(srcProfile.valueOf("@mobileXslt"));
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
				visibilitySets
						.setAllowed(XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/allowed")));
				visibilitySets
						.setObliged(XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/obliged")));
				visibilitySets.setAutoSubscribed(
						XMLUtil.loadDefAndContentSets(srcProfile.selectSingleNode("visibility/autoSubscribed")));
				sp.setVisibility(visibilitySets);
				sourceProfiles.put(sp.getId(), sp);
			}
			ret.setSourceProfilesHash(sourceProfiles);
			ret.setOrderedSourceIDs(orderedSourceIDs);
			// Category visibility
			VisibilitySets visibilitySets = new VisibilitySets();
			// foreach (allowed / autoSubscribed / Obliged)
			visibilitySets
					.setAllowed(XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/allowed")));
			visibilitySets
					.setObliged(XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/obliged")));
			visibilitySets.setAutoSubscribed(
					XMLUtil.loadDefAndContentSets(root.selectSingleNode("/category/visibility/autoSubscribed")));
			ret.setVisibility(visibilitySets);
		} catch (IOException e) {
			String profileId = "null";
			if (profile != null) {
				profileId = profile.getId();
			}
			String msg = "getFreshManagedCategory(" + profileId + "). Error on requesting url " + categoryURL + "'";
			LOG.error(msg, e);
			throw new XMLParseException(msg, e);
		} catch (DocumentException e) {
			String profileId = "null";
			if (profile != null) {
				profileId = profile.getId();
			}
			String msg = "getFreshManagedCategory(" + profileId + "). Can't read configuration file.";
			LOG.error(msg, e);
			throw new XMLParseException(msg, e);
		}
		return ret;
	}

	private ManagedCategory getFreshManagedCategoryActus(ManagedCategoryProfile profile, String ptCasGet) {
		ManagedCategory ret = new ManagedCategory(profile);

		String actualiteURL = profile.getUrlActualites();
		SAXReader reader = new SAXReader();
		try {
			ret.setName("Category-Publisher-" + profile.getId());
			ret.setDescription("Publisher category n° " + profile.getId());
			Hashtable<String, SourceProfile> sourceProfiles = new Hashtable<String, SourceProfile>();
			Map<String, Integer> orderedSourceIDs = Collections.synchronizedMap(new HashMap<String, Integer>());
			Document doc = reader.read(actualiteURL);
			Element root = doc.getRootElement();
			List<Node> rubriquesNodes = root.selectNodes("/actualites/rubriques/rubrique");
			int xmlOrder = 1;
			for (Node node : rubriquesNodes) {
				RubriquesSourceProfile sp = new RubriquesSourceProfile(ret);
				sp.setName(node.selectSingleNode("name").getText());
				sp.setColor(node.selectSingleNode("color").getText());
				sp.setHighLight(false);
				if (node.selectSingleNode("highlight").getText().equals("true")) {
					sp.setHighLight(true);
				}
				sp.setUuid(Integer.valueOf(node.selectSingleNode("uuid").getText()));
				sp.setSourceURL(profile.getUrlActualites() + "#" + sp.getUuid());
				sp.setFileId(sp.getName().trim() + sp.getUuid());
				sp.setComplexItems(true);
				sp.setItemXPath(node.valueOf("@itemXPath"));
				if (sp.isHighLight()) {
					for (Map.Entry<String, Integer> entry : orderedSourceIDs.entrySet()) {
						entry.setValue(entry.getValue() + 1);
					}
					orderedSourceIDs.put(sp.getId(), 1);
					xmlOrder += 1;
				} else {
					orderedSourceIDs.put(sp.getId(), xmlOrder);
				}
				xmlOrder += 1;
				sourceProfiles.put(sp.getId(), sp);

			}
			ret.setSourceProfilesHash(sourceProfiles);
			ret.setOrderedSourceIDs(orderedSourceIDs);
		} catch (DocumentException e) {
			LOG.error("error reading document " + e);
		}
		return ret;
	}

	/**
	 * @return managedCategory
	 */
	public ManagedCategory getManagedCategory() {
		return managedCategory;
	}

	/**
	 * @return exception thowed during run
	 */
	public Exception getException() {
		return exception;
	}

}
