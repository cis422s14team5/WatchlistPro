package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import model.Film;
import model.Media;
import model.TvShow;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Reads and writes to the file system. Used to read and write to the store.txt file.
 */
public class FileIO {

    /**
     * Encodes each Media object in the mediaMap into a JSON string and writes it to the file system using write.
     * @param file the file to save to.
     * @param mediaMap is a map of entities to save from.
     */
    public void save(ObservableMap<String, Media> mediaMap, File file) {
        ArrayList<String> list = new ArrayList<>();
        for (HashMap.Entry entry : mediaMap.entrySet()) {
            Media media = (Media) entry.getValue();
            String jsonString = JSONValue.toJSONString(media.getMap());
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
    protected ObservableMap<String, Media> load(ObservableMap<String, Media> mediaMap, File file) {
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
        for (String string : list) {
            JSONObject object = (JSONObject) JSONValue.parse(string);

            StringProperty title = new SimpleStringProperty();
            title.set(object.get("title").toString());

            StringProperty watched = new SimpleStringProperty();
            watched.set(object.get("watched").toString());

            StringProperty genre = new SimpleStringProperty();
            genre.set(object.get("genre").toString());

            StringProperty runtime = new SimpleStringProperty();
            runtime.set(object.get("runtime").toString());

            StringProperty description = new SimpleStringProperty();
            description.set(object.get("description").toString());

            if (object.get("type").equals("film")) {

                StringProperty director = new SimpleStringProperty();
                director.set(object.get("director").toString());

                StringProperty rating = new SimpleStringProperty();
                rating.set(object.get("rating").toString());

                StringProperty producer = new SimpleStringProperty();
                producer.set(object.get("producer").toString());

                StringProperty writer = new SimpleStringProperty();
                writer.set(object.get("writer").toString());

                mediaMap.put(title.get(),
                        new Film(title, watched, genre, runtime, description, director, rating, producer, writer));
            } else if (object.get("type").equals("tv")) {

                StringProperty creator = new SimpleStringProperty();
                creator.set(object.get("creator").toString());

                StringProperty network = new SimpleStringProperty();
                network.set(object.get("network").toString());

                StringProperty numSeasons = new SimpleStringProperty();
                numSeasons.set(object.get("numSeasons").toString());

                StringProperty numEpisodes = new SimpleStringProperty();
                numEpisodes.set(object.get("numEpisodes").toString());

                Gson gson = new Gson();
                Type observableListType = new TypeToken<ObservableList<String>>(){}.getType();

                ListProperty<String> episodeList = new SimpleListProperty<>();
                ArrayList<String> tempList = gson.fromJson(object.get("episodeList").toString(),
                        observableListType);
                ObservableList<String> observableList = new ObservableListWrapper<>(tempList);
                episodeList.set(observableList);

                mediaMap.put(title.get(),
                        new TvShow(title, watched, genre, runtime, description, creator, network, numSeasons,
                                numEpisodes, episodeList));
            }
        }

        return mediaMap;
    }

    /**
     * Writes the array list to the file "store.txt".
     * @param list is the array list where each element will become a line in the file.
     * @param file the file to write to.
     */
    public void write(List<String> list, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String string: list) {
                writer.write(string + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads from the file into an array list.
     * @param file the file to read from.
     * @return a list where each element is a line of the file.
     */
    public List<String> read(File file) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
