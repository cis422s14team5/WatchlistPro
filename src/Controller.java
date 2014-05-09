import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.collections.ObservableMap;

import java.util.HashMap;

/**
 * Controls the creation of the view, loading, and saving to the file system.
 * Holds the internal data structure entityMap for storing all TV show and film entities.
 */
public class Controller {

    private ObservableMap<String, Entity> entityMap;

    /**
     * Constructor.
     */
    public Controller() {
        entityMap = new ObservableMapWrapper<>(new HashMap<>());

        // testFilm();

        FileIO io = new FileIO();
        io.load(entityMap);
        io.save(entityMap);
    }

//    public void testFilm() {
//        StringProperty title = new SimpleStringProperty();
//        StringProperty genre = new SimpleStringProperty();
//        StringProperty runtime = new SimpleStringProperty();
//        StringProperty director = new SimpleStringProperty();
//        StringProperty rating = new SimpleStringProperty();
//        StringProperty producer = new SimpleStringProperty();
//        StringProperty writer = new SimpleStringProperty();
//        StringProperty description = new SimpleStringProperty();
//
//        title.set("Blade Runner");
//        genre.set("Science Fiction, Thriller, Film noir, Cyberpunk, Dystopia, Future noir, Cult film, " +
//                "Existentialism, Neo-noir, Crime Thriller");
//        runtime.set("120");
//        director.set("Ridley Scott");
//        rating.set("R (USA)");
//        producer.set("Charles de Lauzirika, Michael Deeley");
//        writer.set("David Peoples, Hampton Fancher");
//        description.set("Blade Runner is a 1982 American dystopian science fiction film directed by Ridley Scott " +
//                "starring Harrison Ford, Rutger Hauer, Sean Young, and Edward James Olmos. The screenplay, written " +
//                "by Hampton Fancher and David Peoples, is loosely based on the 1968 novel Do Androids Dream of " +
//                "Electric Sheep? by Philip K. Dick. The film depicts a dystopian Los Angeles in November 2019 in " +
//                "which genetically engineered organic robots called replicants, which are visually " +
//                "indistinguishable from adult humans, are manufactured by the powerful Tyrell Corporation as well " +
//                "as by other \"mega-corporations\" around the world. Their use on Earth is banned and replicants " +
//                "are exclusively used for dangerous, menial, or leisure work on off-world colonies. Replicants who " +
//                "defy the ban and return to Earth are hunted down and \"retired\" by special police operatives " +
//                "known as \"Blade Runners\". The plot focuses on a desperate group of recently escaped replicants " +
//                "hiding in Los Angeles and the burnt-out expert Blade Runner, Rick Deckard, who reluctantly agrees " +
//                "to take on one more assignment to hunt them down. Blade Runner initially polarized critics: some " +
//                "were displeased with the pacing, while others enjoyed its thematic complexity. The film performed " +
//                "poorly in North American theaters but has since become a cult film. It has been hailed for its " +
//                "production design, depicting a \"retrofitted\" future, and remains a leading example of the " +
//                "neo-noir genre. It brought the work of Philip K. Dick to the attention of Hollywood and several " +
//                "later films were based on his work. Ridley Scott regards Blade Runner as \"probably\" his most " +
//                "complete and personal film. In 1993, the film was selected for preservation in the United States " +
//                "National Film Registry by the Library of Congress as being \"culturally, historically, or " +
//                "aesthetically significant\".");
//
//        Film bladeRunner = new Film(title, genre, runtime, description, director, rating, producer, writer);
//        entityMap.put(bladeRunner.getTitle(), bladeRunner);
//    }

    public ObservableMap<String, Entity> getEntityMap() {
        return entityMap;
    }

    /**
     * Main method.
     * @param args is the array of command line arguments.
     */
    public static void main(String[] args) {
        new Controller();
    }
}
