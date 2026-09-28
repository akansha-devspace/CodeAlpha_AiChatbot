import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Swing window for the chatbot.
 * It only handles display and user input; all the "thinking" is done by the Chatbot class.
 */
public class ChatbotGUI extends JFrame {

    private static final Color HEADER_COLOR = new Color(33, 84, 156);
    private static final Color USER_COLOR = new Color(25, 90, 180);
    private static final Color BOT_COLOR = new Color(30, 130, 76);
    private static final Color USER_BACKGROUND = new Color(221, 236, 255);
    private static final Color BOT_BACKGROUND = new Color(238, 238, 238);
    private static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);

    private final Chatbot chatbot;

    private JTextPane chatPane;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private JButton exitButton;
    private JLabel statusLabel;

    // Text styles for the chat area
    private SimpleAttributeSet userNameStyle;
    private SimpleAttributeSet userTextStyle;
    private SimpleAttributeSet botNameStyle;
    private SimpleAttributeSet botTextStyle;
    private SimpleAttributeSet userParagraphStyle;
    private SimpleAttributeSet botParagraphStyle;

    public ChatbotGUI(Chatbot chatbot) {
        super("AI Chatbot Assistant");
        this.chatbot = chatbot;

        createStyles();
        buildInterface();
        addListeners();
        showWelcomeMessage();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 660);
        setMinimumSize(new Dimension(420, 480));
        setLocationRelativeTo(null);   // open in the centre of the screen
    }

    private void createStyles() {
        userNameStyle = new SimpleAttributeSet();
        StyleConstants.setBold(userNameStyle, true);
        StyleConstants.setForeground(userNameStyle, USER_COLOR);
        StyleConstants.setFontSize(userNameStyle, 12);

        userTextStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(userTextStyle, Color.BLACK);
        StyleConstants.setBackground(userTextStyle, USER_BACKGROUND);
        StyleConstants.setFontSize(userTextStyle, 14);

        botNameStyle = new SimpleAttributeSet();
        StyleConstants.setBold(botNameStyle, true);
        StyleConstants.setForeground(botNameStyle, BOT_COLOR);
        StyleConstants.setFontSize(botNameStyle, 12);

        botTextStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(botTextStyle, Color.BLACK);
        StyleConstants.setBackground(botTextStyle, BOT_BACKGROUND);
        StyleConstants.setFontSize(botTextStyle, 14);

        // User messages sit on the right, bot messages on the left
        userParagraphStyle = new SimpleAttributeSet();
        StyleConstants.setAlignment(userParagraphStyle, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setLeftIndent(userParagraphStyle, 90);
        StyleConstants.setRightIndent(userParagraphStyle, 8);

        botParagraphStyle = new SimpleAttributeSet();
        StyleConstants.setAlignment(botParagraphStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setLeftIndent(botParagraphStyle, 8);
        StyleConstants.setRightIndent(botParagraphStyle, 90);
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        // ----- Header -----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel titleLabel = new JLabel("AI CHATBOT ASSISTANT");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitleLabel = new JLabel("Rule-based NLP chatbot built with Java Swing");
        subtitleLabel.setForeground(new Color(210, 225, 245));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ----- Chat area (scrollable) -----
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(MAIN_FONT);
        chatPane.setBackground(Color.WHITE);
        chatPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // ----- Bottom: status, input row, button row -----
        statusLabel = new JLabel("Type a message and press Enter or click Send.");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);

        inputField = new JTextField();
        inputField.setFont(MAIN_FONT);
        inputField.setPreferredSize(new Dimension(300, 34));

        sendButton = createButton("Send", new Color(33, 84, 156));
        clearButton = createButton("Clear Chat", new Color(120, 120, 120));
        exitButton = createButton("Exit", new Color(190, 50, 50));

        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(sendButton, BorderLayout.EAST);

        JPanel buttonRow = new JPanel(new BorderLayout());
        buttonRow.add(clearButton, BorderLayout.WEST);
        buttonRow.add(exitButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(inputRow, BorderLayout.CENTER);
        bottomPanel.add(buttonRow, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 34));
        return button;
    }

    private void addListeners() {
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());   // pressing Enter
        clearButton.addActionListener(e -> clearChat());
        exitButton.addActionListener(e -> exitApplication());
    }

    private void showWelcomeMessage() {
        appendMessage("Bot", chatbot.getWelcomeMessage(), false);
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();

        if (userText.isEmpty()) {
            statusLabel.setText("Please type a message first.");
            inputField.requestFocusInWindow();
            return;
        }

        appendMessage("You", userText, true);
        inputField.setText("");

        try {
            String reply = chatbot.getResponse(userText);
            appendMessage("Bot", reply, false);
            statusLabel.setText(String.format("Last match: %s (confidence %.0f%%)",
                    chatbot.getLastIntentName(), chatbot.getLastScore() * 100));
        } catch (Exception ex) {
            appendMessage("Bot", "Sorry, something went wrong. Please try again.", false);
            statusLabel.setText("Error: " + ex.getMessage());
        }

        inputField.requestFocusInWindow();
    }

    // Adds one message (name line + text) to the chat area with the right style
    private void appendMessage(String sender, String message, boolean fromUser) {
        StyledDocument doc = chatPane.getStyledDocument();
        try {
            int start = doc.getLength();
            doc.insertString(start, sender + "\n", fromUser ? userNameStyle : botNameStyle);
            doc.insertString(doc.getLength(), message + "\n\n", fromUser ? userTextStyle : botTextStyle);
            doc.setParagraphAttributes(start, doc.getLength() - start,
                    fromUser ? userParagraphStyle : botParagraphStyle, false);
            chatPane.setCaretPosition(doc.getLength());   // scroll to the newest message
        } catch (BadLocationException ex) {
            statusLabel.setText("Could not display the message.");
        }
    }

    private void clearChat() {
        chatPane.setText("");
        showWelcomeMessage();
        statusLabel.setText("Chat cleared.");
        inputField.requestFocusInWindow();
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                "Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}