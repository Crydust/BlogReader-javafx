package blogreader.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kristof
 */
public class Feed {

    private final StringProperty title;
    private final ObservableList<FeedItem> itemsList;
    private final ListProperty<FeedItem> items;

    /**
     * 
     */
    public Feed() {
        title = new SimpleStringProperty("Loading ...");
        itemsList = FXCollections.observableArrayList();
        items = new SimpleListProperty<>(this, "items", itemsList);
    }

    /**
     * 
     * @return 
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * 
     * @return 
     */
    public ListProperty itemsProperty() {
        return items;
    }
}
