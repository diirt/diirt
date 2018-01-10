/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.awt.Font;

import org.junit.Test;

public class FontUtilTest {

    @Test
    public void loadRegularFont() {
        final Font font = FontUtil.getLiberationSansRegular();
        assertThat(font, notNullValue());
    }

}
