/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import java.util.List;
import org.diirt.util.array.CollectionNumbers;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListNumber;

/**
 * Multi dimensional array, which can be used for waveforms or more rich data.
 * <p>
 * The data is stored in a linear structure. The sizes array gives the dimensionality
 * and size for each dimension. The ordering defined by the sizes is {@code [..., zSize, ySize, xSize]}.
 * Typical iteration is:
 * <blockquote><pre>
 * for (...) {
 *   for (int z = 0; z &lt; zSize; z++) {
 *     for (int y = 0; y &lt; ySize; y++) {
 *       for (int x = 0; x &lt; xSize; x++) {
 *          array.getData().getDouble(... + z*ySize + y*xSize + x);
 *       }
 *     }
 *   }
 * }</pre></blockquote>
 *
 * @author carcassi
 */
public abstract class Array extends VType {

    /**
     * Return the object containing the array data.
     * <p>
     * This method will either return a {@link List} or a {@link ListNumber}
     * depending of the array type. A collection is returned, instead of an
     * array, so that the type implementation can be immutable or can at
     * least try to prevent modifications. ListNumber has also several
     * advantages over the Java arrays, including the ability to iterate the list
     * regardless of numeric type.
     * <p>
     * If a numeric array is actually needed, refer to {@link CollectionNumbers}.
     *
     * @return the array data
     */
    public abstract Object getData();

    /**
     * The shape of the multidimensional array.
     * <p>
     * The size of the returned list will be the number of the dimension of the array.
     * Each number represents the size of each dimension. The total number
     * of elements in the array is therefore the product of all the
     * numbers in the list returned.
     *
     * @return the dimension sizes
     */
    public abstract ListInt getSizes();
}
