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

    public Feed() {
        title = new SimpleStringProperty("Loading ...");
        itemsList = FXCollections.observableArrayList();
        items = new SimpleListProperty<>(this, "items", itemsList);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList getItems() {
        return items.get();
    }

    public void setItems(ObservableList value) {
        items.set(value);
    }

    public ListProperty itemsProperty() {
        return items;
    }
}
