package logic;

import ui.WindowManager;
import java.util.List;

public class GameSession {
    private final Chatbot currentChatbot;
    private final List<Question> questions;
    private final WindowManager manager;
    private int currentQuestionIndex;
    private int score;

    public enum GameResult {
        CORRECT,
        WRONG_AND_FAILED,   // New: Bot tried to save but failed
        SAVED_BY_CHATBOT,   // Bot tried and succeeded
        GAME_OVER           // No save available
    }

    public GameSession(Chatbot chatbot, List<Question> questions, WindowManager manager) {
        this.currentChatbot = chatbot;
        this.questions = questions;
        this.manager = manager;
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    // FIX 1: Safe way to get subject even if round is over
    public String getSubject() {
        if (!questions.isEmpty()) {
            return questions.get(0).getSubject();
        }
        return "Unknown";
    }

    public Chatbot getCurrentChatbot() {
        return currentChatbot;
    }

    public GameResult submitAnswer(int answerIndex) {
        Question q = getCurrentQuestion();
        if (q == null) return GameResult.GAME_OVER;

        if (answerIndex == q.getCorrectAnswerIndex()) {
            score++;
            currentQuestionIndex++;
            return GameResult.CORRECT;
        } else {
            // WRONG ANSWER LOGIC

            if (!manager.isSaveUsed()) {
                manager.markSaveUsed(); // Mark it as used globally

                boolean saved = currentChatbot.calculateSuccess(q.getSubject());

                if (saved) {
                    currentQuestionIndex++; // Saved! Move on.
                    return GameResult.SAVED_BY_CHATBOT;
                } else {
                    // FIX 2: Bot tried but failed (Different from generic Game Over)
                    return GameResult.WRONG_AND_FAILED;
                }
            } else {
                // No save left at all
                return GameResult.GAME_OVER;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameWon() {
        return currentQuestionIndex >= questions.size();
    }
}