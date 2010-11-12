/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * XML parse for the Replay function.
 *
 * @author carcassi
 */
class ReplayParser {

    private ReplayParser() {
        // Avoid construction
    }

    /**
     * Reads the XML file located at the given uri.
     *
     * @param uri local url for a local file, or absolute uri for any other protocol
     * @return the parsed file
     */
    static XmlValues parse(URI uri) {
        // If relative, resolve it in the current directory
        if (!uri.isAbsolute()) {
            File current = new File(".");
            uri = current.toURI().resolve(uri);
        }
        
        InputStream in = null;
        try {
            JAXBContext jaxbCtx = JAXBContext.newInstance(XmlValues.class);
            Unmarshaller reader = jaxbCtx.createUnmarshaller();
            XmlValues values = (XmlValues) reader.unmarshal(uri.toURL());

            // Adjust all values by using the previous as default
            ReplayValue previousValue = null;
            for (ReplayValue newValue : values.getValues()) {
                if (previousValue == null) {
                    previousValue = newValue;
                } else {
                    newValue.updateNullValues(previousValue);
                }
            }
            return values;
        } catch (Exception ex) {
            throw new RuntimeException("Can't parse file", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    // Can't do anything anyway...
                }
            }
        }
    }

}
