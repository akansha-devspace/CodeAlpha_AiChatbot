# AI Chatbot Assistant (Java + Swing)

A Java-based chatbot with a Swing graphical interface. It uses **rule-based NLP and intent classification** to understand common questions and reply in real time. It runs fully offline: no API key, no internet connection, and no external frameworks.

> **Note:** This project uses rule-based NLP with keyword scoring. It is **not** a trained machine learning or deep learning model.

---

## Features

- Interactive chat window built with Java Swing
- 23 predefined intents with multiple response variations
- Understands greetings, introductions, "How are you?", help, thanks, and goodbye
- Answers Java, OOP, programming, data structures, algorithms, database, operating system, AI/ML, and computer science questions
- Answers internship, learning, and GitHub-submission questions
- Recognises different wordings of the same question (not just exact text)
- Friendly fallback reply for unknown questions
- Handles empty, very short, very long, and symbol-only messages
- Different colours and alignment for user and bot messages
- Scrollable chat history, **Send**, **Clear Chat**, and **Exit** buttons
- Status bar showing the last matched intent and confidence score

## Technologies Used

| Technology | Purpose |
|---|---|
| Java (JDK 8 or higher) | Core language |
| Java Swing | Graphical user interface |
| Java Collections (`List`, `ArrayList`, `Set`, `HashSet`, `LinkedHashSet`) | Storing intents, keywords, and stop words |
| Regular expressions | Cleaning user input |

## NLP Techniques Used

1. **Lowercasing**: `"Hello"` becomes `"hello"`
2. **Punctuation removal**: `"What's Java?!"` becomes `"whats java"`
3. **Tokenization**: splitting the sentence into words
4. **Stop-word removal**: removing common words such as *the, is, can, you, tell, me*
5. **Light stemming**: normalising plurals (`internships` becomes `internship`, `classes` becomes `class`)
6. **Keyword extraction**: keeping only the important words
7. **Phrase matching**: multi-word keywords such as *"how are you"* or *"data structure"*
8. **Scoring and intent selection**: the intent with the highest score wins
9. **Fallback**: a friendly reply when the score is too low

**Example:**

```text
Input     : "Can you tell me something about the Java programming language?"
Keywords  : [java, programming, language]
Best match: JAVA  (score 0.33)
```

## OOP Concepts Used

- **Classes and objects**: `Chatbot`, `Intent`, `KnowledgeBase`, `NLPProcessor`, `ChatbotGUI`
- **Encapsulation**: private fields with getters; `Intent` returns read-only lists
- **Constructors**: used to build every object, with validation in `Intent`
- **Methods**: small methods with a single responsibility
- **Collections**: `List` of intents, `Set` of keywords and stop words
- **Separation of responsibilities**: the GUI, NLP, data, and matching logic are in separate classes

## Project Structure

```text
CodeAlpha_AI_Chatbot/
│
├── src/
│   ├── Main.java            # Starts the application
│   ├── ChatbotGUI.java      # Swing window (display and user input only)
│   ├── Chatbot.java         # Matching, scoring, and choosing the response
│   ├── NLPProcessor.java    # Cleaning, tokenizing, stop words, stemming
│   ├── KnowledgeBase.java   # All intents, keywords, and responses
│   └── Intent.java          # Data class: name + keywords + responses
│
├── screenshots/
│   └── chatbot-window.png
├── README.md
└── .gitignore
```

| Class | Responsibility |
|---|---|
| `Main` | Creates the objects and opens the window |
| `ChatbotGUI` | Shows messages, reads input, calls the chatbot |
| `Chatbot` | Scores every intent and returns the best response |
| `NLPProcessor` | All text-processing steps |
| `KnowledgeBase` | Stores the predefined intents and fallback replies |
| `Intent` | Holds one intent's name, keywords, and responses |

## How the Chatbot Works

```text
User message
    ↓
NLPProcessor: lowercase → remove punctuation → tokenize → remove stop words → stem
    ↓
Chatbot: compare the keywords with every intent in the KnowledgeBase
    ↓
Score each intent  =  matching keywords / total relevant keywords in the message
    ↓
Highest score ≥ 0.10 ?  →  random response from that intent
Otherwise               →  friendly fallback response
```

**Rule-based intent classification.** Each intent has a name, a list of keywords or phrases, and a list of possible responses:

```text
Intent   : GREETING
Keywords : hello, hi, hey, good morning, ...
Responses: "Hello! 👋 How can I help you today?", ...
```

**Tie-breaking.** If two intents have the same score, the one added first wins. Specific intents (like `JAVA`) are therefore placed before general ones (like `GREETING`), so "Hi, what is Java?" gets a Java answer.

## How to Compile and Run

Requirements: JDK 8 or higher and a desktop environment (the app opens a window).

```bash
# 1. Go to the project folder
cd CodeAlpha_AI_Chatbot

# 2. Compile all classes into an "out" folder
javac -d out src/*.java

# 3. Run
java -cp out Main
```

On Windows Command Prompt, use `javac -d out src\*.java`.
Alternatively, open the folder in IntelliJ IDEA, Eclipse, or VS Code and run `Main.java`.

## Sample Conversation

```text
Bot: Hello! 👋 Welcome to AI Chatbot.
     How can I help you? Type 'help' to see what I can talk about.

You: What is Java?
Bot: Java is a popular object-oriented programming language used to build
     applications, web services, Android applications, and more.

You: Can you tell me something about the Java programming language?
Bot: Java is a general-purpose, class-based language known for the idea of
     "write once, run anywhere" thanks to the Java Virtual Machine.

You: What is OOP?
Bot: OOP stands for Object-Oriented Programming. It organizes software
     around objects and classes.

You: Tell me about internships.
Bot: Internships provide practical experience and help students develop
     professional and technical skills.

You: What is the capital of France?
Bot: I'm not sure I understand that yet. Try asking me about Java,
     programming, internships, or computer science.

You: Bye
Bot: Goodbye! 👋 Have a great day!
```

*Each intent has several responses, so the exact wording may vary between runs.*

### Things you can ask

`Hello` · `How are you?` · `Who are you?` · `What can you do?` · `What is Java?` · `What is the difference between JDK and JRE?` · `What is OOP?` · `What is a data structure?` · `Explain binary search` · `What is SQL?` · `What is machine learning?` · `Tips for my internship` · `How do I submit my task on GitHub?` · `Tell me a joke` · `Thanks` · `Bye`

## Screenshots

![Chatbot window](screenshots/chatbot-window.png)

*Replace this image with a screenshot from your own machine if you like.*

## Future Improvements

- Machine learning-based intent classification (for example, Naive Bayes or a small neural network)
- More training data and more intents
- Sentiment analysis to adapt the tone of replies
- Voice input and output (speech-to-text and text-to-speech)
- Database-based conversation history (for example, SQLite via JDBC)
- Integration with a real NLP API
- Web-based interface
- Context memory for follow-up questions

## Author

Akansha | B-tech CSE Student
Submitted as part of the CodeAlpha Java Virtual Internship Program, Batch 2026.