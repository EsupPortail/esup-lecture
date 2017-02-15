package org.esupportail.lecture.domain.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.dao.RubriquesSourceProfile;
import org.esupportail.lecture.dao.XMLUtil;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

public class ItemParser {

	protected static final Log LOG = LogFactory.getLog(SourceProfile.class);
	private String XMLStream;
	private Source source;
	private ArrayList<ComplexItem> visibleItems;
	private ArrayList<RubriquesPlublisher> rubriquesPublisher;
	private Map<String, Author> itemAuth;
	private Map<String, List<RubriquesPlublisher>> rubriquesItem;
	private String itemXpath;

	public ItemParser(Source s) {
		this.setSource(s);
	}

	private void produceXMLStream() {
		String stuff = "";
		stuff += this.startTag();
		if (!this.visibleItems.isEmpty()) {
			for (ComplexItem complexItem : visibleItems) {
				stuff += complexItem.getHtmlContent();
			}
		}
		stuff += this.endTag();
		this.setXMLStream(stuff);

	}

	private VisibilityMode evaluateVisibility(ComplexItem ci) {
		if (ci.getVisibility() == null) {
			return	VisibilityMode.ALLOWED ;
		}
		VisibilitySets visibilitySets = ci.getVisibility();

		VisibilityMode mode = VisibilityMode.NOVISIBLE;

		mode = visibilitySets.whichVisibility();

		return mode;
	}
	private String startTag(){
		String stuff = "";
		stuff += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String [] arrayofPath = this.itemXpath.split("/");
		for (int i = 0; i < arrayofPath.length -1; i++) {
			if (!arrayofPath[i].equals("")){
				stuff += "<"+arrayofPath[i]+">";
			}
		}
		return stuff; 
	}
	
	private String endTag(){
		String stuff = "";
		String [] arrayofPath = this.itemXpath.split("/");
		for (int i = arrayofPath.length -2; i > 0; i--) {
			if (!arrayofPath[i].equals("")){
				stuff += "</"+arrayofPath[i]+">";
			}
		}
		return stuff; 
	}
	private void parseItemsXML() {
		ArrayList<ComplexItem> ret = new ArrayList<ComplexItem>();
		ArrayList<RubriquesPlublisher> rubpub = new ArrayList<RubriquesPlublisher>();
		this.itemAuth = new HashMap<String, Author>();
		this.rubriquesItem = new HashMap<String, List<RubriquesPlublisher>>();
		try {
			// get the XML
			String categoryURL = this.source.getProfile().getSourceURL();
			SAXReader reader = new SAXReader();
			Document document = reader.read(categoryURL);
			Element root = document.getRootElement();
			List<Node> items = root.selectNodes(this.itemXpath);
			for (Node item : items) {
				ComplexItem sp = new ComplexItem(this.source);
				VisibilitySets visibilitySets = new VisibilitySets();
				Node visibilityNode = item.selectSingleNode("visibility/obliged");
				if(visibilityNode != null){
					visibilitySets.setObliged(XMLUtil.loadDefAndContentSets(visibilityNode));
					sp.setVisibility(visibilitySets);
				}
				sp.setHtmlContent(item.asXML());
				String xmlAsString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";//<item>";
				xmlAsString += sp.getHtmlContent();
				byte[] hash = null;
				try {
					hash = MessageDigest.getInstance("MD5").digest(xmlAsString.getBytes());
				} catch (NoSuchAlgorithmException e) {
					LOG.error("error in get hash : " + e.getLocalizedMessage());
				}
				StringBuffer hashString = new StringBuffer();
				for (int i = 0; i < hash.length; ++i) {
					String hex = Integer.toHexString(hash[i]);
					if (hex.length() == 1) {
						hashString.append('0');
						hashString.append(hex.charAt(hex.length() - 1));
					} else {
						hashString.append(hex.substring(hex.length() - 2));
					}
				}
				if (this.evaluateVisibility(sp) != VisibilityMode.NOVISIBLE) {
					List<Node> uuidNodes = item.selectNodes("rubriques/uuid");
					ArrayList<Integer> theUuid = new ArrayList<Integer>();
					List<Node> rubriquesNode = root.selectNodes("/actualites/rubriques/rubrique");
					for (Node uuid : uuidNodes) {
						theUuid.add(Integer.valueOf(uuid.getText()));
						for (Node rubriqueNode : rubriquesNode) {

							if (uuid.getText().equals(rubriqueNode.selectSingleNode("uuid").getText())) {
								String name = rubriqueNode.selectSingleNode("name").getText();
								int uuidNode = Integer.valueOf(rubriqueNode.selectSingleNode("uuid").getText());
								String color = rubriqueNode.selectSingleNode("color").getText();
								String highlightText = rubriqueNode.selectSingleNode("highlight").getText();
								boolean highlight = false;
								if (highlightText.equals("true")) {
									highlight = true;
								}
								RubriquesPlublisher rub = new RubriquesPlublisher(name, uuidNode, color, highlight);
								if (this.getRubriquesItem().get(hashString.toString()) == null) {
									this.getRubriquesItem().put(hashString.toString(),
											new ArrayList<RubriquesPlublisher>());
								}
								this.getRubriquesItem().get(hashString.toString()).add(rub);
							}
						}
						if (this.source.getProfile() instanceof RubriquesSourceProfile) {
							RubriquesSourceProfile profile = (RubriquesSourceProfile) this.source.getProfile();
							if (profile.getUuid() == (Integer.valueOf(uuid.getText()))) {
								ret.add(sp);
							}
						} 
					}
					Node authorNode = item.selectSingleNode("creator");
					Node authorNameNode = item.selectSingleNode("article/dc:creator");
					Author auth = new Author(authorNode.getText(), authorNameNode.getText());
					ExternalService ex = DomainTools.getExternalService();
					if (ex.getUserAttribute("uid").get(0).equals(authorNode.getText())){
						auth.setUserArticleAuthor(true);
					}
					this.itemAuth.put(hashString.toString(), auth);

				}
			}
		} catch (DocumentException e) {
			String msg = "parseItemsXML(). Error parsing stuffs.";
			LOG.error(msg, e);
		} catch (NoExternalValueException e) {
			String msg = "parseItemsXML(). No External Value .";
			LOG.error(msg, e);
		} catch (InternalExternalException e) {
			String msg = "parseItemsXML(). Internal External Exception.";
			LOG.error(msg, e);
		}
		this.setVisibleItems(ret);
		this.setRubriquesPublisher(rubpub);
	}

	private void setVisibleItems(ArrayList<ComplexItem> ret) {
		this.visibleItems = ret;

	}

	public ArrayList<ComplexItem> getItems() {
		return this.visibleItems;
	}

	public String getXMLStream() {
		this.parseItemsXML();
		this.produceXMLStream();
		return this.XMLStream;
	}

	public void setXMLStream(String xMLStream) {
		XMLStream = xMLStream;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public ArrayList<RubriquesPlublisher> getRubriques() {
		return this.rubriquesPublisher;
	}

	public void setRubriquesPublisher(ArrayList<RubriquesPlublisher> rubs) {
		this.rubriquesPublisher = rubs;
	}

	public Map<String, Author> getItemAuth() {
		return itemAuth;
	}

	public void setItemAuth(Map<String, Author> itemAuth) {
		this.itemAuth = itemAuth;
	}

	public Map<String, List<RubriquesPlublisher>> getRubriquesItem() {
		return rubriquesItem;
	}

	public void setRubriquesItem(Map<String, List<RubriquesPlublisher>> rubriquesItem) {
		this.rubriquesItem = rubriquesItem;
	}

	public String getItemXpath() {
		return itemXpath;
	}

	public void setItemXpath(String itemXpath) {
		this.itemXpath = itemXpath;
	}

}
