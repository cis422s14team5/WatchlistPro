package model;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;


public class MediaCollection {

    private ObservableMap<String, Media> map;
    private ObservableList<Media> list;

    public ObservableMap<String, Media> getMap() {
        return map;
    }

    public ObservableList<Media> getList() {
        return list;
    }

    public Media get(String key) {
        return map.get(key);
    }

    public Media get(int index) {
        return list.get(index);
    }

    public void set(ObservableMap<String, Media> map) {
        this.map = map;
        update();
    }

    public void put(String key, Media value) {
        map.put(key, value);
        list.add(value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public Set<Map.Entry<String, Media>> entrySet() {
        return map.entrySet();
    }

    public Collection<Media> values() {
        return map.values();
    }

    public int size() {
        return list.size();
    }

    /**
     * Sorts the list by title.
     */
    public void sort() {
        Collections.sort(list, (m1, m2) -> (m1.getTitle()).compareTo(m2.getTitle()));
    }

    public void update() {
        List<Media> media = new ArrayList<>(map.values());
        this.list = new ObservableListWrapper<>(media);
    }
}
