/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.Writer;
import javax.json.JsonObject;

/**
 *
 * @author carcassi
 */
public class MessageResume extends Message {

    public MessageResume(JsonObject obj) {
        super(obj);
    }

    public MessageResume(int id) {
        super(MessageType.RESUME, id);
    }
    
    @Override
    public void toJson(Writer writer) {
        basicToJson(writer);
    }
    
}
