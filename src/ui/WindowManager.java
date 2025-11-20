package ui;

import data.JsonDataLoader;
import logic.Chatbot;
import logic.GameSession;
import logic.Question;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // We keep this for critical errors only
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class WindowManager {
    private final Stage stage;
    private final Scene mainScene;
    private final StackPane rootStack; // Allows layering (Game + Overlay)

    // Global Game State
    private JsonDataLoader dataLoader;
    private Chatbot currentChatbot;
    private Set<String> completedSubjects;
    private int globalScore;

    // Global Command Usage
    private boolean isAskUsed = false;
    private boolean isCopyUsed = false;
    private boolean isSaveUsed = false;

    public WindowManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Are You Smarter Than a Chatbot?");
        this.stage.setFullScreenExitHint("");

        // FIX: Use StackPane to allow overlays
        this.rootStack = new StackPane();
        this.mainScene = new Scene(rootStack, 1280, 720);

        this.stage.setScene(mainScene);

        dataLoader = new JsonDataLoader();
        dataLoader.loadQuestionsFromDirectory("MCQ");
    }

    // --- NAVIGATION ---

    private void setRoot(Parent content) {
        // Clear everything and set the new content as the base layer
        rootStack.getChildren().clear();
        rootStack.getChildren().add(content);

        stage.setFullScreen(true);
        stage.show();
    }

    // --- NEW: CUSTOM OVERLAY ALERT ---
    // This replaces the native "Alert" window
    public void showCustomAlert(String title, String message, Runnable onDismiss) {
        // 1. Semi-transparent background (dim the game)
        Rectangle dimmer = new Rectangle(stage.getWidth(), stage.getHeight(), Color.rgb(0, 0, 0, 0.7));

        // 2. The Message Box
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setMaxSize(500, 300);
        box.setStyle("-fx-background-color: #222; -fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 3px; -fx-padding: 30;");

        // 3. Text Content
        Label lblTitle = new Label(title);
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setTextFill(Color.web(Theme.ACCENT_COLOR));

        Label lblMsg = new Label(message);
        lblMsg.setFont(Theme.FONT_NORMAL);
        lblMsg.setTextFill(Color.WHITE);
        lblMsg.setWrapText(true);
        lblMsg.setStyle("-fx-text-alignment: center;");

        // 4. OK Button
        Button btnOk = Theme.createStyledButton("CONTINUE");
        btnOk.setOnAction(e -> {
            // Remove the overlay
            rootStack.getChildren().removeAll(dimmer, box);
            // Run the next step (e.g., load next question)
            if (onDismiss != null) onDismiss.run();
        });

        box.getChildren().addAll(lblTitle, lblMsg, btnOk);

        // 5. Add to StackPane (on top of game)
        rootStack.getChildren().addAll(dimmer, box);
    }

    public void showMainMenu() {
        MainMenu menu = new MainMenu(this);
        setRoot(menu.getLayout());
    }

    public void startNewGame() {
        completedSubjects = new HashSet<>();
        globalScore = 0;
        isAskUsed = false;
        isCopyUsed = false;
        isSaveUsed = false;
        showChatbotSelection();
    }

    public void showChatbotSelection() {
        ChatbotSelection screen = new ChatbotSelection(this);
        setRoot(screen.getLayout());
    }

    public void showSubjectSelection() {
        SubjectSelection screen = new SubjectSelection(this, completedSubjects);
        setRoot(screen.getLayout());
    }

    public void onChatbotSelected(Chatbot bot) {
        this.currentChatbot = bot;
        showSubjectSelection();
    }

    public void onSubjectSelected(String subject) {
        try {
            List<Question> roundQuestions = getQuestionsForSubject(subject);

            if (roundQuestions.isEmpty()) {
                roundQuestions.add(new Question("Dummy Q1 (" + subject + ")", null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
                roundQuestions.add(new Question("Dummy Q2 (" + subject + ")", null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
            }

            GameSession roundSession = new GameSession(currentChatbot, roundQuestions, this);
            currentChatbot.revealStats();

            GameUI gameView = new GameUI();
            new GameController(roundSession, gameView, this);

            setRoot(gameView.getRoot());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishRound(String subject, int score) {
        globalScore += score;
        completedSubjects.add(subject);

        if (completedSubjects.size() >= 7) {
            // Use Custom Alert for Victory too!
            showCustomAlert("VICTORY", "You are Smarter Than a Chatbot!\nFinal Score: " + globalScore + "/14", this::showMainMenu);
        } else {
            showSubjectSelection();
        }
    }

    // Global State Getters
    public boolean isAskUsed() { return isAskUsed; }
    public void markAskUsed() { this.isAskUsed = true; }
    public boolean isCopyUsed() { return isCopyUsed; }
    public void markCopyUsed() { this.isCopyUsed = true; }
    public boolean isSaveUsed() { return isSaveUsed; }
    public void markSaveUsed() { this.isSaveUsed = true; }
    public int getGlobalScore() { return globalScore; }

    private List<Question> getQuestionsForSubject(String subject) {
        List<Question> all = dataLoader.generateGameSet();
        return all.stream()
                .filter(q -> q.getSubject().equalsIgnoreCase(subject))
                .limit(2)
                .collect(Collectors.toList());
    }
}