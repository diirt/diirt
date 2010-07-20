/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Multi channel array of EDouble.
 *
 * @author carcassi
 */
public interface EMultiDouble extends MultiScalar<EDouble>, Sts, Time, Ctrl<Double> {

}
