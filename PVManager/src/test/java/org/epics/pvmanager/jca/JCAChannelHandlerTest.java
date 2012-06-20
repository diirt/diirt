/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateReadWriteExpression;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 *
 * @author carcassi
 */
public class JCAChannelHandlerTest {
    
    public JCAChannelHandlerTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Mock JCADataSource dataSource;

    @Test
    public void new1() {
        JCAChannelHandler channel = new JCAChannelHandler("test", dataSource);
        assertThat(channel.getChannelName(), equalTo("test"));
        assertThat(channel.isPutCallback(), equalTo(false));
    }

    @Test
    public void new2() {
        JCAChannelHandler channel = new JCAChannelHandler("test {\"putCallback\":true}", dataSource);
        assertThat(channel.getChannelName(), equalTo("test {\"putCallback\":true}"));
        assertThat(channel.isPutCallback(), equalTo(true));
    }

    @Test
    public void new3() {
        JCAChannelHandler channel = new JCAChannelHandler("test {\"putCallback\":false}", dataSource);
        assertThat(channel.getChannelName(), equalTo("test {\"putCallback\":false}"));
        assertThat(channel.isPutCallback(), equalTo(false));
    }

    @Test(expected=IllegalArgumentException.class)
    public void new4() {
        JCAChannelHandler channel = new JCAChannelHandler("test {\"putCallback\":fase}", dataSource);
    }
}
