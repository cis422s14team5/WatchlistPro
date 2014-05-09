import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Reads and writes to the file system. Used to read and write to the store.txt file.
 */
public class FileIO {

    /**
     * Encodes each Media object in the mediaMap into a JSON string and writes it to the file system using write.
     * @param mediaMap is a map of entities to save from.
     */
    protected void save(ObservableMap<String, Media> mediaMap) {
        ArrayList<String> list = new ArrayList<>();
        for (HashMap.Entry entry : mediaMap.entrySet()) {
            Media media = (Media) entry.getValue();
            String jsonString = JSONValue.toJSONString(media.getMap());
            list.add(jsonString);
        }
        write(list);
    }

    /**
     * Reads the contents of the file using read into an array list and creates an Media object from a JSON string on
     * each line. Fills and returns an mediaMap.
     * @param mediaMap is a map of entities to load into.
     * @return a filled mediaMap
     */
    protected ObservableMap<String, Media> load(ObservableMap<String, Media> mediaMap) {
        ArrayList<String> list = read();
        for (String string : list) {
            JSONObject object = (JSONObject) JSONValue.parse(string);

            if (object.get("type").equals("film")) {
                StringProperty title = new SimpleStringProperty();
                title.set(object.get("title").toString());

                StringProperty genre = new SimpleStringProperty();
                genre.set(object.get("genre").toString());

                StringProperty runtime = new SimpleStringProperty();
                runtime.set(object.get("runtime").toString());

                StringProperty director = new SimpleStringProperty();
                director.set(object.get("director").toString());

                StringProperty rating = new SimpleStringProperty();
                rating.set(object.get("rating").toString());

                StringProperty producer = new SimpleStringProperty();
                producer.set(object.get("producer").toString());

                StringProperty writer = new SimpleStringProperty();
                writer.set(object.get("writer").toString());

                StringProperty description = new SimpleStringProperty();
                description.set(object.get("description").toString());

                mediaMap.put(title.get(),
                        new Film(title, genre, runtime, director, rating, producer, writer, description));
            } else if (object.get("type").equals("tv")) {
                StringProperty title = new SimpleStringProperty();
                title.set(object.get("title").toString());

                StringProperty genre = new SimpleStringProperty();
                genre.set(object.get("genre").toString());

                StringProperty runtime = new SimpleStringProperty();
                runtime.set(object.get("runtime").toString());

                StringProperty creator = new SimpleStringProperty();
                creator.set(object.get("creator").toString());

                StringProperty network = new SimpleStringProperty();
                network.set(object.get("network").toString());

                StringProperty numSeasons = new SimpleStringProperty();
                numSeasons.set(object.get("numSeasons").toString());

                StringProperty numEpisodes = new SimpleStringProperty();
                numEpisodes.set(object.get("numEpisodes").toString());

                StringProperty description = new SimpleStringProperty();
                description.set(object.get("description").toString());

                mediaMap.put(title.get(),
                        new TvShow(title, genre, runtime, creator, network, numSeasons, numEpisodes, description));
            }
        }

        return mediaMap;
    }

    /**
     * Writes the array list to the file "store.txt".
     * @param list is the array list where each element will become a line in the file.
     */
    private void write(ArrayList<String> list) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("store.txt")));
            for (String string: list) {
                writer.write(string);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads from the file "store.txt" into an array list
     * @return a list where each element is a line of the file.
     */
    private ArrayList<String> read() {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("store.txt"));
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
