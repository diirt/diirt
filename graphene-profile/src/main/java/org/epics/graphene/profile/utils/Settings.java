/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.utils;

public interface Settings {
    /**
     * Quote delimiter for a .CSV formatted output file.
     */
    public static final String QUOTE = "\"";
    
    /**
     * Comma delimiter for a .CSV formatted output file.
     */
    public static final String DELIM = ",";
    
    public String getTitle();
    
    public String getOutput();
}
