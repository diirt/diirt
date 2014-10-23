/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.graphene.TimeScales.TimePeriod;
import static java.util.GregorianCalendar.*;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class TimeScalesTest {
    
    @Test
    public void nextUp1() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 1));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 2)));
    }
    
    @Test
    public void nextUp2() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 2));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 5)));
    }
    
    @Test
    public void nextUp3() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 5));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 10)));
    }
    
    @Test
    public void nextUp4() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 10));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 15)));
    }
    
    @Test
    public void nextUp5() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 15));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 30)));
    }
    
    @Test
    public void roundSeconds1() {
        TimeScales.TimePeriod period = TimeScales.toTimePeriod(30.0);
        assertThat(period, equalTo(new TimePeriod(SECOND, 30.0)));
    }
    
    @Test
    public void roundSeconds2() {
        TimeScales.TimePeriod period = TimeScales.toTimePeriod(61.0);
        assertThat(period, equalTo(new TimePeriod(MINUTE, 61.0/60.0)));
    }
    
    @Test
    public void nextDown1() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 15));
        assertThat(period, equalTo(new TimePeriod(SECOND, 10)));
    }
    
    @Test
    public void nextDown2() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 2));
        assertThat(period, equalTo(new TimePeriod(SECOND, 1)));
    }
    
    @Test
    public void nextDown3() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 5));
        assertThat(period, equalTo(new TimePeriod(SECOND, 2)));
    }
    
    @Test
    public void nextDown4() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 10));
        assertThat(period, equalTo(new TimePeriod(SECOND, 5)));
    }
    
    @Test
    public void nextDown5() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 15));
        assertThat(period, equalTo(new TimePeriod(SECOND, 10)));
    }
    
    @Test
    public void nextDown6() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(SECOND, 30));
        assertThat(period, equalTo(new TimePeriod(SECOND, 15)));
    }
    
    @Test
    public void nextDown7() {
        TimeScales.TimePeriod period = TimeScales.nextDown(new TimePeriod(MINUTE, 1));
        assertThat(period, equalTo(new TimePeriod(SECOND, 30)));
    }
    
    //TODO nextDown and nextUp for minutes, hours, weeks, months,
    //Test createReferences() for any time interval and period
    //Leap years, crazy stuff
    
    @Test
    public void round1() {
        GregorianCalendar cal = new GregorianCalendar(2013, 3, 14, 14, 23, 15);
        cal.set(GregorianCalendar.MILLISECOND, 123);
        Date date = cal.getTime();

        TimeScales.round(cal, GregorianCalendar.MILLISECOND);
        assertThat(cal.getTime(), equalTo(date));

        TimeScales.round(cal, GregorianCalendar.SECOND);
        assertThat(cal, equalTo(new GregorianCalendar(2013, 3, 14, 14, 23, 15)));
        
        TimeScales.round(cal, GregorianCalendar.MINUTE);
        assertThat(cal, equalTo(new GregorianCalendar(2013, 3, 14, 14, 23, 0)));
    }
    
    @Test
    public void createReferences1() {
        GregorianCalendar cal = new GregorianCalendar(2013, 2, 14, 14, 23, 15);
        cal.set(GregorianCalendar.MILLISECOND, 123);
        Timestamp start = Timestamp.of(cal.getTime());
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(2)));
        List<Timestamp> references = TimeScales.createReferences(timeInterval, new TimePeriod(MILLISECOND, 50));
        assertThat(references.size(), equalTo(40));
        assertThat(references.get(0), equalTo(create(2013, 3, 14, 14, 23, 15, 150)));
        assertThat(references.get(1), equalTo(create(2013, 3, 14, 14, 23, 15, 200)));
        assertThat(references.get(39), equalTo(create(2013, 3, 14, 14, 23, 17, 100)));
    }
    
    @Test
    public void createReferences2() {
        GregorianCalendar cal = new GregorianCalendar(2013, 2, 14, 14, 23, 15);
        cal.set(GregorianCalendar.MILLISECOND, 123);
        Timestamp start = Timestamp.of(cal.getTime());
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(2)));
        List<Timestamp> references = TimeScales.createReferences(timeInterval, new TimePeriod(MILLISECOND, 100));
        assertThat(references.size(), equalTo(20));
        assertThat(references.get(0), equalTo(create(2013, 3, 14, 14, 23, 15, 200)));
        assertThat(references.get(1), equalTo(create(2013, 3, 14, 14, 23, 15, 300)));
        assertThat(references.get(19), equalTo(create(2013, 3, 14, 14, 23, 17, 100)));
    }
    
    @Test
    public void createReferences3() {
        GregorianCalendar cal = new GregorianCalendar(2013, 2, 14, 14, 23, 15);
        cal.set(GregorianCalendar.MILLISECOND, 123);
        Timestamp start = Timestamp.of(cal.getTime());
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(30)));
        List<Timestamp> references = TimeScales.createReferences(timeInterval, new TimePeriod(SECOND, 10));
        assertThat(references.size(), equalTo(3));
        assertThat(references.get(0), equalTo(create(2013, 3, 14, 14, 23, 20, 0)));
        assertThat(references.get(1), equalTo(create(2013, 3, 14, 14, 23, 30, 0)));
        assertThat(references.get(2), equalTo(create(2013, 3, 14, 14, 23, 40, 0)));
    }
    
    @Test
    public void createReferencesLowerBoundary1() {
	
	//test lower boundary case: 2 references 1 millisecond apart
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 1 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1 ) );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 1 ) ) );
	
    }
    
    @Test
    public void createReferencesLowerBoundary2() {
	//test lower boundary case: 2 references, milliseconds roll over to next second
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 1 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1 ) );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 999 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    
    @Test
    @Ignore //Test case fails
    public void createReferencesLowerBoundary3() {
	//test lower boundary case: 2 references, milliseconds roll over to next second
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 2 ) ) );
	System.out.println( start + " "+ start.plus( TimeDuration.ofMillis( 2 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 2 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 999 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 1 ) ) );
    }
   
    @Test
    public void createReferencesLowerBoundary4() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 3 ) ) );
	System.out.println( start + " "+ start.plus( TimeDuration.ofMillis( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 3 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 999 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 2 ) ) );
    }
    
    @Test
    @Ignore //Test case fails
    public void createReferencesLowerBoundary5() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 998 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 4 ) ) );
	System.out.println( start + " "+ start.plus( TimeDuration.ofMillis( 4 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 998 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 2 ) ) );
    }
    
    @Test
    @Ignore	//Test case fails
    public void createReferencesLowerBoundary6() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 4 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 4 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 999 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 3 ) ) );
    }
    
    @Test
    public void createReferencesLowerBoundary7() {
	
	//test two references, doesn't fit perfectly into time periods
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 998 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 6 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
    @Test
    public void createReferencesLowerBoundary8() {
	
	//test two references, doesn't fit perfectly, but start happens
	//to be a perfect multiple of the time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 996 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 6 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 996 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    @Test
    public void createReferencesBoundary9() {
	//test two references, doesn't fit perfectly, but start happens
	//to be a perfect multiple of the time period, and so does the time interval
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 996 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 8 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 8 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(3) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 996 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
    @Test
    public void createReferencesBoundary10() {
	//test two references, that don't fit perfectly so that we have
	//one unit extra space on each side of the graph
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 6 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
    @Test
    public void createReferencesBoundary11() {
	//test two references, that don't fit perfectly so that we have
	//one unit extra space on left (smaller numbers) side only
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 5 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
        @Test
    public void createReferencesBoundary12() {
	//test two references, that don't fit perfectly so that we have
	//one unit extra space on right (larger numbers) side only
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 5 ) ) );
	System.out.println( start + " " + start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	System.out.println( references );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
    @Test
    public void createLabels1() {
        List<String> labels = TimeScales.createLabels(Arrays.asList(create(2013,03,12,1,30,15,123),
                create(2013,03,12,1,30,16,123),
                create(2013,03,12,1,30,17,123)));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15.123000000",
                "2013/03/12 01:30:16.123000000",
                "2013/03/12 01:30:17.123000000")));
    }
    
    @Test
    public void trimLabelsRight1() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:15.123000000",
                "2013/03/12 01:30:16.123000000",
                "2013/03/12 01:30:17.123000000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15.123",
                "2013/03/12 01:30:16.123",
                "2013/03/12 01:30:17.123")));
    }
    
    @Test
    public void trimLabelsRight2() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:15.123456789",
                "2013/03/12 01:30:16.123456790",
                "2013/03/12 01:30:17.123456791"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15.123456789",
                "2013/03/12 01:30:16.123456790",
                "2013/03/12 01:30:17.123456791")));
    }
    
    @Test
    public void trimLabelsRight3() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:15.123000000",
                "2013/03/12 01:30:16.123100000",
                "2013/03/12 01:30:17.123200000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15.1230",
                "2013/03/12 01:30:16.1231",
                "2013/03/12 01:30:17.1232")));
    }
    
    @Test
    public void trimLabelsRight4() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:15.100000000",
                "2013/03/12 01:30:16.200000000",
                "2013/03/12 01:30:17.300000000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15.1",
                "2013/03/12 01:30:16.2",
                "2013/03/12 01:30:17.3")));
    }
    
    @Test
    public void trimLabelsRight5() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:15.000000000",
                "2013/03/12 01:30:16.000000000",
                "2013/03/12 01:30:17.000000000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:15",
                "2013/03/12 01:30:16",
                "2013/03/12 01:30:17")));
    }
    
    @Test
    public void trimLabelsRight6() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:00.000000000",
                "2013/03/12 01:30:10.000000000",
                "2013/03/12 01:30:20.000000000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30:00",
                "2013/03/12 01:30:10",
                "2013/03/12 01:30:20")));
    }
    
    @Test
    public void trimLabelsRightRight7() {
        List<String> labels = TimeScales.trimLabelsRight(Arrays.asList("2013/03/12 01:30:00.000000000",
                "2013/03/12 01:31:00.000000000",
                "2013/03/12 01:32:00.000000000"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30",
                "2013/03/12 01:31",
                "2013/03/12 01:32")));
    }
    
    @Test
    public void trimLabelsRightLeft1() {
        List<String> labels = TimeScales.trimLabelsLeft(Arrays.asList("2013/03/12 01:30",
                "2013/03/12 01:31",
                "2013/03/12 01:32"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 01:30",
                "01:31",
                "01:32")));
    }
    
    @Test
    public void trimLabelsRightLeft2() {
        List<String> labels = TimeScales.trimLabelsLeft(Arrays.asList("2013/03/12 00:00",
                "2013/03/12 12:00",
                "2013/03/13 00:00"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 00:00",
                "12:00",
                "2013/03/13 00:00")));
    }
    
    @Test
    public void trimLabelsRightLeft3() {
        List<String> labels = TimeScales.trimLabelsLeft(Arrays.asList("2013/03/12 00:00:00.9",
                "2013/03/12 00:00:01.0",
                "2013/03/12 00:00:01.1"));
        assertThat(labels, equalTo(Arrays.asList("2013/03/12 00:00:00.9",
                "00:00:01.0",
                ".1")));
    }
    
    static Timestamp create(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        GregorianCalendar cal = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        cal.set(GregorianCalendar.MILLISECOND, millisecond);
        return Timestamp.of(cal.getTime());
    }
}