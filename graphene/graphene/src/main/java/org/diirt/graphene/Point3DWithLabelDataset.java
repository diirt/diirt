/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Statistics;
import java.util.List;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Range;

/**
 *
 * @author carcassi
 */
public interface Point3DWithLabelDataset {

    public ListNumber getXValues();

    public ListNumber getYValues();

    public ListNumber getZValues();

    public List<String> getLabels();

    public Statistics getXStatistics();

    public Statistics getYStatistics();

    public Statistics getZStatistics();

    public Range getXDisplayRange();

    public Range getYDisplayRange();

    public Range getZDisplayRange();

    public int getCount();

}
