/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.array.performance;

import org.diirt.util.array.CollectionNumber;
import org.diirt.util.array.IteratorNumber;

/**
 *
 * @author carcassi
 */
public abstract class ArrayOperation {
    public abstract double compute(CollectionNumber collection);

    public static final ArrayOperation average = new ArrayOperation() {

        @Override
        public double compute(CollectionNumber collection) {
            IteratorNumber iter = collection.iterator();
            double sum = 0;
            while (iter.hasNext()) {
                sum += iter.nextDouble();
            }
            sum /= collection.size();
            return sum;
        }
    };

    public static final ArrayOperation sum = new ArrayOperation() {

        @Override
        public double compute(CollectionNumber collection) {
            IteratorNumber iter = collection.iterator();
            double sum = 0;
            while (iter.hasNext()) {
                sum += iter.nextDouble();
            }
            return sum;
        }
    };



}
