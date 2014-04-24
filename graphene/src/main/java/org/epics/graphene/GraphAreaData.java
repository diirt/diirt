/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene;

import java.util.List;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;

/**
 *
 * @author carcassi
 */
class GraphAreaData {
    ListInt xReferencePixels;
    ListDouble xReferenceValues;
    List<String> xReferenceLabels;
    
    int xGraphLeft;
    int xGraphRight;
    int yGraphTop;
    int yGraphBottom;
}
