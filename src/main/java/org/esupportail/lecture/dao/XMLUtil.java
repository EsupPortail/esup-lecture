package org.esupportail.lecture.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Node;
import org.esupportail.lecture.domain.model.DefinitionSets;
import org.esupportail.lecture.domain.model.RegexOfSet;
import org.esupportail.lecture.domain.model.RegularOfSet;

import java.util.List;

/**
 * Collection of statis method to parse xml file used by ESUP-Lecture.
 * @author bourges
 */
public class XMLUtil {
    /**
     * Log instance.
     */
    private static final Log LOG = LogFactory.getLog(XMLUtil.class);

    /**
     * return DefinitionSets from a dom4j node.
     * @param node
     * @return DefinitionSets the this part of XML
     */
    @SuppressWarnings("unchecked")
    public static DefinitionSets loadDefAndContentSets(final Node node) {
        if (LOG.isDebugEnabled()) {
            String path = "null";
            if (node != null) {
                path = node.getPath();
            }
            LOG.debug("loadDefAndContentSets(" + path + ")");
        }
        DefinitionSets defAndContentSets = new DefinitionSets();
        if (node != null) {
            // Definition by group enumeration
            List<Node> groups = node.selectNodes("group");
            for (Node group : groups) {
                String name = group.valueOf("@name");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("name = " + name);
                }
                defAndContentSets.addGroup(name);
            }
            // Definition by regular
            List<Node> regulars = node.selectNodes("regular");
            for (Node regular : regulars) {
                String attribute = regular.valueOf("@attribute");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("attribute = " + attribute);
                }
                String value = regular.valueOf("@value");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("value = " + value);
                }
                RegularOfSet regularOfSet = new RegularOfSet();
                regularOfSet.setAttribute(attribute);
                regularOfSet.setValue(value);
                defAndContentSets.addRegular(regularOfSet);
            }
            // Definition by regex
            List<Node> regexs = node.selectNodes("regex");
            for (Node regex : regexs) {
                String attribute = regex.valueOf("@attribute");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("attribute = " + attribute);
                }
                String pattern = regex.valueOf("@pattern");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("pattern = " + pattern);
                }
                RegexOfSet regexOfSet = new RegexOfSet();
                regexOfSet.setAttribute(attribute);
                regexOfSet.setPattern(pattern);
                defAndContentSets.addRegex(regexOfSet);
            }
            //Defintion by guest
            List<Node> guest = node.selectNodes("guest");
            if (!guest.isEmpty()) {
                defAndContentSets.addGuest();
            }
        }
        return defAndContentSets;
    }
}