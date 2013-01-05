/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blogreader.util;

import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Date expResult = DateUtil.createDate(
                2002, GregorianCalendar.OCTOBER, 2, 
                15, 0, 0, 0,
                "GMT+02:00");
        Date result = DateUtil.parseDateRfc822(s);
        assertEquals(expResult, result);
        assertEquals(expResult.getTime(), result.getTime());
        
        s = "Tue, 24 Feb 2009 08:30:36 +0000";
        expResult = DateUtil.createDate(
                2009, GregorianCalendar.FEBRUARY, 24, 
                8, 30, 36, 0,
                "GMT+00:00");
        result = DateUtil.parseDateRfc822(s);
        assertEquals(expResult, result);
        assertEquals(expResult.getTime(), result.getTime());
    }
    
    
    /**
     * Test of createDate method, of class DateUtil.
     */
    @Test
    public void testCreateDate() {
        for (int year = 1900; year < 2100; year++) {
            for (int month = 0; month < 12; month++) {
                for (int day = 1; day < 32; day++) {
                    if (month == 1 && day == 29 && !DateUtil.isLeapYear(year)) {
                        break;
                    } else if(month == 1 && day > 28) {
                        break;
                    } else if ((month == 3 || month == 5 || month == 8 || month == 10) && day > 30) {
                        break;
                    }
                    //System.out.format("%s %s %s%n", year, month, day);
                    DateUtil.createDate(
                            year, month, day, 
                            0, 0, 0, 0,
                            "GMT+00:00");
                }
            }
        }
    }
    
    
}
