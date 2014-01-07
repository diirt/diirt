/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class ValueScales {
    public static ValueScale linearScale() {
        return new LinearValueScale();
    }
    
    public static ValueScale logScale() {
        return new LogValueScale();
    }
}
