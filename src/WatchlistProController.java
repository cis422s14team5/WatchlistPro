import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.ButtonType;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;



import model.Film;
import model.Media;
import model.TvShow;
import freebase.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

// TODO don't allow edit button to be pressed if there is no index selected

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
    private Button deleteButton;
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
    private MenuBar menuBar;

    @FXML
    private MenuItem closeWindowMenu;

    @FXML
    private MenuItem saveMenuItem;

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
//    @FXML
//    private VBox root;


    /**
     * Constructor.
     */
    public WatchlistProController() {
//        System.setProperty("apple.laf.useScreenMenuBar", "true");
        io = new FileIO();
        mediaMap = io.load(new ObservableMapWrapper<>(new HashMap<>()));
        masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
        mediaCreator = new MediaCreator();
    }

    /**
     * Initialize the view.
     * @param url is not used.
     * @param rb is not used.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateMediaList();
        mediaList.getSelectionModel().select(0);

        AquaFx.style(); // Throws CoreText performance errors

        // Menu
        closeWindowMenu.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN));
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
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

            if (editToggleButton.isSelected()) {

                switchView();
            }

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
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Compare first name and last name of every person with filter text.
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
     * Adds new media item to list.
     * @param event is the action event.
     */
    @FXML
    private void addMedia(ActionEvent event) {
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
     * @param event is the action event.
     */
    @FXML
    private void deleteMedia(ActionEvent event) {
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
     * @param event is the action event.
     */
    @FXML
    private void toggleMedia(ActionEvent event) {
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
     * @param event is the action event.
     */
    @FXML
    private void toggleEdit(ActionEvent event) {
        if (mediaList.getSelectionModel().getSelectedItem() != null) {
            mediaName = mediaList.getSelectionModel().getSelectedItem().getTitle();
            if (!editToggleButton.isSelected()) {

                filterField.clear();
                filterField.setDisable(false);
                if (mediaType.equals("film")) {

                    if (mediaName.equals(filmTitleTextField.getText())) {
                        ((Film) mediaMap.get(mediaName)).setTitle(filmTitleTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setGenre(filmGenreTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setDirector(filmDirectorTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setRating(filmRatingTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setRuntime(filmRuntimeTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setProducer(filmProducerTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setWriter(filmWriterTextField.getText());
                        ((Film) mediaMap.get(mediaName)).setDescription(filmDescriptionTextField.getText());
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
                        ((TvShow) mediaMap.get(mediaName)).setTitle(tvTitleTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setGenre(tvGenreTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setCreator(tvCreatorTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNetwork(tvNetworkTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setRuntime(tvRuntimeTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNumSeasons(tvNumSeasonsTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setNumEpisodes(tvNumEpisodesTextField.getText());
                        ((TvShow) mediaMap.get(mediaName)).setDescription(tvDescriptionTextField.getText());
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
     * @param event is the action event.
     */
    @FXML
    void saveList(ActionEvent event) {
        io.save(mediaMap);
    }

    /**
     * Closes the window when the menu item is selected.
     * @param event is the action event.
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        close();
    }

    /**
     * Closes the window.
     */
    private void close() {
        io.save(mediaMap);
        Platform.exit();
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

        if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
            setFilmEditPane();
        } else if (mediaList.getSelectionModel().getSelectedItem() instanceof TvShow) {
            setTvEditPane();
        } else {
            mediaList.getSelectionModel().select(0);
        }

        // edit pane is enabled
        if (editToggleButton.isSelected()) {
            editToggleButton.setText("Done");
            fetchButton.setDisable(false);
            mediaEditType = mediaType;

            // select tv or film edit panel
            switch (mediaEditType) {
                case "tv":
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

    /**
     * Fetches the media data from Freebase.
     * @param event is the action event.
     */
    @FXML
    private void fetchMedia(ActionEvent event) {
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
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}