package blogreader.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 *
 * @author kristof
 */
public class Feed {

    private final StringProperty title;
    private final ListProperty<FeedItem> items;

    /** */
    public Feed() {
        title = new SimpleStringProperty("Loading ...");
        items = new SimpleListProperty<>(this, "items",
                FXCollections.<FeedItem>observableArrayList());
    }

    /** @return */
    public StringProperty titleProperty() {
        return title;
    }

    /** @return */
    public ListProperty itemsProperty() {
        return items;
    }
}
