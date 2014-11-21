/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.cf.service;

import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;

/**
 * @author shroffk
 * 
 */
public class ChannelFinderService extends Service {

    public ChannelFinderService() {
	super(new ServiceDescription("cf", "ChannelFinder service")
	.addServiceMethod(new QueryServiceMethod()));	
    }

}
