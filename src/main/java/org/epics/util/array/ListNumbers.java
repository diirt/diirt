/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public class ListNumbers {
    
    public static SortedListView sortedView(ListNumber values) {
        SortedListView view = new SortedListView(values);
        SortedListView.quicksort(view);
        return view;
    }
    
    public static SortedListView sortedView(ListNumber values, ListInt indexes) {
        SortedListView view = new SortedListView(values, indexes);
        return view;
    }
}
