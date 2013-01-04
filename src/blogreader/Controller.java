package blogreader;

import blogreader.model.FeedItem;
import blogreader.model.Feed;
import blogreader.util.DocumentLoader;
import blogreader.util.FeedParser;
import com.sun.webpane.webkit.JSObject;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.w3c.dom.Document;

/**
 *
 * @author kristof
 */
public class Controller implements Initializable {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    private static final String FEED_URL = "http://www.crydust.be/blog/feed/";
    private static final String TITLE_COLUMN_TEXT = "Title";
    private static final String DATE_COLUMN_TEXT = "Date";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TITLE_PROPERTY_NAME = "title";
    private static final String WEBVIEW_TEMPLATE = ""
            + "<!DOCTYPE html>%n"
            + "<html>%n"
            + "<head>%n"
            + "<style>%n"
            + "body {%n"
            + "    font-size: 14px;%n"
            + "    font-family: Arial, Helvetica, sans-serif;%n"
            + "    line-height: 1.4;%n"
            + "    background: #fff;%n"
            + "    color: #4c4c4c;%n"
            + "}%n"
            + "a {%n"
            + "    color: #105cb6;%n"
            + "    text-decoration: underline;%n"
            + "}%n"
            + "pre, code, tt {%n"
            + "    font-size: 14px;%n"
            + "    font-family: Consolas, 'andale mono', 'lucida console', monospace;%n"
            + "    line-height: 1.5;"
            + "}%n"
            + "%n"
            + "pre {%n"
            + "    border-left: 1px dotted #a8a8a8;%n"
            + "    padding-left: 1.5em;%n"
            + "    white-space : pre;%n"
            + "}%n"
            + "</style>%n"
            + "<script>%n"
            + "function init(){%n"
            + "    var i, leni;%n"
            + "    function _browse (e) {%n"
            + "        e.preventDefault();%n"
            + "        javaObj.browse(e.target.href);%n"
            + "        return false;%n"
            + "    }%n"
            + "    for (i = 0, leni = document.links.length; i < leni; i += 1) {%n"
            + "        document.links[i].addEventListener('click', _browse, false);%n"
            + "    }%n"
            + "}%n"
            + "</script>"
            + "</head>%n"
            + "<body>%n"
            + "%s"
            + "</body>"
            + "</html>";
    @FXML
    private Label titleLabel;
    @FXML
    private TableView<FeedItem> itemsTableView;
    @FXML
    private WebView itemWebView;
    private final Feed model = new Feed();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        logger.log(Level.INFO, "initialize");

        // initialize label
        titleLabel.textProperty().bind(model.titleProperty());

        // initialize table
        // * add two columns
        // * bind the model
        // * add selection listener
        TableColumn titleColumn = new TableColumn();
        titleColumn.setText(TITLE_COLUMN_TEXT);
        titleColumn.prefWidthProperty().bind(itemsTableView.widthProperty().subtract(200));
        titleColumn.setCellValueFactory(new PropertyValueFactory<FeedItem, String>(TITLE_PROPERTY_NAME));

        TableColumn pubDateColumn = new TableColumn();
        pubDateColumn.setText(DATE_COLUMN_TEXT);
        pubDateColumn.setPrefWidth(180);
        pubDateColumn.setCellValueFactory(new PubDateValueFactory());

        itemsTableView.getColumns().add(titleColumn);
        itemsTableView.getColumns().add(pubDateColumn);
        itemsTableView.itemsProperty().bind(model.itemsProperty());
        itemsTableView.getSelectionModel().selectedItemProperty()
                .addListener(new SelectionChangeListener(itemWebView));
        
        

        // initialize webview
        // * disable the contextmenu
        // * listen to statechanges
        itemWebView.setContextMenuEnabled(false);
        final WebEngine webEngine = itemWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(
                new WebEngineChangeListener(webEngine));

        // fetch data in the background
        logger.log(Level.INFO, "Begin loading feed");
        new Thread(new Task<Document>() {
            @Override
            protected Document call() throws Exception {
                return DocumentLoader.loadDocument(FEED_URL);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                model.itemsProperty().clear();
                Document doc = this.getValue();
                if (doc != null) {
                    final String title = FeedParser.readTitle(doc);
                    final List<FeedItem> items = FeedParser.readItems(doc);
                    if (title != null && items != null) {
                        logger.log(Level.INFO, "Feed loaded and read");
                        model.titleProperty().set(title);
                        model.itemsProperty().addAll(items);
                        itemsTableView.getSelectionModel().selectFirst();
                    } else {
                        logger.log(Level.SEVERE, "Error reading feed");
                        model.titleProperty().set("Error reading feed");
                    }
                } else {
                    logger.log(Level.SEVERE, "Error loading feed");
                    model.titleProperty().set("Error loading feed");
                }
            }
        }).start();

    }

    /**
     * Convenience method to load a web page in the system default browser.<br
     * /> This in a separate thread obviously.
     *
     * @param url
     */
    private static void browse(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Event handler for the button.<br />Loads the item link in a browser.
     *
     * @param event
     */
    @FXML
    void onLinkButtonClick(ActionEvent event) {
        browse(itemsTableView.getSelectionModel().selectedItemProperty().get().getLink());
    }

    /**
     * Bridge class<br /> Allows the webview from loading urls in the default
     * browser
     */
    private static class Bridge {

        private static final Logger logger = Logger.getLogger(Bridge.class.getName());

        public Bridge() {
        }

        public void browse(final String url) {
            Controller.browse(url);
        }

        public void log(final String message) {
            logger.log(Level.INFO, message);
        }
    }

    /**
     * Load the items description when the selected item changes.<br /> The
     * stylesheet makes the text legible.<br /> The script captures clicks on
     * every link.
     */
    private static class SelectionChangeListener implements ChangeListener<FeedItem> {

        private final WebView webView;

        public SelectionChangeListener(final WebView webView) {
            this.webView = webView;
        }

        @Override
        public void changed(ObservableValue<? extends FeedItem> observable,
                FeedItem oldValue, FeedItem newValue) {
            webView.getEngine().loadContent(String.format(WEBVIEW_TEMPLATE,
                    newValue.getDescription()));
        }
    }


    /**
     * Retrieves and formats the pubDate.
     */
    private static class PubDateValueFactory implements Callback<CellDataFeatures<FeedItem, String>, ObservableValue<String>> {

        public PubDateValueFactory() {
        }

        @Override
        public ObservableValue<String> call(CellDataFeatures<FeedItem, String> p) {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            return new SimpleStringProperty(df.format(p.getValue().getPubDate()));
        }
    }

    /**
     * Listens to changes in webengine state and each time a page is loaded:
     * 
     * <ul>
     * <li>Add a bridge from the javascript to the java world.
     * <li>Run javascript init function.
     * </ul>
     */
    private static class WebEngineChangeListener implements ChangeListener<State> {

        private final WebEngine webEngine;

        public WebEngineChangeListener(WebEngine webEngine) {
            this.webEngine = webEngine;
        }

        @Override
        public void changed(ObservableValue<? extends State> p, State oldState, State newState) {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject win = (JSObject) webEngine.executeScript("window");
                win.setMember("javaObj", new Bridge());
                webEngine.executeScript("init()");
            }
        }
    }
}
