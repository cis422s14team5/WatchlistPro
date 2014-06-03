package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;

/**
 * Stores the fields of a media object. The super class of TvShow and Film.
 */
public class Media {

    private HashMap<String, String> map;
    private StringProperty title;
    private StringProperty watched;
    private StringProperty genre;
    private StringProperty runtime;
    private StringProperty description;

    /**
     * Constructor.
     */
    public Media(StringProperty title, StringProperty watched, StringProperty genre, StringProperty runtime, StringProperty description) {
        this.title = title;
        this.watched = watched;
        this.genre = genre;
        this.runtime = runtime;
        this.description = description;
        watched = new SimpleStringProperty();
        watched.set("No");
        map = new HashMap<>();
    }

    /**
     * Get the map.
     * @return the map.
     */
    public HashMap<String, String> getMap() {
        return map;
    }

    /**
     * Get the title.
     * @return the title.
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Get the title property.
     * @return the title property.
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Set the title.
     * @param title the title.
     */
    public void setTitle(String title) {
        this.title.set(title);
        map.put("title", title);
    }

    /**
     * Get the watched field.
     * @return the watched field.
     */
    public String getWatched() {
        return watched.get();
    }

    /**
     * Get the watched property.
     * @return the watched property.
     */
    public StringProperty watchedProperty() {
        return watched;
    }

    /**
     * Set the watched field.
     * @param watched is the value to set.
     */
    public void setWatched(String watched) {
        this.watched.set(watched);
        map.put("watched", watched);
    }

    /**
     * Get the genre.
     * @return the genre.
     */
    public String getGenre() {
        return genre.get();
    }

    /**
     * Get the genre property.
     * @return the genre property.
     */
    public StringProperty genreProperty() {
        return genre;
    }

    /**
     * Set the genre.
     * @param genre is the value to set.
     */
    public void setGenre(String genre) {
        this.genre.set(genre);
        map.put("genre", genre);
    }

    /**
     * Get the runtime.
     * @return the runtime.
     */
    public String getRuntime() {
        return runtime.get();
    }

    /**
     * Get the runtime property.
     * @return the runtime property.
     */
    public StringProperty runtimeProperty() {
        return runtime;
    }

    /**
     * Set the runtime field.
     * @param runtime is the value to set.
     */
    public void setRuntime(String runtime) {
        this.runtime.set(runtime);
        map.put("runtime", runtime);
    }

    /**
     * Get the description field.
     * @return the description field.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Get the description property.
     * @return the description property.
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Set the description field.
     * @param description is the value to set.
     */
    public void setDescription(String description) {
        this.description.set(description);
        map.put("description", description);
    }
}
