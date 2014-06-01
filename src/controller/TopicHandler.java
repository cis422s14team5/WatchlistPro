package controller;

import client.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Episode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the processing of Freebase JSON topics into our required fields.
 */
public class TopicHandler {

    /**
     * Handles film JSON topics and puts the required elements into a list.
     * @param topic is the JSON string to process.
     * @return a list of strings used to fill in the edit pane fields.
     */
    public List<String> handleFilmOutput(JSONObject topic) {
        List<String> output = new ArrayList<>();

        String title;
        try {
            title = JsonPath.read(topic, "$.property['/type/object/name'].values[0].value").toString();
        } catch (Exception e) {
            title = "";
        }

        JSONArray genres;
        try {
            genres = JsonPath.read(topic, "$.property['/film/film/genre'].values");
        } catch (Exception e) {
            genres = new JSONArray();
        }
        ArrayList<String> genreList = new ArrayList<>();
        if (genres.size() > 0) {
            for (Object obj : genres) {
                JSONObject genre = (JSONObject) obj;
                genreList.add(genre.get("text").toString());
            }
        } else {
            genreList.add("");
        }

        String rating;
        try {
            rating = JsonPath.read(topic, "$property['/film/film/rating'].values[0].text").toString();
        } catch (Exception e) {
            rating = "";
        }

        String director;
        try {
            director = JsonPath.read(topic, "$.property['/film/film/directed_by'].values[0].text").toString();
        } catch (Exception e) {
            director = "";
        }

        // TODO fix film runtime
        //String runtime = JsonPath.read(topic, "$.property['/film/film/runtime'].values[0].text").toString();

        JSONArray producers;
        try {
            producers = JsonPath.read(topic, "$.property['/film/film/produced_by'].values");
        } catch (Exception e) {
            producers = new JSONArray();
        }
        ArrayList<String> producersList = new ArrayList<>();
        if (producers.size() > 0) {
            for (Object obj : producers) {
                JSONObject producer = (JSONObject) obj;
                producersList.add(producer.get("text").toString());
            }
        } else {
            producersList.add("");
        }

        JSONArray writers;
        try {
            writers = JsonPath.read(topic, "$.property['/film/film/written_by'].values");
        } catch (Exception e) {
            writers = new JSONArray();
        }
        ArrayList<String> writersList = new ArrayList<>();
        if (writers.size() > 0) {
            for (Object obj : writers) {
                JSONObject writer = (JSONObject) obj;
                writersList.add(writer.get("text").toString());
            }
        } else {
            writersList.add("");
        }

        JSONArray descriptions;
        try {
            descriptions = JsonPath.read(topic, "$.property['/common/topic/description'].values");
        } catch (Exception e) {
            descriptions = new JSONArray();
        }
        ArrayList<String> descriptionList = new ArrayList<>();
        if (descriptions.size() > 0) {
            for (Object obj : descriptions) {
                JSONObject description = (JSONObject) obj;
                descriptionList.add(description.get("value").toString());
            }
        } else {
            descriptionList.add("");
        }

        output.clear();
        output.add(title);
        output.add("No"); // Watched.

        String genreTemp = "";
        for (int i = 0; i < genreList.size(); i++) {
            genreTemp += genreList.get(i);
            if (i != genreList.size() - 1) {
                genreTemp += ", ";
            }
        }
        output.add(genreTemp);

        output.add(String.format(director));
        output.add(String.format(rating));

        output.add(""); // runtime

        String producersTemp = "";
        for (int i = 0; i < producersList.size(); i++) {
            producersTemp += producersList.get(i);
            if (i != producersList.size() - 1) {
                producersTemp += ", ";
            }
        }
        output.add(producersTemp);

        String writersTemp = "";
        for (int i = 0; i < writersList.size(); i++) {
            writersTemp += writersList.get(i);
            if (i != writersList.size() - 1) {
                writersTemp += ", ";
            }
        }
        output.add(writersTemp);

        String descriptionTemp = "";
        for (int i = 0; i < descriptionList.size(); i++) {
            descriptionTemp += descriptionList.get(i);
            if (i != descriptionList.size() - 1) {
                descriptionTemp += ", ";
            }
        }
        output.add(descriptionTemp);

        return output;
    }

    /**
     * Handles TV show JSON topics and puts the required elements them into a list.
     * @param topic is the JSON string to process.
     * @return a list of strings used to fill in the edit pane fields.
     */
    public List<String> handleTvOutput(JSONObject topic) {
        List<String> output = new ArrayList<>();
        Gson gson = new Gson();

        String title;
        try {
            title = JsonPath.read(topic, "$.property['/type/object/name'].values[0].value").toString();
        } catch (Exception e) {
            title = "";
        }

        JSONArray genres;
        try {
            genres = JsonPath.read(topic, "$.property['/tv/tv_program/genre'].values");
        } catch (Exception e) {
            genres = new JSONArray();
        }
        ArrayList<String> genreList = new ArrayList<>();
        if (genres.size() > 0) {
            for (Object obj : genres) {
                JSONObject genre = (JSONObject) obj;
                genreList.add(genre.get("text").toString());
            }
        } else {
            genreList.add("");
        }

        JSONArray creators;
        try {
            creators = JsonPath.read(topic, "$.property['/tv/tv_program/program_creator'].values");
        } catch (Exception e) {
            creators = new JSONArray();
        }
        ArrayList<String> creatorList = new ArrayList<>();
        if (creators.size() > 0) {
            for (Object obj : creators) {
                JSONObject creator = (JSONObject) obj;
                creatorList.add(creator.get("text").toString());
            }
        } else {
            creatorList.add("");
        }

        String network;
        try {
            JSONObject networkTemp = JsonPath.read(topic,
                    "$.property['/tv/tv_program/original_network'].values[0]");
            network = JsonPath.read(networkTemp,
                    "$.property['/tv/tv_network_duration/network'].values[0].text");
        } catch (Exception e) {
            network = "";
        }

        // TODO fix TV runtime
        //String runtime = JsonPath.read(topic, "$.property['/tv/tv_program/runtime'].values[0].text").toString();

        JSONArray descriptions;
        try {
            descriptions = JsonPath.read(topic, "$.property['/common/topic/description'].values");
        } catch (Exception e) {
            descriptions = new JSONArray();
        }
        ArrayList<String> descriptionList = new ArrayList<>();
        if (descriptions.size() > 0) {
            for (Object obj : descriptions) {
                JSONObject description = (JSONObject) obj;
                descriptionList.add(description.get("value").toString());
            }
        } else {
            descriptionList.add("");
        }

        String numSeasons;
        try {
            numSeasons = String.format("%s", (int) Float.parseFloat(JsonPath.read(topic,
                    "$.property['/tv/tv_program/number_of_seasons'].values[0].value").toString()));
        } catch (Exception e) {
            numSeasons = "0";
        }

        String numEpisodes;
        try {
            numEpisodes = JsonPath.read(topic,
                    "$.property['/tv/tv_program/number_of_episodes'].values[0].value").toString();
        } catch (Exception e) {
            numEpisodes = "0";
        }

        // Get episodes for every season
        JSONArray seasons =
                JsonPath.read(topic,"$.property['/tv/tv_program/seasons'].values");
        ArrayList<String> seasonIdList = new ArrayList<>();
        for (Object obj : seasons) {
            JSONObject season = (JSONObject) obj;
            seasonIdList.add(season.get("id").toString());
        }

        List<List<String>> seasonList = new ArrayList<>();
        for (int i = 0; i < seasonIdList.size(); i++) {
            seasonList.add(new ArrayList<>());
        }

        //System.out.println("seasonList size: " + seasonList.size());

        int episodeNumber = 1;
        for (String id : seasonIdList) {
            JSONObject seasonTopic = getTopic(id);

            String seasonNumber;
            try {
                seasonNumber = JsonPath.read(seasonTopic,
                        "$.property['/tv/tv_series_season/season_number'].values[0].text").toString();
            } catch (Exception e) {
                seasonNumber = "0";
            }
            JSONArray episodes;
            try {
                episodes = JsonPath.read(seasonTopic, "$.property['/tv/tv_series_season/episodes'].values");
            } catch (Exception e) {
                episodes = new JSONArray();
            }

            List<String> episodeList = new ArrayList<>();

            for (Object obj : episodes) {
                JSONObject episode = (JSONObject) obj;

                String number;
                if (episodeNumber < 10) {
                    number = "0" + episodeNumber;
                } else {
                    number = Integer.toString(episodeNumber);
                }

                Episode ep = new Episode(seasonNumber, "E" + number + " " + episode.get("text").toString(), false);
                episodeList.add(gson.toJson(ep.getMap()));
                episodeNumber++;
            }

            seasonList.set(Integer.parseInt(seasonNumber), episodeList);
            //seasonList.add(episodeList);
            episodeNumber = 1;
        }

//        System.out.println("Episodes");
//        for (List<String> season : seasonList) {
//            for (String episode : season) {
//                System.out.println(episode);
//            }
//        }


        output.clear();

        // Index 0
        output.add(title);

        // Index 1
        output.add("No"); // Watched.

        // Index 2
        String genreTemp = "";
        for (int i = 0; i < genreList.size(); i++) {
            genreTemp += genreList.get(i);
            if (i != genreList.size() - 1) {
                genreTemp += ", ";
            }
        }
        output.add(genreTemp);

        // Index 3
        String creatorsTemp = "";
        for (int i = 0; i < creatorList.size(); i++) {
            creatorsTemp += creatorList.get(i);
            if (i != creatorList.size() - 1) {
                creatorsTemp += ", ";
            }
        }
        output.add(creatorsTemp);

        // Index 4
        output.add(String.format(network));

        // Index 5
        output.add(""); // runtime

        // Index 6
        output.add(String.format(numSeasons));

        // Index 7
        if (!numEpisodes.equals("[]") || !numEpisodes.equals("")) {
            int num = (int) Float.parseFloat(numEpisodes);
            output.add(Integer.toString(num));
        } else {
            output.add("");
        }

        // Index 8
        String descriptionTemp = "";
        for (int i = 0; i < descriptionList.size(); i++) {
            descriptionTemp += descriptionList.get(i);
            if (i != descriptionList.size() - 1) {
                descriptionTemp += ", ";
            }
        }
        output.add(descriptionTemp);

        // Index 9
        output.add(gson.toJson(seasonList));

        return output;
    }

    /**
     * Get a topic from the server. Used to get a TV show season.
     * @return a JSON Freebase topic.
     */
    private JSONObject getTopic(String topicId) {
        Client client = new Client();
        try {
            Thread getTopic = client.send("getTopic");
            Thread topic = client.send(topicId);
            Thread quit = client.send("quit");

            getTopic.start();
            getTopic.join();

            topic.start();
            topic.join();

            quit.start();
            quit.join();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return client.getTopic();
    }
}
