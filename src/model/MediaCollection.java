package model;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * A collection of media objects used in the Controller to manage its ListView a store the current watchlist.
 */
public class MediaCollection {

    private ObservableMap<String, Media> map;
    private ObservableList<Media> list;

    public MediaCollection() {
        map = new ObservableMapWrapper<>(new HashMap<>());
        list = new ObservableListWrapper<>(new ArrayList<>());
    }

    /**
     * Gets the map.
     * @return the map.
     */
    public ObservableMap<String, Media> getMap() {
        return map;
    }

    /**
     * Gets the list.
     * @return the list.
     */
    public ObservableList<Media> getList() {
        return list;
    }

    /**
     * Gets a media object by key from the map.
     * @param key is the key for the media object.
     * @return the media object.
     */
    public Media get(String key) {
        return map.get(key);
    }

    /**
     * Gets a media object by index from the list.
     * @param index is the index for the media object.
     * @return the media object.
     */
    public Media get(int index) {
        return list.get(index);
    }

    /**
     * Sets the map.
     * @param map is the new map.
     */
    public void set(ObservableMap<String, Media> map) {
        this.map = map;
        update();
    }

    /**
     * Puts a view key, value pair in the map.
     * @param key is the new key.
     * @param value is the new value.
     */
    public void put(String key, Media value) {
        map.put(key, value);
        list.add(value);
    }

    /**
     * Removes a media object from the map by its key.
     * @param key the media object's key.
     */
    public void remove(String key) {
        map.remove(key);
    }

    /**
     * Clears the contents of the map.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Gets an entry set of media objects from the map.
     * @return the set of media objects.
     */
    public Set<Map.Entry<String, Media>> entrySet() {
        return map.entrySet();
    }

    /**
     * Gets a list of all values from the map.
     * @return a list of values.
     */
    public Collection<Media> values() {
        return map.values();
    }

    /**
     * Gets the size of the list.
     * @return the list size.
     */
    public int size() {
        return list.size();
    }

    /**
     * Sorts the list by title.
     */
    public void sort() {
        Collections.sort(list, (m1, m2) -> (m1.getTitle()).compareTo(m2.getTitle()));
    }

    /**
     * Creates new version of the list and map.
     */
    public void update() {
        List<Media> media = new ArrayList<>(map.values());
        this.list = new ObservableListWrapper<>(media);
    }
}
