import java.util.Set;

/**
 * The "brain" of the application.
 *
 * It uses rule-based intent classification (NOT a trained machine learning model):
 * 1. Clean the user's message with NLPProcessor
 * 2. Score every intent in the KnowledgeBase
 * 3. Pick the intent with the highest score
 * 4. Return one of its responses, or a fallback if the score is too low
 */
public class Chatbot {

    private static final int MAX_MESSAGE_LENGTH = 500;
    private static final int MIN_MESSAGE_LENGTH = 2;
    private static final double MIN_SCORE = 0.10;   // below this we do not trust the match

    private final KnowledgeBase knowledgeBase;
    private final NLPProcessor nlp;

    // Details of the last match, so the GUI can display them
    private String lastIntentName = "NONE";
    private double lastScore = 0.0;

    public Chatbot(KnowledgeBase knowledgeBase, NLPProcessor nlp) {
        this.knowledgeBase = knowledgeBase;
        this.nlp = nlp;
    }

    public String getWelcomeMessage() {
        return knowledgeBase.getWelcomeMessage();
    }

    public String getLastIntentName() {
        return lastIntentName;
    }

    public double getLastScore() {
        return lastScore;
    }

    /** Takes the raw user message and returns the bot's reply. */
    public String getResponse(String userInput) {
        lastIntentName = "NONE";
        lastScore = 0.0;

        // ----- Input checks -----
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Please type a message so I can help you.";
        }
        if (userInput.length() > MAX_MESSAGE_LENGTH) {
            return "That message is too long for me. Please keep it under " + MAX_MESSAGE_LENGTH + " characters.";
        }
        String cleaned = nlp.clean(userInput);
        if (cleaned.isEmpty()) {
            return "I couldn't find any words in that message. Please use letters or numbers.";
        }
        if (cleaned.length() < MIN_MESSAGE_LENGTH) {
            return "That message is very short. Could you type a little more?";
        }

        // ----- NLP + matching -----
        Set<String> userKeywords = nlp.extractKeywords(userInput);
        String normalizedText = nlp.normalize(userInput);

        Intent bestIntent = null;
        double bestScore = 0.0;

        for (Intent intent : knowledgeBase.getIntents()) {
            double score = calculateScore(intent, userKeywords, normalizedText);
            if (score > bestScore) {            // ">" (not ">=") so the earlier intent wins a tie
                bestScore = score;
                bestIntent = intent;
            }
        }

        // ----- Choose the reply -----
        if (bestIntent == null || bestScore < MIN_SCORE) {
            lastIntentName = "UNKNOWN";
            lastScore = bestScore;
            return knowledgeBase.getFallbackResponse();
        }

        lastIntentName = bestIntent.getName();
        lastScore = bestScore;
        return bestIntent.getRandomResponse();
    }

    /**
     * Score = matching keywords / total relevant keywords in the user's message (0.0 to 1.0).
     * - A single-word keyword matches if it is one of the user's keywords.
     * - A multi-word phrase (like "how are you") matches if it appears in the message.
     */
    private double calculateScore(Intent intent, Set<String> userKeywords, String normalizedText) {
        int matches = 0;
        String paddedText = " " + normalizedText + " ";

        for (String keyword : intent.getKeywords()) {
            if (keyword.contains(" ")) {
                if (paddedText.contains(" " + keyword + " ")) {
                    matches++;
                }
            } else if (userKeywords.contains(keyword)) {
                matches++;
            }
        }

        int totalRelevant = Math.max(1, userKeywords.size());
        return Math.min(1.0, (double) matches / totalRelevant);
    }
}