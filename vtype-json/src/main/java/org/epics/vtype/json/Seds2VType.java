/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.vtype.json;

import javax.json.JsonObject;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VType;

/**
 * 
 * @author carcassi
 */
public class Seds2VType {

    public static VType toVType(JsonObject json) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public static JsonObject toJson(VType vType) {
        if (vType instanceof VNumber) {
            return toJson((VNumber) vType);
        } else if (vType instanceof VBoolean) {
            return toJson((VBoolean) vType);
        } else if (vType instanceof VString) {
            return toJson((VString) vType);
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    static JsonObject toJson(VNumber vNumber) {
        return new JsonVTypeBuilder()
                .addObject("value", vNumber.getValue())
                .addAlarm(vNumber)
                .addTime(vNumber)
                .addDisplay(vNumber)
                .build();
    }
    
    static JsonObject toJson(VBoolean vBoolean) {
        return new JsonVTypeBuilder()
                .add("value", vBoolean.getValue())
                .addAlarm(vBoolean)
                .addTime(vBoolean)
                .build();
    }
    
    static JsonObject toJson(VString vString) {
        return new JsonVTypeBuilder()
                .add("value", vString.getValue())
                .addAlarm(vString)
                .addTime(vString)
                .build();
    }
}
