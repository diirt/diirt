/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class LinearAbsoluteTimeScaleTest {

    @Test
    public void scaleNormalizedTime1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        assertThat(linearScale.scaleNormalizedTime(0.0, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleNormalizedTime(0.25, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleNormalizedTime(0.5, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleNormalizedTime(1.0, 1.0, 100.0), equalTo(100.0));
    }

    @Test
    public void scaleTimestamp1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = Timestamp.now();
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(8)));
        assertThat(linearScale.scaleTimestamp(start, timeInterval, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(2)), timeInterval, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(4)), timeInterval, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(8)), timeInterval, 1.0, 100.0), equalTo(100.0));
    }
    
    @Test
    public void references1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 123);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(20)));
        TimeAxis timeAxis = linearScale.references(timeInterval, 2, 11);
        assertAxisEquals(timeInterval, new ArrayDouble(1877.0/20000.0,
                3877.0/20000.0,
                5877.0/20000.0,
                7877.0/20000.0,
                9877.0/20000.0,
                11877.0/20000.0,
                13877.0/20000.0,
                15877.0/20000.0,
                17877.0/20000.0,
                19877.0/20000.0), 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 50, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 52, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 54, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 56, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 58, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 0, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 2, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 4, 0)),
                Arrays.asList("2013/05/10 16:13:46",
                "16:13:48",
                "16:13:50",
                "16:13:52",
                "16:13:54",
                "16:13:56",
                "16:13:58",
                "16:14:00",
                "16:14:02",
                "16:14:04"), timeAxis);
        
    }

    @Test
    public void references2() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 100);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(5)));
        TimeAxis timeAxis = linearScale.references(timeInterval, 2, 11);
        assertAxisEquals(timeInterval, new ArrayDouble(4.0/50.0,
                9.0/50.0,
                14.0/50.0,
                19.0/50.0,
                24.0/50.0,
                29.0/50.0,
                34.0/50.0,
                39.0/50.0,
                44.0/50.0,
                49.0/50.0), 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 49, 0)),
                Arrays.asList("2013/05/10 16:13:44.5",
                "16:13:45.0",
                ".5",
                "16:13:46.0",
                ".5",
                "16:13:47.0",
                ".5",
                "16:13:48.0",
                ".5",
                "16:13:49.0"), timeAxis);    
    }
    
    @Test
    public void createZeroReferences() {
	//Test for requiring 0 references
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 0 , 0 );
	ArrayList< Timestamp > noTimestamps = new ArrayList< Timestamp >();
	ArrayList< String > noLabels = new ArrayList< String >();
	
	assertAxisEquals(timeInterval, 
		new ArrayDouble(

		), 
		noTimestamps,
		noLabels,
		timeAxis);   
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createZeroReferences2() {
	//Test for requiring negative amount of references
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, -100 , -10 );
	assertTrue( false );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createZeroReferences3() {
	//Test for max refs is less than min refs
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 99999 , 2 );
	assertTrue( false );
    }
     
    @Test
    public void references1MsPeriod1() {
	//Test creating 2 references with the smallest milliseconds time interval
	//possible or 1 ms.
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );
        assertAxisEquals(timeInterval, 
		new ArrayDouble(
		    0.0/1.0,
		    1.0/1.0
		), 
                Arrays.asList(
		    TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 ),
		    TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 54 )
		),
                Arrays.asList(
		    "2014/11/13 10:31:23.053",
		    ".054"
		), 
		timeAxis);    
    }
    
    @Test
    public void references1MsPeriod2() {
	//Test creating 3 references with the smallest milliseconds time interval
	//possible or 1 ms. 
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 2 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );
        assertAxisEquals(timeInterval, 
		new ArrayDouble(
		    0.0/2.0,
		    1.0/2.0,
		    2.0/2.0
		), 
                Arrays.asList(
		    TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 ),
		    TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 54 ),
		    TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 55 )
		),
                Arrays.asList(
		    "2014/11/13 10:31:23.053",
		    ".054",
		    ".055"
		), 
		timeAxis);    
    }
    
    @Test
    public void references1MsPeriod3() {
	//Test creating a large amount of references with a very small scale
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
	
	//we can only go up to minutes, because going up to hours would make
	//the interval >1000000, which would take a while to run
	int MS_INTERVAL = 180000;
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( MS_INTERVAL ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );
	
	double[] normalValues = new double[ MS_INTERVAL+1 ];
	for ( int i=0 ; i<=MS_INTERVAL ; i++ ) {
	    normalValues[ i ] = ((double)(i)/MS_INTERVAL);
	}
	ArrayList< Timestamp > times = new ArrayList< Timestamp >();
	ArrayList< String > labels = new ArrayList< String >();
	times.add( TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 ) );
	labels.add( "2014/11/13 10:31:23.053" );
	
	int timeLeft = MS_INTERVAL;
	int hour = 10;
	int minute = 31;
	int second = 23;
	int ms = 53;
	while( timeLeft > 0 ) {
	    timeLeft--;
	    ms++;
	    String nextLabel = "." + formatNumericalString( ms%1000 , 3 );
	    boolean useFullLabel = false;

	    if ( ms >= 1000 ) {
		second += (int)(ms/1000);
		ms %= 1000;
		useFullLabel = true;
	    }
	    
	    if ( second >= 60 ) {
		minute += (int)(second/60);
		second %= 60;
		useFullLabel = true;
	    }
	    
	    if ( minute >= 60 ) {
		hour += (int)(minute/60);
		minute %= 60;
		useFullLabel = true;
	    }
	    
	    times.add( TimeScalesTest.create( 2014 , 11  , 13 , hour , minute , second , ms ) );
	    if ( useFullLabel ) {
		String fullLabel = formatNumericalString( hour , 2 ) + ":" + formatNumericalString( minute , 2 ) + ":" + formatNumericalString( second , 2 ) + "." + formatNumericalString( ms , 3 );
		labels.add( fullLabel );
	    }
	    else {
		labels.add( nextLabel );
	    }
	}
	
        assertAxisEquals(timeInterval, 
		new ArrayDouble(
		    normalValues
		), 
		times,
		labels, 
		timeAxis);    
    }
    
    @Test
    public void references1MsPeriod4() {
	//Test creating a medium amount of references with a very small scale
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
	
	int MS_INTERVAL = 4321;
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( MS_INTERVAL ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );
	
	double[] normalValues = new double[ MS_INTERVAL+1 ];
	for ( int i=0 ; i<=MS_INTERVAL ; i++ ) {
	    normalValues[ i ] = ((double)(i)/MS_INTERVAL);
	}
	ArrayList< Timestamp > times = new ArrayList< Timestamp >();
	ArrayList< String > labels = new ArrayList< String >();
	times.add( TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 ) );
	labels.add( "2014/11/13 10:31:23.053" );
	
	int timeLeft = MS_INTERVAL;
	int hour = 10;
	int minute = 31;
	int second = 23;
	int ms = 53;
	while( timeLeft > 0 ) {
	    timeLeft--;
	    ms++;
	    String nextLabel = "." + formatNumericalString( ms%1000 , 3 );
	    boolean useFullLabel = false;

	    if ( ms >= 1000 ) {
		second += (int)(ms/1000);
		ms %= 1000;
		useFullLabel = true;
	    }
	    
	    if ( second >= 60 ) {
		minute += (int)(second/60);
		second %= 60;
		useFullLabel = true;
	    }
	    
	    if ( minute >= 60 ) {
		hour += (int)(minute/60);
		minute %= 60;
		useFullLabel = true;
	    }
	    
	    times.add( TimeScalesTest.create( 2014 , 11  , 13 , hour , minute , second , ms ) );
	    if ( useFullLabel ) {
		String fullLabel = formatNumericalString( hour , 2 ) + ":" + formatNumericalString( minute , 2 ) + ":" + formatNumericalString( second , 2 ) + "." + formatNumericalString( ms , 3 );
		labels.add( fullLabel );
	    }
	    else {
		labels.add( nextLabel );
	    }
	}
	
        assertAxisEquals(timeInterval, 
		new ArrayDouble(
		    normalValues
		), 
		times,
		labels, 
		timeAxis);    
    }
    
    @Test
    public void references5MsPeriod1() {
	//Test creating 3 references with a medium milliseconds time interval
	//of 5 ms
	TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofMillis( 16 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 3 , 3 );
        assertAxisEquals(
	    timeInterval, 
	    new ArrayDouble(
		2.0/16.0,
		7.0/16.0,
		12.0/16.0
	    ), 
	    Arrays.asList(
		TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 55 ),
		TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 60 ),
		TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 65 )
	    ),
	    Arrays.asList(
		"2014/11/13 10:31:23.055",
		".060",
		".065"
	    ), 
	    timeAxis); 
    }

    public static void assertAxisEquals(TimeInterval timeInterval, ListDouble normalizedValues, List<Timestamp> timestamps, List<String> labels, TimeAxis axis) {
        assertThat(axis.getTimeInterval(), equalTo(timeInterval));
        assertThat(axis.getNormalizedValues(), equalTo(normalizedValues));
        assertThat(axis.getTimestamps(), equalTo(timestamps));
        assertThat(axis.getTickLabels(), equalTo(labels));
    }
    
    /**
     * Formats the given string to contain the given number of digits, adding
     * leading zeros if necessary.
     * 
     * @param number a number to format
     * @param digits the number of digits the number must contain
     * @return the formatted number that contains the given number of digits
     */
    public String formatNumericalString( int number , int digits ) {
	String rtn = String.valueOf( number );
	if ( rtn.length() > digits ) {
	    throw new IllegalArgumentException( "Impossible to format " + number + " to have " + digits + " digits." );
	}
	while( rtn.length() < digits ) {
	    rtn = "0" + rtn;
	}
	return rtn;
    }
}