import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple Natural Language Processing (NLP) helper.
 *
 * Steps used:
 * 1. Lowercase the text
 * 2. Remove punctuation
 * 3. Tokenize (split into words)
 * 4. Remove stop words (common words such as "the", "is", "can")
 * 5. Light stemming (turn plurals like "internships" into "internship")
 */
public class NLPProcessor {

    private final Set<String> stopWords;

    public NLPProcessor() {
        stopWords = new HashSet<>(Arrays.asList(
                "a", "an", "the", "is", "are", "am", "was", "were", "be", "been",
                "to", "of", "in", "on", "at", "for", "and", "or", "but", "with", "from", "by", "as",
                "can", "could", "would", "will", "should", "shall", "may", "might",
                "please", "me", "my", "mine", "i", "im", "ive", "it", "its", "this", "that",
                "these", "those", "do", "does", "did", "have", "has", "had",
                "you", "your", "yours", "we", "our", "us", "there",
                "some", "something", "any", "anything", "about", "tell", "explain", "give", "show",
                "want", "need", "know", "like", "let", "lets",
                "what", "whats", "which", "how", "who", "why", "when", "where",
                "so", "very", "just", "also", "if", "than", "then"));
    }

    /** Lowercases the text and removes punctuation. "What's Java?!" becomes "whats java". */
    public String clean(String text) {
        if (text == null) {
            return "";
        }
        String result = text.toLowerCase();
        result = result.replaceAll("['\u2019]", "");            // whats, im, dont
        result = result.replaceAll("[^a-z0-9+#\\s]", " ");      // keep letters, digits, + and # (c++, c#)
        result = result.replaceAll("\\s+", " ").trim();         // collapse extra spaces
        return result;
    }

    /** Splits cleaned text into words. */
    public List<String> tokenize(String cleanedText) {
        List<String> tokens = new ArrayList<>();
        if (cleanedText == null || cleanedText.isEmpty()) {
            return tokens;
        }
        for (String token : cleanedText.split(" ")) {
            if (token.matches(".*[a-z0-9].*")) {                // ignore tokens like "+" or "#"
                tokens.add(token);
            }
        }
        return tokens;
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    /** Returns only the words that are not stop words. */
    public List<String> removeStopWords(List<String> tokens) {
        List<String> result = new ArrayList<>();
        for (String token : tokens) {
            if (!isStopWord(token)) {
                result.add(token);
            }
        }
        return result;
    }

    /** Very simple stemming: only normalises plurals (arrays -> array, classes -> class). */
    public String stem(String word) {
        int length = word.length();
        if (length > 4 && word.endsWith("ies")) {
            return word.substring(0, length - 3) + "y";
        }
        if (length > 4 && (word.endsWith("sses") || word.endsWith("ches")
                || word.endsWith("shes") || word.endsWith("xes"))) {
            return word.substring(0, length - 2);
        }
        if (length > 3 && word.endsWith("s") && !word.endsWith("ss")
                && !word.endsWith("us") && !word.endsWith("is")) {
            return word.substring(0, length - 1);
        }
        return word;
    }

    /** Full pipeline: clean -> tokenize -> remove stop words -> stem. Returns unique keywords. */
    public Set<String> extractKeywords(String text) {
        Set<String> keywords = new LinkedHashSet<>();
        List<String> tokens = removeStopWords(tokenize(clean(text)));
        for (String token : tokens) {
            keywords.add(stem(token));
        }
        return keywords;
    }

    /**
     * Cleans, tokenizes and stems the text but keeps stop words.
     * Used for matching multi-word phrases such as "how are you".
     */
    public String normalize(String text) {
        List<String> stemmed = new ArrayList<>();
        for (String token : tokenize(clean(text))) {
            stemmed.add(stem(token));
        }
        return String.join(" ", stemmed);
    }
}