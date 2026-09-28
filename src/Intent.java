import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents one "intent" the chatbot can recognise, for example GREETING or JAVA.
 * An intent stores its name, the keywords/phrases that trigger it,
 * and the possible responses the bot can give.
 */
public class Intent {

    private static final Random RANDOM = new Random();

    private final String name;
    private final List<String> keywords;
    private final List<String> responses;

    public Intent(String name, List<String> keywords, List<String> responses) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Intent name cannot be empty.");
        }
        if (responses == null || responses.isEmpty()) {
            throw new IllegalArgumentException("Intent '" + name + "' needs at least one response.");
        }
        this.name = name;
        this.keywords = new ArrayList<>(keywords);
        this.responses = new ArrayList<>(responses);
    }

    public String getName() {
        return name;
    }

    // Read-only views, so other classes cannot change the lists by accident (encapsulation)
    public List<String> getKeywords() {
        return Collections.unmodifiableList(keywords);
    }

    public List<String> getResponses() {
        return Collections.unmodifiableList(responses);
    }

    // Picks one of the responses at random so the bot does not sound repetitive
    public String getRandomResponse() {
        return responses.get(RANDOM.nextInt(responses.size()));
    }
}