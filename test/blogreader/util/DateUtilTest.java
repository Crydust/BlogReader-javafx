/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blogreader.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author kristof
 */
public class DateUtilTest {

    /**
     * Test of parseDateRfc822 method, of class DateUtil.
     */
    @Test
    public void testParseDateRfc822() {
        String s = "Wed, 02 Oct 2002 15:00:00 +0200";
        DateTime expResult = DateUtil.createDate(
                2002, DateTimeConstants.OCTOBER, 2, 
                15, 0, 0, 0,
                "+02:00");
        DateTime result = DateUtil.parseDateRfc822(s);
        assertNotNull(result);
        if (result != null) {
            assertEquals(expResult.getMillis(), result.getMillis());
        }
        //assertEquals(expResult, result);
        //assertTrue(expResult.equals(result));
        assertEquals(0, DateTimeComparator.getInstance().compare(expResult, result));
        
        s = "Tue, 24 Feb 2009 08:30:36 +0000";
        expResult = DateUtil.createDate(
                2009, DateTimeConstants.FEBRUARY, 24, 
                8, 30, 36, 0,
                "+00:00");
        result = DateUtil.parseDateRfc822(s);
        assertNotNull(result);
        if (result != null) {
            assertEquals(expResult.getMillis(), result.getMillis());
        }
        //assertEquals(expResult, result);
        //assertTrue(expResult.equals(result));
        assertEquals(0, DateTimeComparator.getInstance().compare(expResult, result));
    }
    
    /**
     * Test of createDate method, of class DateUtil.
     */
    @Test
    public void testCreateDate() {
        for (int year = 1900; year < 2100; year++) {
            for (int month = 1; month < 13; month++) {
                for (int day = 1; day < 32; day++) {
                    if (month == 2 && day == 29 && !isLeapYear(year)) {
                        break;
                    } else if(month == 2 && day > 28) {
                        break;
                    } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                        break;
                    }
                    //System.out.format("%s %s %s%n", year, month, day);
                    DateUtil.createDate(
                            year, month, day, 
                            0, 0, 0, 0,
                            "+00:00");
                }
            }
        }
    }
    
    private static boolean isLeapYear(int year) {
        if (year >= 1583) {
            // gregorian
            return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        }
        else {
            // julian
            return year % 4 == 0;
        }
    }
    
}
