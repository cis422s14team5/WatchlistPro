import javafx.beans.property.StringProperty;

import java.util.HashMap;

/**
 * Stores the fields of an media object. The super class of TvShow and Film.
 */
public class Media {

    private HashMap<String, String> map;
    private StringProperty title;
    private StringProperty genre;
    private StringProperty runtime;
    private StringProperty description;

    /**
     * Constructor.
     */
    public Media(StringProperty title, StringProperty genre, StringProperty runtime, StringProperty description) {
        this.title = title;
        this.genre = genre;
        this.runtime = runtime;
        this.description = description;
        map = new HashMap<>();
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
        map.put("title", title);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
        map.put("genre", genre);
    }

    public String getRuntime() {
        return runtime.get();
    }

    public StringProperty runtimeProperty() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime.set(runtime);
        map.put("runtime", runtime);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
        map.put("description", description);
    }


}
