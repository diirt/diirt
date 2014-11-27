/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListInt;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;

/**
 *
 * @author carcassi
 */
public class TimeScales {
    
    final public static int HOUR_FIELD_ID = GregorianCalendar.HOUR_OF_DAY;
    final public static int FIRST_HOUR = 0;
    final public static int DAY_FIELD_ID = GregorianCalendar.DAY_OF_WEEK;
    final public static int FIRST_DAY = 1;
    final public static int WEEK_FIELD_ID = GregorianCalendar.WEEK_OF_MONTH;
    final public static int FIRST_WEEK = 1;
    
    public static TimeScale linearAbsoluteScale() {
        return new LinearAbsoluteTimeScale();
    }
    
    static class TimePeriod {
        public int fieldId;
        public double amount;

        public TimePeriod(int fieldId, double amount) {
            this.fieldId = fieldId;
            this.amount = amount;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.fieldId;
            hash = 59 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TimePeriod other = (TimePeriod) obj;
            if (this.fieldId != other.fieldId) {
                return false;
            }
            if (Double.doubleToLongBits(this.amount) != Double.doubleToLongBits(other.amount)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "TimePeriod{" + "fieldId=" + fieldId + ", amount=" + amount + '}';
        }
        
    }

    static TimePeriod nextUp(TimePeriod period) {
	//TODO nanoseconds rounding up
	switch(period.fieldId) {
            case GregorianCalendar.MILLISECOND:
                if (period.amount < 2) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 2);
                }
                if (period.amount < 5) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 5);
                }
                if (period.amount < 10) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 10);
                }
                if (period.amount < 20) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 20);
                }
                if (period.amount < 50) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 50);
                }
                if (period.amount < 100) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 100);
                }
                if (period.amount < 200) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 200);
                }
                if (period.amount < 500) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
                }
                return new TimePeriod(GregorianCalendar.SECOND, 1);
            case GregorianCalendar.SECOND:
                if (period.amount < 2) {
                    return new TimePeriod(GregorianCalendar.SECOND, 2);
                }
                if (period.amount < 5) {
                    return new TimePeriod(GregorianCalendar.SECOND, 5);
                }
                if (period.amount < 10) {
                    return new TimePeriod(GregorianCalendar.SECOND, 10);
                }
                if (period.amount < 15) {
                    return new TimePeriod(GregorianCalendar.SECOND, 15);
                }
                if (period.amount < 30) {
                    return new TimePeriod(GregorianCalendar.SECOND, 30);
                }
                return new TimePeriod(GregorianCalendar.MINUTE, 1);
	    case GregorianCalendar.MINUTE:
		if ( period.amount < 2 ) {
		    return new TimePeriod( GregorianCalendar.MINUTE , 2 );
		}
		if ( period.amount < 5 ) {
		    return new TimePeriod( GregorianCalendar.MINUTE , 5 );
		}
		if ( period.amount < 10 ) {
		    return new TimePeriod( GregorianCalendar.MINUTE , 10 );
		}
		if ( period.amount < 15 ) {
		    return new TimePeriod( GregorianCalendar.MINUTE , 15 );
		}
		if ( period.amount < 30 ) {
		    return new TimePeriod( GregorianCalendar.MINUTE , 30 );
		}
		return new TimePeriod( HOUR_FIELD_ID, 1 );
	    case HOUR_FIELD_ID:
		if ( period.amount < 2 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 2 );
		}
		if ( period.amount < 3 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 3 );
		}
		if ( period.amount < 6 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 6 );
		}
		if ( period.amount < 12 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 12 );
		}
		
		//*MC why is this necessary? otherwise, we get error
		if ( period.amount < 24 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 24 );
		}
		return new TimePeriod( DAY_FIELD_ID , 1 );
	    case DAY_FIELD_ID:
		if ( period.amount < 2 ) {
		    return new TimePeriod( DAY_FIELD_ID , 2 );
		}
		if ( period.amount < 4 ) {
		    return new TimePeriod( DAY_FIELD_ID , 4 );
		}
		return new TimePeriod( WEEK_FIELD_ID , 1 );
	    case WEEK_FIELD_ID:
		if ( period.amount < 2 ) {
		    return new TimePeriod( WEEK_FIELD_ID , 2 );
		}
		return new TimePeriod( GregorianCalendar.MONTH , 1 );
	    case GregorianCalendar.MONTH:
		if ( period.amount < 2 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 2 );
		}
		if ( period.amount < 4 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 4 );
		}
		if ( period.amount < 8 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 8 );
		}
		return new TimePeriod( GregorianCalendar.YEAR , 1 );
	    case GregorianCalendar.YEAR:
		return new TimePeriod( GregorianCalendar.YEAR , period.amount/4+1 );
        }
        return null;
    }
    
    /**
     * Determines the time(s) that will be represented by reference lines on
     * a time graph.
     * 
     * @param timeInterval the interval of time spanning the duration of the
     * time graph
     * @param period the interval of time between each reference line
     * @return a list of times evenly spaced by the duration of <code>period</code>
     * and encompassing the duration of <code>timeInterval</code>
     */
    static List<Timestamp> createReferences(TimeInterval timeInterval, TimePeriod period) {
        Date start = timeInterval.getStart().toDate();
        Date end = timeInterval.getEnd().toDate();
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(end);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(start);
        round(cal, period.fieldId);
        cal.set(period.fieldId, (cal.get(period.fieldId) / (int) period.amount) * (int) period.amount);
        List<Timestamp> references = new ArrayList<>();
        while (endCal.compareTo(cal) >= 0) {
            Timestamp newTime = Timestamp.of(cal.getTime());
            if (timeInterval.contains(newTime)) {
                references.add(newTime);
            }
            cal.add(period.fieldId, (int) period.amount);
        }
        return references;
    }

    static void round(GregorianCalendar cal, int field) {
        
        if (GregorianCalendar.MILLISECOND == field) {
            return;
        }
        cal.set(GregorianCalendar.MILLISECOND, 0);
        
        if (GregorianCalendar.SECOND == field) {
            return;
        }
        cal.set(GregorianCalendar.SECOND, 0);

        if (GregorianCalendar.MINUTE == field) {
            return;
        }
        cal.set(GregorianCalendar.MINUTE, 0);
	
	if ( HOUR_FIELD_ID == field ) {
	    return;
	}
	cal.set( HOUR_FIELD_ID , FIRST_HOUR );
	
	if ( DAY_FIELD_ID == field ) {
	    return;
	}
	cal.set(DAY_FIELD_ID , FIRST_DAY );
	
	if ( WEEK_FIELD_ID == field ) {
	    return;
	}
	
	//here, we are rounding down to the first week (i.e. the first day of
	//the month), so the day of the week and the week of the month no 
	//longer matter - we just set the day to be the first day of the month
	cal.set( GregorianCalendar.DAY_OF_MONTH , 1 );
	
	if ( GregorianCalendar.MONTH == field ) {
	    return;
	}
	
	cal.set( GregorianCalendar.MONTH , 0 );
	
	if ( GregorianCalendar.YEAR == field ) {
	    return;
	}
	cal.set( GregorianCalendar.YEAR , 0 );
	
        return;
    }
    
    static TimePeriod nextDown(TimePeriod period) {
        switch(period.fieldId) {
	    case GregorianCalendar.YEAR:
		return new TimePeriod( GregorianCalendar.YEAR , period.amount/4+1 );
	    case GregorianCalendar.MONTH:
		if ( period.amount > 8 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 8 );
		}
		if ( period.amount > 4 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 4 );
		}
		if ( period.amount > 2 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 2 );
		}
		if ( period.amount > 1 ) {
		    return new TimePeriod( GregorianCalendar.MONTH , 1 );
		}
		return new TimePeriod( WEEK_FIELD_ID , 2 );
	    case WEEK_FIELD_ID:
		if ( period.amount > 2 ) {
		    return new TimePeriod( WEEK_FIELD_ID , 2 );
		}
		if ( period.amount > 1 ) {
		    return new TimePeriod( WEEK_FIELD_ID , 1 );
		}
		return new TimePeriod( DAY_FIELD_ID , 3 );
	    case DAY_FIELD_ID:
		if ( period.amount > 3 ) {
		    return new TimePeriod( DAY_FIELD_ID , 3 );
		}
		if ( period.amount > 1 ) {
		    return new TimePeriod( DAY_FIELD_ID , 1 );
		}
		return new TimePeriod( HOUR_FIELD_ID , 12 );
	    case HOUR_FIELD_ID:
		if ( period.amount > 12 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 12 );
		}
		if ( period.amount > 8 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 8 );
		}
		if ( period.amount > 4 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 4 );
		}
		if ( period.amount > 2 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 2 );
		}
		if ( period.amount > 1 ) {
		    return new TimePeriod( HOUR_FIELD_ID , 1 );
		}
		return new TimePeriod( GregorianCalendar.MINUTE , 30 );
            case GregorianCalendar.MINUTE:
                return new TimePeriod(GregorianCalendar.SECOND, 30);
            case GregorianCalendar.SECOND:
                if (period.amount > 30) {
                    return new TimePeriod(GregorianCalendar.SECOND, 30);
                }
                if (period.amount > 15) {
                    return new TimePeriod(GregorianCalendar.SECOND, 15);
                }
                if (period.amount > 10) {
                    return new TimePeriod(GregorianCalendar.SECOND, 10);
                }
                if (period.amount > 5) {
                    return new TimePeriod(GregorianCalendar.SECOND, 5);
                }
                if (period.amount > 2) {
                    return new TimePeriod(GregorianCalendar.SECOND, 2);
                }
                if (period.amount > 1) {
                    return new TimePeriod(GregorianCalendar.SECOND, 1);
                }
                return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
            case GregorianCalendar.MILLISECOND:
                if (period.amount > 500) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
                }
                if (period.amount > 200) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 200);
                }
                if (period.amount > 100) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 100);
                }
                if (period.amount > 50) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 50);
                }
                if (period.amount > 20) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 20);
                }
                if (period.amount > 10) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 10);
                }
                if (period.amount > 5) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 5);
                }
                if (period.amount > 2) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 2);
                }
                if (period.amount > 1) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 1);
                }
        }
        return new TimePeriod( GregorianCalendar.MILLISECOND , 1 );
	
	//TODO nanoseconds rounding down
    }
    
    static TimePeriod toTimePeriod(double seconds) {
	if ( seconds >= 36288000 ) {
	    return new TimePeriod( GregorianCalendar.YEAR , seconds/3024000 );
	}
	if ( seconds >= 3024000 ) {
	    return new TimePeriod( GregorianCalendar.MONTH , seconds/3024000 );
	}
	if ( seconds >= 604800 ) {
	    return new TimePeriod( WEEK_FIELD_ID , seconds/604800.0 );
	}
	if ( seconds >= 86400 ) {
	    return new TimePeriod( DAY_FIELD_ID , seconds/86400.0 );
	}
	if ( seconds >= 3600 ) {
	    return new TimePeriod( HOUR_FIELD_ID , seconds/3600.0 );
	}
        if (seconds >= 60) {
            return new TimePeriod(GregorianCalendar.MINUTE, seconds / 60.0);
        }
        if (seconds >= 1) {
            return new TimePeriod(GregorianCalendar.SECOND, seconds);
        }
        return new TimePeriod(GregorianCalendar.MILLISECOND, 1000*seconds);
    }
    
    static double normalize(Timestamp time, TimeInterval timeInterval) {
        // XXX: if interval is more than 292 years, this will not work
        double range = timeInterval.getEnd().durationFrom(timeInterval.getStart()).toNanosLong();
        double value = time.durationBetween(timeInterval.getStart()).toNanosLong();
        return value / range;
    }
    
    private static TimestampFormat format = new TimestampFormat("yyyy/MM/dd HH:mm:ss.NNNNNNNNN");
    private static ArrayInt possibleStopFromEnd = new ArrayInt(0,1,2,3,4,5,6,7,8,10,13,19,22,25,28);
    private static ArrayInt possibleStopFromStart = new ArrayInt(0,11,19,28);
    private static String zeroFormat = "0000/01/01 00:00:00.000000000";
    
    static List<String> createLabels(List<Timestamp> timestamps) {
        if (timestamps.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<String> result = new ArrayList<>(timestamps.size());
        for (Timestamp timestamp : timestamps) {
            result.add(format.format(timestamp));
        }
        
        return result;
    }
    
    static int commonEnd(String a, String b) {
        int currentStopFromEnd = 0;
        while(a.charAt(b.length() - 1 - currentStopFromEnd) == b.charAt(b.length() - 1 - currentStopFromEnd)) {
            currentStopFromEnd++;
        }
        return currentStopFromEnd;
    }
    
    static int commonStart(String a, String b) {
        int commonStart = 0;
        while(a.charAt(commonStart) == b.charAt(commonStart)) {
            commonStart++;
        }
        return commonStart;
    }
    
    static List<String> trimLabelsRight(List<String> labels) {
        if (labels.isEmpty()) {
            return labels;
        }
        
        // Calculate the useless part common to all strings
        int currentStopFromEnd = zeroFormat.length();
        for (int i = 0; i < labels.size(); i++) {
            String otherLabel = labels.get(i);
            currentStopFromEnd = Math.min(currentStopFromEnd, commonEnd(otherLabel, zeroFormat));
        }
        
        // Round down to a possible stop
        int finalStop = 0;
        for (int i = 0; possibleStopFromEnd.getInt(i) <= currentStopFromEnd; i++) {
            finalStop = possibleStopFromEnd.getInt(i);
        }
        
        if (finalStop == 0) {
            return labels;
        }
        
        // Trim labels
        List<String> result = new ArrayList<>(labels.size());
        for (String label : labels) {
            result.add(label.substring(0, zeroFormat.length() - finalStop));
        }
        
        return result;
    }
    
    static List<String> trimLabelsLeft(List<String> labels) {
        if (labels.isEmpty()) {
            return labels;
        }
        
        List<String> result = new ArrayList<>(labels.size());
        String previousLabel = labels.get(0);
        result.add(previousLabel);
        
        for (int i = 1; i < labels.size(); i++) {
            String nextLabel = labels.get(i);
            int commonStart = commonStart(previousLabel, nextLabel);
            int finalStart = 0;
            for (int j = 0; possibleStopFromStart.getInt(j) <= commonStart; j++) {
                finalStart = possibleStopFromStart.getInt(j);
            }
            result.add(nextLabel.substring(finalStart, nextLabel.length()));
            previousLabel = nextLabel;
        }
        
        return result;
    }
    
    static List<String> trimLabels( List< String > labels ) {
	//TODO
	return null;
    }
}
