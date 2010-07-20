/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar enum with alarm and timestamp.
 *
 * @author carcassi
 */
public interface EEnum extends Scalar<String>, Enum, Alarm, Time {
}
