/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.diirt.util.array.IteratorDouble;
import org.diirt.util.array.IteratorNumber;

/**
 *
 * @author carcassi
 */
public class Iterators {

    /**
     *Creates a pointer for the given data at index 0.
     * @param data double[]
     * @return IteratorDouble, pointer to the data at index 0.
     */
    public static IteratorDouble arrayIterator(final double[] data) {
        return new IteratorDouble() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public double nextDouble() {
                if (!hasNext())
                    throw new NoSuchElementException();
                double value = data[index];
                index++;
                return value;
            }
        };
    }

    /**
     *Takes a list of IteratorDoubles and combines them into one IteratorDouble.
     * @param iterators the iterators to combine
     * @return the combined iterators
     */
    public static IteratorDouble combine(final Collection<IteratorNumber> iterators) {
        return new IteratorDouble() {

            private IteratorNumber currentIterator = null;
            private Iterator<IteratorNumber> iterator = iterators.iterator();

            @Override
            public boolean hasNext() {
                if (currentIterator != null && currentIterator.hasNext()) {
                    return true;
                }

                if (iterator.hasNext()) {
                    currentIterator = iterator.next();
                    return hasNext();
                }

                return false;
            }

            @Override
            public double nextDouble() {
                return currentIterator.nextDouble();
            }
        };
    }
}
