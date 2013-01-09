package blogreader.model;

import org.joda.time.DateTime;

/**
 *
 * @author kristof
 */
public class FeedItem {

    private final String title;
    private final DateTime pubDate;
    private final String link;
    private final String description;

    /**
     * 
     * @param title
     * @param pubDate
     * @param link
     * @param description 
     */
    public FeedItem(final String title, final DateTime pubDate, final String link, final String description) {
        this.title = title;
        this.pubDate = new DateTime(pubDate);
        this.link = link;
        this.description = description;
    }

    /** @return */
    public String getTitle() {
        return title;
    }

    /** @return */
    public DateTime getPubDate() {
        return new DateTime(pubDate);
    }

    /** @return */
    public String getLink() {
        return link;
    }

    /** @return */
    public String getDescription() {
        return description;
    }

    /** @return */
    @Override
    public String toString() {
        return "Item{" + "title=" + title + ", pubDate=" + pubDate + '}';
    }
}
