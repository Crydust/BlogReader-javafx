/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blogreader.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 * @author kristof
 */
public class DocumentLoaderTest {

    /**
     * Test of loadDocument method, of class DocumentLoader.
     *
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testLoadDocument_InputStream() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(64)
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<rss>")
                .append("<channel>")
                .append("<title>Kristof Neirynck</title>")
                .append("<item>")
                .append("<title>Optimizing JavaScript is harder than ever</title>")
                .append("<link>http://www.crydust.be/blog/2011/06/11/optimizing-javascript/</link>")
                .append("<pubDate>Sat, 11 Jun 2011 15:57:44 +0000</pubDate>")
                .append("<description><![CDATA[We&#8217;ve all got our favorite browser. And while developing for the web we test in that one browser first. Optimizing JavaScript for one browser only is a tempting idea. I mean &#8230; how much difference could there be between browser engines anyway? Let&#8217;s use the Fibonacci sequence as an example. I wrote six different versions [...]]]></description>")
                .append("<content:encoded><![CDATA[<p>We&#8217;ve all got our favorite browser. And while developing for the web we test in that one browser first. Optimizing JavaScript for one browser only is a tempting idea. I mean &#8230; how much difference could there be between browser engines anyway?</p>\n<p><a href=\"http://www.crydust.be/blog/wp-content/uploads/jsperf_fibonacci.png\"><img src=\"http://www.crydust.be/blog/wp-content/uploads/jsperf_fibonacci-150x150.png\" alt=\"\" title=\"jsperf_fibonacci\" width=\"150\" height=\"150\" class=\"alignright size-thumbnail wp-image-153\" /></a> Let&#8217;s use the Fibonacci sequence as an example. I wrote <a href=\"http://jsperf.com/fibonacci4\">six different versions</a> to calculate it. Obviously the naive, recursive way of calculating it is the slowest, but that is where similarities between browsers end. Internet explorer seems to be faster at looping than rounding numbers. And Chrome seems to favor local variables over global ones so much it doesn&#8217;t mind doing a lot more calculations.</p>]]></content:encoded>")
                .append("</item>")
                .append("</channel>")
                .append("</rss>");
         InputStream in = StringUtil.inputStreamFromString(sb.toString(), "UTF-8");
         Document result = DocumentLoader.loadDocument(in);
         assertNotNull(result);
    }

}
