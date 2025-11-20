package ui;

import logic.Chatbot;
import logic.GameSession;
import logic.Question;
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
        updateUI();
    }

    private void initController() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ui.getOptionButtons()[i].setOnAction(e -> handleAnswer(index));
        }

        ui.getBtnAsk().setOnAction(e -> handleAskBot());
        ui.getBtnCopy().setOnAction(e -> handleCopyPaste());

        if (manager.isAskUsed()) {
            ui.getBtnAsk().setDisable(true);
            ui.getBtnAsk().setText("ASK USED");
        }
        if (manager.isCopyUsed()) {
            ui.getBtnCopy().setDisable(true);
            ui.getBtnCopy().setText("COPY USED");
        }
        if (manager.isSaveUsed()) {
            ui.getBtnSave().setText("SAVE USED");
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR + "; -fx-border-color: " + Theme.ERROR_COLOR + ";");
        }
    }

    private void updateUI() {
        Question q = session.getCurrentQuestion();
        if (q == null) return;

        ui.getSubjectLabel().setText("Subject: " + q.getSubject());
        ui.getQuestionLabel().setText(q.getText());

        if (q.getCodeSnippet() != null && !q.getCodeSnippet().isEmpty()) {
            ui.getCodeArea().setText(q.getCodeSnippet());
            ui.getCodeArea().setVisible(true);
        } else {
            ui.getCodeArea().setVisible(false);
        }

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

        Chatbot bot = session.getCurrentChatbot();
        ui.getBotNameLabel().setText("Bot: " + bot.getName());
        if (bot.isRevealed()) {
            ui.getStatsLabel().setText("Stats:\nSTR: " + bot.getStrengthSubject() + "\nWK: " + bot.getWeaknessSubject());
        } else {
            ui.getStatsLabel().setText("Stats:\n[HIDDEN]");
        }

        ui.getDialogLabel().setText("Waiting...");
    }

    private void handleAnswer(int index) {
        GameSession.GameResult result = session.submitAnswer(index);

        if (result == GameSession.GameResult.CORRECT) {
            // Use Custom Overlay with a callback to check status AFTER they click "Continue"
            manager.showCustomAlert("CORRECT!", "Good job! Proceeding...", this::checkGameStatus);
        }
        else if (result == GameSession.GameResult.SAVED_BY_CHATBOT) {
            ui.getBtnSave().setText("SAVE USED");
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR + "; -fx-border-color: " + Theme.ERROR_COLOR + ";");

            manager.showCustomAlert("SAVED!", "You were wrong, but " + session.getCurrentChatbot().getName() + " saved you!", this::checkGameStatus);
        }
        else if (result == GameSession.GameResult.WRONG_AND_FAILED) {
            manager.showCustomAlert("GAME OVER", "You were wrong!\nBot attempted to Save you, but FAILED.", manager::showMainMenu);
        }
        else {
            manager.showCustomAlert("GAME OVER", "You threw an exception! Final Score: " + manager.getGlobalScore(), manager::showMainMenu);
        }
    }

    private void checkGameStatus() {
        if (session.isGameWon()) {
            manager.showCustomAlert("ROUND COMPLETE", "Returning to Subject Board...", () ->
                    manager.finishRound(session.getSubject(), session.getScore())
            );
        } else {
            updateUI();
        }
    }

    private void handleAskBot() {
        manager.markAskUsed();
        ui.getBtnAsk().setDisable(true);
        ui.getBtnAsk().setText("ASK USED");

        Question q = session.getCurrentQuestion();
        Chatbot bot = session.getCurrentChatbot();
        boolean success = bot.calculateSuccess(q.getSubject());

        if (success) {
            ui.getDialogLabel().setText("I am 90% sure it is Option " + (q.getCorrectAnswerIndex() + 1));
        } else {
            int wrong;
            do { wrong = new Random().nextInt(4); } while(wrong == q.getCorrectAnswerIndex());
            ui.getDialogLabel().setText("I am guessing... Option " + (wrong + 1) + "?");
        }
    }

    private void handleCopyPaste() {
        manager.markCopyUsed();
        ui.getBtnCopy().setDisable(true);
        ui.getBtnCopy().setText("COPY USED");
        handleAskBot();
    }
}