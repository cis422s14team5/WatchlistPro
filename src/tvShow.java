import javafx.beans.property.StringProperty;

/**
 * Stores the fields of a TV show entity.
 */
public class TvShow extends Media {

    private StringProperty creator;
    private StringProperty network;
    private StringProperty numSeasons;
    private StringProperty numEpisodes;

    /**
     * Constructor.
     */
    public TvShow(StringProperty title, StringProperty genre, StringProperty runtime, StringProperty description,
                  StringProperty creator, StringProperty network, StringProperty numSeasons, StringProperty numEpisodes) {
        super(title, genre, runtime, description);
        this.creator = creator;
        this.network = network;
        this.numSeasons = numSeasons;
        this.numEpisodes = numEpisodes;

        getMap().put("type", "tv");
        getMap().put("title", title.get());
        getMap().put("genre", genre.get());
        getMap().put("runtime", runtime.get());
        getMap().put("director", creator.get());
        getMap().put("rating", network.get());
        getMap().put("producer", numSeasons.get());
        getMap().put("writer", numEpisodes.get());
        getMap().put("description", description.get());
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
}
