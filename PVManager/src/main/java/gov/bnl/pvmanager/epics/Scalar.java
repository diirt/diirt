/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type definition for all scalar types. {@link #getValue()} never returns
 * null, even if the channel never connected. One <b>must always look</b>
 * at the alarm severity to be able to correctly interpret the value.
 * <p>
 * Coding to {@code Scalart<T extends Object>} allows to write a client that works with all
 * scalars, regardless of their type.
 * Coding to {@code Scalart<T extends Number>} allows to write a client that works with all
 * numbers, regardless of their type.
 *
 * @author carcassi
 */
public interface Scalar<T> {
    T getValue();
}
