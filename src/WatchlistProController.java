/**
 * @author jasonjkeller
 */

package watchlistpro;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import watchlistpro.model.Show;

public class WatchlistProController implements Initializable {
    // data structures
    private ObservableMap<String, Show> mediaMap = FXCollections.observableHashMap();
    private ObservableList<Show> masterMediaList = FXCollections.observableArrayList();

    // control variables
    int mediaIndex = -1;
    String mediaName = null;    
    String mediaType = "film";
    String mediaEditType;
    String newMediaName = null;
    
    @FXML
    private ListView<Show> mediaList;
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
     * Initialize sample data in the constructor.
     */
    public WatchlistProController() {
        // here: load data from file into list
        masterMediaList.add(new Show("Star Wars", "No"));
        masterMediaList.add(new Show("Dune", "No"));
        masterMediaList.add(new Show("Terminator", "No"));
        masterMediaList.add(new Show("Aliens", "No"));
        masterMediaList.add(new Show("Star Trek", "No"));
        masterMediaList.add(new Show("Battlestar Galactica", "No"));
        masterMediaList.add(new Show("The Avengers", "No"));
        masterMediaList.add(new Show("Batman", "No"));
        masterMediaList.add(new Show("Game of Thrones", "No"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        WebEngine webEngine = webView.getEngine();
//        webEngine.load("http://www.freebase.com/m/06mmr");
        // Init ListView.
        mediaList.setItems(masterMediaList);
        mediaList.setCellFactory((list) -> {
            return new ListCell<Show>() {
                @Override
                protected void updateItem(Show show, boolean empty) {
                    super.updateItem(show, empty);

                    if (show == null || empty) {
                        setText(null);
                    } else {
                        setText(show.getShowName());
                    }
                }
            };
        });
        
        // Handle ListView selection changes.
        mediaList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                mediaName = newValue.getShowName();
                mediaIndex = mediaList.getSelectionModel().getSelectedIndex();
                System.out.println("ListView Selection Changed (selected: " + mediaName + ") at index " + mediaIndex + "\n");
            }
        });

//        // 0. Initialize the columns.
//        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
//        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Show> filteredData = new FilteredList<>(masterMediaList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(show -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (show.getShowName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } 
//                else if (show.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
//                    return true; // Filter matches last name.
//                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Show> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
//        sortedData.comparatorProperty().bind(list.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        mediaList.setItems(sortedData);
    }    

    @FXML // adds new media item to list
    private void addMedia(ActionEvent event) {              
        if (newMediaTextField.getCharacters() != null && newMediaTextField.getLength() > 0){
            newMediaName = newMediaTextField.getCharacters().toString();
            masterMediaList.add(new Show(newMediaName, "No"));
            newMediaTextField.clear();
            System.out.printf("newMediaName: %s, mediaType: %s\n", newMediaName, mediaType);
        }
    }

    @FXML // deletes the selected media item
    private void deleteMedia(ActionEvent event) {
        if (masterMediaList.size() > 0 && mediaIndex >= 0 && mediaIndex < masterMediaList.size()){
            // Handle ListView selection changes.
            System.out.println("deleteMediaButton Action\n");

            masterMediaList.remove(mediaIndex);  
            mediaIndex = -1;
        }  
    }

    @FXML // toggles the type of media to be added
    private void toggleMedia(ActionEvent event) {
        if (mediaToggleButton.isSelected()){
            mediaToggleButton.setText("TV");
            mediaType = "tv";
        }else{
            mediaToggleButton.setText("Film");
            mediaType = "film";
        }
    }

    @FXML // toggles bewtween film/tv edit and display panes
    private void toggleEdit(ActionEvent event) {
        // edit pane is enabled
        if (editToggleButton.isSelected()){
            editToggleButton.setText("Save");
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
                    
                    System.out.println("tv");
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
                    
                    System.out.println("film"); 
                    break;
            }
        }else{
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
                    
                    System.out.println("tv");
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
                    
                    System.out.println("film");                    
                    break;
            }            
        }        
    }

    @FXML
    private void fetchMedia(ActionEvent event) {
        switch (mediaEditType) {
            case "tv":
                // fetch data and populate tv edit pane with it
                setTvEditPane("title", "genre", "creator", "network", "runtime", "numSeasons", "numEpisodes", "description");
                System.out.println("tv");
                break;
            case "film":
                // fetch data and populate film edit pane with it
                setFilmEditPane("title", "genre", "director", "rating", "runtime", "producer", "writer", "description");
                System.out.println("film"); 
                break;
        }        
    }
    
    // populates the film display pane based on values from the film edit pane
    private void setFilmDisplayPane(){
        filmTitleLabel.setText(filmTitleTextField.getCharacters().toString());   
        filmGenreLabel.setText(filmGenreTextField.getCharacters().toString());
        filmDirectorLabel.setText(filmDirectorTextField.getCharacters().toString());
        filmRatingLabel.setText(filmRatingTextField.getCharacters().toString());
        filmRuntimeLabel.setText(filmRuntimeTextField.getCharacters().toString());
        filmProducerLabel.setText(filmProducerTextField.getCharacters().toString());
        filmWriterLabel.setText(filmWriterTextField.getCharacters().toString());
        filmDescriptionLabel.setText(filmDescriptionTextField.getText());
    }
    
    // populates the film edit pane based on passed in arguments
    private void setFilmEditPane(String title, String genre, String director, 
            String rating, String runtime, String producer, String writer, String description){
        filmTitleTextField.setText(title);
        filmGenreTextField.setText(genre);
        filmDirectorTextField.setText(director);
        filmRatingTextField.setText(rating);
        filmRuntimeTextField.setText(runtime);
        filmProducerTextField.setText(producer);
        filmWriterTextField.setText(writer);
        filmDescriptionTextField.setText(description);
    }
    
    // populates the tv display pane based on values from the film edit pane
    private void setTvDisplayPane(){
        tvTitleLabel.setText(tvTitleTextField.getCharacters().toString());   
        tvGenreLabel.setText(tvGenreTextField.getCharacters().toString());
        tvCreatorLabel.setText(tvCreatorTextField.getCharacters().toString());
        tvNetworkLabel.setText(tvNetworkTextField.getCharacters().toString());
        tvRuntimeLabel.setText(tvRuntimeTextField.getCharacters().toString());
        tvNumSeasonsLabel.setText(tvNumSeasonsTextField.getCharacters().toString());
        tvNumEpisodesLabel.setText(tvNumEpisodesTextField.getCharacters().toString());
        tvDescriptionLabel.setText(tvDescriptionTextField.getText());
    }
    
    // populates the tv edit pane based on passed in arguments
    private void setTvEditPane(String title, String genre, String creator, 
            String network, String runtime, String numSeasons, String numEpisodes, String description){
        tvTitleTextField.setText(title);
        tvGenreTextField.setText(genre);
        tvCreatorTextField.setText(creator);
        tvNetworkTextField.setText(network);
        tvRuntimeTextField.setText(runtime);
        tvNumSeasonsTextField.setText(numSeasons);
        tvNumEpisodesTextField.setText(numEpisodes);
        tvDescriptionTextField.setText(description);
    }  
}