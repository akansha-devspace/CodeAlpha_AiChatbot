import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Starts the AI Chatbot application.
 */
public class Main {

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("This program needs a graphical display to show the Swing window.");
            return;
        }

        // Swing windows should be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                NLPProcessor nlp = new NLPProcessor();
                KnowledgeBase knowledgeBase = new KnowledgeBase(nlp);
                Chatbot chatbot = new Chatbot(knowledgeBase, nlp);

                ChatbotGUI gui = new ChatbotGUI(chatbot);
                gui.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "The chatbot could not start: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}