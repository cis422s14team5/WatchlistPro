package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Episode;
import model.Film;
import model.Media;
import model.TvShow;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Reads and writes to the file system. Used to read and write to the store.txt file.
 */
public class FileIO {

    private Gson gson;

    /**
     * Constructor.
     */
    public FileIO() {
        gson = new Gson();
    }

    /**
     * Encodes each Media object in the mediaMap into a JSON string and writes it to the file system using write.
     * @param file the file to save to.
     * @param mediaMap is a map of entities to save from.
     */
    public void save(ObservableMap<String, Media> mediaMap, File file) {
        ArrayList<String> list = new ArrayList<>();
        for (HashMap.Entry entry : mediaMap.entrySet()) {
            Media media = (Media) entry.getValue();
            String jsonString = gson.toJson(media.getMap());
            list.add(jsonString);
        }
        write(list, file);
    }

    /**
     * Reads the contents of the file using read into an array list and creates a Media object from a JSON string on
     * each line. Fills and returns a mediaMap.
     * @param list is the list of JSON strings to load.
     * @return a filled mediaMap
     */
    public ObservableMap<String, Media> load(List<String> list) {
        ObservableMap<String, Media> mediaMap = new ObservableMapWrapper<>(new HashMap<>());
        return setProperties(mediaMap, list);
    }

    /**
     * Reads the contents of the file using read into an array list and creates a Media object from a JSON string on
     * each line. Fills and returns a mediaMap.
     * @param mediaMap is a map of entities to load into.
     * @param file the file to load from.
     * @return a filled mediaMap
     */
    public ObservableMap<String, Media> load(ObservableMap<String, Media> mediaMap, File file) {
        List<String> list = read(file);
        return setProperties(mediaMap, list);
    }

    /**
     * Set the media object properties and return the map.
     * @param mediaMap is a map of entities to load into.
     * @param list is the list of JSON strings to load.
     * @return a filled mediaMap
     */
    private ObservableMap<String, Media> setProperties(ObservableMap<String, Media> mediaMap, List<String> list) {
        Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();

        if (!list.isEmpty() && list.get(0).equals("{}")) {
            list = new ArrayList<>();
        }
        for (String string : list) {
            Map<String, String> map = gson.fromJson(string, mapType);

            StringProperty title = new SimpleStringProperty();
            title.set(map.get("title"));

            StringProperty watched = new SimpleStringProperty();
            watched.set(map.get("watched"));

            StringProperty genre = new SimpleStringProperty();
            genre.set(map.get("genre"));

            StringProperty runtime = new SimpleStringProperty();
            runtime.set(map.get("runtime"));

            StringProperty description = new SimpleStringProperty();
            description.set(map.get("description"));

            if (map.get("type").equals("film")) {

                StringProperty director = new SimpleStringProperty();
                director.set(map.get("director"));

                StringProperty rating = new SimpleStringProperty();
                rating.set(map.get("rating"));

                StringProperty producer = new SimpleStringProperty();
                producer.set(map.get("producer"));

                StringProperty writer = new SimpleStringProperty();
                writer.set(map.get("writer"));

                mediaMap.put(title.get(),
                        new Film(title, watched, genre, runtime, description, director, rating, producer, writer));
            } else if (map.get("type").equals("tv")) {

                StringProperty creator = new SimpleStringProperty();
                creator.set(map.get("creator"));

                StringProperty network = new SimpleStringProperty();
                network.set(map.get("network"));

                StringProperty numSeasons = new SimpleStringProperty();
                numSeasons.set(map.get("numSeasons"));

                StringProperty numEpisodes = new SimpleStringProperty();
                numEpisodes.set(map.get("numEpisodes"));

                // Convert episode list from JSON string to ObservableList.
                Type arrayListType = new TypeToken<ArrayList<List<HashMap<String, String>>>>(){}.getType();
                ArrayList<List<HashMap<String, String>>> arrayList = gson.fromJson(map.get("episodeList"), arrayListType);
                // Build seasonList
                ObservableList<List<Episode>> seasonList = FXCollections.observableArrayList();
                for (List<HashMap<String, String>> episodes : arrayList) {
                    List<Episode> episodeList = new ArrayList<>();
                    for (HashMap<String, String> hashMap : episodes) {
                        Episode episode = new Episode(hashMap.get("seasonNum"), hashMap.get("episodeName"),
                                Boolean.parseBoolean(hashMap.get("watched")));
                        episodeList.add(episode);
                    }
                    seasonList.add(episodeList);
                }

                ListProperty<List<Episode>> episodeList = new SimpleListProperty<>();
                episodeList.set(seasonList);

                // Convert episode list from JSON string to ObservableList.
                Type boolListType = new TypeToken<ArrayList<Boolean>>(){}.getType();
                ArrayList<Boolean> tempBoolList = gson.fromJson(map.get("seasonWatchedList"), boolListType);
                ObservableList<Boolean> boolList = new ObservableListWrapper<>(tempBoolList);

                ListProperty<Boolean> seasonWatchedList = new SimpleListProperty<>();
                seasonWatchedList.set(boolList);

                mediaMap.put(title.get(),
                        new TvShow(title, watched, genre, runtime, description, creator, network, numSeasons,
                                numEpisodes, episodeList, seasonWatchedList));
            }
        }

        return mediaMap;
    }

    /**
     * Writes the array list to the file "store.txt".
     * @param list is the array list where each element will become a line in the file.
     * @param file the file to write to.
     */
    private void write(List<String> list, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String string: list) {
                writer.write(string + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads from the file into an array list.
     * @param file the file to read from.
     * @return a list where each element is a line of the file.
     */
    private List<String> read(File file) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (Exception e) {
            write(list, file);
        }
        return list;
    }
}
