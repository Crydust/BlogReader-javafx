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
    
    public static Date parseDateRfc822(final String s) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            date = format.parse(s);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
}
