package watchlistpro;

import com.aquafx_project.AquaFx;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Film;
import model.Media;
import model.TvShow;
import client.Client;

import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls the WatchlistPro view.
 */
public class WatchlistProController implements Initializable {

    // Data structures
    private ObservableMap<String, Media> mediaMap;
    private ObservableList<Media> masterMediaList;

    // Control variables
    private int mediaIndex = -1;
    private String mediaName = null;
    private String mediaType = "film";
    private String mediaEditType;

    // Other
    private MediaCreator mediaCreator;
    private FileIO io;
    private Stage stage;
    private File saveFile;

    // View components
    @FXML
    private ListView<Media> mediaList;
    @FXML
    private TextField filterField;
    @FXML
    private ToggleButton mediaToggleButton;
    @FXML
    private TextField newMediaTextField;
    @FXML
    private ToggleButton editToggleButton;
    @FXML
    private Button fetchButton;
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
    private MenuItem closeWindowMenu;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem serverSave;
    @FXML
    private MenuItem serverLoad;
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

    /**
     * Constructor.
     */
    public WatchlistProController() {
        // TODO Need to get the last open file from Preferences, see clearRecents
        saveFile = new File("store.txt");
        io = new FileIO();
        mediaMap = io.load(new ObservableMapWrapper<>(new HashMap<>()), saveFile);
        masterMediaList = new ObservableListWrapper<>(new ArrayList<Media>(mediaMap.values()));
        mediaCreator = new MediaCreator();
    }

    /**
     * Initialize the view.
     * @param url is not used.
     * @param rb is not used.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AquaFx.style(); // Throws CoreText performance errors

        updateMediaList();
        mediaList.getSelectionModel().select(0);

        //stage.setOnCloseRequest((final WindowEvent windowEvent) -> closeWindow());
    }

    @FXML
    public void createNew() {
        // TODO create new media map, rebuild master media list and update media list
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New Media File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            mediaMap.clear();
            io.save(mediaMap, saveFile);
            masterMediaList = new ObservableListWrapper<>(new ArrayList<Media>(mediaMap.values()));
            updateMediaList();
        }
    }

    @FXML
    public void openLibrary() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            saveFile = selectedFile;
            io.load(mediaMap, saveFile);
            masterMediaList = new ObservableListWrapper<>(new ArrayList<Media>(mediaMap.values()));
            updateMediaList();
        }
    }

    @FXML
    public void clearRecents() {
        // TODO clear recently opened media libraries
        // TODO save recently opened media libraries as entries on this list
        // http://stackoverflow.com/questions/3062630/showing-the-most-recent-opened-items-in-a-menu-bar

    }

    /**
     * Adds new media item to list.
     */
    @FXML
    public void addMedia() {
        if (editToggleButton.isSelected()) {
            editToggleButton.setSelected(false);
            switchView();
        }
        if (newMediaTextField.getCharacters() != null && newMediaTextField.getLength() > 0) {
            mediaName = newMediaTextField.getCharacters().toString();

            if (mediaType.equals("film")) {
                Film film = mediaCreator.createFilm(mediaName);
                masterMediaList.add(film);
                mediaMap.put(mediaName, film);
            } else {
                TvShow show = mediaCreator.createTvShow(mediaName);
                masterMediaList.add(show);
                mediaMap.put(mediaName, show);
            }
            newMediaTextField.clear();
            filterField.clear();
            updateMediaList();

            setListIndex();
            editToggleButton.setSelected(true);
            switchView();
        }
    }

    /**
     * Deletes the selected media item.
     */
    @FXML
    public void deleteMedia() {
        if (masterMediaList.size() > 0 && mediaIndex >= 0 && mediaIndex < masterMediaList.size()) {

            if (editToggleButton.isSelected()) {
                editToggleButton.setSelected(false);
                switchView();
            }

            // Handle ListView selection changes.
            if (mediaList.getSelectionModel().getSelectedItem() != null) {
                String title = mediaList.getSelectionModel().getSelectedItem().getTitle();
                mediaMap.remove(title);
                masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
                updateMediaList();
                filterField.clear();
                mediaList.getSelectionModel().select(0);
            }
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
                        mediaMap.get(mediaName).setTitle(filmTitleTextField.getText());
                        mediaMap.get(mediaName).setGenre(filmGenreTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setDirector(filmDirectorTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setRating(filmRatingTextField.getText());
                        mediaMap.get(mediaName).setRuntime(filmRuntimeTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setProducer(filmProducerTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setWriter(filmWriterTextField.getText());
                        mediaMap.get(mediaName).setDescription(filmDescriptionTextField.getText());
                    } else {
                        Film film = mediaCreator.createFilm(filmTitleTextField.getText());
                        film.setGenre(filmGenreTextField.getText());
                        film.setDirector(filmDirectorTextField.getText());
                        film.setRating(filmRatingTextField.getText());
                        film.setRuntime(filmRuntimeTextField.getText());
                        film.setProducer(filmProducerTextField.getText());
                        film.setWriter(filmWriterTextField.getText());
                        film.setDescription(filmDescriptionTextField.getText());

                        mediaMap.remove(mediaName);
                        mediaMap.put(filmTitleTextField.getText(), film);
                    }
                    mediaName = filmTitleTextField.getText();

                    masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
                    updateMediaList();
                } else {
                    if (mediaName.equals(tvTitleTextField.getText())) {
                        mediaMap.get(mediaName).setTitle(tvTitleTextField.getText());
                        mediaMap.get(mediaName).setGenre(tvGenreTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setCreator(tvCreatorTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNetwork(tvNetworkTextField.getText());
                        mediaMap.get(mediaName).setRuntime(tvRuntimeTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNumSeasons(tvNumSeasonsTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNumEpisodes(tvNumEpisodesTextField.getText());
                        mediaMap.get(mediaName).setDescription(tvDescriptionTextField.getText());
                    } else {
                        TvShow show = mediaCreator.createTvShow(tvTitleTextField.getText());
                        show.setGenre(tvGenreTextField.getText());
                        show.setCreator(tvCreatorTextField.getText());
                        show.setNetwork(tvNetworkTextField.getText());
                        show.setRuntime(tvRuntimeTextField.getText());
                        show.setNumSeasons(tvNumSeasonsTextField.getText());
                        show.setNumEpisodes(tvNumEpisodesTextField.getText());
                        show.setDescription(tvDescriptionTextField.getText());

                        mediaMap.remove(mediaName);
                        mediaMap.put(tvTitleTextField.getText(), show);

                    }
                    mediaName = tvTitleTextField.getText();
                    masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
                    updateMediaList();
                }
                setListIndex();
            } else {
                filterField.setDisable(true);
            }
            switchView();

        } else {
            editToggleButton.setSelected(false);
        }
    }

    /**
     * Saves the contents of the mediaMap to the file system when the menu item is selected.
     */
    @FXML
    public void saveList() {
        io.save(mediaMap, saveFile);
    }

    /**
     * Closes the window when the menu item is selected.
     */
    @FXML
    public void closeWindow() {
        io.save(mediaMap, saveFile);
        Platform.exit();
    }

    /**
     * Save the contents of the media map to the server.
     */
    @FXML
    public void saveToServer() {
        String output = "";
        for (HashMap.Entry entry : mediaMap.entrySet()) {
            Media media = (Media) entry.getValue();
            String jsonString = JSONValue.toJSONString(media.getMap());
            output += jsonString + "<('_')>";
        }
        Client client = new Client();
        try {
            Thread save = client.send("save");
            Thread outputThread = client.send(output);
            Thread quit = client.send("quit");

            save.start();
            save.join();

            outputThread.start();
            outputThread.join();

            quit.start();
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
        Client client = new Client();
        client.setFile(saveFile);
        try {
            Thread load = client.send("load");
            Thread quit = client.send("quit");

            load.start();
            load.join();

            quit.start();
            quit.join();

            io.load(mediaMap, saveFile);
            masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
            updateMediaList();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception");
            e.printStackTrace();
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

            ArrayList<String> outputList = client.getOutputList();
            if (mediaEditType.equals("film")) {
                setFilmEditPane(outputList);
            } else {
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

    private void updateMediaList() {
        mediaList.setItems(masterMediaList);
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

        // Handle ListView selection changes.
        mediaList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switchView();

            if (newValue != null) {

                if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
                    setFilmDisplayPane();
                } else {
                    setTvDisplayPane();
                }
                //mediaName = newValue.getTitle();
                mediaIndex = mediaList.getSelectionModel().getSelectedIndex();
            }
        });

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Media> filteredData = new FilteredList<>(masterMediaList, p -> true);

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

        sort();
        if (!mediaList.equals(masterMediaList)) {
            //setFilteredListIndex();
            for (int i = 0; i < mediaList.getItems().size(); i++) {
                if (mediaList.getItems().get(i) != null){
                    mediaList.getSelectionModel().select(i);
                }
            }

        }
    }

    /**
     * Sorts the master media list.
     */
    private void sort() {
        Collections.sort(masterMediaList, (m1, m2) -> (m1.getTitle()).compareTo(m2.getTitle()));
    }

    /**
     * Sets the index of the ListView to the last created or edited item.
     */
    private void setListIndex() {
        for (int i = 0; i < masterMediaList.size(); i++) {
            if (masterMediaList.get(i).getTitle().equals(mediaName)) {
                mediaList.getSelectionModel().select(i);
            }
        }
    }

    /**
     * Switches between the display view to the edit view.
     */
    private void switchView() {
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
     * Populates the film display pane based on values from the film edit pane.
     */
    private void setFilmDisplayPane() {
        Film film = (Film) mediaList.getSelectionModel().getSelectedItem();

        filmTitleLabel.setText(film.getTitle());
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
    private void setFilmEditPane(ArrayList<String> outputList) {
        filmTitleTextField.setText(outputList.get(0));
        filmGenreTextField.setText(outputList.get(1));
        filmDirectorTextField.setText(outputList.get(2));
        filmRatingTextField.setText(outputList.get(3));
        filmRuntimeTextField.setText(outputList.get(4));
        filmProducerTextField.setText(outputList.get(5));
        filmWriterTextField.setText(outputList.get(6));
        filmDescriptionTextField.setText(outputList.get(7));
    }

    /**
     * Populates the tv edit pane based on passed in arguments.
     */
    private void setTvEditPane(ArrayList<String> outputList) {
        tvTitleTextField.setText(outputList.get(0));
        tvGenreTextField.setText(outputList.get(1));
        tvCreatorTextField.setText(outputList.get(2));
        tvNetworkTextField.setText(outputList.get(3));
        tvRuntimeTextField.setText(outputList.get(4));
        tvNumSeasonsTextField.setText(outputList.get(5));
        tvNumEpisodesTextField.setText(outputList.get(6));
        tvDescriptionTextField.setText(outputList.get(7));
    }

    /**
     * Sets the stage.
     * @param stage the stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}