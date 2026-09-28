import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Stores everything the chatbot "knows": the list of intents (keywords + responses)
 * and the fallback answers for unknown questions.
 *
 * ORDER MATTERS: if two intents get the same score, the one added first wins,
 * so more specific intents are added before general ones.
 */
public class KnowledgeBase {

    private static final String WELCOME_MESSAGE =
            "Hello! \uD83D\uDC4B Welcome to AI Chatbot.\n"
            + "How can I help you? Type 'help' to see what I can talk about.";

    private final NLPProcessor nlp;
    private final List<Intent> intents = new ArrayList<>();
    private final List<String> fallbackResponses = Arrays.asList(
            "I'm not sure I understand that yet. Try asking me about Java, programming, internships, or computer science.",
            "Sorry, I don't know the answer to that yet. Type 'help' to see the topics I can talk about.");
    private final Random random = new Random();

    public KnowledgeBase(NLPProcessor nlp) {
        this.nlp = nlp;
        loadIntents();
    }

    public List<Intent> getIntents() {
        return Collections.unmodifiableList(intents);
    }

    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    public String getFallbackResponse() {
        return fallbackResponses.get(random.nextInt(fallbackResponses.size()));
    }

    /**
     * Adds an intent. Keywords are written as one comma-separated string.
     * They are normalised with the same NLP steps that are applied to user input,
     * so "Internships" in the list will match "internship" typed by the user.
     */
    private void addIntent(String name, String keywordList, String... responses) {
        List<String> keywords = new ArrayList<>();
        for (String keyword : keywordList.split(",")) {
            String normalized = nlp.normalize(keyword);
            if (!normalized.isEmpty()) {
                keywords.add(normalized);
            }
        }
        intents.add(new Intent(name, keywords, Arrays.asList(responses)));
    }

    private void loadIntents() {

        // ---------- About the chatbot ----------
        addIntent("HOW_IT_WORKS",
                "how do you work, how does this work, how does this chatbot work, how does the chatbot work, "
                + "how were you built, how were you made, how are you built, how are you made, rule based, "
                + "how do you understand",
                "I use rule-based NLP. Your message is lowercased, cleaned, split into words, and stripped of stop words. "
                + "Then I compare the remaining keywords with my list of intents, give each one a score, "
                + "and reply with the best match. I am not a trained machine learning model.");

        addIntent("HOW_ARE_YOU",
                "how are you, how are you doing, how is it going, hows it going, how you doing, "
                + "how do you do, whats up, how have you been",
                "I'm doing great, thank you for asking! What would you like to know today?",
                "All systems running smoothly! \uD83D\uDE0A What can I help you with?");

        addIntent("WORKS_OFFLINE",
                "offline, api key, internet connection, need internet, without internet, use internet",
                "Yes, I run completely on your own computer. No internet connection, API key, or external AI service is needed.");

        // ---------- Learning and internship ----------
        addIntent("LEARNING",
                "learn, study, tutorial, beginner, roadmap, resource, course, practice, "
                + "start learning, learning java, learning programming, get started, getting started",
                "A good way to start: pick one language (Java is a great choice), learn the basics such as variables, "
                + "loops, and methods, and then practise with small projects every day. Official Java tutorials, "
                + "W3Schools, and freeCodeCamp are good free places to begin.",
                "Start small: write tiny programs first, then build projects like a calculator, a to-do list, or a grade tracker. "
                + "Regular daily practice matters more than long, rare study sessions.");

        addIntent("SUBMISSION",
                "submit, submission, github, git, repository, repo, readme, upload, commit, push",
                "To submit your work: create a GitHub repository, add your source code and a clear README.md, "
                + "commit and push your files, then share the repository link as instructed by your internship.",
                "Basic Git steps: git init, git add ., git commit -m \"your message\", then link your GitHub repository "
                + "with git remote add origin <url> and run git push.");

        addIntent("INTERNSHIP_TIPS",
                "tip, advice, succeed, success, prepare, preparation, resume, cv, interview, apply, application",
                "Tips for a successful internship: finish tasks on time, ask questions when you are stuck, "
                + "write clean and documented code, communicate regularly, and treat every task as portfolio work.",
                "To prepare: practise programming basics, build a few small projects, keep your resume and GitHub profile "
                + "tidy, and revise core topics like OOP and data structures.");

        addIntent("INTERNSHIP",
                "internship, intern, trainee, training",
                "Internships provide practical experience and help students develop professional and technical skills.",
                "An internship is a short-term position where students or freshers work on real tasks, "
                + "learn from professionals, and build experience for their careers.");

        // ---------- Java and programming ----------
        addIntent("OOP",
                "oop, oops, object oriented, object, class, inheritance, polymorphism, encapsulation, abstraction",
                "OOP stands for Object-Oriented Programming. It organizes software around objects and classes.",
                "The four main pillars of OOP are encapsulation, inheritance, polymorphism, and abstraction. "
                + "Java uses these ideas heavily.");

        addIntent("JAVA_TOOLS",
                "jvm, jdk, jre, bytecode, javac, virtual machine, runtime environment, development kit",
                "JVM (Java Virtual Machine) runs Java bytecode. JRE (Java Runtime Environment) contains the JVM and libraries "
                + "needed to run programs. JDK (Java Development Kit) adds developer tools such as the javac compiler.",
                "The javac compiler turns Java source code into bytecode, and the JVM runs that bytecode on any operating system. "
                + "That is why Java is called platform independent.");

        addIntent("JAVA",
                "java",
                "Java is a popular object-oriented programming language used to build applications, web services, "
                + "Android applications, and more.",
                "Java is a general-purpose, class-based language known for the idea of \"write once, run anywhere\" "
                + "thanks to the Java Virtual Machine.");

        addIntent("DATA_STRUCTURES",
                "data structure, array, arraylist, linked list, linkedlist, stack, queue, tree, graph, hashmap, hashset, collection",
                "A data structure is a way of organizing and storing data so it can be used efficiently. "
                + "Common examples are arrays, lists, stacks, queues, trees, and hash maps.",
                "In Java, the Collections Framework provides ready-made data structures such as ArrayList, LinkedList, "
                + "HashMap, and HashSet.");

        addIntent("ALGORITHMS",
                "algorithm, sort, sorting, searching, binary search, linear search, recursion, recursive, big o, complexity, time complexity",
                "An algorithm is a step-by-step set of instructions for solving a problem. Common examples are sorting, "
                + "searching, and recursion. Efficiency is often described using Big O notation.",
                "Binary search finds an item in a sorted list by repeatedly halving the search range, "
                + "which makes it much faster than checking every item one by one.");

        addIntent("PROGRAMMING",
                "programming, programmer, coding, code, python, javascript, c++, c#, variable, loop, function, "
                + "debug, debugging, bug, syntax, compiler, compile",
                "Programming is the process of writing instructions that a computer can follow. "
                + "The basics include variables, conditions, loops, functions, and debugging.",
                "Popular programming languages include Java, Python, JavaScript, C++, and C#. "
                + "The best choice depends on what you want to build.");

        // ---------- Computer science basics ----------
        addIntent("AI_ML",
                "ai, artificial intelligence, machine learning, deep learning, ml, nlp, natural language processing, neural network",
                "Artificial Intelligence (AI) is the field of building systems that perform tasks that normally need human intelligence. "
                + "Machine learning is a part of AI where models learn patterns from data instead of only following hand-written rules.",
                "NLP (Natural Language Processing) helps computers work with human language. This chatbot uses simple rule-based NLP: "
                + "cleaning text, tokenizing, removing stop words, and matching keywords.");

        addIntent("DATABASE",
                "database, sql, mysql, postgresql, dbms, nosql",
                "A database is an organized collection of data. SQL (Structured Query Language) is used to create, read, update, "
                + "and delete data in relational databases such as MySQL. In Java, JDBC connects programs to databases.");

        addIntent("OPERATING_SYSTEM",
                "operating system, os, kernel, linux",
                "An operating system (OS) manages a computer's hardware and software and provides services for programs. "
                + "Examples are Windows, Linux, macOS, and Android. Its main jobs are process, memory, and file management.");

        addIntent("COMPUTER_SCIENCE",
                "computer science, cs, computing, hardware, software, cpu, computer",
                "Computer science is the study of computers and computation, including algorithms, data structures, "
                + "programming languages, software design, networks, databases, and artificial intelligence.",
                "Hardware (CPU, memory, storage) is the physical part of a computer, and software is the set of programs "
                + "that run on it. Software is written in programming languages using algorithms and data structures.");

        // ---------- General conversation ----------
        addIntent("HELP",
                "help, assist, assistance, support, what can you do, what can i ask, how to use, command, topic, option",
                "I can help with:\n"
                + "\u2022 Greetings and small talk\n"
                + "\u2022 Java (JDK, JVM, OOP)\n"
                + "\u2022 Programming, data structures, and algorithms\n"
                + "\u2022 Databases, operating systems, and AI/ML basics\n"
                + "\u2022 Internship tips and GitHub submission\n"
                + "\u2022 Programming jokes\n"
                + "Try: 'What is Java?' or 'Tell me about internships.'");

        addIntent("JOKE",
                "joke, funny, laugh, humor, humour",
                "Why do programmers prefer dark mode? Because light attracts bugs! \uD83D\uDE04",
                "Why do Java developers wear glasses? Because they don't C#!",
                "There are 10 kinds of people: those who understand binary and those who don't.");

        addIntent("INTRODUCTION",
                "who are you, your name, yourself, introduce, what are you, who made you, who created you, "
                + "who built you, who developed you, robot, bot, chatbot, assistant",
                "I'm the AI Chatbot Assistant \uD83E\uDD16, a Java-based chatbot that uses rule-based NLP. "
                + "I can answer questions about Java, programming, internships, and computer science.",
                "I'm a simple chatbot written in Java. I match the keywords in your message to topics I know about. "
                + "Type 'help' to see what I can talk about.");

        addIntent("GOODBYE",
                "bye, goodbye, see you, see you later, good night, take care, farewell, cya, talk later",
                "Goodbye! \uD83D\uDC4B Have a great day!",
                "See you later! Come back anytime you have a question.");

        addIntent("THANKS",
                "thank, thank you, thx, appreciate, grateful",
                "You're welcome! Happy to help. \uD83D\uDE0A",
                "No problem! Let me know if you have more questions.");

        addIntent("GREETING",
                "hello, hi, hey, good morning, good afternoon, good evening, greetings, howdy, hola, namaste",
                "Hello! \uD83D\uDC4B How can I help you today?",
                "Hi there! \uD83D\uDE0A Ask me about Java, programming, internships, or computer science.",
                "Hey! Nice to meet you. What would you like to know?");
    }
}