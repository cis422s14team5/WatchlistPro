package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the fields of a TV show entity.
 */
public class TvShow extends Media {

    private StringProperty creator;
    private StringProperty network;
    private StringProperty numSeasons;
    private StringProperty numEpisodes;
    private ListProperty<List<Episode>> episodeList;

    private ListProperty<Boolean> seasonWatchedList;
    private Gson gson;

    /**
     * Constructor.
     */
    public TvShow(StringProperty title, StringProperty watched, StringProperty genre, StringProperty runtime,
                  StringProperty description, StringProperty creator, StringProperty network,
                  StringProperty numSeasons, StringProperty numEpisodes, ListProperty<List<Episode>> episodeList,
                  ListProperty<Boolean> seasonWatchedList) {
        super(title, watched, genre, runtime, description);
        this.creator = creator;
        this.network = network;
        this.numSeasons = numSeasons;
        this.numEpisodes = numEpisodes;
        this.episodeList = episodeList;
        this.seasonWatchedList = seasonWatchedList;

        gson = new Gson();

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
        getMap().put("episodeList", getJSONEpisodeList(episodeList));
        getMap().put("seasonWatchedList", gson.toJson(seasonWatchedList));
    }

    /**
     * Gets the number of episodes in this TV show.
     * @return the number of episodes.
     */
    public String getNumEpisodes() {
        return numEpisodes.get();
    }

    /**
     * Gets the numEpisodes property.
     * @return the numEpisodes property.
     */
    public StringProperty numEpisodesProperty() {
        return numEpisodes;
    }

    /**
     * Sets the numEpisodes property.
     * @param numEpisodes is the value to set.
     */
    public void setNumEpisodes(String numEpisodes) {
        this.numEpisodes.set(numEpisodes);
        getMap().put("numEpisodes", numEpisodes);
    }
    
    /**
     * Gets the network of this TV show.
     * @return the network.
     */
    public String getNetwork() {
        return network.get();
    }

    /**
     * Gets the network property.
     * @return the network property.
     */
    public StringProperty networkProperty() {
        return network;
    }

    /**
     * Sets the network property.
     * @param network is the value to set.
     */
    public void setNetwork(String network) {
        this.network.set(network);
        getMap().put("network", network);
    }

    /**
     * Gets the number of season in this TV show.
     * @return the number of seaons.
     */
    public String getNumSeasons() {
        return numSeasons.get();
    }

    /**
     * Gets the numSeasons property.
     * @return the numSeasons property.
     */
    public StringProperty numSeasonsProperty() {
        return numSeasons;
    }

    /**
     * Sets the numSeasons property.
     * @param numSeasons is the value to set.
     */
    public void setNumSeasons(String numSeasons) {
        this.numSeasons.set(numSeasons);
        getMap().put("numSeasons", numSeasons);
    }

    /**
     * Gets the creator of this TV show.
     * @return the creator.
     */
    public String getCreator() {
        return creator.get();
    }

    /**
     * Gets the creator property.
     * @return the creator property.
     */
    public StringProperty creatorProperty() {
        return creator;
    }

    /**
     * Sets the creator property.
     * @param creator is the value to set.
     */
    public void setCreator(String creator) {
        this.creator.set(creator);
        getMap().put("creator", creator);
    }

    /**
     * Gets the episode list for this TV show.
     * @return the episode list.
     */
    public ObservableList<List<Episode>> getEpisodeList() {
        return episodeList.get();
    }

    /**
     * Gets the episodeList property.
     * @return the episodeList property.
     */
    public ListProperty<List<Episode>> episodeListProperty() {
        return episodeList;
    }

    /**
     * Sets the episodeList property.
     * @param episodeList is the value to set.
     */
    public void setEpisodeList(ListProperty<List<Episode>> episodeList) {
        this.episodeList.set(episodeList);
        getMap().put("episodeList", getJSONEpisodeList(episodeList));
    }

    private String getJSONEpisodeList(ListProperty<List<Episode>> list) {
        ObservableList<List<Map<String, String>>> seasonList = FXCollections.observableArrayList();
        for (List<Episode> episodes : list) {
            List<Map<String, String>> episodeList = new ArrayList<>();
            for (Episode episode : episodes) {
                HashMap<String, String> map = new HashMap<>();
                map.put("seasonNum", episode.getSeasonNum());
                map.put("episodeName", episode.getEpisodeName());
                map.put("watched", Boolean.toString(episode.getWatched()));
                episodeList.add(map);
            }
            seasonList.add(episodeList);
        }
        Type observableListType = new TypeToken<ObservableList<String>>(){}.getType();

        return gson.toJson(seasonList, observableListType);
    }

    public ObservableList<Boolean> getSeasonWatchedList() {
        return seasonWatchedList.get();
    }

    public ListProperty<Boolean> seasonWatchedListProperty() {
        return seasonWatchedList;
    }

    public void setSeasonWatchedList(ObservableList<Boolean> seasonWatchedList) {
        this.seasonWatchedList.set(seasonWatchedList);
        getMap().put("seasonWatchedList", gson.toJson(seasonWatchedList));
    }

    public void setSeasonWatchedList(int index, boolean bool) {
        this.seasonWatchedList.set(index, bool);
        getMap().put("seasonWatchedList", gson.toJson(seasonWatchedList));
    }
}
