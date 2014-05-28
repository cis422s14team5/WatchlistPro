package model;

/**
 * Author: Jason Keller
 * Class Name: Episode.java
 * Class Description: An object representing
 * a single episode of a tv show.
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Episode {
    private final StringProperty seasonNum;
    private final StringProperty episodeName;
    private final StringProperty watched;

    /**
     * Constructor for Episode object
     * @param seasonNum season number
     * @param episodeName episode name
     * @param watched watched true/false
     */
    public Episode(String seasonNum, String episodeName, String watched) {
        this.seasonNum = new SimpleStringProperty(seasonNum);
        this.episodeName = new SimpleStringProperty(episodeName);
        this.watched = new SimpleStringProperty(watched);
    }

    /**
     * getter for season number
     * @return season number
     */
    public String getSeasonNum() {
        return seasonNum.get();
    }

    /**
     * setter for season number
     * @param season season number
     */
    public void setSeasonNum(String season) {
        this.seasonNum.set(season);
    }

    /**
     * getter for season number property
     * @return season number property
     */
    public StringProperty seasonNumProperty() {
        return seasonNum;
    }

    /**
     * getter for episode name
     * @return episode name
     */
    public String getEpisodeName() {
        return episodeName.get();
    }

    /**
     * setter for episode name
     * @param episodeName episode name
     */
    public void setEpisodeName(String episodeName) {
        this.episodeName.set(episodeName);
    }

    /**
     * getter for episode name property
     * @return episode name property
     */
    public StringProperty episodeNameProperty() {
        return episodeName;
    }

    /**
     * getter for watched
     * @return watched
     */
    public String getWatched() {
        return watched.get();
    }

    /**
     * setter for watched
     * @param watched watched true/false
     */
    public void setWatched(String watched) {
        this.watched.set(watched);
    }

    /**
     * getter for watched property
     * @return watched property
     */
    public StringProperty watchedProperty() {
        return watched;
    }
}