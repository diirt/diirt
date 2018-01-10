/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles date and time operations.
 *
 * @author asbarber
 */
public final class DateUtils {

    /**
     * Prevents instantiation.
     */
    private DateUtils(){}

    /**
     * Format of a date, either with delimiting characters or without.
     */
    public static enum DateFormat{
        /**
         * yyyy/MM/dd HH:mm:ss delimiting of a date
         */
        DELIMITED,

        /**
         * yyyyMMddHHmmss non-delimiting of a date
         */
        NONDELIMITED
    };

    /**
     * Gets the current date specified by the given format.
     * @param format style to format the date
     * @return the current date in the specified format
     */
    public static String getDate(DateFormat format){
        SimpleDateFormat dateFormat;
        switch(format){
            case DELIMITED: dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");   break;
            case NONDELIMITED: dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");     break;
            default: return "";
        }

        return dateFormat.format(new Date());
    }

}
