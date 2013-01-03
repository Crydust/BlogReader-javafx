package blogreader.util;

import blogreader.model.FeedItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author kristof
 */
public abstract class FeedParser {

    private static final Logger logger = Logger.getLogger(FeedParser.class.getName());

    private FeedParser() {
    }

    public static String readTitle(final Node doc) {
        return XPathUtil.readString(doc, "/rss/channel/title/text()");
    }

    public static List<FeedItem> readItems(final Node doc) {
        NodeList itemNodes = XPathUtil.readNodeList(doc, "/rss/channel/item");
        int leni = itemNodes.getLength();
        List<FeedItem> items = new ArrayList<>(leni);
        for (int i = 0; i < leni; i++) {
            Node itemNode = itemNodes.item(i);
            FeedItem item = new FeedItem(
                    XPathUtil.readString(itemNode, "./title/text()"),
                    DateUtil.parseDateRfc822(XPathUtil.readString(itemNode, "./pubDate/text()")),
                    XPathUtil.readString(itemNode, "./link/text()"),
                    XPathUtil.readString(itemNode, "./description/text()"));
            items.add(item);
        }
        return Collections.unmodifiableList(items);
    }
}
