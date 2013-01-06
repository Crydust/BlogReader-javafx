package blogreader.util;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author kristof
 */
public abstract class DateUtil {

    private static final Logger logger = Logger.getLogger(DateUtil.class.getName());
    private static final DateTimeFormatter RFC822_DATETIMEFORMAT = DateTimeFormat
            .forPattern("EEE, dd MMM yyyy HH:mm:ss Z")
            .withLocale(Locale.ENGLISH);

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
    @Nullable
    public static DateTime parseDateRfc822(@Nullable final String s) {
        DateTime date = null;
        if (s != null) {
            try {
                date = RFC822_DATETIMEFORMAT.parseDateTime(s);
            } catch (IllegalArgumentException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return date;
    }

    /**
     * create a DateTime and try to avoid gotchas
     *
     * @param year as in 1970, 2020, ...
     * @param month human month, use DateTimeConstants if unsure [1, 12]
     * @param date day of the month [1, 31]
     * @param hourOfDay hour of day as on a 24 hour clock [0, 23]
     * @param minute
     * @param second
     * @param milliSecond
     * @param timeZoneID a locale independent, fixed offset, datetime zone can
     * be specified. The form [+-]hh:mm can be used.
     * @return
     */
    @Nonnull
    public static DateTime createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int milliSecond, @Nonnull String timeZoneID) {
        assert month >= 1 && month <= 12;
        assert dayOfMonth >= 1 && dayOfMonth <= 31;
        assert dayOfMonth <= 29 || month != 2;
        assert (dayOfMonth <= 30 || !(month == 4 || month == 6 || month == 9 || month == 11));
        assert hourOfDay >= 0 && hourOfDay <= 23;
        assert minute >= 0 && minute <= 59;
        assert second >= 0 && second <= 59;
        assert milliSecond >= 0 && milliSecond <= 999;
        assert timeZoneID != null
                && timeZoneID.length() == 6
                && (timeZoneID.charAt(0) == '+' || timeZoneID.charAt(0) == '-')
                && timeZoneID.charAt(3) == ':';

        return new DateTime(year, month, dayOfMonth, hourOfDay, minute, second, milliSecond, DateTimeZone.forID(timeZoneID));
    }
}
