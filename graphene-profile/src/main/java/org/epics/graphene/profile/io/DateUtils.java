/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.io;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static enum DateFormat{DELIMITED, NONDELIMITED};
    
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
