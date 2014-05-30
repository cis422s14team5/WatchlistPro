package model;

/**
 * Author: Jason Keller
 * Class Name: Episode.java
 * Class Description: An object representing a single episode of a tv show.
 */
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;

public class Episode {

    private final StringProperty seasonNum;
    private final StringProperty episodeName;
    private final BooleanProperty watched;
    private HashMap<String, String> map;

    /**
     * Constructor for Episode object.
     * @param seasonNum season number
     * @param episodeName episode name
     * @param watched watched true/false boolean
     */
    public Episode(String seasonNum, String episodeName, Boolean watched) {
        this.seasonNum = new SimpleStringProperty(seasonNum);
        this.episodeName = new SimpleStringProperty(episodeName);
        this.watched = new SimpleBooleanProperty(watched);

        map = new HashMap<>();
        map.put("seasonNum", seasonNum);
        map.put("episodeName", episodeName);
        map.put("watched", watched.toString());
    }

    /**
     * Getter for season number.
     * @return season number
     */
    public String getSeasonNum() {
        return seasonNum.get();
    }

    /**
     * Setter for season number.
     * @param season season number
     */
    public void setSeasonNum(String season) {
        this.seasonNum.set(season);
        map.put("seasonNum", season);
    }

    /**
     * Getter for season number property.
     * @return season number property
     */
    public StringProperty seasonNumProperty() {
        return seasonNum;
    }

    /**
     * Getter for episode name.
     * @return episode name
     */
    public String getEpisodeName() {
        return episodeName.get();
    }

    /**
     * Setter for episode name.
     * @param episodeName episode name
     */
    public void setEpisodeName(String episodeName) {
        this.episodeName.set(episodeName);
        map.put("episodeName", episodeName);
    }

    /**
     * Getter for episode name property.
     * @return episode name property
     */
    public StringProperty episodeNameProperty() {
        return episodeName;
    }

    /**
     * Getter for watched.
     * @return watched
     */
    public Boolean getWatched() {
        return watched.get();
    }

    /**
     * Setter for watched.
     * @param watched watched true/false
     */
    public void setWatched(Boolean watched) {
        this.watched.set(watched);
        map.put("watched", watched.toString());
    }

    /**
     * Getter for watched property.
     * @return watched property
     */
    public BooleanProperty watchedProperty() {
        return watched;
    }

    public HashMap<String, String> getMap() {
        return map;
    }
}