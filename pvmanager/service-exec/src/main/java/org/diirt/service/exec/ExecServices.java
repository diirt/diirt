/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.diirt.service.Service;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class to create ExecServices.
 *
 * @author carcassi
 */
public class ExecServices {

    private ExecServices() {
        // Prevent instantiation
    }

    /**
     * Creates a service with exec arguments based on the description of an XML
     * file.
     *
     * @param input a stream with an xml file; can't be null
     * @return the new service for exec commands
     */
    public static Service createFromXml(InputStream input) {
        if (input == null){
            throw new IllegalArgumentException("Input must not be null");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String ver = xPath.evaluate("/execService/@ver", document);
            String serviceName = xPath.evaluate("/execService/@name", document);
            String serviceDesecription = xPath.evaluate("/execService/@description", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }

            ExecServiceDescription service = new ExecServiceDescription(serviceName, serviceDesecription);
            service.executorService(GenericExecService.defaultExecutor);

            NodeList methods = (NodeList) xPath.evaluate("/execService/methods/method", document, XPathConstants.NODESET);
            for (int i = 0; i < methods.getLength(); i++) {
                Node method = methods.item(i);
                String methodName = xPath.evaluate("@name", method);
                String methodDescription = xPath.evaluate("@description", method);
                String command = xPath.evaluate("command", method);
                String resultName = xPath.evaluate("result/@name", method);
                String resultDescription = xPath.evaluate("result/@description", method);

                ExecServiceMethodDescription execMethod = new ExecServiceMethodDescription(methodName, methodDescription);
                execMethod.command(command);
                if (!resultName.trim().isEmpty()) {
                    execMethod.queryResult(resultName, resultDescription);
                }

                NodeList arguments = (NodeList) xPath.evaluate("argument", method, XPathConstants.NODESET);
                for (int nArg = 0; nArg < arguments.getLength(); nArg++) {
                    Node argument = arguments.item(nArg);
                    String argName = xPath.evaluate("@name", argument);
                    String argDescription = xPath.evaluate("@description", argument);
                    String argType = xPath.evaluate("@type", argument);
                    Class<?> argClass = null;
                    switch(argType) {
                        case "VNumber": argClass = VNumber.class;
                            break;
                        case "VString": argClass = VString.class;
                            break;
                        default: throw new IllegalArgumentException("Type " + argType + " not supported.");
                    }
                    if (!argName.trim().isEmpty()) {
                        execMethod.addArgument(argName, argDescription, argClass);
                    }
                }
                service.addServiceMethod(execMethod);
            }

            return service.createService();
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(ExecServices.class.getName()).log(Level.FINEST, "Couldn't create service", ex);
            throw new IllegalArgumentException("Couldn't create service", ex);
        }
    }
}
