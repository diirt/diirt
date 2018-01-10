/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.diirt.util.time.TimeInterval;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Ignore;

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
        Instant start = Instant.now();
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofSeconds(8)));
        assertThat(linearScale.scaleTimestamp(start, timeInterval, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleTimestamp(start.plus(Duration.ofSeconds(2)), timeInterval, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleTimestamp(start.plus(Duration.ofSeconds(4)), timeInterval, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleTimestamp(start.plus(Duration.ofSeconds(8)), timeInterval, 1.0, 100.0), equalTo(100.0));
    }

    @Test
    public void references1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 123);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofSeconds(20)));
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
    @Ignore
    public void references2() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 100);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofSeconds(5)));
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
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 0 , 0 );
    ArrayList< Instant > noTimestamps = new ArrayList< Instant >();
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
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, -100 , -10 );
        assertTrue( false );
    }

    @Test(expected=IllegalArgumentException.class)
    public void createZeroReferences3() {
    //Test for max refs is less than min refs
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 1 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 99999 , 2 );
        assertTrue( false );
    }

    @Test
    @Ignore
    public void references1MsPeriod1() {
    //Test creating 2 references with the smallest milliseconds time interval
    //possible or 1 ms.
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 1 ) ) );
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
    @Ignore
    public void references1MsPeriod2() {
    //Test creating 3 references with the smallest milliseconds time interval
    //possible or 1 ms.
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 2 ) ) );
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
    @Ignore
    public void references1MsPeriod3() {
    //Test creating a large amount of references with a very small scale
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );

    //we can only go up to minutes, because going up to hours would make
    //the interval >1000000, which would take a while to run
    int MS_INTERVAL = 180000;
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( MS_INTERVAL ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );

    double[] normalValues = new double[ MS_INTERVAL+1 ];
    for ( int i=0 ; i<=MS_INTERVAL ; i++ ) {
        normalValues[ i ] = ((double)(i)/MS_INTERVAL);
    }
    ArrayList< Instant > times = new ArrayList< Instant >();
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
    @Ignore
    public void references1MsPeriod4() {
    //Test creating a medium amount of references with a very small scale
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );

    int MS_INTERVAL = 4321;
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( MS_INTERVAL ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 99999999 );

    double[] normalValues = new double[ MS_INTERVAL+1 ];
    for ( int i=0 ; i<=MS_INTERVAL ; i++ ) {
        normalValues[ i ] = ((double)(i)/MS_INTERVAL);
    }
    ArrayList< Instant > times = new ArrayList< Instant >();
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
    @Ignore
    public void references5MsPeriod1() {
    //Test creating 3 references with a medium milliseconds time interval
    //of 5 ms
    TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Instant start = TimeScalesTest.create( 2014 , 11 , 13 , 10 , 31 , 23 , 53 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 16 ) ) );
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

    @Test
    @Ignore
    public void references5MsPeriod2() {
    //test creating references that overflow into the next second
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Dec 24, 2014 11:59:58.989 PM
    //End: Dec 24, 2014 11:59:59.007 PM
        Instant start = TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 58 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 58 , 990 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 58 , 995 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 59 , 0 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 59 , 5 )
            ),
            Arrays.asList(
                "2014/12/24 23:59:58.990",
                ".995",
                "23:59:59.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references5MsPeriod3() {
    //test creating references that overflow into the next minute
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Dec 24, 2014 11:58:59.989 PM
    //End: Dec 24, 2014 11:59:00.007 PM
        Instant start = TimeScalesTest.create( 2014 , 12 , 24 , 23 , 58 , 59 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 58 , 59 , 990 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 58 , 59 , 995 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 0 , 5 )
            ),
            Arrays.asList(
                "2014/12/24 23:58:59.990",
                ".995",
                "23:59:00.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references5MsPeriod4() {
    //test creating references that overflow into the next hour
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Dec 24, 2014 10:59:59.989 PM
    //End: Dec 24, 2014 11:00:00.007 PM
        Instant start = TimeScalesTest.create( 2014 , 12 , 24 , 22 , 59 , 59 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 12 , 24 , 22 , 59 , 59 , 990 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 22 , 59 , 59 , 995 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 0 , 0 , 5 )
            ),
            Arrays.asList(
                "2014/12/24 22:59:59.990",
                ".995",
                "23:00:00.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references5MsPeriod5() {
    //test creating references that overflow into the next day
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Dec 24, 2014 11:59:59.989 PM
    //End: Dec 25, 2014 12:00:00.007 AM
        Instant start = TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 59 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 59 , 990 ),
                TimeScalesTest.create( 2014 , 12 , 24 , 23 , 59 , 59 , 995 ),
                TimeScalesTest.create( 2014 , 12 , 25 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 12 , 25 , 0 , 0 , 0 , 5 )
            ),
            Arrays.asList(
                "2014/12/24 23:59:59.990",
                ".995",
                "2014/12/25 00:00:00.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references5MsPeriod6() {
    //test creating references that overflow into the next month
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Nov 30, 2014 11:59:59.989 PM
    //End: Dec 1, 2014 12:00:00.007 AM
        Instant start = TimeScalesTest.create( 2014 , 11 , 30 , 23 , 59 , 59 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 11 , 30 , 23 , 59 , 59 , 990 ),
                TimeScalesTest.create( 2014 , 11 , 30 , 23 , 59 , 59 , 995 ),
                TimeScalesTest.create( 2014 , 12 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 12 , 1 , 0 , 0 , 0 , 5 )
            ),
            Arrays.asList(
                "2014/11/30 23:59:59.990",
                ".995",
                "2014/12/01 00:00:00.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references5MsPeriod7() {
    //test creating references that overflow into the next year
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Dec 31, 2014 11:59:59.989 PM
    //End: Jan 1, 2015 12:00:00.007 AM
        Instant start = TimeScalesTest.create( 2014 , 12 , 31 , 23 , 59 , 59 , 989 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 18 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                1.0/18.0,
                6.0/18.0,
                11.0/18.0,
                16.0/18.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 12 , 31 , 23 , 59 , 59 , 990 ),
                TimeScalesTest.create( 2014 , 12 , 31 , 23 , 59 , 59 , 995 ),
                TimeScalesTest.create( 2015 , 1 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2015 , 1 , 1 , 0 , 0 , 0 , 5 )
            ),
            Arrays.asList(
                "2014/12/31 23:59:59.990",
                ".995",
                "2015/01/01 00:00:00.000",
                ".005"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references500MsPeriod1() {
    //test 4 references with a 500 ms period
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: March 23, 2014 6:29:45.156 PM
    //End: March 23, 2014 6:29:47.156 PM
        Instant start = TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 45 , 156 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 2000 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 4 , 4 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                344.0/2000.0,
                844.0/2000.0,
                1344.0/2000.0,
                1844.0/2000.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 45 , 500 ),
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 46 , 0 ),
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 46 , 500 ),
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 47 , 0 )
            ),
            Arrays.asList(
                "2014/03/23 18:29:45.5",
                "18:29:46.0",
                ".5",
                "18:29:47.0"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references500MsPeriod2() {
    //test 2 references on a 500 ms period
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: March 23, 2014 6:29:59.800 PM
    //End: March 23, 2014 6:30:00.837 PM
        Instant start = TimeScalesTest.create( 2014 , 3 , 23 , 18 , 29 , 59 , 800 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofMillis( 1037 ) ) );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 2 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                200.0/1037.0,
                700.0/1037.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 30 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 3 , 23 , 18 , 30 , 0 , 500 )
            ),
            Arrays.asList(
                "2014/03/23 18:30:00.0",
                ".5"
            ),
            timeAxis);
    }

    @Test
    @Ignore
    public void references1MonthPeriod1() {
    //test 6 references with 1 month periods
    TimeScale linearScale = TimeScales.linearAbsoluteScale();

    //Start: Mar 23, 2014 18:00:00.000
    //End: Oct 19, 2014 18:00:00.000
        Instant start = TimeScalesTest.create( 2014 , 3 , 23 , 18 , 0 , 0 , 00 );
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(Duration.ofHours( 24*7*5*6 ) ) );
    //System.out.println( start.toDate() );
    //System.out.println( start.plus( TimeDuration.ofHours( 24*7*5*6 ) ).toDate() );
        TimeAxis timeAxis = linearScale.references( timeInterval, 2 , 7 );
        assertAxisEquals(
            timeInterval,
            new ArrayDouble(
                    198.0/5040.0,
                    918.0/5040.0,
                    1662.0/5040.0,
                    2382.0/5040.0,
                    3126.0/5040.0,
                    3870.0/5040.0,
                    4590.0/5040.0
            ),
            Arrays.asList(
                TimeScalesTest.create( 2014 , 4 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 5 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 6 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 7 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 8 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 9 , 1 , 0 , 0 , 0 , 0 ),
                TimeScalesTest.create( 2014 , 10 , 1 , 0 , 0 , 0 , 0 )
            ),
            Arrays.asList(
                "2014/04",
                "2014/05",
                "2014/06",
                "2014/07",
                "2014/08",
                "2014/09",
                "2014/10"
            ),
            timeAxis);
    }

    public static void assertAxisEquals(TimeInterval timeInterval, ListDouble normalizedValues, List<Instant> timestamps, List<String> labels, TimeAxis axis) {
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