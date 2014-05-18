package model;

import javafx.beans.property.StringProperty;

/**
 * Stores the fields of a film entity.
 */
public class Film extends Media {

    private StringProperty director;
    private StringProperty rating;
    private StringProperty producer;
    private StringProperty writer;


    /**
     * Constructor.
     */
    public Film(StringProperty title, StringProperty watched, StringProperty genre, StringProperty runtime, StringProperty description,
                StringProperty director, StringProperty rating, StringProperty producer, StringProperty writer) {
        super(title, watched, genre, runtime, description);
        this.director = director;
        this.rating = rating;
        this.producer = producer;
        this.writer = writer;

        getMap().put("type", "film");
        getMap().put("title", title.get());
        getMap().put("genre", genre.get());
        getMap().put("runtime", runtime.get());
        getMap().put("director", director.get());
        getMap().put("rating", rating.get());
        getMap().put("producer", producer.get());
        getMap().put("writer", writer.get());
        getMap().put("description", description.get());
    }

    public String getDirector() {
        return director.get();
    }

    public StringProperty directorProperty() {
        return director;
    }

    public void setDirector(String director) {
        this.director.set(director);
        getMap().put("director", director);
    }

    public String getRating() {
        return rating.get();
    }

    public StringProperty ratingProperty() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating.set(rating);
        getMap().put("rating", rating);
    }

    public String getProducer() {
        return producer.get();
    }

    public StringProperty producerProperty() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer.set(producer);
        getMap().put("producer", producer);
    }

    public String getWriter() {
        return writer.get();
    }

    public StringProperty writerProperty() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer.set(writer);
        getMap().put("writer", writer);
    }

}
