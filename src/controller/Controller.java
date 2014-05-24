package controller;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.TextFieldType;

import com.sun.javafx.collections.ObservableMapWrapper;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import model.Film;
import model.Media;
import model.MediaCollection;
import model.TvShow;
import client.Client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import view.AboutDialog;

import java.io.*;
import java.lang.String;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

// TODO warn user that it will not save if they dont press done after editing
// TODO put fetching in its own thread, add progress bar or loading wheel
// TODO open from server should create a new library and open the save dialog
// TODO notify the user if fetch does not find title
// TODO store entire path of saved files on New and on first run open the New dialog

/**
 * Controls the WatchlistPro view.
 */
public class Controller implements Initializable {

    private MediaCollection watchlist;
    private MediaCreator mediaCreator;
    private FileIO io;
    private Stage stage;
    private File saveFile;
    private Preferences preferences;
    private LinkedList<String> recentList;
    private ByteArrayHandler byteArrayHandler;
    private String username;
    private String password;

    // Control variables
    private int mediaIndex;
    private String mediaName;
    private String mediaType;
    private String mediaEditType;

    // View components
    @FXML
    private ListView<Media> mediaList;
    @FXML
    private Menu openRecentMenuItem;
    @FXML
    private TextField filterField;
    @FXML
    private TextField newMediaTextField;
    @FXML
    private VBox filmDisplayPane;
    @FXML
    private Label filmTitleLabel;
    @FXML
    private Label filmGenreLabel;
    @FXML
    private Label filmDirectorLabel;
    @FXML
    private Label filmRatingLabel;
    @FXML
    private Label filmRuntimeLabel;
    @FXML
    private Label filmProducerLabel;
    @FXML
    private Label filmWriterLabel;
    @FXML
    private Label filmDescriptionLabel;
    @FXML
    private VBox filmEditPane;
    @FXML
    private TextField filmTitleTextField;
    @FXML
    private TextField filmGenreTextField;
    @FXML
    private TextField filmDirectorTextField;
    @FXML
    private TextField filmRatingTextField;
    @FXML
    private TextField filmRuntimeTextField;
    @FXML
    private TextField filmProducerTextField;
    @FXML
    private TextField filmWriterTextField;
    @FXML
    private TextArea filmDescriptionTextField;
    @FXML
    private VBox tvDisplayPane;
    @FXML
    private Label tvTitleLabel;
    @FXML
    private Label tvGenreLabel;
    @FXML
    private Label tvCreatorLabel;
    @FXML
    private Label tvNetworkLabel;
    @FXML
    private Label tvRuntimeLabel;
    @FXML
    private Label tvNumSeasonsLabel;
    @FXML
    private Label tvNumEpisodesLabel;
    @FXML
    private Label tvDescriptionLabel;
    @FXML
    private VBox tvEditPane;
    @FXML
    private TextField tvTitleTextField;
    @FXML
    private TextField tvGenreTextField;
    @FXML
    private TextField tvCreatorTextField;
    @FXML
    private TextField tvNetworkTextField;
    @FXML
    private TextField tvRuntimeTextField;
    @FXML
    private TextField tvNumSeasonsTextField;
    @FXML
    private TextField tvNumEpisodesTextField;
    @FXML
    private TextArea tvDescriptionTextField;
    @FXML
    private ToggleButton editToggleButton;
    @FXML
    private Button fetchButton;
    @FXML
    private ToggleButton mediaToggleButton;
    @FXML
    private SeparatorMenuItem recentSeparator;
    @FXML
    private MenuItem clearMenuItem;
    @FXML
    private CheckBox tvWatchedCheckBox;
    @FXML
    private Label tvWatchedLabel;
    @FXML
    private CheckBox filmWatchedCheckBox;
    @FXML
    private Label filmWatchedLabel;

    /**
     * Constructor.
     */
    public Controller() {
        mediaCreator = new MediaCreator();
        byteArrayHandler = new ByteArrayHandler();
        io = new FileIO();
        preferences = Preferences.userRoot().node(this.getClass().getName());
        watchlist = new MediaCollection();

        mediaName = null;
        mediaIndex = -1;
        mediaType = "film";

        // TODO get username and password from user input
        username = "user1";
        password = "12345";

        File defaultFile = new File("watchlist.wl");

        // Setup Open Recent List
        recentList = byteArrayHandler.readByteArray(preferences.getByteArray("recentList", "".getBytes()));
        if (!recentList.isEmpty()) {
            File recentFile = new File(recentList.get(0));
            if (!recentFile.exists()) {
                io.save(new ObservableMapWrapper<>(new HashMap<>()), recentFile);
            }
            saveFile = recentFile;
        } else if (!defaultFile.exists()){
            io.save(new ObservableMapWrapper<>(new HashMap<>()), defaultFile);
            saveFile = defaultFile;
        } else {
            saveFile = defaultFile;
        }

        HashMap<String, Media> map = new HashMap<>();
        watchlist.set(io.load(new ObservableMapWrapper<>(map), saveFile));
    }

    /**
     * Initialize the view.
     * @param url is not used.
     * @param rb is not used.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Throws CoreText performance errors on OS X Mavericks.
        AquaFx.style();
        AquaFx.createTextFieldStyler().setType(TextFieldType.SEARCH).style(filterField);

        mediaList.setCellFactory((list) -> new ListCell<Media>() {
            @Override
            protected void updateItem(Media Media, boolean empty) {
                super.updateItem(Media, empty);

                if (Media == null || empty) {
                    setText(null);
                } else {
                    setText(Media.getTitle());
                }
            }
        });

        // Handle Media List selection changes.
        mediaList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switchPane();

            if (newValue != null) {

                if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
                    setFilmDisplayPane();
                } else {
                    setTvDisplayPane();
                }
                mediaIndex = mediaList.getSelectionModel().getSelectedIndex();
            }
        });

        updateMediaList();

        mediaList.getSelectionModel().select(0);
    }

    // Button and Field Methods

    /**
     * Adds new media item to list.
     */
    @FXML
    public void addMedia() {
        if (editToggleButton.isSelected()) {
            editToggleButton.setSelected(false);
            switchPane();
        }
        if (newMediaTextField.getCharacters() != null && newMediaTextField.getLength() > 0) {
            mediaName = newMediaTextField.getCharacters().toString();

            if (mediaType.equals("film")) {
                Film film = mediaCreator.createFilm(mediaName);
                watchlist.put(mediaName, film);
            } else {
                TvShow show = mediaCreator.createTvShow(mediaName);
                watchlist.put(mediaName, show);
            }
            newMediaTextField.clear();
            filterField.clear();
            updateMediaList();

            setListIndex();
            editToggleButton.setSelected(true);
            switchPane();
        }
    }

    /**
     * Deletes the selected media item.
     */
    @FXML
    public void deleteMedia() {
        if (watchlist.size() > 0 && mediaIndex >= 0 && mediaIndex < watchlist.size()) {

            if (editToggleButton.isSelected()) {
                editToggleButton.setSelected(false);
                switchPane();
            }

            // Handle ListView selection changes.
            if (mediaList.getSelectionModel().getSelectedItem() != null) {
                String title = mediaList.getSelectionModel().getSelectedItem().getTitle();
                watchlist.remove(title);
                watchlist.update();
                updateMediaList();
                filterField.clear();
                mediaList.getSelectionModel().select(0);
            }
        }

        if (mediaList.getItems().size() <= 0) {
            clearDisplayPane();
        }
    }

    /**
     * Toggles the type of media to be added.
     */
    @FXML
    public void toggleMedia() {
        if (mediaToggleButton.isSelected()){
            mediaToggleButton.setText("TV");
            mediaType = "tv";
        } else {
            mediaToggleButton.setText("Film");
            mediaType = "film";
        }
    }

    /**
     * Toggles between film/tv edit and display panes.
     */
    @FXML
    public void toggleEdit() {
        if (mediaList.getSelectionModel().getSelectedItem() != null) {
            mediaName = mediaList.getSelectionModel().getSelectedItem().getTitle();
            if (!editToggleButton.isSelected()) {

                filterField.clear();
                filterField.setDisable(false);
                if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {

                    if (mediaName.equals(filmTitleTextField.getText())) {
                        watchlist.get(mediaName).setTitle(filmTitleTextField.getText());
                        watchlist.get(mediaName).setGenre(filmGenreTextField.getText());
                        ((Film) watchlist.get(mediaName)).setDirector(filmDirectorTextField.getText());
                        ((Film) watchlist.get(mediaName)).setRating(filmRatingTextField.getText());
                        watchlist.get(mediaName).setRuntime(filmRuntimeTextField.getText());
                        ((Film) watchlist.get(mediaName)).setProducer(filmProducerTextField.getText());
                        ((Film) watchlist.get(mediaName)).setWriter(filmWriterTextField.getText());
                        watchlist.get(mediaName).setDescription(filmDescriptionTextField.getText());
                    } else {
                        Film film = mediaCreator.createFilm(filmTitleTextField.getText());
                        film.setGenre(filmGenreTextField.getText());
                        film.setDirector(filmDirectorTextField.getText());
                        film.setRating(filmRatingTextField.getText());
                        film.setRuntime(filmRuntimeTextField.getText());
                        film.setProducer(filmProducerTextField.getText());
                        film.setWriter(filmWriterTextField.getText());
                        film.setDescription(filmDescriptionTextField.getText());

                        watchlist.remove(mediaName);
                        watchlist.put(filmTitleTextField.getText(), film);
                    }
                    mediaName = filmTitleTextField.getText();
                    watchlist.update();
                    updateMediaList();
                } else {
                    if (mediaName.equals(tvTitleTextField.getText())) {
                        watchlist.get(mediaName).setTitle(tvTitleTextField.getText());
                        watchlist.get(mediaName).setGenre(tvGenreTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setCreator(tvCreatorTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setNetwork(tvNetworkTextField.getText());
                        watchlist.get(mediaName).setRuntime(tvRuntimeTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setNumSeasons(tvNumSeasonsTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setNumEpisodes(tvNumEpisodesTextField.getText());
                        watchlist.get(mediaName).setDescription(tvDescriptionTextField.getText());
                    } else {
                        TvShow show = mediaCreator.createTvShow(tvTitleTextField.getText());
                        show.setGenre(tvGenreTextField.getText());
                        show.setCreator(tvCreatorTextField.getText());
                        show.setNetwork(tvNetworkTextField.getText());
                        show.setRuntime(tvRuntimeTextField.getText());
                        show.setNumSeasons(tvNumSeasonsTextField.getText());
                        show.setNumEpisodes(tvNumEpisodesTextField.getText());
                        show.setDescription(tvDescriptionTextField.getText());

                        watchlist.remove(mediaName);
                        watchlist.put(tvTitleTextField.getText(), show);

                    }
                    mediaName = tvTitleTextField.getText();
                    
                    updateMediaList();
                }
                setListIndex();
            } else {
                filterField.setDisable(true);
            }
            switchPane();

        } else {
            editToggleButton.setSelected(false);
        }
    }

    /**
     * Fetches the media data from Freebase.
     */
    @FXML
    public void fetchMedia() {
        try {
            Client client = new Client();
            String command = mediaList.getSelectionModel().getSelectedItem().getTitle();

            Thread type = client.send(mediaEditType);
            Thread search = client.send(command);
            Thread quit = client.send("quit");

            type.start();
            type.join();

            search.start();
            search.join();

            quit.start();
            quit.join();

            TopicHandler handler = new TopicHandler();
            List<String> outputList;
            if (mediaEditType.equals("film")) {
                outputList = handler.filmOutput(client.getTopic());
                setFilmEditPane(outputList);
            } else {
                outputList = handler.tvOutput(client.getTopic());
                setTvEditPane(outputList);
            }

        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception");
            e.printStackTrace();
        }
    }

    /**
     * Sets the watched value for the currently selected film.
     */
    @FXML
    public void setWatched() {
        Media media = mediaList.getSelectionModel().getSelectedItem();
        if (filmWatchedCheckBox.isSelected() || tvWatchedCheckBox.isSelected()) {
            media.setWatched("Yes");
        } else {
            media.setWatched("No");
        }
    }

    // File Menu

    /**
     * Creates a new library text file using a file selected by the user.
     */
    @FXML
    public void createNew() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New Media File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Watchlist Files (*.wl)", "*.wl"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            stage.setTitle("WatchlistPro - " + saveFile.getName());
            watchlist.clear();
            io.save(watchlist.getMap(), saveFile);
            updateMediaList();
            clearDisplayPane();
            updateRecentMenu(saveFile.getName());
        }
    }

    /**
     * Opens a media library text file selected by the user from the file system.
     */
    @FXML
    public void openLibrary() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Watchlist Files (*.wl)", "*.wl"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            stage.setTitle("WatchlistPro - " + saveFile.getName());
            watchlist.clear();
            clearDisplayPane();
            io.load(watchlist.getMap(), saveFile);
            updateMediaList();
            mediaList.getSelectionModel().select(0);
            updateRecentMenu(saveFile.getName());
        }
    }

    /**
     * Clears the recent list in the menu.
     */
    @FXML
    public void clearRecentMenu() {
        openRecentMenuItem.getItems().clear();
        openRecentMenuItem.getItems().add(recentSeparator);
        openRecentMenuItem.getItems().add(clearMenuItem);
        openRecentMenuItem.setDisable(true);
        recentList.clear();
        preferences.remove("recentList");
    }

    /**
     * Saves the contents of the watchlist to the file system when the menu item is selected.
     */
    @FXML
    public void saveList() {
        io.save(watchlist.getMap(), saveFile);
    }

    /**
     * Saves the contents of the watchlist to the file system when the menu item is selected.
     */
    @FXML
    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Watchlist Files (*.wl)", "*.wl"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            io.save(watchlist.getMap(), saveFile);
            updateMediaList();
            mediaList.getSelectionModel().select(0);
            updateRecentMenu(saveFile.getName());
        }
    }

    /**
     * Closes the window when the menu item is selected.
     */
    @FXML
    public void closeWindow() {
        io.save(watchlist.getMap(), saveFile);
        preferences.remove("recentList");
        preferences.putByteArray("recentList", byteArrayHandler.writeByteArray(recentList));
        Platform.exit();
    }

    // Server Menu

    /**
     * Create an account on the server.
     */
    @FXML
    public void createAccount() {
        // TODO popup a window with fields for username, password; buttons for cancel, create
        Client client = new Client();
        try {
            Thread account = client.send("add " + username + " " + password);
            Thread quit = client.send("quit");

            account.start();
            account.join();

            quit.start();
            quit.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a save file from the server.
     */
    @FXML
    public void loginToServer() {
        // TODO popup a window with field for username, password; buttons for cancel, login, create account
        Client client = new Client();
        try {
            Thread login = client.send("login " + username + " " + password);
            Thread quit = client.send("quit");

            login.start();
            login.join();

            quit.start();
            quit.join();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Get the list of saves from the server.
     */
    @FXML
    public void getSaves() {
        Client client = new Client();
        try {
            Thread saves = client.send("getsaves " + username);
            Thread quit = client.send("quit");

            saves.start();
            saves.join();

            quit.start();
            quit.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the contents of the media map to the server.
     */
    @FXML
    public void saveToServer() {
        // TODO popup window with field for save name; buttons for cancel, save
        String saveName = "save1";

        String output = "";
        for (Map.Entry<String, Media> entry : watchlist.entrySet()) {
            Media media = entry.getValue();
            String jsonString = JSONValue.toJSONString(media.getMap());
            output += jsonString + "//";
        }

        Client client = new Client();
        try {
            Thread save = client.send("save " + username + " " + saveName + " " + output);
            Thread quit = client.send("quit");

            save.start();
            save.join();

            quit.start();
            quit.join();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception");
            e.printStackTrace();
        }
    }

    /**
     * Load the library from the server into the media map.
     */
    @FXML
    public void loadFromServer() {
        // TODO call getSaves and populate a popup window's list with all the server save names
        // TODO the window needs a list, cancel button, load button
        getSaves();
        String saveName = "save1";
        Client client = new Client();
        try {
            Thread load = client.send("load " + " " + username + " " + saveName);
            Thread quit = client.send("quit");

            load.start();
            load.join();

            quit.start();
            quit.join();

            io.load(watchlist.getMap(), saveFile);

            updateMediaList();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception");
            e.printStackTrace();
        }
    }

    // Help Menu

    /**
     * Opens the About pane.
     */
    @FXML
    public void openAbout() {
        new AboutDialog();
    }

    // Helper Methods

    /**
     * Updates the media list and facilitates filtering.
     */
    private void updateMediaList() {
        watchlist.update();
        mediaList.setItems(watchlist.getList());

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Media> filteredData = new FilteredList<>(watchlist.getList(), p -> true);

        // Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(Media -> {
            // If filter text is empty, display all entries.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            return Media.getTitle().toLowerCase().contains(lowerCaseFilter);
        }));

        mediaList.setItems(filteredData);

        watchlist.sort();

        // Reselect the correct media object in the media list.
        if (!mediaList.equals(watchlist.getList())) {
            for (int i = 0; i < mediaList.getItems().size(); i++) {
                if (mediaList.getItems().get(i) != null){
                    mediaList.getSelectionModel().select(i);
                }
            }

        }
    }
    

    /**
     * Sets the index of the ListView to the last created or edited item.
     */
    private void setListIndex() {
        for (int i = 0; i < watchlist.size(); i++) {
            if (watchlist.get(i).getTitle().equals(mediaName)) {
                mediaList.getSelectionModel().select(i);
            }
        }
    }

    /**
     * Switches between the display pane to the edit pane.
     */
    private void switchPane() {
        if (mediaList.getSelectionModel().getSelectedItem() != null) {
            if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
                mediaEditType = "film";
            } else if (mediaList.getSelectionModel().getSelectedItem() instanceof TvShow) {
                mediaEditType = "tv";
            } else {
                mediaList.getSelectionModel().select(0);
            }

            // edit pane is enabled
            if (editToggleButton.isSelected()) {
                editToggleButton.setText("Done");
                fetchButton.setDisable(false);

                // select tv or film edit panel
                switch (mediaEditType) {
                    case "tv":
                        setTvEditPane();
                        // set tv edit pane visible
                        tvEditPane.setVisible(true);
                        tvEditPane.setDisable(false);
                        // set all other panes invisible
                        filmDisplayPane.setVisible(false);
                        tvDisplayPane.setVisible(false);
                        filmEditPane.setVisible(false);
                        filmEditPane.setDisable(true);
                        break;
                    case "film":
                        setFilmEditPane();

                        // set film edit pane visible
                        filmEditPane.setVisible(true);
                        filmEditPane.setDisable(false);
                        // set all other panes invisible
                        filmDisplayPane.setVisible(false);
                        tvDisplayPane.setVisible(false);
                        tvEditPane.setVisible(false);
                        tvEditPane.setDisable(true);
                        break;
                }
            } else {
                // display pane is enabled
                editToggleButton.setText("Edit");
                fetchButton.setDisable(true);

                // select tv or film display panel
                switch (mediaEditType) {
                    case "tv":
                        // set tv display pane to display contents of tv edit pane
                        setTvDisplayPane();
                        // set tv display pane visible
                        tvDisplayPane.setVisible(true);
                        // set all other panes invisible
                        filmEditPane.setVisible(false);
                        filmEditPane.setDisable(true);
                        filmDisplayPane.setVisible(false);
                        tvEditPane.setVisible(false);
                        tvEditPane.setDisable(true);
                        break;

                    case "film":
                        // set film display pane to display contents of film edit pane
                        setFilmDisplayPane();
                        // set film display pane visible
                        filmDisplayPane.setVisible(true);
                        // set all other panes invisible
                        filmEditPane.setVisible(false);
                        filmEditPane.setDisable(true);
                        tvDisplayPane.setVisible(false);
                        tvEditPane.setVisible(false);
                        tvEditPane.setDisable(true);
                        break;
                }
            }
        }
    }

    /**
     * Update the Open Recent menu.
     * @param name the file name to add.
     */
    private void updateRecentMenu(String name) {
        if (!recentList.contains(name)) {
            recentList.push(name);
        } else {
            recentList.remove(name);
            recentList.push(name);
            int index = 0;
            for (int i = 0; i < openRecentMenuItem.getItems().size(); i++) {
                if (openRecentMenuItem.getItems().get(i).getText().equals(name)) {
                    index = i;
                }
            }
            openRecentMenuItem.getItems().remove(index);
        }
        checkRecentSize();
        addRecent(name);
        preferences.remove("recentList");
        preferences.putByteArray("recentList", byteArrayHandler.writeByteArray(recentList));
    }

    /**
     * Creates the Open Recent menu.
     */
    public void createRecentMenu() {
        if (!recentList.isEmpty()) {
            for (int i = recentList.size() - 1; i >= 0; i--) {
                addRecent(recentList.get(i));
            }
            //recentList.forEach(this::addRecent);
        } else {
            openRecentMenuItem.setDisable(true);
        }

    }

    /**
     * Adds a file to the Open Recent menu.
     * @param name is the file name to add.
     */
    private void addRecent(String name) {
        if (openRecentMenuItem.isDisable()) {
            openRecentMenuItem.setDisable(false);
        }
        MenuItem item = new MenuItem(name);
        item.setOnAction(actionEvent -> {
            saveFile = new File(name);
            stage.setTitle("WatchlistPro - " + saveFile.getName());
            watchlist.clear();
            clearDisplayPane();
            io.load(watchlist.getMap(), saveFile);
            updateMediaList();
            mediaList.getSelectionModel().select(0);
            updateRecentMenu(saveFile.getName());
        });
        openRecentMenuItem.getItems().add(0, item);
    }

    private void checkRecentSize() {
        if (recentList.size() > 10) {
            // Remove the last item.
            recentList.remove(recentList.get(recentList.size() - 1));
            openRecentMenuItem.getItems().remove(
                    openRecentMenuItem.getItems().get(openRecentMenuItem.getItems().size() - 3));
        }
    }

    /**
     * Clears all the fields in both the film and tv display panes.
     */
    private void clearDisplayPane() {        
        filmTitleLabel.setText("");
        filmWatchedLabel.setText("");
        filmGenreLabel.setText("");
        filmDirectorLabel.setText("");
        filmRatingLabel.setText("");
        filmRuntimeLabel.setText("");
        filmProducerLabel.setText("");
        filmWriterLabel.setText("");
        filmDescriptionLabel.setText("");

        tvTitleLabel.setText("");
        tvWatchedLabel.setText("");
        tvGenreLabel.setText("");
        tvCreatorLabel.setText("");
        tvNetworkLabel.setText("");
        tvRuntimeLabel.setText("");
        tvNumSeasonsLabel.setText("");
        tvNumEpisodesLabel.setText("");
        tvDescriptionLabel.setText("");
    }

    /**
     * Populates the film display pane based on values from the film edit pane.
     */
    private void setFilmDisplayPane() {
        Film film = (Film) mediaList.getSelectionModel().getSelectedItem();

        filmTitleLabel.setText(film.getTitle());
        filmWatchedLabel.setText(film.getWatched());
        filmGenreLabel.setText(film.getGenre());
        filmDirectorLabel.setText(film.getDirector());
        filmRatingLabel.setText(film.getRating());
        filmRuntimeLabel.setText(film.getRuntime());
        filmProducerLabel.setText(film.getProducer());
        filmWriterLabel.setText(film.getWriter());
        filmDescriptionLabel.setText(film.getDescription());
    }

    /**
     * Populates the tv display pane based on values from the film edit pane.
     */
    private void setTvDisplayPane() {
        TvShow show = (TvShow) mediaList.getSelectionModel().getSelectedItem();

        tvTitleLabel.setText(show.getTitle());
        tvWatchedLabel.setText(show.getWatched());
        tvGenreLabel.setText(show.getGenre());
        tvCreatorLabel.setText(show.getCreator());
        tvNetworkLabel.setText(show.getNetwork());
        tvRuntimeLabel.setText(show.getRuntime());
        tvNumSeasonsLabel.setText(show.getNumSeasons());
        tvNumEpisodesLabel.setText(show.getNumEpisodes());
        tvDescriptionLabel.setText(show.getDescription());
    }

    /**
     * Populates the film edit pane based on passed in arguments.
     */
    private void setFilmEditPane() {
        Film film = (Film) mediaList.getSelectionModel().getSelectedItem();

        filmTitleTextField.setText(film.getTitle());
        if (film.getWatched().equals("Yes")) {
            filmWatchedCheckBox.setSelected(true);
        }
        filmGenreTextField.setText(film.getGenre());
        filmDirectorTextField.setText(film.getDirector());
        filmRatingTextField.setText(film.getRating());
        filmRuntimeTextField.setText(film.getRuntime());
        filmProducerTextField.setText(film.getProducer());
        filmWriterTextField.setText(film.getWriter());
        filmDescriptionTextField.setText(film.getDescription());
    }

    /**
     * Populates the tv edit pane based on passed in arguments.
     */
    private void setTvEditPane() {
        TvShow show = (TvShow) mediaList.getSelectionModel().getSelectedItem();

        tvTitleTextField.setText(show.getTitle());
        if (show.getWatched().equals("Yes")) {
            tvWatchedCheckBox.setSelected(true);
        }
        tvGenreTextField.setText(show.getGenre());
        tvCreatorTextField.setText(show.getCreator());
        tvNetworkTextField.setText(show.getNetwork());
        tvRuntimeTextField.setText(show.getRuntime());
        tvNumSeasonsTextField.setText(show.getNumSeasons());
        tvNumEpisodesTextField.setText(show.getNumEpisodes());
        tvDescriptionTextField.setText(show.getDescription());
    }

    /**
     * Populates the film edit pane based on passed in arguments.
     */
    private void setFilmEditPane(List<String> outputList) {
        filmTitleTextField.setText(outputList.get(0));
        if (outputList.get(1).equals("Yes")) {
            filmWatchedCheckBox.setSelected(true);
        }
        filmGenreTextField.setText(outputList.get(2));
        filmDirectorTextField.setText(outputList.get(3));
        filmRatingTextField.setText(outputList.get(4));
        filmRuntimeTextField.setText(outputList.get(5));
        filmProducerTextField.setText(outputList.get(6));
        filmWriterTextField.setText(outputList.get(7));
        filmDescriptionTextField.setText(outputList.get(8));
    }

    /**
     * Populates the tv edit pane based on passed in arguments.
     */
    private void setTvEditPane(List<String> outputList) {
        tvTitleTextField.setText(outputList.get(0));
        if (outputList.get(1).equals("Yes")) {
            tvWatchedCheckBox.setSelected(true);
        }
        tvGenreTextField.setText(outputList.get(2));
        tvCreatorTextField.setText(outputList.get(3));
        tvNetworkTextField.setText(outputList.get(4));
        tvRuntimeTextField.setText(outputList.get(5));
        tvNumSeasonsTextField.setText(outputList.get(6));
        tvNumEpisodesTextField.setText(outputList.get(7));
        tvDescriptionTextField.setText(outputList.get(8));

        JSONObject seasons = (JSONObject) JSONValue.parse(outputList.get(9));
        int numSeasons = Integer.parseInt(outputList.get(6));

        ArrayList<ArrayList<String>> seasonList = new ArrayList<>();
        for (int i = 0; i <= numSeasons; i++) {
            JSONArray array = (JSONArray) seasons.get(String.valueOf(i));
            seasonList.add(array);
        }

        // TODO put seasonList in view
        System.out.println(seasonList);
    }

    /**
     * Sets the stage.
     * @param stage the stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Get the current save file.
     * @return the save file.
     */
    public File getSaveFile() {
        return saveFile;
    }

}