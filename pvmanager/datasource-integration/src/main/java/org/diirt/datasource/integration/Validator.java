/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.util.List;

/**
 *
 * @author carcassi
 */
public interface Validator {

    public List<String> validate(List<Object> values);
}
