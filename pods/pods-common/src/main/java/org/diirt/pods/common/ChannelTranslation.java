/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
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
    public enum Permission {READ_WRITE, READ_ONLY, NONE};

    private final String formula;
    private final Permission permission;

    ChannelTranslation(String formula, Permission permission) {
        this.formula = formula;
        this.permission = permission;
    }

    public String getFormula() {
        return formula;
    }

    public Permission getPermission() {
        return permission;
    }

}
