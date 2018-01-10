/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.settings;

/**
 * Representation of an object that can be outputted by some or all
 * of its data members.
 *
 * @author asbarber
 */
public interface Settings {

    /**
     * List of headers for the data members.
     * @return header data fields
     */
    public Object[] getTitle();

    /**
     * List of headers for the data members.
     * @return header data fields
     */
    public Object[] getOutput();
}
