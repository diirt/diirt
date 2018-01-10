/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Given a channel name, it translates it to a channel or formula. This allows
 * the server to provide aliases for actual channels or formulas.
 *
 * @author carcassi
 */
public abstract class ChannelTranslator {

    /**
     * Translates the channel name to the actual channel or formula to connect.
     *
     * @param channel the incoming channel name
     * @return the translation; null if the translator can't provide one
     */
    public ChannelTranslation translate(String channel) {
        return translate(new ChannelRequest(channel));
    }

    /**
     * Translates the channel request to the actual channel or formula to connect.
     *
     * @param request the incoming channel request
     * @return the translation; null if the translator can't provide one
     */
    public abstract ChannelTranslation translate(ChannelRequest request);

    /**
     * Creates a channel translator that uses the regex to match the channel and
     * the optional substitution string to perform the translation.
     *
     * @param regex a regular expression
     * @param substitution the substitution string; can be null
     * @param permission the permission to enforce on the channel
     * @return the translator
     */
    public static ChannelTranslator regexTranslator(String regex, String substitution, ChannelTranslation.Permission permission) {
        return new RegexChannelTranslator(regex, substitution, permission);
    }

    /**
     * Creates a channel translator that uses the regex to match the channel and
     * the optional substitution string to perform the translation.
     *
     * @param regex a regular expression
     * @param substitution the substitution string; can be null
     * @param permission the permission to enforce on the channel
     * @param allowedUsers the users allowed to access the channel
     * @return the translator
     */
    public static ChannelTranslator regexTranslator(String regex, String substitution, ChannelTranslation.Permission permission, Collection<String> allowedUsers) {
        return new RegexChannelTranslator(regex, substitution, permission, allowedUsers);
    }

    /**
     * Creates a channel translator that returns the first successful match
     * from the list of give translators.
     *
     * @param translators a list of translators
     * @return the combined translator
     */
    public static ChannelTranslator compositeTranslator(List<ChannelTranslator> translators) {
        return new CompositeChannelTranslator(translators);
    }

    public static ChannelTranslator loadTranslator(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String ver = xPath.evaluate("/mappings/@version", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }

            List<ChannelTranslator> translators = new ArrayList<>();

            NodeList mappings = (NodeList) xPath.evaluate("/mappings/mapping", document, XPathConstants.NODESET);
            for (int i = 0; i < mappings.getLength(); i++) {
                Node method = mappings.item(i);
                String regex = xPath.evaluate("@channel", method);
                Node substitutionNode = method.getAttributes().getNamedItem("substitution");
                String substitution = null;
                if (substitutionNode != null) {
                    substitution = substitutionNode.getNodeValue();
                }

                Node userNode = method.getAttributes().getNamedItem("user");
                List<String> allowedUsers = null;
                if (userNode != null) {
                    allowedUsers = new ArrayList<>();
                    for (String token : userNode.getNodeValue().split(",")) {
                        allowedUsers.add(token.trim());
                    }
                }

                String permissionName = xPath.evaluate("@permission", method);
                ChannelTranslation.Permission permission = ChannelTranslation.Permission.valueOf(permissionName);

                translators.add(regexTranslator(regex, substitution, permission, allowedUsers));
            }

            return compositeTranslator(translators);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(ChannelTranslator.class.getName()).log(Level.FINEST, "Couldn't load mapping", ex);
            throw new IllegalArgumentException("Couldn't load mapping", ex);
        }
    }
}
