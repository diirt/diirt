/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Test
    public void longString1() {
        JCAChannelHandler channel = new JCAChannelHandler("test", dataSource);
        assertThat(channel.isLongString(), equalTo(false));
    }

    @Test
    public void longString2() {
        JCAChannelHandler channel = new JCAChannelHandler("test.NAME$", dataSource);
        assertThat(channel.isLongString(), equalTo(true));
    }

    @Test
    public void longString3() {
        JCAChannelHandler channel = new JCAChannelHandler("test.NAME", dataSource);
        assertThat(channel.isLongString(), equalTo(false));
    }

    @Test
    public void longString4() {
        JCAChannelHandler channel = new JCAChannelHandler("test$", dataSource);
        assertThat(channel.isLongString(), equalTo(false));
    }

    @Test
    public void longString5() {
        JCAChannelHandler channel = new JCAChannelHandler("test.$", dataSource);
        assertThat(channel.isLongString(), equalTo(true));
    }

    @Test
    public void longString6() {
        JCAChannelHandler channel = new JCAChannelHandler("test {\"longString\":true}", dataSource);
        assertThat(channel.isLongString(), equalTo(true));
    }

    @Test
    public void longString7() {
        JCAChannelHandler channel = new JCAChannelHandler("test.NAME$ {\"longString\":true}", dataSource);
        assertThat(channel.isLongString(), equalTo(true));
    }

    @Test
    public void longString8() {
        JCAChannelHandler channel = new JCAChannelHandler("test.NAME$ {\"longString\":false}", dataSource);
        assertThat(channel.isLongString(), equalTo(false));
    }

    @Test
    public void longString9() {
        JCAChannelHandler channel = new JCAChannelHandler("test.NAME$ {\"putCallback\":true}", dataSource);
        assertThat(channel.isLongString(), equalTo(true));
    }

    @Test
    public void toBytes1() {
        String test = "testing";
        byte[] result = JCAChannelHandler.toBytes(test);
        assertThat(new String(result), equalTo("testing\0"));
    }

    @Test
    public void toString1() {
        byte[] test = new byte[] {97,98,99,0,100};
        String result = JCAChannelHandler.toString(test);
        assertThat(result, equalTo("abc"));
    }

    @Test
    public void toString2() {
        byte[] test = new byte[] {97,98,99,100};
        String result = JCAChannelHandler.toString(test);
        assertThat(result, equalTo("abcd"));
    }
}
