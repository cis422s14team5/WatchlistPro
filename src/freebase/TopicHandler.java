package freebase;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TopicHandler {

    public ArrayList<String> filmOutput(JSONObject topic) {
        ArrayList<String> output = new ArrayList<>();

        String title = JsonPath.read(topic, "$.property['/type/object/name'].values[0].value").toString();

        JSONArray genres = JsonPath.read(topic,  "$.property['/film/film/genre'].values");
        ArrayList<String> genreList = new ArrayList<>();
        for (Object obj : genres) {
            JSONObject genre = (JSONObject) obj;
            genreList.add(genre.get("text").toString());
        }

        String rating = JsonPath.read(topic, "$property['/film/film/rating'].values[0].text").toString();
        String director = JsonPath.read(topic, "$.property['/film/film/directed_by'].values[0].text").toString();

        //String runtime = JsonPath.read(topic, "$.property['/film/film/runtime'].values[0].text").toString();

        JSONArray producers = JsonPath.read(topic, "$.property['/film/film/produced_by'].values");
        ArrayList<String> producersList = new ArrayList<>();
        for (Object obj : producers) {
            JSONObject producer = (JSONObject) obj;
            producersList.add(producer.get("text").toString());
        }

        JSONArray writers = JsonPath.read(topic, "$.property['/film/film/written_by'].values");
        ArrayList<String> writersList = new ArrayList<>();
        for (Object obj : writers) {
            JSONObject writer = (JSONObject) obj;
            writersList.add(writer.get("text").toString());
        }

        JSONArray descriptions =
                JsonPath.read(topic, "$.property['/common/topic/description'].values");
        ArrayList<String> descriptionList = new ArrayList<>();
        for (Object obj : descriptions) {
            JSONObject description = (JSONObject) obj;
            descriptionList.add(description.get("value").toString());
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

        String title = JsonPath.read(topic,"$.property['/type/object/name'].values[0].value").toString();

        JSONArray genres = JsonPath.read(topic,  "$.property['/tv/tv_program/genre'].values");
        ArrayList<String> genreList = new ArrayList<>();
        for (Object obj : genres) {
            JSONObject genre = (JSONObject) obj;
            genreList.add(genre.get("text").toString());
        }

        JSONArray creators = JsonPath.read(topic,  "$.property['/tv/tv_program/program_creator'].values");
        ArrayList<String> creatorList = new ArrayList<>();
        for (Object obj : creators) {
            JSONObject creator = (JSONObject) obj;
            creatorList.add(creator.get("text").toString());
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

        JSONArray descriptions =
                JsonPath.read(topic, "$.property['/common/topic/description'].values");
        ArrayList<String> descriptionList = new ArrayList<>();
        for (Object obj : descriptions) {
            JSONObject description = (JSONObject) obj;
            descriptionList.add(description.get("value").toString());
        }

        String numSeasons = String.format("%s", (int) Float.parseFloat(JsonPath.read(topic,
                "$.property['/tv/tv_program/number_of_seasons'].values[0].value").toString()));
        String numEpisodes = JsonPath.read(topic,
                "$.property['/tv/tv_program/number_of_episodes'].values[0].value").toString();

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
