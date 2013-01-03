package blogreader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kristof
 */
public abstract class DateUtil {

    private static final Logger logger = Logger.getLogger(DateUtil.class.getName());

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
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            date = format.parse(s);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
}
