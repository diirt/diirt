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
    public void createReferencesLowerBoundary3() {
	//test lower boundary case: 2 references, milliseconds roll over to next second
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 2 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 2 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
   
    @Test
    public void createReferencesLowerBoundary4() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 3 ) );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 999 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 2 ) ) );
    }
    
    @Test
    public void createReferencesLowerBoundary5() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 998 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 4 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    @Test
    public void createReferencesLowerBoundary6() {
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 4 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    @Test
    public void createReferencesLowerBoundary7() {
	
	//test two references, doesn't fit perfectly into time periods
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 998 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 6 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
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
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
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
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
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
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
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
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
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
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 4 ) ) );
    }
    
    @Test
    public void createReferenceBad1() {
	//test end before start
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( -5 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferenceBad2() {
	//test end equals start and is not a multiple of the time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 1 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 0 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferenceBad3() {
	//test end equals start and is a multiple of the time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 0 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    @Test
    @Ignore //TODO uncaught / by 0 exception
    public void createReferenceBad4() {
	//time period is 0
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 1 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 0 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferenceBad5() {
	//test negative timer interval (i.e. end is before start)
		//test end equals start and is a multiple of the time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 0 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 4 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
    }
    
    @Test
    public void createReferencesEmpty1() {
	//test time period too big
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 1 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( -5 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferencesEmpty2() {
	//test units do not get mixed up
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( SECOND , 3 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferencesEmpty3() {
	//test units do not get mixed up
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMillis( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 3000 ) );
	assertThat( references.size() , equalTo(0) );
    }
    
    @Test
    public void createReferencesOverflowMilliseconds1() {
	//test units do not get mixed up
	//and they can overflow into a larger unit
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 3000 ) );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 3 , 0 ) ) );
    }
    
    @Test
    public void createReferencesOverflowMilliseconds2() {
	//test units do not get mixed up and they can overflow into 
	//a larger unit
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 999 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1333 ) );
	assertThat( references.size() , equalTo(3) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 333 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 2 , 666 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 3 , 999 ) ) );
    }
    
    @Test
    public void createReferencesOverflowMilliseconds3() {
	//test units do not get mixed up and they can overflow into 
	//a larger unit. also test when we are 1 millisecond off from next
	//reference line
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 998 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1333 ) );
	assertThat( references.size() , equalTo(2) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 333 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 2 , 666 ) ) );
    }
    
    @Test
    public void createReferencesOverflowMilliseconds4() {
	//test units do not get mixed up and they can overflow into 
	//a larger unit. also test when we are 1 millisecond after next
	//reference line
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 1 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MILLISECOND , 1333 ) );
	assertThat( references.size() , equalTo(3) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 2 , 333 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 3 , 666 ) ) );
    }
    
    @Test
    public void createReferencesSeconds1() {
	//test seconds: straightforward, 5 seconds interval over 1 second periods
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 5 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( SECOND , 1 ) );
	assertThat( references.size() , equalTo(6) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 1 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 2 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 3 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 4 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 5 , 0 ) ) );
    }
    
    @Test
    public void createReferencesSeconds2() {
	//test seconds: straightforward, 10 second interval over 2 second periods
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 10 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( SECOND , 2 ) );
	assertThat( references.size() , equalTo(6) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 2 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 4 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 6 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 8 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 10 , 0 ) ) );
    }
    
    @Test
    public void createReferencesSeconds3() {
	//test seconds: straightforward, 30 second interval over 5 second periods,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 1 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 30 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( SECOND , 5 ) );
	assertThat( references.size() , equalTo(6) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 5 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 10 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 15 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 20 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 25 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 30 , 0 ) ) );
    }
    
    @Test
    public void createReferencesSeconds4() {
	//test seconds: straightforward, 50 second interval over 10 second periods,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 500 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofSeconds( 50 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( SECOND , 10 ) );
	assertThat( references.size() , equalTo(5) );
	assertThat( references.get( 0 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 10 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 20 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 30 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 40 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create(2014 , 11 , 22 , 11 , 30 , 50 , 0 ) ) );
    }
    
    @Test
    public void createReferencesMinutes1() {
	//test minutes: straightforward, 10 minutes interval over 1 minute periods,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 500 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMinutes( 10 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MINUTE , 1 ) );
	assertThat( references.size() , equalTo(10) );

	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 22 , 11 , 31 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 22 , 11 , 32 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 22 , 11 , 33 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 11 , 22 , 11 , 34 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2014 , 11 , 22 , 11 , 35 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2014 , 11 , 22 , 11 , 36 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2014 , 11 , 22 , 11 , 37 , 0 , 0 ) ) );
	assertThat( references.get( 7 ) , equalTo( create( 2014 , 11 , 22 , 11 , 38 , 0 , 0 ) ) );
	assertThat( references.get( 8 ) , equalTo( create( 2014 , 11 , 22 , 11 , 39 , 0 , 0 ) ) );
	assertThat( references.get( 9 ) , equalTo( create( 2014 , 11 , 22 , 11 , 40 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesMinutes2() {
	//test minutes: straightforward, 50 minutes interval over 10 minute periods,
	//starts on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofMinutes( 50 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MINUTE , 10 ) );
	assertThat( references.size() , equalTo(6) );

	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 22 , 11 , 30 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 22 , 11 , 40 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 22 , 11 , 50 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 11 , 22 , 12 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2014 , 11 , 22 , 12 , 10 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2014 , 11 , 22 , 12 , 20 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesHours1() {
	//test hours: straightforward, 3 hours interval over 1 hour periods,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 679 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.HOUR_FIELD_ID , 1 ) );
	assertThat( references.size() , equalTo(3) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 22 , 12 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 22 , 13 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 22 , 14 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    //Note: this only works if hour type is set to HOUR_OF_DAY
    //This failes if hour type is set to HOUR
    public void createReferencesHours2() {
	//test hours: straightforward, 30 hour interval over 5 hour periods,
	//starts on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 0 , 0 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 30 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.HOUR_FIELD_ID , 5 ) );
	assertThat( references.size() , equalTo(7) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 22 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 22 , 5 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 22 , 10 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 11 , 22 , 15 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2014 , 11 , 22 , 20 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2014 , 11 , 23 , 1 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2014 , 11 , 23 , 6 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesDays1() {
	//test days: straightforward, 7 day interval over 1 hour periods,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 27 , 4 , 12 , 24 );
	cal.set( GregorianCalendar.MILLISECOND , 234 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*7 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.DAY_FIELD_ID , 1 ) );
	assertThat( references.size() , equalTo( 7 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 28 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 29 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 30 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 12 , 1 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2014 , 12 , 2 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2014 , 12 , 3 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2014 , 12 , 4 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesDays2() {
	//test days: straightforward, 30 day interval over 5 day periods,
	//starts on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 20 , 0 , 0 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*30 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.DAY_FIELD_ID , 5 ) );
	assertThat( references.size() , equalTo( 7 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 20 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 11 , 25 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 11 , 30 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 12 , 5 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2014 , 12 , 10 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2014 , 12 , 15 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2014 , 12 , 20 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesDays3() {
	//test days wrapping over to next year
	GregorianCalendar cal = new GregorianCalendar( 2014 , 11 , 27 , 4 , 12 , 24 );
	cal.set( GregorianCalendar.MILLISECOND , 234 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*7 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.DAY_FIELD_ID , 1 ) );
	assertThat( references.size() , equalTo( 7 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 12 , 28 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 12 , 29 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 12 , 30 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2014 , 12 , 31 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2015 , 1 , 1 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2015 , 1 , 2 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2015 , 1 , 3 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesLeapDay() {
	//test days wrapping over to March when Feburary is of a leap year
	Timestamp start = create(2012, 2, 27, 4, 12, 24, 234);
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*7 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.DAY_FIELD_ID , 1 ) );
	assertThat( references.size() , equalTo( 7 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2012 , 2 , 28 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2012 , 2 , 29 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2012 , 3 , 1 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create( 2012 , 3 , 2 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create( 2012 , 3 , 3 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2012 , 3 , 4 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 6 ) , equalTo( create( 2012 , 3 , 5 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    //@Ignore	    //Weeks do not round up to the start of the next week
		    //rather, they just start on the day rounded up
		    //Does setting the week have any effect on GregorianCalendar
		    //the way we are using it?
    public void createReferencesWeeks1() {
	//test weeks: straightforward, 3 weeks interval over 1 week periods
	//not starting on perfect multiple of period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 27 , 4 , 12 , 24 );
	cal.set( GregorianCalendar.MILLISECOND , 234 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*7*3 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( TimeScales.WEEK_FIELD_ID , 1 ) );
	assertThat( references.size() , equalTo( 3 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 11 , 30 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2014 , 12 , 7 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create( 2014 , 12 , 14 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesMonths1() {
	//test months: straightforward, 5 months interval over 1 month periods
	//not starting on perfect multiple of period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 27 , 4 , 12 , 24 );
	cal.set( GregorianCalendar.MILLISECOND , 234 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*7*5*5 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( MONTH , 1 ) );
	System.out.println( start.plus( TimeDuration.ofHours( 24*7*5*5 ) ).toDate() );
	System.out.println( references.get( 0 ).toDate().toString() );
	assertThat( references.size() , equalTo( 6 ) );
	assertThat( references.get( 0 ) , equalTo( create( 2014 , 12 , 1 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create( 2015 , 1 , 1 , 0 , 0 , 0 , 0 ) ) );	
	assertThat( references.get( 2 ) , equalTo( create( 2015 , 2 , 1 , 0 , 0 , 0 , 0 ) ) );	
	assertThat( references.get( 3 ) , equalTo( create( 2015 , 3 , 1 , 0 , 0 , 0 , 0 ) ) );	
	assertThat( references.get( 4 ) , equalTo( create( 2015 , 4 , 1 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 5 ) , equalTo( create( 2015 , 5 , 1 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    @Ignore //Failed: off by exactly 3 days because rounding down does not round
	    //perfectly to January 1 - instead, it rounds down to January 4
    public void createReferencesYears1() {
	//test years: straightforward, 50 years interval over 10 year period,
	//but does not start on perfect multiple of time period
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 500 );
	Timestamp start = Timestamp.of( cal.getTime() );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( 24*366*50 ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( YEAR , 10 ) );
	assertThat( references.size() , equalTo(5) );

	assertThat( references.get( 0 ) , equalTo( create(2020 , 1 , TimeScales.FIRST_DAY , TimeScales.FIRST_HOUR , 0 , 0 , 0 ) ) );
	assertThat( references.get( 1 ) , equalTo( create(2030 , 0 , 0 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 2 ) , equalTo( create(2040 , 0 , 0 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 3 ) , equalTo( create(2050 , 0 , 0 , 0 , 0 , 0 , 0 ) ) );
	assertThat( references.get( 4 ) , equalTo( create(2060 , 0 , 0 , 0 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    @Ignore //What's the maximum bound on years?
    public void createReferencesOverflow() {
	//test trying to overflow years
	GregorianCalendar cal = new GregorianCalendar( 2014 , 10 , 22 , 11 , 30 , 0 );
	cal.set( GregorianCalendar.MILLISECOND , 0 );
	Timestamp start = Timestamp.of( cal.getTime() );
	long hours = 24l * 366l * 999999999l;
	System.out.println( hours );
	TimeInterval timeInterval = TimeInterval.between( start , start.plus( TimeDuration.ofHours( hours ) ) );
	List<Timestamp> references = TimeScales.createReferences( timeInterval , new TimePeriod( YEAR , 999999999l ) );
	System.out.println( new TimePeriod( YEAR , 999999999l ) );
	System.out.println( TimeDuration.ofHours( hours ) );
	System.out.println( hours + " " + Double.valueOf( hours ).doubleValue() );
	assertThat( references.size() , equalTo(1) );
	assertThat( references.get( 0 ) , equalTo( create( 2014+999999999 , 11 , 22 , 11 , 0 , 0 , 0 ) ) );
    }
    
    @Test
    public void createReferencesStructureTest1() {
	//test the structure of the createReferences() method
	//case 1: while (endCal.compareTo(cal) >= 0) exectures and
	//if (timeInterval.contains(newTime)) executes.
	//this is achieved in a prior test case:
	createReferencesSeconds1();
    }
    
    @Test
    public void createReferencesStructureTest2() {
	//test the structure of the createReferences() method
	//case 2: while (endCal.compareTo(cal) >= 0) does not execute
	//this is acheived in a prior test case:
	createReferencesEmpty3();
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