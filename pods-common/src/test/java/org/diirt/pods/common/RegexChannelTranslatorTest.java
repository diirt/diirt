/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diirt.pods.common;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class RegexChannelTranslatorTest {

    @Test
    public void translate1() {
        RegexChannelTranslator translator = new RegexChannelTranslator("(.*)", "$1", true);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.isReadOnly(), equalTo(true));
        
        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.isReadOnly(), equalTo(true));
        
        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.isReadOnly(), equalTo(true));
    }

    @Test
    public void translate2() {
        RegexChannelTranslator translator = new RegexChannelTranslator(".*", null, true);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target.getFormula(), equalTo("sim://noise"));
        assertThat(target.isReadOnly(), equalTo(true));
        
        target = translator.translate("=1+2");
        assertThat(target.getFormula(), equalTo("=1+2"));
        assertThat(target.isReadOnly(), equalTo(true));
        
        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.isReadOnly(), equalTo(true));
    }

    @Test
    public void translate3() {
        RegexChannelTranslator translator = new RegexChannelTranslator("abc-.*", null, true);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target, nullValue());
        
        target = translator.translate("=1+2");
        assertThat(target, nullValue());
        
        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("abc-345{dfkj:34}"));
        assertThat(target.isReadOnly(), equalTo(true));
    }

    @Test
    public void translate4() {
        RegexChannelTranslator translator = new RegexChannelTranslator("(.*)-(.*)", "$2-$1", false);
        ChannelTranslation target = translator.translate("sim://noise");
        assertThat(target, nullValue());
        
        target = translator.translate("=1+2");
        assertThat(target, nullValue());
        
        target = translator.translate("abc-345{dfkj:34}");
        assertThat(target.getFormula(), equalTo("345{dfkj:34}-abc"));
        assertThat(target.isReadOnly(), equalTo(false));
    }
    
}
