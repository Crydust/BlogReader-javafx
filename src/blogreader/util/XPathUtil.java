package blogreader.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author kristof
 */
public abstract class XPathUtil {

    private static final Logger logger = Logger.getLogger(XPathUtil.class.getName());

    private XPathUtil() {
    }

    /**
     * Retrieves a string based on an XPath expression.
     * 
     * @param node
     * @param expression
     * @return 
     */
    @Nullable
    public static String readString(@Nullable final Node node, @Nonnull final String expression) {
        String result = null;
        Object object = readObject(node, XPathConstants.STRING, expression);
        if (object instanceof String) {
            result = (String) object;
        } else {
            logger.log(Level.SEVERE, "node is null");
        }
        return result;
    }

    /**
     * Retrieves a NodeList based on an XPath expression.
     * 
     * @param node
     * @param expression
     * @return 
     */
    @Nullable
    public static NodeList readNodeList(@Nullable final Node node, @Nonnull final String expression) {
        NodeList result = null;
        Object object = readObject(node, XPathConstants.NODESET, expression);
        if (object instanceof NodeList) {
            result = (NodeList) object;
        } else {
            logger.log(Level.SEVERE, "node is null");
        }
        return result;
    }

    @Nullable
    private static Object readObject(@Nullable final Node node, @Nonnull final QName qname, @Nonnull final String expression) {
        Object result = null;
        if (node != null) {
            try {
                XPathFactory xFactory = XPathFactory.newInstance();
                XPath xpath = xFactory.newXPath();
                XPathExpression expr = xpath.compile(expression);
                result = expr.evaluate(node, qname);
            } catch (XPathExpressionException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } else {
            logger.log(Level.SEVERE, "node is null");
        }
        return result;
    }
    
}
