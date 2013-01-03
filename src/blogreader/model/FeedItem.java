package blogreader.model;

import java.util.Date;

/**
 *
 * @author kristof
 */
public class FeedItem {

    private final String title;
    private final Date pubDate;
    private final String link;
    private final String description;

    public FeedItem(final String title, final Date pubDate, final String link, final String description) {
        this.title = title;
        this.pubDate = (Date) pubDate.clone();
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Date getPubDate() {
        return (Date) pubDate.clone();
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
