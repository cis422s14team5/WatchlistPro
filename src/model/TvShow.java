package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;

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
    public ObservableList<String> getEpisodeList() {
        return episodeList.get();
    }

    /**
     * Gets the episodeList property.
     * @return the episodeList property.
     */
    public ListProperty<String> episodeListProperty() {
        return episodeList;
    }

    /**
     * Sets the episodeList property.
     * @param episodeList is the value to set.
     */
    public void setEpisodeList(ListProperty<String> episodeList) {
        this.episodeList.set(episodeList);
        getMap().put("episodeList", gson.toJson(episodeList));
    }
}
