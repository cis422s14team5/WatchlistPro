package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Stores the fields of a TV show entity.
 */
public class TvShow extends Media {

    private StringProperty creator;
    private StringProperty network;
    private StringProperty numSeasons;
    private StringProperty numEpisodes;
    private ListProperty<String> episodeList;
    private Gson gson;

    /**
     * Constructor.
     */
    public TvShow(StringProperty title, StringProperty watched, StringProperty genre, StringProperty runtime,
                  StringProperty description, StringProperty creator, StringProperty network,
                  StringProperty numSeasons, StringProperty numEpisodes, ListProperty<String> episodeList) {
        super(title, watched, genre, runtime, description);
        this.creator = creator;
        this.network = network;
        this.numSeasons = numSeasons;
        this.numEpisodes = numEpisodes;
        this.episodeList = episodeList;

        gson = new Gson();
        Type observableListType = new TypeToken<ObservableList<String>>(){}.getType();

        getMap().put("type", "tv");
        getMap().put("title", title.get());
        getMap().put("watched", watched.get());
        getMap().put("genre", genre.get());
        getMap().put("runtime", runtime.get());
        getMap().put("creator", creator.get());
        getMap().put("network", network.get());
        getMap().put("numSeasons", numSeasons.get());
        getMap().put("numEpisodes", numEpisodes.get());
        getMap().put("description", description.get());
        getMap().put("episodeList", gson.toJson(episodeList, observableListType));
    }

    public String getNumEpisodes() {
        return numEpisodes.get();
    }

    public StringProperty numEpisodesProperty() {
        return numEpisodes;
    }

    public void setNumEpisodes(String numEpisodes) {
        this.numEpisodes.set(numEpisodes);
        getMap().put("numEpisodes", numEpisodes);
    }

    public String getNetwork() {
        return network.get();
    }

    public StringProperty networkProperty() {
        return network;
    }

    public void setNetwork(String network) {
        this.network.set(network);
        getMap().put("network", network);
    }

    public String getNumSeasons() {
        return numSeasons.get();
    }

    public StringProperty numSeasonsProperty() {
        return numSeasons;
    }

    public void setNumSeasons(String numSeasons) {
        this.numSeasons.set(numSeasons);
        getMap().put("numSeasons", numSeasons);
    }

    public String getCreator() {
        return creator.get();
    }

    public StringProperty creatorProperty() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator.set(creator);
        getMap().put("creator", creator);
    }

    public ObservableList<String> getEpisodeList() {
        return episodeList.get();
    }

    public ListProperty<String> episodeListProperty() {
        return episodeList;
    }

    public void setEpisodeList(ListProperty<String> episodeList) {
        this.episodeList.set(episodeList);
        getMap().put("episodeList", gson.toJson(episodeList));
    }
}
