/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

/**
 * The output of the translation: the outgoing channel/formula plus other
 * connection information.
 *
 * @author carcassi
 */
public class ChannelTranslation {
    private final String formula;
    private final boolean readOnly;

    ChannelTranslation(String formula, boolean readOnly) {
        this.formula = formula;
        this.readOnly = readOnly;
    }

    public String getFormula() {
        return formula;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
    
}
