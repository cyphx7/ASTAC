package ui;

import data.JsonDataLoader;
import logic.Chatbot;
import logic.GameSession;
import logic.Question;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.*;
import java.util.stream.Collectors;

public class WindowManager {
    private final Stage stage;

    // Global Game State
    private JsonDataLoader dataLoader;
    private Map<String, List<Question>> masterQuestionBank;
    private Chatbot currentChatbot;
    private Set<String> completedSubjects;
    private int globalScore;
    private final int QUESTIONS_PER_ROUND = 2; // [cite: 10]

    public WindowManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Are You Smarter Than a Chatbot?");
        this.stage.setMaximized(true);

        // Load Data Once at Startup
        dataLoader = new JsonDataLoader();
        dataLoader.loadQuestionsFromDirectory("MCQ");

        // We need a way to get ALL questions.
        // NOTE: You might need to add a getter 'getAllQuestionsMap()' to JsonDataLoader
        // For now, we will assume generateGameSet returns a mix and we filter,
        // OR better: we use the loader's internal map if we expose it.
        // SIMPLIFICATION: We will just generate a large set for now.
    }

    public void showMainMenu() {
        MainMenu menu = new MainMenu(this);
        setScene(new Scene(menu.getLayout(), 1280, 720));
    }

    public void startNewGame() {
        // Reset State
        completedSubjects = new HashSet<>();
        globalScore = 0;
        showChatbotSelection(); // Step 1: Pick Bot [cite: 21]
    }

    public void onChatbotSelected(Chatbot bot) {
        this.currentChatbot = bot;
        showSubjectSelection(); // Step 2: Pick Subject [cite: 23]
    }

    public void showChatbotSelection() {
        ChatbotSelection screen = new ChatbotSelection(this);
        setScene(new Scene(screen.getLayout(), 1280, 720));
    }

    public void showSubjectSelection() {
        SubjectSelection screen = new SubjectSelection(this, completedSubjects);
        setScene(new Scene(screen.getLayout(), 1280, 720));
    }

    public void onSubjectSelected(String subject) {
        // Step 3: Start Round for this subject
        try {
            // Get 2 random questions for this subject [cite: 10]
            List<Question> roundQuestions = getQuestionsForSubject(subject);

            if (roundQuestions.isEmpty()) {
                // Dummy fallback if JSON missing
                roundQuestions.add(new Question("Dummy Q1 for " + subject, null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
                roundQuestions.add(new Question("Dummy Q2 for " + subject, null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
            }

            GameSession roundSession = new GameSession(currentChatbot, roundQuestions);

            // Reveal stats now [cite: 24]
            currentChatbot.revealStats();

            GameUI gameView = new GameUI();
            // Pass 'this' so controller can call back when round ends
            new GameController(roundSession, gameView, this);

            setScene(new Scene(gameView.getRoot(), 1280, 720));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRoundComplete(int roundScore) {
        globalScore += roundScore;

        // Mark current subject as done
        Question q = dataLoader.generateGameSet().get(0); // Just to get current subject safely?
        // Better: We track it in the loop.
        // Since we don't have the subject string passed back easily,
        // we rely on the GameController handling the logic or we assume the selected one.
        // IMPROVEMENT: Let's rely on completedSubjects being updated by the selection logic
        // OR simply mark the subject passed in 'onSubjectSelected' as done.
    }

    // Helper to handle round end from Controller
    public void finishRound(String subject, int score) {
        globalScore += score;
        completedSubjects.add(subject); // [cite: 13]

        if (completedSubjects.size() >= 7) {
            // Win Condition [cite: 34]
            Alert win = new Alert(Alert.AlertType.INFORMATION);
            win.setTitle("VICTORY");
            win.setHeaderText("You are Smarter Than a Chatbot!");
            win.setContentText("Final Score: " + globalScore + "/14");
            win.showAndWait();
            showMainMenu();
        } else {
            // Go back to board [cite: 27]
            showSubjectSelection();
        }
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    // Helper to filter questions (You might need to adjust JsonDataLoader for this)
    private List<Question> getQuestionsForSubject(String subject) {
        // This relies on your JsonDataLoader having questions loaded.
        // Since we can't edit JsonDataLoader right now, we will generate a set and filter.
        List<Question> all = dataLoader.generateGameSet(); // This gets 14 random ones.
        // Real implementation: JsonDataLoader should have a 'getQuestions(subject)' method.
        // For now, we filter the random set or create dummies.
        return all.stream()
                .filter(q -> q.getSubject().equalsIgnoreCase(subject))
                .limit(2)
                .collect(Collectors.toList());
    }
}