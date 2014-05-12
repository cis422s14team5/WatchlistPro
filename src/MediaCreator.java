import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Film;
import model.TvShow;

/**
 * Creates new media objects.
 */
public class MediaCreator {

    /**
     * Creates a new Film object.
     * @param titleString is the title of the Film object.
     * @return a new Film object.
     */
    public Film createFilm(String titleString) {
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
    public TvShow createTvShow(String titleString) {
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
}
