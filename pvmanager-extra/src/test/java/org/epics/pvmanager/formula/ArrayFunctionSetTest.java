/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.displayNone;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.epics.vtype.ValueFactory.newVDoubleArray;
import static org.epics.vtype.ValueFactory.newVNumber;
import static org.epics.vtype.ValueFactory.newVString;
import static org.epics.vtype.ValueFactory.newVStringArray;
import static org.epics.vtype.ValueFactory.timeNow;

import java.util.Arrays;
import java.util.List;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.ValueFactory;
import static org.epics.vtype.ValueFactory.newVDoubleArray;
import static org.epics.vtype.ValueFactory.newVNumber;
import static org.epics.vtype.ValueFactory.timeNow;
import org.epics.vtype.table.ListNumberProvider;
import org.epics.vtype.table.VTableFactory;
import org.junit.Test;

/**
 * @author shroffk
 * 
 */
public class ArrayFunctionSetTest extends BaseTestForFormula {

    private FormulaFunctionSet set = new ArrayFunctionSet();

    @Test
    public void arrayOfString() {
	VString[] data = { newVString("x", alarmNone(), timeNow()),
		newVString("y", alarmNone(), timeNow()),
		newVString("z", alarmNone(), timeNow()) };
	VStringArray expected = newVStringArray(Arrays.asList("x", "y", "z"),
		alarmNone(), timeNow());
	testFunction(set, "arrayOf", expected, (Object[]) data);
    }

    @Test
    public void arrayOfNumber() {
	VNumber[] data = {
		newVDouble(Double.valueOf(1), alarmNone(), timeNow(),
			displayNone()),
		newVDouble(Double.valueOf(2), alarmNone(), timeNow(),
			displayNone()),
		newVDouble(Double.valueOf(3), alarmNone(), timeNow(),
			displayNone()) };
	ListDouble expectedData = new ArrayDouble(1, 2, 3);
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(set, "arrayOf", expected, (Object[]) data);
    }

    @Test
    public void addArrayDoubleOfNumber() {
	testTwoArgArrayFunction(set, "+", new ArrayDouble(1, 2, 3),
		new ArrayDouble(4, 5, 6), new ArrayDouble(5, 7, 9));
    }

    @Test
    public void addArrayIntOfNumber() {
	testTwoArgArrayFunction(set, "+", new ArrayInt(1, 2, 3), new ArrayInt(
		4, 5, 6), new ArrayDouble(5, 7, 9));
    }

    @Test
    public void subtractArrayOfNumber() {
	testTwoArgArrayFunction(set, "-", new ArrayDouble(4, 5, 6),
		new ArrayDouble(4, 3, 2), new ArrayDouble(0, 2, 4));
    }

    @Test
    public void multiplyArrayWithNumber() {
	testTwoArgArrayFunction(set, "*", new ArrayDouble(1, 2, 3),
		Double.valueOf(2), new ArrayDouble(2, 4, 6));
    }

    @Test
    public void divideArrayWithNumber() {
	testTwoArgArrayFunction(set, "/", new ArrayDouble(2, 4, 6),
		Double.valueOf(2), new ArrayDouble(1, 2, 3));
    }

    @Test
    public void rescaleArray() {
	ListDouble data = new ArrayDouble(1, 2, 3);
	ListDouble expectedData = new ArrayDouble(2, 3, 4);
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(
		set,
		"rescale",
		expected,
		newVDoubleArray(data , alarmNone(), timeNow(),
			displayNone()),
		newVNumber(1.0, alarmNone(), timeNow(),
			displayNone()),
		newVNumber(1.0, alarmNone(), timeNow(),
			displayNone()));
    }
    
    @Test
    public void subArray(){
	ListDouble data = new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	ListDouble expectedData = new ArrayDouble(2, 3, 4);
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(set, "subArray", expected,
		newVDoubleArray(data, alarmNone(), timeNow(), displayNone()),
		newVNumber(2, alarmNone(), timeNow(), displayNone()),
		newVNumber(5, alarmNone(), timeNow(), displayNone()));
    }
    
    @Test
    public void elementAtArray1(){	
	ListDouble data = new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	VNumber expected = newVNumber(5.0, alarmNone(),timeNow(), displayNone());
	
	testFunction(set, "elementAt", expected,
		newVDoubleArray(data, alarmNone(), timeNow(), displayNone()),
		newVNumber(5, alarmNone(), timeNow(), displayNone()));
    }
    
    @Test
    public void elementAtArray2(){	
        List<String> data = Arrays.asList("A", "B", "C", "D", "E");
	VString expected = newVString("C", alarmNone(),timeNow());
	
	testFunction(set, "elementAt", expected,
		newVStringArray(data, alarmNone(), timeNow()),
		newVNumber(2, alarmNone(), timeNow(), displayNone()));
    }
    
    @Test
    public void elementAtArray3(){	
	VString expected = null;
	
	testFunction(set, "elementAt", expected,
		null,
		newVNumber(2, alarmNone(), timeNow(), displayNone()));
    }
    
    @Test
    public void arrayWithBoundaries(){
        // TODO: should test alarm, time and display
        VNumberArray array = newVDoubleArray(new ArrayDouble(1,2,3,4), alarmNone(), timeNow(), displayNone());
        ListNumberProvider generator = VTableFactory.step(-1, 0.5);
        VNumberArray expected = ValueFactory.newVNumberArray(new ArrayDouble(1,2,3,4), new ArrayInt(4),
                Arrays.asList(ValueFactory.newDisplay(new ArrayDouble(-1, -0.5, 0, 0.5, 1), "")), alarmNone(), timeNow(), displayNone());
	
	testFunction(set, "arrayWithBoundaries", expected,
		array,
		generator);
    }
   
}
