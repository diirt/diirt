/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.pods.common.ChannelTranslator.*;
import static org.diirt.pods.common.ChannelTranslation.Permission.*;

/**
 *
 * @author carcassi
 */
public class ChannelTranslatorTest {

    @Test
    public void regexTranslator1() {
        ChannelTranslator translator = regexTranslator("(.*)", "$1", READ_ONLY);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));
    }

    @Test
    public void regexTranslator2() {
        ChannelTranslator translator = regexTranslator(".*", null, READ_ONLY);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));
    }

    @Test
    public void regexTranslator3() {
        ChannelTranslator translator = regexTranslator("abc-.*", null, READ_ONLY);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target, nullValue());

        target = translator.translate("=1+2");
        assertThat(target, nullValue());

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));
    }

    @Test
    public void regexTranslator4() {
        ChannelTranslator translator = regexTranslator("(.*)-(.*)", "$2-$1", READ_WRITE);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target, nullValue());

        target = translator.translate("=1+2");
        assertThat(target, nullValue());

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("345{dfkj:34}-abc"));
        assertThat(target.getPermission(), equalTo(READ_WRITE));
    }

    @Test
    public void compositeTranslator1() {
        ChannelTranslator translator = compositeTranslator(Arrays.asList(regexTranslator("abc-(.*)", null, READ_WRITE), regexTranslator(".*", null, READ_ONLY)));
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.getPermission(), equalTo(READ_WRITE));
    }

    @Test
    public void compositeTranslator2() {
        ChannelTranslator translator = compositeTranslator(Arrays.asList(regexTranslator("=.*", null, NONE), regexTranslator(".*", null, READ_ONLY)));
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));

        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.getPermission(), equalTo(NONE));

        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.getPermission(), equalTo(READ_ONLY));
    }

    @Test
    public void loadTranslator1() throws Exception{
        File file = new File(getClass().getResource("mappings1.xml").toURI());
        try (InputStream input = new FileInputStream(file)) {
            ChannelTranslator translator = loadTranslator(input);
            ChannelTranslation target = translator.translate("specialchannel");
            assertThat(target.getFormula(), equalTo("specialchannel"));
            assertThat(target.getPermission(), equalTo(READ_WRITE));

            target = translator.translate("ABC-DEF");
            assertThat(target.getFormula(), equalTo("DEF"));
            assertThat(target.getPermission(), equalTo(READ_ONLY));

            target = translator.translate("diff-ABC-DEF");
            assertThat(target.getFormula(), equalTo("='ABC'-'DEF'"));
            assertThat(target.getPermission(), equalTo(READ_ONLY));

            target = translator.translate("abc-345{dfkj:34}");
            assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
            assertThat(target.getPermission(), equalTo(NONE));
        }
    }

    @Test
    public void loadTranslator2() throws Exception{
        File file = new File(getClass().getResource("mappings2.xml").toURI());
        try (InputStream input = new FileInputStream(file)) {
            ChannelTranslator translator = loadTranslator(input);
            ChannelTranslation target = translator.translate("channel1");
            assertThat(target.getFormula(), equalTo("channel1"));
            assertThat(target.getPermission(), equalTo(READ_ONLY));

            target = translator.translate(new ChannelRequest("channel1", "carcassi", null, null, null));
            assertThat(target.getFormula(), equalTo("channel1"));
            assertThat(target.getPermission(), equalTo(READ_WRITE));

            target = translator.translate("channel2-name");
            assertThat(target, nullValue());

            target = translator.translate(new ChannelRequest("channel2-name", "shroff", null, null, null));
            assertThat(target.getFormula(), equalTo("name"));
            assertThat(target.getPermission(), equalTo(READ_ONLY));
        }
    }

}
