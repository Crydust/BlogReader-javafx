package blogreader.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author kristof
 */
public abstract class DocumentLoader {

    private static final Logger logger = Logger.getLogger(DateUtil.class.getName());

    private DocumentLoader() {
    }

    /**
     * Synchronously downloads an RSS feed and returns an XML Document.
     *
     * @param spec URL for the RSS feed to download
     * @return
     */
    @Nullable
    public static Document loadDocument(@Nonnull final String spec) {
        assert spec != null;
        Document doc = null;
        InputStream in = null;
        try {
            URL url = new URL(spec);
            URLConnection urlc = url.openConnection();
            urlc.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            in = urlc.getInputStream();
            doc = loadDocument(in);
        } catch (IOException ex) {
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

    @Nullable
    public static Document loadDocument(@Nonnull final InputStream in) {
        assert in != null;
        Document doc = null;
        try {
            doc = getDocumentBuilder().parse(new BufferedInputStream(in));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return doc;
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://xml.org/sax/features/namespaces", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder;
    }
}
