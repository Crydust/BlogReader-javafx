/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blogreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author kristof
 */
public class FeedRequest {

    private static final Logger logger = Logger.getLogger(FeedRequest.class.getName());
    private final String title;
    private final List<Item> items;
    
    public FeedRequest() {
        Document doc = loadDocument("http://www.crydust.be/blog/feed/");
        title = readTitle(doc);
        items = readItems(doc);
    }

    public String getTitle() {
        return title;
    }

    public List<Item> getItems() {
        return items;
    }

    private static Document loadDocument(final String spec) {
        Document doc = null;
        InputStream in = null;
        try {
            URL url = new URL(spec);
            URLConnection urlc = url.openConnection();
            urlc.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            in = urlc.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(in);
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return doc;
    }

    private static Object readObject(final Node doc, final QName qname, final String expression) {
        Object result = null;
        if (doc != null) {
            try {
                XPathExpression expr = null;
                XPathFactory xFactory = XPathFactory.newInstance();
                XPath xpath = xFactory.newXPath();
                expr = xpath.compile(expression);
                result = expr.evaluate(doc, qname);
            } catch (XPathExpressionException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } else {
            logger.log(Level.SEVERE, "doc is null");
        }
        return result;
    }

    private static String readString(final Node doc, final String expression) {
        String result = "?";
        Object object = readObject(doc, XPathConstants.STRING, expression);
        if (object instanceof String) {
            result = (String) object;
        } else {
            logger.log(Level.SEVERE, "doc is null");
        }
        return result;
    }

    private static NodeList readNodeList(final Node doc, final String expression) {
        NodeList result = null;
        Object object = readObject(doc, XPathConstants.NODESET, expression);
        if (object instanceof NodeList) {
            result = (NodeList) object;
        } else {
            logger.log(Level.SEVERE, "doc is null");
        }
        return result;
    }

    private static String readTitle(final Node doc) {
        return readString(doc, "/rss/channel/title/text()");
    }

    private static List<Item> readItems(final Node doc) {
        NodeList itemNodes = readNodeList(doc, "/rss/channel/item");
        int leni = itemNodes.getLength();
        List<Item> items = new ArrayList<>(leni);
        for (int i = 0; i < leni; i++) {
            Node itemNode = itemNodes.item(i);
            Item item = new Item(
                    readString(itemNode, "./title/text()"),
                    parseDateRfc822(readString(itemNode, "./pubDate/text()")),
                    readString(itemNode, "./link/text()"),
                    readString(itemNode, "./description/text()"));
            items.add(item);
            System.out.println(item);
        }
        return Collections.unmodifiableList(items);
    }

    private static Date parseDateRfc822(final String s) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            date = format.parse(s);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
    public static class Item {

        private final String title;
        private final Date pubDate;
        private final String link;
        private final String description;

        public Item(final String title, final Date pubDate, final String link, final String description) {
            this.title = title;
            this.pubDate = pubDate;
            this.link = link;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public Date getPubDate() {
            return pubDate;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Item{" + "title=" + title + ", pubDate=" + pubDate + '}';
        }

    }
}
