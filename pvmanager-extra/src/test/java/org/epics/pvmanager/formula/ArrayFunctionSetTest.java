/**
 * 
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.displayNone;
import static org.epics.vtype.ValueFactory.newVString;
import static org.epics.vtype.ValueFactory.newVStringArray;
import static org.epics.vtype.ValueFactory.timeNow;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.epics.vtype.ValueFactory.newVDoubleArray;

import java.util.Arrays;

import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
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
	testFunction(set, "arrayOf", expected, data);
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
	double[] expectedData = { 1, 2, 3 };
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(set, "arrayOf", expected, data);
    }
}
