package blogreader.util;

import blogreader.model.FeedItem;
import static blogreader.util.DateUtil.*;
import static blogreader.util.ObjectUtil.*;
import static blogreader.util.XPathUtil.*;
import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;
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

    /**
     * reads the RSS feed title or returns null when that fails.
     *
     * @param node
     * @return
     */
    @Nullable
    public static String readTitle(@NonNull final Node node) {
        return readString(node, "/rss/channel/title/text()");
    }

    /**
     * reads the RSS feed items or returns null when that fails.
     *
     * @param node
     * @return
     */
    @Nullable
    public static List<FeedItem> readItems(@NonNull final Node node) {
        NodeList itemNodes = readNodeList(node, "/rss/channel/item");
        if (itemNodes != null) {
            int leni = itemNodes.getLength();
            List<FeedItem> items = new ArrayList<>(leni);
            for (int i = 0; i < leni; i++) {
                Node itemNode = itemNodes.item(i);
                FeedItem item = new FeedItem(
                        readString(itemNode, "./title/text()"),
                        parseDateRfc822(readString(itemNode, "./pubDate/text()")),
                        readString(itemNode, "./link/text()"),
                        firstNonNull(readString(itemNode, "./encoded/text()"), readString(itemNode, "./description/text()")));
                items.add(item);
            }
            return Collections.unmodifiableList(items);
        }
        return null;
    }
}
