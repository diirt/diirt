/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/*******************************************************************************
 * Copyright (c) 2010-2014 ITER Organization.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.epics.pvmanager.timecache;

/**
 * {@link DataRequestThread} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataRequestListener {

	/**
	 * Informs that a new chunk is available from the specified
	 * {@link DataRequestThread}.
	 */
	public void newData(DataChunk chunk, DataRequestThread thread);

	/**
	 * Informs that the specified {@link DataRequestThread} has finished
	 * requesting the source.
	 */
	public void intervalComplete(DataRequestThread thread);

}
