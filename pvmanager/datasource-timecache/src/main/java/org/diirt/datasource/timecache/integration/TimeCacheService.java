/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.integration;

import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TimeCacheService extends Service {

        public TimeCacheService() {
                super(new ServiceDescription("tc", "TimeCache service"));
        }

}
