
package watchlistpro;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import watchlistpro.model.Film;
import watchlistpro.model.Media;
import watchlistpro.model.TvShow;

/**
 * Controls the WatchlistPro view.
 */
public class WatchlistProController implements Initializable {
    // data structures
    private ObservableMap<String, Media> mediaMap;
    private ObservableList<Media> masterMediaList;

    // control variables
    int mediaIndex = -1;
    String mediaName = null;    
    String mediaType = "film";
    String mediaEditType;
    String newMediaName = null;
    
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
        FileIO io = new FileIO();
        mediaMap = io.load(new ObservableMapWrapper<>(new HashMap<>()));
        masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
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
                mediaName = newValue.getTitle();
                mediaIndex = mediaList.getSelectionModel().getSelectedIndex();
            }
        });

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Media> filteredData = new FilteredList<>(masterMediaList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
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
            newMediaName = newMediaTextField.getCharacters().toString();

            if (mediaType.equals("film")) {
                Film film = createFilm(newMediaName);
                masterMediaList.add(film);
                mediaMap.put(newMediaName, film);
            } else {
                TvShow show = createTvShow(newMediaName);
                masterMediaList.add(show);
                mediaMap.put(newMediaName, show);
            }
            newMediaTextField.clear();
            filterField.clear();
            updateMediaList();

            for (int i = 0; i < masterMediaList.size(); i++) {
                if (masterMediaList.get(i).getTitle().equals(newMediaName)) {
                    mediaList.getSelectionModel().select(i);
                }
            }
            editToggleButton.setSelected(true);
            switchView();
        }
    }

    /**
     * Creates a new Film object.
     * @param titleString is the title of the Film object.
     * @return a new Film object.
     */
    private Film createFilm(String titleString) {
        StringProperty title = new SimpleStringProperty();
        title.set(titleString);

        StringProperty genre = new SimpleStringProperty();
        genre.set("");

        StringProperty runtime = new SimpleStringProperty();
        runtime.set("");

        StringProperty director = new SimpleStringProperty();
        director.set("");

        StringProperty rating = new SimpleStringProperty();
        rating.set("");

        StringProperty producer = new SimpleStringProperty();
        producer.set("");

        StringProperty writer = new SimpleStringProperty();
        writer.set("");

        StringProperty description = new SimpleStringProperty();
        description.set("");

        return new Film(title, genre, runtime, description, director, rating, producer, writer);
    }

    /**
     * Creates a new TvShow object.
     * @param titleString is the title of the TvShow object.
     * @return a new TvShow object.
     */
    private TvShow createTvShow(String titleString) {
        StringProperty title = new SimpleStringProperty();
        title.set(titleString);

        StringProperty genre = new SimpleStringProperty();
        genre.set("");

        StringProperty runtime = new SimpleStringProperty();
        runtime.set("");

        StringProperty creator = new SimpleStringProperty();
        creator.set("");

        StringProperty network = new SimpleStringProperty();
        network.set("");

        StringProperty numSeasons = new SimpleStringProperty();
        numSeasons.set("");

        StringProperty numEpisodes = new SimpleStringProperty();
        numEpisodes.set("");

        StringProperty description = new SimpleStringProperty();
        description.set("");

        return new TvShow(title, genre, runtime, description, creator, network, numSeasons, numEpisodes);
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
        String media = mediaList.getSelectionModel().getSelectedItem().getTitle();

        if (!editToggleButton.isSelected()) {
            if (mediaType.equals("film")) {
                ((Film) mediaMap.get(media)).setTitle(filmTitleTextField.getText());
                ((Film) mediaMap.get(media)).setGenre(filmGenreLabel.getText());
                ((Film) mediaMap.get(media)).setDirector(filmDirectorTextField.getText());
                ((Film) mediaMap.get(media)).setRating(filmRatingTextField.getText());
                ((Film) mediaMap.get(media)).setRuntime(filmRuntimeTextField.getText());
                ((Film) mediaMap.get(media)).setProducer(filmProducerTextField.getText());
                ((Film) mediaMap.get(media)).setWriter(filmWriterTextField.getText());
                ((Film) mediaMap.get(media)).setDescription(filmDescriptionTextField.getText());
            } else {
                ((TvShow) mediaMap.get(media)).setTitle(tvTitleTextField.getText());
                ((TvShow) mediaMap.get(media)).setGenre(tvGenreTextField.getText());
                ((TvShow) mediaMap.get(media)).setCreator(tvCreatorTextField.getText());
                ((TvShow) mediaMap.get(media)).setNetwork(tvNetworkTextField.getText());
                ((TvShow) mediaMap.get(media)).setRuntime(tvRuntimeTextField.getText());
                ((TvShow) mediaMap.get(media)).setNumSeasons(tvNumSeasonsTextField.getText());
                ((TvShow) mediaMap.get(media)).setNumEpisodes(tvNumEpisodesTextField.getText());
                ((TvShow) mediaMap.get(media)).setDescription(tvDescriptionTextField.getText());
            }

            masterMediaList = new ObservableListWrapper<>(new ArrayList<>(mediaMap.values()));
            updateMediaList();

            for (int i = 0; i < masterMediaList.size(); i++) {
                if (masterMediaList.get(i).getTitle().equals(media)) {
                    mediaList.getSelectionModel().select(i);
                }
            }
        }
        switchView();
    }

    /**
     * Switches between the display view to the edit view.
     */
    private void switchView() {
        if (mediaList.getSelectionModel().getSelectedItem() instanceof Film) {
            setFilmEditPane();
        } else {
            setTvEditPane();
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
        switch (mediaEditType) {
            case "tv":
                // fetch data and populate tv edit pane with it
                //setTvEditPane("title", "genre", "creator", "network", "runtime", "numSeasons", "numEpisodes", "description");
                break;
            case "film":
                // fetch data and populate film edit pane with it
                //setFilmEditPane("title", "genre", "director", "rating", "runtime", "producer", "writer", "description");
                break;
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
}