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
        getMap().put("watched", watched.get());
        getMap().put("genre", genre.get());
        getMap().put("runtime", runtime.get());
        getMap().put("director", director.get());
        getMap().put("rating", rating.get());
        getMap().put("producer", producer.get());
        getMap().put("writer", writer.get());
        getMap().put("description", description.get());
    }

    /**
     * Get the director field.
     * @return the director.
     */
    public String getDirector() {
        return director.get();
    }

    /**
     * Get the director property.
     * @return the director property.
     */
    public StringProperty directorProperty() {
        return director;
    }

    /**
     * Set the director field.
     * @param director is the value to set.
     */
    public void setDirector(String director) {
        this.director.set(director);
        getMap().put("director", director);
    }

    /**
     * Get the rating field.
     * @return the rating.
     */
    public String getRating() {
        return rating.get();
    }

    /**
     * Get the rating property.
     * @return the rating property.
     */
    public StringProperty ratingProperty() {
        return rating;
    }

    /**
     * Set the rating field.
     * @param rating is the value to set.
     */
    public void setRating(String rating) {
        this.rating.set(rating);
        getMap().put("rating", rating);
    }

    /**
     * Get the producer field.
     * @return the producer.
     */
    public String getProducer() {
        return producer.get();
    }

    /**
     * Get the producer property.
     * @return the producer property.
     */
    public StringProperty producerProperty() {
        return producer;
    }

    /**
     * Set the producer field.
     * @param producer is the value to set.
     */
    public void setProducer(String producer) {
        this.producer.set(producer);
        getMap().put("producer", producer);
    }

    /**
     * Get the writer field.
     * @return the writer.
     */
    public String getWriter() {
        return writer.get();
    }

    /**
     * Get the writer property.
     * @return the writer property.
     */
    public StringProperty writerProperty() {
        return writer;
    }

    /**
     * Set the writer field.
     * @param writer is the value to set.
     */
    public void setWriter(String writer) {
        this.writer.set(writer);
        getMap().put("writer", writer);
    }

}
