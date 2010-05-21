/*
 * Copyright 2010 Brookhaven National Laboratory.
 * All rights reserved. Use is subject to license terms.
 */

/**
 * Common types for EPICS systems.
 * <p>
 * This package contains the data definitions for all the EPICS types.
 * These are in terms of Java interfaces so that each data source can
 * map directly to their own structure.
 * <p>
 * The interfaces starting with Dbr represent actual types that can be
 * taken by a data source. The other interfaces represent atomic elements
 * that can be treated separately so that generic support can be written against
 * them (i.e. one can write support for alarms regardless of the actual type).
 */
package gov.bnl.pvmanager.epics;
