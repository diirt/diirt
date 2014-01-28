/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.util.text;

/**
 * Configuration options for the CSV parser
 *
 * @author carcassi
 */
public class CsvParserConfiguration {
    private String separators = ",;\t ";
    private boolean autoTimestamps = true;

    public String getSeparators() {
        return separators;
    }

    public boolean isAutoTimestamps() {
        return autoTimestamps;
    }
    
}
