import javafx.beans.property.StringProperty;

import java.util.HashMap;


public class Entity {

    private HashMap<String, String> map;

    public Entity(StringProperty title, StringProperty genre, StringProperty runtime, StringProperty description) {
        this.title = title;
        this.genre = genre;
        this.runtime = runtime;
        this.description = description;
        map = new HashMap<>();
    }

    private StringProperty title;
    private StringProperty genre;
    private StringProperty runtime;
    private StringProperty description;

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
