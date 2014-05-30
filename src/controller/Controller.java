package controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import model.*;
import client.Client;
import util.FileIO;
import view.AboutDialog;

import java.io.*;
import java.lang.String;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.prefs.Preferences;

// TODO warn user that it will not save if they dont press done after editing
// TODO during load from server warn user that overwrite will occur if file already exists
// TODO OR load from server should create a new library and open the save dialog
// TODO during save to server give user option to changed save name, warn if overwrite will occur, use get saves
// TODO notify the user if fetch does not find title
// TODO notify user if not connected to the internet for all server based functions
// TODO add logged in as: label
// TODO allow delete button to call deleteMedia() only when no other field is in focus
// TODO doc comments for media and film
// TODO drop down list buttons for TV/Film toggle
// TODO sort ListView into TV, Film, or All with dropdown

// TODO encrypt username and password with RSA, first thing server sends is its public key

/**
 * Controls the WatchlistPro view.
 */
public class Controller implements Initializable {

    private MediaCollection watchlist;
    private MediaCreator mediaCreator;
    private FileIO io;
    private Stage stage;
    private LinkedList<String> recentList;
    private ByteArrayHandler byteArrayHandler;
    private String username;
    private String password;
    private Boolean isLoggedIn;

    private TreeItem<Episode> masterRoot; // master root of dropdown menu
    private List<TreeItem<Episode>> seasonRootList; // list of season roots

    private String slash;
    private File saveDir;
    private File saveFile;

    private Gson gson;
    private Preferences preferences;

    private ObservableList<List<Episode>> masterSeasonList;
    private ListProperty<List<Episode>> episodeList;
    private int numberOfSeasons;

    // Control variables
    private int mediaIndex;
    private String mediaName;
    private String mediaType;
    private String mediaEditType;
    private int seasonNum; // loop counter for episodes table

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
//    @FXML
//    private TableView<String> episodeTable;
//    @FXML
//    private TableColumn<TvShow, String> seasonNumCol;
//    @FXML
//    private TableColumn<TvShow, String> episodeTitleCol;
//    @FXML
//    private TableColumn<TvShow, String> watchedCol;
    @FXML
    private TreeTableView<Episode> tvEpisodeTable;
    @FXML
    private TreeTableColumn<Episode,String> seasonCol;
    @FXML
    private TreeTableColumn<Episode, String> episodeCol;
    @FXML
    private TreeTableColumn<Episode, Boolean> watchedCol;


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

        preferences.remove("recentList");
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

//                if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
//                    setFilmDisplayPane();
//                } else {
//                    setTvDisplayPane();
//                }
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
                        ((TvShow) watchlist.get(mediaName)).setEpisodeList(episodeList);
                    } else {
                        TvShow show = mediaCreator.createTvShow(tvTitleTextField.getText());
                        show.setGenre(tvGenreTextField.getText());
                        show.setCreator(tvCreatorTextField.getText());
                        show.setNetwork(tvNetworkTextField.getText());
                        show.setRuntime(tvRuntimeTextField.getText());
                        show.setNumSeasons(tvNumSeasonsTextField.getText());
                        show.setNumEpisodes(tvNumEpisodesTextField.getText());
                        show.setDescription(tvDescriptionTextField.getText());
                        show.setEpisodeList(episodeList);

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
        logoutFromServer();
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
//                System.out.printf("username: %s\n", username);
//                System.out.printf("password: %s\n", password);

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
        // if user entered name & password try to create account
        if (getUserCredentials()) {
            Client client = new Client();
            try {


                Thread account = client.send("add" + "-=-" + username + "-=-" + password);
                Thread quit = client.send("quit");

                account.start();
                account.join();

                // clear createUserNameField and createPasswordField
                createUserNameField.clear();
                createPasswordField.clear();

                // set view back to main application
                createAccountPane.setVisible(false);
                createAccountPane.setDisable(true);
                root.setVisible(true);

                quit.start();
                quit.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
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

//                System.out.printf("username: %s\n", username);
//                System.out.printf("password: %s\n", password);
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
        if (getUserLogin()) {
            isLoggedIn = true;
            loginMenuItem.setDisable(true);
            logoutMenuItem.setDisable(false);
            Client client = new Client();
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void logoutFromServer() {
        if (isLoggedIn) {
            isLoggedIn = false;
            loginMenuItem.setDisable(false);
            logoutMenuItem.setDisable(true);
            Client client = new Client();
            try {
                Thread logout = client.send("logout" + "-=-" + username + "-=-" + password);
                Thread quit = client.send("quit");

                logout.start();
                logout.join();

                quit.start();
                quit.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
            username = "";
            password = "";
        }
    }

    /**
     * Get the list of saves from the server.
     */
    @FXML
    public void getSaves() {
        if (isLoggedIn) {
            Client client = new Client();
            try {
                Thread saves = client.send("getsaves" + "-=-" + username);
                Thread quit = client.send("quit");

                saves.start();
                saves.join();

                quit.start();
                quit.join();

                String[] saveArray = client.getSaveArray();
                List<String> arrayList = Arrays.asList(saveArray);
                ObservableList<String> list = new ObservableListWrapper<>(arrayList);
                loadList.setItems(list);
                loadList.getSelectionModel().select(0);
                switchToLoadChoice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            switchToLoginPage();
        }
    }

    /**
     * Save the contents of the media map to the server.
     */
    @FXML
    public void saveToServer() {
        if (isLoggedIn) {
            String saveName = saveFile.getName();

            String data = "";
            for (Map.Entry<String, Media> entry : watchlist.entrySet()) {
                Media media = entry.getValue();
                String jsonString = gson.toJson(media.getMap()); // JSONValue.toJSONString(media.getMap());
                data += jsonString + "//";
            }

            Client client = new Client();
            try {
                Thread save = client.send("save" + "-=-" + username + "-=-" + saveName + "-=-" + data);
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
        } else {
            switchToLoginPage();
        }

    }

    @FXML
    protected void cancelLoadChoice() {
        loadFromServerPane.setVisible(false);
        loadFromServerPane.setDisable(true);
        root.setVisible(true);
    }

    @FXML
    public void sendLoadChoice() {
        loadFromServer();
        cancelLoadChoice();
    }

    /**
     * Load the library from the server into the media map.
     */
    @FXML
    protected void loadFromServer() {
        if (isLoggedIn) {
            Client client = new Client();
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
            } catch (IOException e) {
                System.err.println("IOException");
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.err.println("Interrupted Exception");
                e.printStackTrace();
            }
        } else {
            switchToLoginPage();
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
     * Populates the tv display pane based on values from the film edit pane. Called on start.
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

        numberOfSeasons = Integer.parseInt(show.getNumSeasons());
        masterSeasonList.addAll(show.getEpisodeList());
        episodeList.set(masterSeasonList);
        addEpisodesToTable(numberOfSeasons, masterSeasonList);
//
//        System.out.println(show.getEpisodeList().size());
//
//        for (List<Episode> episodes : show.getEpisodeList()) {
//            for (Episode episode : episodes) {
//                System.out.println(episode.getEpisodeName());
//            }
//        }

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
     * Populates the tv edit pane based on passed in arguments. Called on start.
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

//        numberOfSeasons = Integer.parseInt(show.getNumSeasons());
//        masterSeasonList.addAll(show.getEpisodeList());
//        addEpisodesToTable(numberOfSeasons, masterSeasonList);

//        System.out.println(show.getEpisodeList().size());
//
//        for (List<Episode> episodes : show.getEpisodeList()) {
//            for (Episode episode : episodes) {
//                System.out.println(episode.getEpisodeName());
//            }
//        }
    }

    /**
     * Populates the film edit pane based on passed in arguments. Called after fetch.
     */
    protected void setFilmEditPane(List<String> outputList) {
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
     * Populates the tv edit pane based on passed in arguments. Called after fetch.
     */
    protected void setTvEditPane(List<String> outputList) {
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

//        System.out.println(outputList.get(0) + " Episode List:");
//        System.out.println();

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

        numberOfSeasons = Integer.parseInt(outputList.get(6));
        masterSeasonList.addAll(seasonList);
        episodeList.set(masterSeasonList);
        addEpisodesToTable(numberOfSeasons, masterSeasonList);

        for (List<Episode> episodes : masterSeasonList) {
            for (Episode episode : episodes) {
                System.out.println(episode.getEpisodeName());
            }
        }

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

    public void addEpisodesToTable(int numSeasons, ObservableList<List<Episode>> seasons) {
        for (seasonNum = 0; seasonNum < numSeasons; seasonNum++){
            // create season roots, save them to list
            seasonRootList.add(new TreeItem<>(new Episode("Season " + (seasonNum + 1), "", false)));

            // for each episode object in the current season
            seasons.get(seasonNum).stream().forEach((episode) ->
                    seasonRootList.get(seasonNum).getChildren().add(new TreeItem<>(episode)));

            // add season roots to master root
            masterRoot.getChildren().add(seasonNum, seasonRootList.get(seasonNum));
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

//    public void initializeAccelerators() {
//        stage.getScene().setOnKeyReleased(keyEvent -> {
//            if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
//                deleteMedia();
//            }
//        });
//    }
}