package controller;

import client.Client;
import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.TextFieldType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import org.controlsfx.control.action.Action;
import view.AboutDialog;
import view.DialogPane;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

// TODO try to break everything

/**
 * Controls the WatchlistPro view.
 */
public class Controller implements Initializable {

    // Classes
    private MediaCollection watchlist;
    private MediaCreator mediaCreator;
    private FileIO io;
    private ByteArrayHandler byteArrayHandler;
    private DialogPane dialogPane;
    private Stage stage;
    private Gson gson;
    private Preferences preferences;

    // Variables
    private Boolean isTvEditPane = false;
    private Boolean isLoggedIn;
    private File saveDir;
    private File saveFile;
    private int sortState;
    private int mediaIndex;
    private int seasonNum;
    private String username;
    private String password;
    private String slash;
    private String mediaName;
    private String mediaType;
    private String mediaEditType;

    // Data structures
    private TreeItem<Episode> masterRoot; // master root of dropdown menu
    private List<TreeItem<Episode>> seasonRootList; // list of season roots
    private LinkedList<String> recentList;
    private ObservableList<List<Episode>> masterSeasonList;
    private ListProperty<List<Episode>> episodeList;

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
    private SplitMenuButton addButton;
    @FXML
    private MenuItem filmSelectButton;
    @FXML
    private MenuItem tvSelectButton;
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
    @FXML
    private VBox userLoginPane;
    @FXML
    private VBox root;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox createAccountPane;
    @FXML
    private TextField createUserNameField;
    @FXML
    private PasswordField createPasswordField;
    @FXML
    private ListView<String> loadList;
    @FXML
    private VBox loadFromServerPane;
    @FXML
    private MenuItem loginMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private VBox progressIndicatorPane;

    // edit pane tree table
    @FXML
    private TreeTableView<Episode> tvEpisodeTable;
    @FXML
    private TreeTableColumn<Episode, String> seasonCol;
    @FXML
    private TreeTableColumn<Episode, String> episodeCol;
    @FXML
    private TreeTableColumn<Episode, Boolean> watchedCol;

    // display pane tree table
    @FXML
    private TreeTableView<Episode> tvEpisodeDisplayTable;
    @FXML
    private TreeTableColumn<Episode, String> seasonDisplayCol;
    @FXML
    private TreeTableColumn<Episode, String> episodeDisplayCol;
    @FXML
    private TreeTableColumn<Episode, Boolean> watchedDisplayCol;
    @FXML
    private MenuItem usernameMenuItem;
    @FXML
    private MenuButton sortMenuButton;


    /**
     * Constructor.
     */
    public Controller() {
        checkOS();

        masterRoot = new TreeItem<>(new Episode("Master", "", false));
        seasonRootList = new ArrayList<>();
        mediaCreator = new MediaCreator();
        byteArrayHandler = new ByteArrayHandler();
        io = new FileIO();
        preferences = Preferences.userRoot().node(this.getClass().getName());
        watchlist = new MediaCollection();
        gson = new Gson();

        sortState = 0;

        mediaName = null;
        mediaIndex = -1;
        mediaType = "film";
        isLoggedIn = false;

        username = "";
        password = "";

        masterSeasonList = FXCollections.observableArrayList();
        episodeList = new SimpleListProperty<>();
        episodeList.set(masterSeasonList);

        File defaultFile = new File(saveDir + slash + "watchlist.wl");

        dialogPane = new DialogPane();

        // Setup Open Recent List
        recentList = byteArrayHandler.readByteArray(preferences.getByteArray("recentList", "".getBytes()));
        if (!recentList.isEmpty()) {
            File recentFile = new File(saveDir + slash + recentList.get(0));
            if (!recentFile.exists()) {
                io.save(new ObservableMapWrapper<>(new HashMap<>()), recentFile);
            }
            saveFile = recentFile;
        } else if (!defaultFile.exists()) {
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
        // AquaFx throws CoreText performance errors on OS X Mavericks.
        AquaFx.style();
        AquaFx.createTextFieldStyler().setType(TextFieldType.SEARCH).style(filterField);

        mediaList.setCellFactory((list) -> new ListCell<Media>() {
            @Override
            protected void updateItem(Media media, boolean empty) {
                super.updateItem(media, empty);

                if (media == null || empty) {
                    setText(null);
                } else {
                    setText(media.getTitle());
                }
            }

        });


        // Handle Media List selection changes.
        mediaList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switchPane();

            if (newValue != null) {
                mediaIndex = mediaList.getSelectionModel().getSelectedIndex();
            }
        });

        updateMediaList();

        mediaList.getSelectionModel().select(0);

        loadList.setCellFactory((list) -> new ListCell<String>() {
            @Override
            protected void updateItem(String string, boolean empty) {
                super.updateItem(string, empty);

                if (string == null || empty) {
                    setText(null);
                } else {
                    setText(string);
                }
            }
        });

        logoutMenuItem.setDisable(true);

        /**
         * edit pane
         */
        //Cell factory for the data the season number column
        seasonCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().seasonNumProperty()
        );

        //Cell factory for the data the episode column
        episodeCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().episodeNameProperty()
        );

        //Cell factory for the data in the watched column
        watchedCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, Boolean> param) ->
                        param.getValue().getValue().watchedProperty()
        );

        // add checkboxes
        watchedCol.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(watchedCol));

        // add master root to TreeTableView
        tvEpisodeTable.setRoot(masterRoot);
        // set root as expanded by default
        masterRoot.setExpanded(true);
        // hide master root
        tvEpisodeTable.setShowRoot(false);
        // set TreeTableView as editable
        tvEpisodeTable.setEditable(true);
        // set watched column as editable
        watchedCol.setEditable(true);
        // add columns to TreeTableView
        tvEpisodeTable.getColumns().setAll(seasonCol, episodeCol, watchedCol);
        // populate the table with data

        seasonCol.prefWidthProperty().bind(tvEpisodeTable.widthProperty().multiply(0.20));
        episodeCol.prefWidthProperty().bind(tvEpisodeTable.widthProperty().multiply(0.64));
        watchedCol.prefWidthProperty().bind(tvEpisodeTable.widthProperty().multiply(0.15));

        /**
         * display pane
         */
        //Cell factory for the data the season number column
        seasonDisplayCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().seasonNumProperty()
        );

        //Cell factory for the data the episode column
        episodeDisplayCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().episodeNameProperty()
        );

        //Cell factory for the data in the watched column
        watchedDisplayCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, Boolean> param) ->
                        param.getValue().getValue().watchedProperty()
        );

        // add master root to TreeTableView
        tvEpisodeDisplayTable.setRoot(masterRoot);
        // set root as expanded by default
        masterRoot.setExpanded(true);
        // hide master root
        tvEpisodeDisplayTable.setShowRoot(false);
        // set TreeTableView as editable
        tvEpisodeDisplayTable.setEditable(true);
        // set watched column as editable
        watchedDisplayCol.setEditable(true);
        // add columns to TreeTableView
        tvEpisodeDisplayTable.getColumns().setAll(seasonDisplayCol, episodeDisplayCol, watchedDisplayCol);
        // populate the table with data

        seasonDisplayCol.prefWidthProperty().bind(tvEpisodeDisplayTable.widthProperty().multiply(0.20));
        episodeDisplayCol.prefWidthProperty().bind(tvEpisodeDisplayTable.widthProperty().multiply(0.64));
        watchedDisplayCol.prefWidthProperty().bind(tvEpisodeDisplayTable.widthProperty().multiply(0.15));
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
                boolean sameTitle = false;
                for (Media media : watchlist.getList()) {
                    if (media.getTitle().equals(mediaName) && media instanceof TvShow) {
                        sameTitle = true;
                    }
                }
                if (!sameTitle) {
                    Film film = mediaCreator.createFilm(mediaName);
                    watchlist.put(mediaName, film);
                } else {
                    Action action = dialogPane.createConfirmDialog("Problem with that name", "Each item in your watchlist must have a unique name.\n" +
                            "Adding this item will overwrite the film named: " + mediaName + "\n\n" +
                            "Selecting No will open the TV show " + mediaName);
                    if (action.toString().equals("YES")) {
                        Film film = mediaCreator.createFilm(mediaName);
                        watchlist.put(mediaName, film);
                    }
                }
            } else {
                boolean sameTitle = false;
                for (Media media : watchlist.getList()) {
                    if (media.getTitle().equals(mediaName) && media instanceof Film) {
                        sameTitle = true;
                    }
                }
                if (!sameTitle) {
                    TvShow show = mediaCreator.createTvShow(mediaName);
                    watchlist.put(mediaName, show);
                } else {
                    Action action = dialogPane.createConfirmDialog("Problem with that name", "Each item in your watchlist must have a unique name.\n" +
                            "Adding this item will overwrite the TV show named: " + mediaName + "\n\n" +
                            "Selecting No will open the film " + mediaName);
                    if (action.toString().equals("YES")) {
                        TvShow show = mediaCreator.createTvShow(mediaName);
                        watchlist.put(mediaName, show);
                    }
                }

            }
            newMediaTextField.clear();
            filterField.clear();
            setSortToAll();

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
     * Selects film as the media to be added.
     */
    @FXML
    public void setMediaToFilm() {
        mediaType = "film";
        addButton.setText("Add Film");
        newMediaTextField.setPromptText("Enter Name of Film to Add");
        filmSelectButton.setVisible(false);
        tvSelectButton.setVisible(true);
    }

    /**
     * Selects tv as the media to be added.
     */
    @FXML
    public void setMediaToTV() {
        mediaType = "tv";
        addButton.setText("Add TV Show");
        newMediaTextField.setPromptText("Enter Name of TV Show to Add");
        tvSelectButton.setVisible(false);
        filmSelectButton.setVisible(true);
    }

    /**
     * Toggles between film/tv edit and display panes.
     */
    @FXML
    public void toggleEdit() {
        if (mediaList.getSelectionModel().getSelectedItem() != null) {
            mediaName = mediaList.getSelectionModel().getSelectedItem().getTitle();
            // Toggling from edit to display.
            if (!editToggleButton.isSelected()) {
                filterField.clear();
                filterField.setDisable(false);
                isTvEditPane = false;
                if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
                    if (filmTitleTextField.getText().equals("")) {
                        filmTitleTextField.setText(mediaName);
                    }
                    if (mediaName.equals(filmTitleTextField.getText())) {
                        // A new media object does not need to be created.
                        watchlist.get(mediaName).setTitle(filmTitleTextField.getText());
                        watchlist.get(mediaName).setGenre(filmGenreTextField.getText());
                        ((Film) watchlist.get(mediaName)).setDirector(filmDirectorTextField.getText());
                        ((Film) watchlist.get(mediaName)).setRating(filmRatingTextField.getText());
                        watchlist.get(mediaName).setRuntime(filmRuntimeTextField.getText());
                        ((Film) watchlist.get(mediaName)).setProducer(filmProducerTextField.getText());
                        ((Film) watchlist.get(mediaName)).setWriter(filmWriterTextField.getText());
                        watchlist.get(mediaName).setDescription(filmDescriptionTextField.getText());
                    } else {
                        // A new media object needs to be created.
                        Film film = mediaCreator.createFilm(filmTitleTextField.getText());
                        film.setGenre(filmGenreTextField.getText());
                        film.setDirector(filmDirectorTextField.getText());
                        film.setRating(filmRatingTextField.getText());
                        film.setRuntime(filmRuntimeTextField.getText());
                        film.setProducer(filmProducerTextField.getText());
                        film.setWriter(filmWriterTextField.getText());
                        film.setDescription(filmDescriptionTextField.getText());

                        // Refresh the watchlist after creation.
                        watchlist.remove(mediaName);
                        watchlist.put(filmTitleTextField.getText(), film);
                    }
                    mediaName = filmTitleTextField.getText();
                    watchlist.update();
                    updateMediaList();
                } else if (mediaList.getSelectionModel().getSelectedItem() instanceof TvShow) {
                    if (tvTitleTextField.getText().equals("")) {
                        tvTitleTextField.setText(mediaName);
                    }

                    String tvNumEpisodes = tvNumEpisodesTextField.getText();
                    if (tvNumEpisodes.equals("")) {
                        tvNumEpisodes = "0";
                    }

                    String tvNumSeasons = tvNumSeasonsTextField.getText();
                    if (tvNumSeasons.equals("")) {
                        tvNumSeasons = "0";
                    }

                    if (mediaName.equals(tvTitleTextField.getText())) {
                        // A new media object does not need to be created.
                        watchlist.get(mediaName).setTitle(tvTitleTextField.getText());
                        watchlist.get(mediaName).setGenre(tvGenreTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setCreator(tvCreatorTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setNetwork(tvNetworkTextField.getText());
                        watchlist.get(mediaName).setRuntime(tvRuntimeTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setNumSeasons(tvNumSeasons);
                        ((TvShow) watchlist.get(mediaName)).setNumEpisodes(tvNumEpisodes);
                        watchlist.get(mediaName).setDescription(tvDescriptionTextField.getText());
                        ((TvShow) watchlist.get(mediaName)).setEpisodeList(episodeList);

                        List<Boolean> episodeBoolList = new ArrayList<>();
                        List<Boolean> seasonBoolList = new ArrayList<>();
                        List<TreeItem<Episode>> list = tvEpisodeTable.getRoot().getChildren();

                        for (int i = 0; i < list.size(); i++) {
                            seasonBoolList.add(list.get(i).getValue().getWatched());
                            for (int j = 0; j < list.get(i).getChildren().size(); j++) {
                                episodeBoolList.add(list.get(i).getChildren().get(j).getValue().getWatched());
                            }
                        }



                        ((TvShow) watchlist.get(mediaName)).getSeasonWatchedList().setAll(seasonBoolList);

                        int count = 0;

                        for (int i = 0; i < list.size(); i++) {
                            boolean seasonBool = list.get(i).getValue().getWatched();
                            ((TvShow) watchlist.get(mediaName)).setSeasonWatchedList(i, seasonBool);
                            for (int j = 0; j < list.get(i).getChildren().size(); j++) {
                                if (seasonBool) {
                                    list.get(i).getChildren().get(j).getValue().setWatched(true);
                                } else {
                                    list.get(i).getChildren().get(j).getValue().setWatched(episodeBoolList.get(count));
                                }
                                count++;
                            }
                        }

                        // tv show display pane table
                        addEpisodesToTable(episodeList, tvEpisodeTable, seasonCol, episodeCol, watchedCol);
                    } else {
                        // A new media object needs to be created.
                        TvShow show = mediaCreator.createTvShow(tvTitleTextField.getText());
                        show.setGenre(tvGenreTextField.getText());
                        show.setCreator(tvCreatorTextField.getText());
                        show.setNetwork(tvNetworkTextField.getText());
                        show.setRuntime(tvRuntimeTextField.getText());
                        show.setNumSeasons(tvNumSeasons);
                        show.setNumEpisodes(tvNumEpisodes);
                        show.setDescription(tvDescriptionTextField.getText());
                        show.setEpisodeList(episodeList);
                        // tv show display pane table
                        addEpisodesToTable(episodeList, tvEpisodeTable, seasonCol, episodeCol, watchedCol);

                        ObservableList<Boolean> tempList = new ObservableListWrapper<>(new ArrayList<>());
                        for (int i = 0; i < episodeList.size(); i++) {
                            tempList.add(false);
                        }
                        show.setSeasonWatchedList(tempList);

                        // Refresh the watchlist after creation.
                        watchlist.remove(mediaName);
                        watchlist.put(tvTitleTextField.getText(), show);
                    }
                    mediaName = tvTitleTextField.getText();
                    updateMediaList();
                }
                setListIndex();
            } else {
                isTvEditPane = true;
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
        Thread thread = new Thread(new Fetch(this, mediaList, mediaEditType));
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Sets the watched value for the currently selected film.
     */
    @FXML
    public void setWatched() {
        Media media = mediaList.getSelectionModel().getSelectedItem();
        if (filmWatchedCheckBox.isSelected() || tvWatchedCheckBox.isSelected()) {
            media.setWatched("true");
        } else {
            media.setWatched("false");
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
        fileChooser.setInitialDirectory(saveDir);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Watchlist Files (*.wl)", "*.wl"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            stage.setTitle("WatchlistPro - " + saveFile.getName());
            watchlist.clear();
            saveList();
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
        fileChooser.setInitialDirectory(saveDir);
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
        File file = new File(saveDir + slash + saveFile.getName());
        io.save(watchlist.getMap(), file);
    }

    /**
     * Saves the contents of the watchlist to the file system when the menu item is selected.
     */
    @FXML
    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.setInitialDirectory(saveDir);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Watchlist Files (*.wl)", "*.wl"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            stage.setTitle("WatchlistPro - " + saveFile.getName());
            saveList();
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
        if (isLoggedIn) {
            logoutFromServer();
        }
        saveList();
        preferences.remove("recentList");
        preferences.putByteArray("recentList", byteArrayHandler.writeByteArray(recentList));
        Platform.exit();
        System.exit(0);
    }

    // Server Menu

    /**
     * Closes user login pane and returns to main application pane. Triggered by Cancel button.
     */
    @FXML
    public void cancelAccountCreation() {
        createAccountPane.setVisible(false);
        createAccountPane.setDisable(true);
        root.setVisible(true);
    }

    /**
     * Switches view to account creation pane. Triggered by Server menu > Create Account.
     */
    @FXML
    public void switchToAccountCreatePage() {
        // displays account creation pane
        cancelServerLogin();
        cancelLoadChoice();
        Platform.runLater(createUserNameField::requestFocus);
        createAccountPane.setVisible(true);
        createAccountPane.setDisable(false);
        root.setVisible(false);
    }

    /**
     * Gets username and password to create account with. Sets username and password.
     * @return true if user entered both a name and password, else false.
     */
    @FXML
    protected boolean getUserCredentials() {
        // user entered something in userNameField
        if (createUserNameField.getText() != null && !createUserNameField.getText().isEmpty()) {
            username = createUserNameField.getText();
            // user entered something in both userNameField and passwordField
            if (createPasswordField.getText() != null && !createPasswordField.getText().isEmpty()) {
                password = createPasswordField.getText();

                return true;
            }
        }
        return false;
    }

    /**
     * Create an account on the server. Triggered by Create Account button on account pane.
     */
    @FXML
    public void createAccount() {
        cancelAccountCreation();
        cancelLoadChoice();
        Client client = new Client();
        // if user entered name & password try to create account
        if (isLoggedIn) {
            logoutFromServer();
        }
        if (getUserCredentials() && client.isConnected()) {
            try {
                Thread account = client.send("add");
                Thread add = client.send(username + "-=-" + password);
                Thread quit = client.send("quit");

                account.start();
                account.join();

                add.start();
                add.join();

                // clear createUserNameField and createPasswordField
                createUserNameField.clear();
                createPasswordField.clear();

                // set view back to main application
                createAccountPane.setVisible(false);
                createAccountPane.setDisable(true);
                root.setVisible(true);

                quit.start();
                quit.join();

                if (!client.isAccountCreated()) {
                    DialogPane dialogPane = new DialogPane();
                    dialogPane.createWarningDialog("Failed to create account", "The account name you enter is not valid.");
                } else {

                    userNameField.setText(username);
                    passwordField.setText(password);
                    loginToServer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cancelAccountCreation();
        }
    }

    /**
     * Closes user login pane and returns to main application pane. Triggered by Cancel button.
     */
    @FXML
    public void cancelServerLogin() {
        userLoginPane.setVisible(false);
        userLoginPane.setDisable(true);
        root.setVisible(true);
    }

    /**
     * Switches view to login pane. Triggered by Server menu > Login.
     */
    @FXML
    protected void switchToLoginPage() {
        // displays account login pane

        cancelAccountCreation();
        cancelLoadChoice();

        Platform.runLater(userNameField::requestFocus);
        userLoginPane.setVisible(true);
        userLoginPane.setDisable(false);
        root.setVisible(false);
    }

    /**
     * Gets username and password from login attempt. Sets username and password.
     * @return true if user entered both a name and password, else false.
     */
    @FXML
    protected boolean getUserLogin() {
        // user entered something in userNameField
        if (userNameField.getText() != null && !userNameField.getText().isEmpty()) {
            username = userNameField.getText();
            // user entered something in both userNameField and passwordField
            if (passwordField.getText() != null && !passwordField.getText().isEmpty()) {
                password = passwordField.getText();
                return true;
            }
        }
        return false;
    }

    /**
     * Login to the server. Triggered by Login button on login pane.
     */
    @FXML
    public void loginToServer() {
        // if user entered name & password try to login
        Client client = new Client();

        if (getUserLogin() && client.isConnected()) {
            try {
                Thread login = client.send("login" + "-=-" + username + "-=-" + password);
                Thread quit = client.send("quit");

                login.start();
                login.join();

                // clear userNameField and passwordField
                userNameField.clear();
                passwordField.clear();

                // set view back to main application
                userLoginPane.setVisible(false);
                userLoginPane.setDisable(true);
                root.setVisible(true);

                quit.start();
                quit.join();

                if (client.isLoggedIn()) {
                    usernameMenuItem.setText("Logged in as: " + username);
                    isLoggedIn = true;
                    loginMenuItem.setDisable(true);
                    logoutMenuItem.setDisable(false);
                } else {
                    DialogPane dialogPane = new DialogPane();
                    dialogPane.createWarningDialog("Not logged in" , "You were not logged in to the server. " +
                            "Check your username and password and try again.");
                    switchToLoginPage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cancelServerLogin();
        }
    }

    /**
     * Logs the user out of the server.
     */
    @FXML
    protected void logoutFromServer() {
        Client client = new Client();
        if (isLoggedIn && client.isConnected()) {
            try {
                Thread logout = client.send("logout" + "-=-" + username + "-=-" + password);
                Thread quit = client.send("quit");

                logout.start();
                logout.join();

                quit.start();
                quit.join();

                if (!client.isLoggedIn()) {
                    usernameMenuItem.setText("Logged in as: <none>");
                    isLoggedIn = false;
                    loginMenuItem.setDisable(false);
                    logoutMenuItem.setDisable(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            username = "";
            password = "";
        }
    }

    /**
     * Get the list of saves from the server for load from server.
     */
    @FXML
    public void getSaves() {
        Client client = new Client();
        if (isLoggedIn && client.isConnected()) {
            try {
                Thread saves = client.send("getsaves" + "-=-" + username);
                Thread quit = client.send("quit");

                saves.start();
                saves.join();

                quit.start();
                quit.join();

                String[] saveArray = client.getSaveArray();
                if (!saveArray[0].isEmpty()) {
                    System.out.println();
                    List<String> arrayList = Arrays.asList(saveArray);
                    ObservableList<String> list = new ObservableListWrapper<>(arrayList);
                    loadList.setItems(list);
                    loadList.getSelectionModel().select(0);
                    switchToLoadChoice();
                } else {
                    dialogPane.createWarningDialog("No server saves found",
                            "It looks like you've never saved to the server before.\n" +
                                    "Try saving to the server before loading from the server.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!isLoggedIn && client.isConnected()) {
            switchToLoginPage();
        } else {
            cancelLoadChoice();
        }
    }

    /**
     * Checks the list of saves on the server to see if any of the names are the same as the currently opened
     * save file. Used during save to server.
     */
    @FXML
    public void checkSaves() {
        Client client = new Client();
        if (isLoggedIn && client.isConnected()) {
            try {
                Thread saves = client.send("getsaves" + "-=-" + username);
                Thread quit = client.send("quit");

                saves.start();
                saves.join();

                quit.start();
                quit.join();

                String[] saveArray = client.getSaveArray();
                if (saveArray != null) {
                    Optional<String> optional = Optional.of("");
                    List<String> arrayList = Arrays.asList(saveArray);
                    boolean isSame = false;
                    for (String string : arrayList) {
                        if (string.equals(saveFile.getName())) {
                            isSame = true;
                            DialogPane dialogPane = new DialogPane();
                            optional = dialogPane.createInputDialog("Conflicting names",
                                    "The server already has a file with the same name as your currently opened file.\n" +
                                            "Not changing the name will overwrite your server file.",
                                    "Enter a new name:", saveFile.getName());
                        }
                    }
                    if (isSame && optional.isPresent()) {
                        String[] nameArray = new String[2];
                        nameArray = optional.get().split(".wl");
                        String name = nameArray[0];
                        if (nameArray[0] == null) {
                            name = "";
                        }
                        saveToServer(name + ".wl");
                        saveFile = new File(name + ".wl");
                        stage.setTitle("WatchlistPro - " + saveFile.getName());
                        saveList();
                        updateRecentMenu(saveFile.getName());
                    } else if (!isSame) {
                        saveToServer(saveFile.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!isLoggedIn && client.isConnected()) {
            switchToLoginPage();
        }

    }

    /**
     * Cancels the load choice.
     */
    @FXML
    protected void cancelLoadChoice() {
        loadFromServerPane.setVisible(false);
        loadFromServerPane.setDisable(true);
        root.setVisible(true);
    }

    /**
     * Sends the load choice to the server.
     */
    @FXML
    public void sendLoadChoice() {
        String selectedFile = loadList.getSelectionModel().getSelectedItem();
        Action action;
        boolean found = false;
        if (!saveFile.getName().equals(selectedFile)) {
            for (String mediaName : recentList) {
                if (selectedFile.equals(mediaName)) {
                    found = true;
                    action = dialogPane.createConfirmDialog("Warning!",
                            "Doing this will overwrite your WatchList with the same name. Continue?");
                    if (action.toString().equals("YES")) {
                        loadFromServer();
                        cancelLoadChoice();

                    } else {
                        cancelLoadChoice();
                    }
                }
            }
        } else {
            action = dialogPane.createConfirmDialog("Warning!",
                    "Doing this will overwrite your WatchList with the same name. Continue?");
            if (action.toString().equals("YES")) {
                loadFromServer();
                cancelLoadChoice();

            } else {
                cancelLoadChoice();
            }
        }

        if (!found) {
            loadFromServer();
            cancelLoadChoice();
        }
    }

    /**
     * Load the library from the server into the media map.
     */
    @FXML
    protected void loadFromServer() {
        cancelServerLogin();
        cancelAccountCreation();
        Client client = new Client();
        if (isLoggedIn && client.isConnected()) {
            try {
                saveFile = new File(saveDir + slash + loadList.getSelectionModel().getSelectedItem());

                stage.setTitle("WatchlistPro - " + saveFile.getName());

                watchlist.clear();

                if (!saveFile.exists()) {
                    saveList();
                }

                updateMediaList();
                clearDisplayPane();
                updateRecentMenu(saveFile.getName());

                Thread load = client.send(
                        "load" + "-=-" + username + "-=-" + saveFile);
                Thread quit = client.send("quit");

                load.start();
                load.join();

                quit.start();
                quit.join();

                io.load(watchlist.getMap(), saveFile);

                updateMediaList();
                mediaList.getSelectionModel().select(0);
            } catch (IOException e) {
                System.err.println("IOException");
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.err.println("Interrupted Exception");
                e.printStackTrace();
            }
        } else if (!isLoggedIn && client.isConnected()) {
            switchToLoginPage();
        } else {
            cancelLoadChoice();
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
        MediaCollection temp = new MediaCollection();

        watchlist.update();
        if (sortState == 0) {
            temp = watchlist;
            mediaList.setItems(temp.getList());
        } else if (sortState == 1) {
            temp.update();
            for (Media media : watchlist.getList()) {
                if (media instanceof Film) {
                    temp.put(media.getTitle(), media);
                }
            }
            mediaList.setItems(temp.getList());
        } else if (sortState == 2) {
            temp.update();
            for (Media media : watchlist.getList()) {
                if (media instanceof TvShow) {
                    temp.put(media.getTitle(), media);
                }
            }
            mediaList.setItems(temp.getList());
        }


        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Media> filteredData = new FilteredList<>(temp.getList(), p -> true);

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

        temp.sort();

        // Reselect the correct media object in the media list.
        if (!mediaList.equals(temp.getList())) {
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
                editToggleButton.setText("Save");
                fetchButton.setDisable(false);

                // select tv or film edit panel
                switch (mediaEditType) {
                    case "tv":
                        masterSeasonList = setTvEditPane();
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
                        masterSeasonList = setTvDisplayPane();
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
     * Save the contents of the media map to the server.
     */
    public void saveToServer(String saveName) {
        Client client = new Client();
        if (isLoggedIn && client.isConnected()) {
            String data = "";
            for (Map.Entry<String, Media> entry : watchlist.entrySet()) {
                Media media = entry.getValue();
                String jsonString = gson.toJson(media.getMap());
                data += jsonString + "//";
            }

            if (data.equals("")) {
                data = "{}";
            }

            try {
                Thread save = client.send("save" + "-=-" + username + "-=-" + saveName + "-=-" + data);
                Thread quit = client.send("quit");

                save.start();
                save.join();

                quit.start();
                quit.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!isLoggedIn && client.isConnected()) {
            switchToLoginPage();
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
            saveFile = new File(saveDir + slash + name);
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

    /**
     * Check the size of the recent list.
     */
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

        tvEpisodeTable.getColumns().clear();
        tvEpisodeDisplayTable.getColumns().clear();
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
     * Populates the tv display pane based on values from the model. Called on start.
     */
    private ObservableList<List<Episode>> setTvDisplayPane() {
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

        ObservableList<List<Episode>> tempList = new ObservableListWrapper<>(new ArrayList<>());
        for (List<Episode> episodes : show.getEpisodeList()) {
            tempList.add(episodes);
        }
        // tv show display pane table
        addEpisodesToTable(tempList, tvEpisodeDisplayTable, seasonDisplayCol, episodeDisplayCol, watchedDisplayCol);

        if (show.getWatched().equals("true")) {
            checkEveryEpisodeInShow();
        }

        List<TreeItem<Episode>> list = tvEpisodeDisplayTable.getRoot().getChildren();
        for (int i = 0; i < list.size(); i++) {
            boolean seasonWatched = false;
            if (show.getSeasonWatchedList().get(i)) {
                list.get(i).getValue().setWatched(true);
                seasonWatched = true;
            }
            boolean watched = true;
            for (int j = 0; j < list.get(i).getChildren().size(); j++) {
                if (!list.get(i).getChildren().get(j).getValue().getWatched()) {
                    watched = false;
                }
                if (seasonWatched) {
                    list.get(i).getChildren().get(j).getValue().setWatched(true);
                }
            }

            if (watched && list.get(i).getChildren().size() > 0) {
                list.get(i).getValue().setWatched(true);
            }
        }

        episodeList.set(tempList);

        masterSeasonList = new ObservableListWrapper<>(new ArrayList<>());

        return tempList;
    }

    /**
     * Populates the film edit pane based on passed in arguments.
     */
    private void setFilmEditPane() {
        Film film = (Film) mediaList.getSelectionModel().getSelectedItem();

        filmTitleTextField.setText(film.getTitle());
        if (film.getWatched().equals("true")) {
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
     * Populates the tv edit pane based on passed in arguments. Called on start.
     */
    private ObservableList<List<Episode>> setTvEditPane() {
        TvShow show = (TvShow) mediaList.getSelectionModel().getSelectedItem();

        tvTitleTextField.setText(show.getTitle());
        if (show.getWatched().equals("true")) {
            tvWatchedCheckBox.setSelected(true);
        }
        tvGenreTextField.setText(show.getGenre());
        tvCreatorTextField.setText(show.getCreator());
        tvNetworkTextField.setText(show.getNetwork());
        tvRuntimeTextField.setText(show.getRuntime());
        tvNumSeasonsTextField.setText(show.getNumSeasons());
        tvNumEpisodesTextField.setText(show.getNumEpisodes());
        tvDescriptionTextField.setText(show.getDescription());

        ObservableList<List<Episode>> tempList = new ObservableListWrapper<>(new ArrayList<>());
        for (List<Episode> episodes : show.getEpisodeList()) {
            tempList.add(episodes);
        }
        // tv show edit pane table
        addEpisodesToTable(tempList, tvEpisodeTable, seasonCol, episodeCol, watchedCol);

        if (show.getWatched().equals("true")) {
            checkEveryEpisodeInShow();
        }

        List<TreeItem<Episode>> list = tvEpisodeTable.getRoot().getChildren();
        for (int i = 0; i < list.size(); i++) {
            boolean watched = true;
            for (int j = 0; j < list.get(i).getChildren().size(); j++) {
                if (!list.get(i).getChildren().get(j).getValue().getWatched()) {
                    watched = false;
                }
            }
            if (watched && list.size() > 0) {
                list.get(i).getValue().setWatched(true);
            }
        }

        episodeList.set(tempList);

        masterSeasonList = new ObservableListWrapper<>(new ArrayList<>());

        return tempList;

    }

    /**
     * Populates the film edit pane based on passed in arguments. Called after fetch.
     */
    protected void setFilmEditPane(List<String> outputList) {
        filmTitleTextField.setText(outputList.get(0));
        if (outputList.get(1).equals("true")) {
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
     * Populates the tv edit pane based on passed in arguments. Called after fetch.
     */
    protected void setTvEditPane(List<String> outputList) {
        tvTitleTextField.setText(outputList.get(0));
        if (outputList.get(1).equals("true")) {
            tvWatchedCheckBox.setSelected(true);
        }
        tvGenreTextField.setText(outputList.get(2));
        tvCreatorTextField.setText(outputList.get(3));
        tvNetworkTextField.setText(outputList.get(4));
        tvRuntimeTextField.setText(outputList.get(5));
        tvNumSeasonsTextField.setText(outputList.get(6));
        tvNumEpisodesTextField.setText(outputList.get(7));
        tvDescriptionTextField.setText(outputList.get(8));

        Type listType = new TypeToken<List<List<String>>>(){}.getType();
        List<List<String>> list = gson.fromJson(outputList.get(9), listType);

        ObservableList<List<Episode>> seasonList = FXCollections.observableArrayList();
        for (List<String> episodes : list) {
            List<Episode> episodeList = new ArrayList<>();
            for (String json : episodes) {
                Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                HashMap<String, String> map = gson.fromJson(json, mapType);
                Episode episode = new Episode(map.get("seasonNum"), map.get("episodeName"), Boolean.parseBoolean(map.get("watched")));
                episodeList.add(episode);
            }
            seasonList.add(episodeList);
        }

        addEpisodesToTable(seasonList, tvEpisodeTable, seasonCol, episodeCol, watchedCol);

        masterSeasonList = new ObservableListWrapper<>(new ArrayList<>());

        episodeList.set(seasonList);

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

    /**
     * Switch to the server file choose pane when loading from the server.
     */
    private void switchToLoadChoice() {
        // displays account login pane
        //Platform.runLater(userNameField::requestFocus);
        cancelServerLogin();
        cancelAccountCreation();
        loadFromServerPane.setVisible(true);
        loadFromServerPane.setDisable(false);
        root.setVisible(false);

    }

    /**
     * Starts the progress indicator during fetch.
     */
    protected void startProgressIndicator() {
        progressIndicatorPane.setDisable(false);
        progressIndicatorPane.setVisible(true);
    }

    /**
     * Stops the progress indicator after fetch
     */
    protected void stopProgressIndicator() {
        progressIndicatorPane.setDisable(true);
        progressIndicatorPane.setVisible(false);
    }

    /**
     * Add all the episodes for each season of the TV show to the tree tables.
     * @param seasons is the TV show's season list.
     * @param treeTable is episode table to be set
     * @param seasCol is season column of table
     * @param epCol is episode column of table
     * @param watchCol is watched column of table
     */
    public void addEpisodesToTable(ObservableList<List<Episode>> seasons, TreeTableView<Episode> treeTable,
                                   TreeTableColumn<Episode, String> seasCol, TreeTableColumn<Episode, String> epCol, TreeTableColumn<Episode, Boolean> watchCol) {

        episodeList = new SimpleListProperty<>();
        episodeList.set(seasons);

        masterRoot = new TreeItem<>(new Episode("Master", "", false));
        seasonRootList = new ArrayList<>();

        seasCol = new TreeTableColumn<>();
        epCol = new TreeTableColumn<>();
        watchCol = new TreeTableColumn<>();

        seasCol.setText("Season");
        epCol.setText("Episode");
        watchCol.setText("Watched");

        seasCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().seasonNumProperty()
        );

        //Cell factory for the data the episode column
        epCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, String> param) ->
                        param.getValue().getValue().episodeNameProperty()
        );

        //Cell factory for the data in the watched column
        watchCol.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Episode, Boolean> param) ->
                        param.getValue().getValue().watchedProperty()
        );

        if (isTvEditPane) {
            // add checkboxes
            watchCol.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(watchCol));
        }

        treeTable.setRoot(masterRoot);
        // set root as expanded by default
        masterRoot.setExpanded(true);
        // hide master root
        treeTable.setShowRoot(false);
        // set TreeTableView as editable
        treeTable.setEditable(true);
        // set watched column as editable
        watchCol.setEditable(true);
        // add columns to TreeTableView
        treeTable.getColumns().setAll(seasCol, epCol, watchCol);

        for (seasonNum = 0; seasonNum < seasons.size(); seasonNum++) {
            // create season roots, save them to list
            seasonRootList.add(new TreeItem<>(new Episode("Season " + (seasonNum), "", false)));

            // for each episode object in the current season
            seasons.get(seasonNum).stream().forEach((episode) ->
                    seasonRootList.get(seasonNum).getChildren().add(new TreeItem<>(episode)));

            // add season roots to master root
            masterRoot.getChildren().add(seasonNum, seasonRootList.get(seasonNum));
        }
        seasCol.prefWidthProperty().bind(treeTable.widthProperty().multiply(0.20));
        epCol.prefWidthProperty().bind(treeTable.widthProperty().multiply(0.64));
        watchCol.prefWidthProperty().bind(treeTable.widthProperty().multiply(0.15));
    }

    /**
     * Sets all episodes in a show to watched if the main check box is checked.
     */
    private void checkEveryEpisodeInShow () {

        List<TreeItem<Episode>> list = tvEpisodeTable.getRoot().getChildren();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getChildren().size(); j++) {
                list.get(i).getChildren().get(j).getValue().setWatched(true);
            }
        }
    }

    /**
     * Checks what the OS to decide where to read/write the save files.
     */
    public void checkOS() {
        String os = System.getProperty("os.name");
        if (os.equals("Windows")) {
            saveDir = new File(System.getProperty("user.home"), "Application Data\\WatchLists");
            slash = "\\";
        } else {
            saveDir = new File(System.getProperty("user.home") + "/WatchLists");
            slash = "/";
        }

        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
    }

    /**
     * Set mediaList to display all films/tv shows
     */
    public void setSortToAll() {
        sortState = 0;
        sortMenuButton.setText("View All");
        updateMediaList();
    }

    /**
     * Set mediaList to display only films
     */
    public void setSortToFilmOnly() {
        sortState = 1;
        sortMenuButton.setText("View Films Only");
        updateMediaList();
    }

    /**
     * Set mediaList to display only tv shows
     */
    public void setSortToTvOnly() {
        sortState = 2;
        sortMenuButton.setText("View TV Shows Only");
        updateMediaList();
    }

}