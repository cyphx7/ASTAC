package logic;

import java.util.List;

public class GameSession {
    private int score;
    private int questionsAnswered;
    private boolean saveCommandUsed;
    private Chatbot currentChatbot;
    private List<Question> gameQuestions; // The 14 selected questions
    private int currentQuestionIndex;

    public GameSession(Chatbot startingChatbot, List<Question> selectedQuestions) {
        this.currentChatbot = startingChatbot;
        this.gameQuestions = selectedQuestions;
        this.score = 0;
        this.questionsAnswered = 0;
        this.currentQuestionIndex = 0;
        this.saveCommandUsed = false;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= gameQuestions.size()) return null;
        return gameQuestions.get(currentQuestionIndex);
    }

    // Returns true if game should continue, false if Game Over
    public GameResult submitAnswer(int playerAnswerIndex) {
        Question currentQ = getCurrentQuestion();

        // 1. Check if Player is Correct
        if (playerAnswerIndex == currentQ.getCorrectAnswerIndex()) {
            score++; // Logic: Score increases on correct answer
            advanceToNextQuestion();
            return GameResult.CORRECT;
        }

        // 2. Player is Wrong - Check for Save Command
        if (!saveCommandUsed) {
            saveCommandUsed = true; // Mark as used

            // Ask logic.Chatbot internally
            boolean chatbotSuccess = currentChatbot.calculateSuccess(currentQ.getSubject());

            if (chatbotSuccess) {
                // logic.Chatbot saved the player!
                // Logic: Player survives, but NO points awarded.
                advanceToNextQuestion();
                return GameResult.SAVED_BY_CHATBOT;
            } else {
                // logic.Chatbot failed too
                return GameResult.GAME_OVER;
            }
        }

        // 3. Wrong and Save already used
        return GameResult.GAME_OVER;
    }

    private void advanceToNextQuestion() {
        currentQuestionIndex++;
        questionsAnswered++;
    }

    public boolean isGameWon() {
        // Win logic: Finished all 14 questions
        return currentQuestionIndex >= 14;
    }

    public int getScore() { return score; }
    public int getQuestionsAnswered() { return questionsAnswered; }

    public enum GameResult {
        CORRECT,
        SAVED_BY_CHATBOT,
        GAME_OVER
    }

    // Add this to logic.GameSession.java
    public Chatbot getCurrentChatbot() {
        return this.currentChatbot;
    }
}