/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar string with alarm and timestamp.
 *
 * @author carcassi
 */
public interface EString extends Scalar<String>, Sts, Time {
}
