package blogreader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kristof
 */
public abstract class DateUtil {

    private static final Logger logger = Logger.getLogger(DateUtil.class.getName());
    private static final SimpleDateFormat RFC822_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    private static final Map<String, TimeZone> TIME_ZONES = new HashMap<>();

    private DateUtil() {
    }
    
    /**
     * parses a RFC 822 formated string to a date
     * 
     * @example Wed, 02 Oct 2002 15:00:00 +0200
     * @example Tue, 24 Feb 2009 08:30:36 +0000
     * @param s
     * @return 
     */
    public static Date parseDateRfc822(final String s) {
        Date date = null;
        try {
            date = RFC822_DATE_FORMAT.parse(s);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * Find out if a year is a leap year or not.
     * 
     * @param year
     * @return 
     */
    public static boolean isLeapYear(int year) {
        if (year >= 1583) {
            // gregorian
            return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        }
        else {
            // julian
            return year % 4 == 0;
        }
    }
    
    /**
     * Speeds up getting a TimeZone by using a cache.
     * 
     * @param timeZoneID a NormalizedCustomID of the form GMT Sign TwoDigitHours : Minutes "GMT+02:00"
     * @return 
     */
    public static TimeZone getTimeZone(String timeZoneID) {
        assert timeZoneID != null;
        TimeZone tz = TIME_ZONES.get(timeZoneID);
        if (tz == null) {
            synchronized (TIME_ZONES) {
                if (!TIME_ZONES.containsKey(timeZoneID)) {
                    tz = TimeZone.getTimeZone(timeZoneID);
                    assert timeZoneID.length() == 9
                            && timeZoneID.startsWith("GMT")
                            && (timeZoneID.charAt(3) == '+' || timeZoneID.charAt(3) == '-')
                            && timeZoneID.charAt(6) == ':';
                    TIME_ZONES.put(timeZoneID, tz);
                } else {
                    tz = TIME_ZONES.get(timeZoneID);
                }
            }
        }
        return tz;
    }

    /**
     * create a date using GregorianCalendar and tries to avoid gotchas
     * 
     * @param year as in 1970, 2020, ...
     * @param month GregorianCalendar month (use the constants if you can) [0, 11]
     * @param date day of the month [1, 31]
     * @param hourOfDay hour of day as on a 24 hour clock [0, 23]
     * @param minute
     * @param second
     * @param milliSecond
     * @param timeZoneID a NormalizedCustomID of the form GMT Sign TwoDigitHours : Minutes "GMT+02:00"
     * @return
     */
    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int milliSecond, String timeZoneID) {
        assert month >= 0 && month <= 11;
        assert dayOfMonth >= 1 && dayOfMonth <= 31;
        assert dayOfMonth <= 28 || month != 1 || isLeapYear(year);
        assert dayOfMonth <= 29 || month != 1;
        assert (dayOfMonth <= 30 || !(month == 3 || month == 5 || month == 8 || month == 10));
        assert hourOfDay >= 0 && hourOfDay <= 23;
        assert minute >= 0 && minute <= 59;
        assert second >= 0 && second <= 59;
        assert milliSecond >= 0 && milliSecond <= 999;
        
        Calendar cal = GregorianCalendar.getInstance(getTimeZone(timeZoneID));
        cal.set(year, month, dayOfMonth, hourOfDay, minute, second);
        cal.set(GregorianCalendar.MILLISECOND, milliSecond);
        return cal.getTime();
    }

}
