package blogreader.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public static Document loadDocument(final String spec) {
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
    
}
