import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileIO {

    protected void save(ObservableMap<String, Entity> entityMap) {
        ArrayList<String> list = new ArrayList<>();
        for (HashMap.Entry entry : entityMap.entrySet()) {
            Entity entity = (Entity) entry.getValue();
            String jsonText = JSONValue.toJSONString(entity.getMap());
            list.add(jsonText);
        }
        write(list);
    }

    protected void load(ObservableMap<String, Entity> entityMap) {
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

                entityMap.put(title.get(),
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
                description.set(object.get("title").toString());

                entityMap.put(title.get(),
                        new Tv(title, genre, runtime, creator, network, numSeasons, numEpisodes, description));
            }
        }
    }

    private void write(ArrayList<String> list) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File("output.txt")));
            for (String string: list) {
                writer.write(string);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> read() {
        BufferedReader reader;
        ArrayList<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("output.txt"));
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
