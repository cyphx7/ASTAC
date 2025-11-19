package ui;

import logic.Chatbot;      // Import from 'logic' folder
import logic.GameSession;
import logic.Question;

import javafx.scene.control.Alert;
import java.util.List;
import java.util.Random;

public class GameController {
    private final GameSession session;
    private final GameUI ui;
    private final WindowManager manager;

    public GameController(GameSession session, GameUI ui, WindowManager manager) {
        this.session = session;
        this.ui = ui;
        this.manager = manager;
        initController();
        updateUI(); // Load first question
    }

    private void initController() {
        // Bind Answer Buttons
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ui.getOptionButtons()[i].setOnAction(e -> handleAnswer(index));
        }

        // Bind Command Buttons
        ui.getBtnAsk().setOnAction(e -> handleAskBot());
        ui.getBtnCopy().setOnAction(e -> handleCopyPaste());
    }

    private void updateUI() {
        Question q = session.getCurrentQuestion();
        if (q == null) return;

        ui.getSubjectLabel().setText("Subject: " + q.getSubject());
        ui.getQuestionLabel().setText(q.getText());

        // Code Snippet Logic
        if (q.getCodeSnippet() != null && !q.getCodeSnippet().isEmpty()) {
            ui.getCodeArea().setText(q.getCodeSnippet());
            ui.getCodeArea().setVisible(true);
        } else {
            ui.getCodeArea().setVisible(false);
        }

        // Update Buttons
        List<String> opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            if (i < opts.size()) {
                ui.getOptionButtons()[i].setText(opts.get(i));
                ui.getOptionButtons()[i].setDisable(false);
            } else {
                ui.getOptionButtons()[i].setText("-");
                ui.getOptionButtons()[i].setDisable(true);
            }
        }

        // Update Bot Stats
        Chatbot bot = session.getCurrentChatbot(); // You might need to add this getter to logic.GameSession
        ui.getBotNameLabel().setText("Bot: " + bot.getName());
        if (bot.isRevealed()) {
            ui.getStatsLabel().setText("Stats:\nSTR: " + bot.getStrengthSubject() + "\nWK: " + bot.getWeaknessSubject());
        } else {
            ui.getStatsLabel().setText("Stats:\n[HIDDEN]");
        }

        ui.getDialogLabel().setText("Waiting...");
    }

    private void handleAnswer(int index) {
        // 1. Submit the answer to the backend logic
        GameSession.GameResult result = session.submitAnswer(index);

        // 2. Handle the result
        if (result == GameSession.GameResult.CORRECT) {
            // CASE A: Correct Answer
            showAlert("Correct!", "Good job!");
            checkGameStatus(); // Move to next question or finish round

        } else if (result == GameSession.GameResult.SAVED_BY_CHATBOT) {
            // CASE B: Wrong, but Saved
            showAlert("SAVED!", "You were wrong, but " + session.getCurrentChatbot().getName() + " saved you!");

            // Visual update for the Used Save button
            ui.getBtnSave().setText("SAVE USED");
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR +
                    "; -fx-border-color: " + Theme.ERROR_COLOR +
                    "; -fx-background-color: " + Theme.BTN_BG_COLOR + ";");

            checkGameStatus(); // Move to next question since they survived

        } else {
            // CASE C: Game Over (Wrong and No Save left)
            showAlert("GAME OVER", "You threw an exception! Final Score: " + session.getScore());

            // CRITICAL CHANGE: Go back to the Menu instead of closing
            manager.showMainMenu();
        }
    }

    private void checkGameStatus() {
        if (session.isGameWon()) {
            // Round is done!
            showAlert("ROUND COMPLETE", "Returning to Board...");

            // Get the subject of the last question to mark it complete
            String subject = session.getCurrentQuestion().getSubject();

            // Notify Manager
            manager.finishRound(subject, session.getScore());
        } else {
            updateUI();
        }
    }

    private void handleAskBot() {
        Question q = session.getCurrentQuestion();
        Chatbot bot = session.getCurrentChatbot(); // Need this getter in logic.GameSession
        boolean success = bot.calculateSuccess(q.getSubject());

        if (success) {
            ui.getDialogLabel().setText("I am 90% sure it is Option " + (q.getCorrectAnswerIndex() + 1));
        } else {
            // Random wrong answer
            int wrong;
            do { wrong = new Random().nextInt(4); } while(wrong == q.getCorrectAnswerIndex());
            ui.getDialogLabel().setText("I am guessing... Option " + (wrong + 1) + "?");
        }
        ui.getBtnAsk().setDisable(true);
    }

    private void handleCopyPaste() {
        // Similar logic to AskBot but calls handleAnswer() at the end
        // For brevity, implemented simplified:
        handleAskBot();
        // In real implementation, you'd force the answer here.
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(content);
        a.showAndWait();
    }
}