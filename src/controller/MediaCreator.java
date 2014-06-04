package controller;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Episode;
import model.Film;
import model.TvShow;

import java.util.ArrayList;
import java.util.List;

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

        StringProperty watched = new SimpleStringProperty();
        watched.set("false");

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

        return new Film(title, watched, genre, runtime, description, director, rating, producer, writer);
    }

    /**
     * Creates a new TvShow object.
     * @param titleString is the title of the TvShow object.
     * @return a new TvShow object.
     */
    public TvShow createTvShow(String titleString) {
        StringProperty title = new SimpleStringProperty();
        title.set(titleString);

        StringProperty watched = new SimpleStringProperty();
        watched.set("false");

        StringProperty genre = new SimpleStringProperty();
        genre.set("");

        StringProperty runtime = new SimpleStringProperty();
        runtime.set("");

        StringProperty creator = new SimpleStringProperty();
        creator.set("");

        StringProperty network = new SimpleStringProperty();
        network.set("");

        StringProperty numSeasons = new SimpleStringProperty();
        numSeasons.set("0");

        StringProperty numEpisodes = new SimpleStringProperty();
        numEpisodes.set("0");

        StringProperty description = new SimpleStringProperty();
        description.set("");

        ListProperty<List<Episode>> episodeList = new SimpleListProperty<>();
        episodeList.set(new ObservableListWrapper<>(new ArrayList<>()));

        ListProperty<Boolean> seasonWatchedList = new SimpleListProperty<>();
        seasonWatchedList.set(new ObservableListWrapper<>(new ArrayList<>()));

        return new TvShow(title, watched, genre, runtime, description, creator, network, numSeasons, numEpisodes,
                episodeList, seasonWatchedList);
    }
}
