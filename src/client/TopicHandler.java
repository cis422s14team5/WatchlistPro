package client;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TopicHandler {

    public ArrayList<String> filmOutput(JSONObject topic) {
        ArrayList<String> output = new ArrayList<>();

        String title;
        try {
            title = JsonPath.read(topic, "$.property['/type/object/name'].values[0].value").toString();
        } catch (Exception e) {
            title = "";
        }

        JSONArray genres;
        try {
            genres = JsonPath.read(topic, "$.property['/film/film/genre'].values");
        } catch (InvalidPathException e) {
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

//        String website;
//        try {
//            website = JsonPath.read(topic,
//                    "$.property['/common/topic/official_website'].values[0].value").toString();
//        } catch (com.jayway.jsonpath.InvalidPathException e) {
//            website = "No website listed.";
//        }

        output.clear();
        output.add(title);

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

    public ArrayList<String> tvOutput(JSONObject topic) {
        ArrayList<String> output = new ArrayList<>();

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
        } catch (InvalidPathException e) {
            network = "";
        }

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
        } catch (NumberFormatException e) {
            numSeasons = "";
        }

        String numEpisodes;
        try {
            numEpisodes = JsonPath.read(topic,
                    "$.property['/tv/tv_program/number_of_episodes'].values[0].value").toString();
        } catch (Exception e) {
            numEpisodes = "";
        }

//        JSONArray episodes =
//                JsonPath.read(topic,"$.property['/tv/tv_program/episodes'].values");
//        ArrayList<String> episodeList = new ArrayList<>();
//        for (Object obj : episodes) {
//            JSONObject episode = (JSONObject) obj;
//            episodeList.add(episode.get("text").toString());
//        }

//        String website;
//        try {
//            website =
//                    JsonPath.read(topic,
//                            "$.property['/common/topic/official_website'].values[0].value").toString();
//        } catch (com.jayway.jsonpath.InvalidPathException e) {
//            website = "No website listed.";
//        }

        output.clear();
        output.add(title);

        String genreTemp = "";
        for (int i = 0; i < genreList.size(); i++) {
            genreTemp += genreList.get(i);
            if (i != genreList.size() - 1) {
                genreTemp += ", ";
            }
        }
        output.add(genreTemp);

        String creatorsTemp = "";
        for (int i = 0; i < creatorList.size(); i++) {
            creatorsTemp += creatorList.get(i);
            if (i != creatorList.size() - 1) {
                creatorsTemp += ", ";
            }
        }
        output.add(creatorsTemp);

        output.add(String.format(network));

        output.add(""); // runtime

        output.add(String.format(numSeasons));
        if (!numEpisodes.equals("[]")) {
            int num = (int) Float.parseFloat(numEpisodes);
            output.add(Integer.toString(num));
        } else {
            output.add("");
        }

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
}
