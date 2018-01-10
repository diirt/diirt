/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.vtype.ValueFactory.*;
import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayShort;
import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListShort;
import org.diirt.util.text.NumberFormats;
import org.mockito.Mockito;

/**
 *
 * @author carcassi
 */
public class SimpleValueFormatTest {
    Display display = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
            Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
            Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);

    Display displayInt = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
            Double.MIN_VALUE, "", NumberFormats.format(0), Double.MAX_VALUE,
            Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);

    @Test
    public void defaultPrecision() {
        ValueFormat f = new SimpleValueFormat(3);
        assertThat(f.format(newVDouble(1234.5678, display)), equalTo("1234.568"));
        assertThat(f.format(newVIntArray(new ArrayInt(1, 2, 3), alarmNone(), timeNow(), displayInt)), equalTo("[1, 2, 3]"));
        assertThat(f.format(newVIntArray(new ArrayInt(1), alarmNone(), timeNow(), displayInt)), equalTo("[1]"));
        assertThat(f.format(newVIntArray(new ArrayInt(1, 2, 3, 4, 5), alarmNone(), timeNow(), displayInt)), equalTo("[1, 2, 3, ...]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1, 2, 3}), alarmNone(), timeNow(), display)), equalTo("[1.000, 2.000, 3.000]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1}), alarmNone(), timeNow(), display)), equalTo("[1.000]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1, 2, 3, 4, 5}), alarmNone(), timeNow(), display)), equalTo("[1.000, 2.000, 3.000, ...]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), display)), equalTo("[1.000, 2.000, 3.000]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1), alarmNone(), timeNow(), display)), equalTo("[1.000]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1, 2, 3, 4, 5), alarmNone(), timeNow(), display)), equalTo("[1.000, 2.000, 3.000, ...]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo("[A, B, C]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A"), alarmNone(), timeNow())), equalTo("[A]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A", "B", "C", "D", "E"), alarmNone(), timeNow())), equalTo("[A, B, C, ...]"));
        assertThat(f.format(newVEnumArray(new ArrayInt(2, 0, 0), Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo("[C, A, A]"));
        assertThat(f.format(newVEnumArray(new ArrayInt(2), Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo("[C]"));
        assertThat(f.format(newVEnumArray(new ArrayInt(2, 0, 0, 1, 0), Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo("[C, A, A, ...]"));
    }

    @Test
    public void testMandatedPrecision() {
        Display display = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
        Display displayInt = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(0), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
        ValueFormat f = new SimpleValueFormat(3);
        f.setNumberFormat(NumberFormats.format(2));
        assertThat(f.format(newVDouble(1234.5678, display)), equalTo("1234.57"));
        assertThat(f.format(newVIntArray(new ArrayInt(1, 2, 3), alarmNone(), timeNow(), displayInt)), equalTo("[1.00, 2.00, 3.00]"));
        assertThat(f.format(newVIntArray(new ArrayInt(1), alarmNone(), timeNow(), displayInt)), equalTo("[1.00]"));
        assertThat(f.format(newVIntArray(new ArrayInt(1, 2, 3, 4, 5), alarmNone(), timeNow(), displayInt)), equalTo("[1.00, 2.00, 3.00, ...]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1, 2, 3}), alarmNone(), timeNow(), display)), equalTo("[1.00, 2.00, 3.00]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1}), alarmNone(), timeNow(), display)), equalTo("[1.00]"));
        assertThat(f.format(newVFloatArray(new ArrayFloat(new float[] {1, 2, 3, 4, 5}), alarmNone(), timeNow(), display)), equalTo("[1.00, 2.00, 3.00, ...]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), display)), equalTo("[1.00, 2.00, 3.00]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1), alarmNone(), timeNow(), display)), equalTo("[1.00]"));
        assertThat(f.format(newVDoubleArray(new ArrayDouble(1, 2, 3, 4, 5), alarmNone(), timeNow(), display)), equalTo("[1.00, 2.00, 3.00, ...]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo("[A, B, C]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A"), alarmNone(), timeNow())), equalTo("[A]"));
        assertThat(f.format(newVStringArray(Arrays.asList("A", "B", "C", "D", "E"), alarmNone(), timeNow())), equalTo("[A, B, C, ...]"));
    }

    @Test
    public void parseVDouble1() {
        ValueFormat f = new SimpleValueFormat(3);
        VDouble reference = newVDouble(3.0);
        assertThat(f.parseObject("3.14", reference), equalTo((Object) 3.14));
        assertThat(f.parseDouble("3.14"), equalTo(3.14));
        assertThat(f.parseDouble("1333"), equalTo(1333.0));
    }

    @Test
    public void parseVFloat1() {
        ValueFormat f = new SimpleValueFormat(3);
        VFloat reference = Mockito.mock(VFloat.class);
        assertThat(f.parseObject("3.14", reference), equalTo((Object) 3.14f));
        assertThat(f.parseFloat("3.14"), equalTo(3.14f));
        assertThat(f.parseFloat("1333"), equalTo(1333.0f));
    }

    @Test
    public void parseVInt1() {
        ValueFormat f = new SimpleValueFormat(3);
        VInt reference = Mockito.mock(VInt.class);
        assertThat(f.parseObject("314", reference), equalTo((Object) 314));
        assertThat(f.parseInt("314"), equalTo(314));
        assertThat(f.parseInt("1333"), equalTo(1333));
    }

    @Test
    public void parseVShort1() {
        ValueFormat f = new SimpleValueFormat(3);
        VShort reference = Mockito.mock(VShort.class);
        assertThat(f.parseObject("314", reference), equalTo((Object) (short) 314));
        assertThat(f.parseShort("314"), equalTo((short) 314));
        assertThat(f.parseShort("1333"), equalTo((short) 1333));
    }

    @Test
    public void parseVByte1() {
        ValueFormat f = new SimpleValueFormat(3);
        VByte reference = Mockito.mock(VByte.class);
        assertThat(f.parseObject("23", reference), equalTo((Object) (byte) 23));
        assertThat(f.parseByte("23"), equalTo((byte) 23));
        assertThat(f.parseByte("112"), equalTo((byte) 112));
    }

    @Test
    public void parseVString1() {
        ValueFormat f = new SimpleValueFormat(3);
        VString reference = Mockito.mock(VString.class);
        assertThat(f.parseObject("Testing", reference), equalTo((Object) "Testing"));
        assertThat(f.parseString("Testing"), equalTo("Testing"));
        assertThat(f.parseString("Foo"), equalTo("Foo"));
    }

    @Test
    public void parseVEnum1() {
        ValueFormat f = new SimpleValueFormat(3);
        List<String> labels = Arrays.asList("A", "B", "C");
        VEnum reference = newVEnum(0, labels, alarmNone(), timeNow());
        assertThat(f.parseObject("A", reference), equalTo((Object) 0));
        assertThat(f.parseEnum("A", labels), equalTo(0));
        assertThat(f.parseEnum("B", labels), equalTo(1));
    }

    @Test
    public void parseVDoubleArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VDoubleArray reference = Mockito.mock(VDoubleArray.class);
        assertThat(f.parseObject("3.14", reference), equalTo((Object) new ArrayDouble(3.14)));
        assertThat(f.parseDoubleArray("3.14"), equalTo((ListDouble) new ArrayDouble(3.14)));
        assertThat(f.parseDoubleArray("1333, 3.14"), equalTo((ListDouble) new ArrayDouble(1333, 3.14)));
        assertThat(f.parseDoubleArray("1.0, 2.0, 3.0, 4.0"), equalTo((ListDouble) new ArrayDouble(1.0, 2.0, 3.0, 4.0)));
    }

    @Test
    public void parseVFloatArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VFloatArray reference = Mockito.mock(VFloatArray.class);
        assertThat(f.parseObject("3.14", reference), equalTo((Object) new ArrayFloat(3.14f)));
        assertThat(f.parseFloatArray("3.14"), equalTo((ListFloat) new ArrayFloat(3.14f)));
        assertThat(f.parseFloatArray("1333, 3.14"), equalTo((ListFloat) new ArrayFloat(1333f, 3.14f)));
        assertThat(f.parseFloatArray("1.0, 2.0, 3.0, 4.0"), equalTo((ListFloat) new ArrayFloat(1.0f, 2.0f, 3.0f, 4.0f)));
    }

    @Test
    public void parseVIntArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VIntArray reference = Mockito.mock(VIntArray.class);
        assertThat(f.parseObject("3", reference), equalTo((Object) new ArrayInt(3)));
        assertThat(f.parseIntArray("3"), equalTo((ListInt) new ArrayInt(3)));
        assertThat(f.parseIntArray("1333, 3"), equalTo((ListInt) new ArrayInt(1333, 3)));
        assertThat(f.parseIntArray("1, 2, 3, 4"), equalTo((ListInt) new ArrayInt(1, 2, 3, 4)));
    }

    @Test
    public void parseVShortArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VShortArray reference = Mockito.mock(VShortArray.class);
        assertThat(f.parseObject("3", reference), equalTo((Object) new ArrayShort((short) 3)));
        assertThat(f.parseShortArray("3"), equalTo((ListShort) new ArrayShort((short) 3)));
        assertThat(f.parseShortArray("1333, 3"), equalTo((ListShort) new ArrayShort(new short[]{1333, 3})));
        assertThat(f.parseShortArray("1, 2, 3, 4"), equalTo((ListShort) new ArrayShort(new short[]{1, 2, 3, 4})));
    }

    @Test
    public void parseVByteArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VByteArray reference = Mockito.mock(VByteArray.class);
        assertThat(f.parseObject("3", reference), equalTo((Object) new ArrayByte(new byte[] {3})));
        assertThat(f.parseByteArray("3"), equalTo((ListByte) new ArrayByte(new byte[] {3})));
        assertThat(f.parseByteArray("113, 3"), equalTo((ListByte) new ArrayByte(new byte[] {113, 3})));
        assertThat(f.parseByteArray("1, 2, 3, 4"), equalTo((ListByte) new ArrayByte(new byte[] {1, 2, 3, 4})));
    }

    @Test
    public void parseVStringArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        VStringArray reference = Mockito.mock(VStringArray.class);
        assertThat(f.parseObject("test", reference), equalTo((Object) Arrays.asList("test")));
        assertThat(f.parseStringArray("test"), equalTo(Arrays.asList("test")));
        assertThat(f.parseStringArray("a, b"), equalTo(Arrays.asList("a", "b")));
        assertThat(f.parseStringArray("a, b, c, d"), equalTo(Arrays.asList("a", "b", "c", "d")));
    }

    @Test
    public void parseVEnumArray1() {
        ValueFormat f = new SimpleValueFormat(3);
        List<String> labels = Arrays.asList("A", "B", "C");
        VEnumArray reference = newVEnumArray(new ArrayInt(1), labels, alarmNone(), timeNow());
        assertThat(f.parseObject("A", reference), equalTo((Object) new ArrayInt(0)));
        assertThat(f.parseEnumArray("A", labels), equalTo((ListInt) new ArrayInt(0)));
        assertThat(f.parseEnumArray("B, A", labels), equalTo((ListInt) new ArrayInt(1, 0)));
        assertThat(f.parseEnumArray("B, A, C,A", labels), equalTo((ListInt) new ArrayInt(1, 0, 2, 0)));
    }

}