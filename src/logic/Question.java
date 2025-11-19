package logic;

import java.util.List;

public class Question {
    private String text;
    private String codeSnippet; // Can be null if it's a theoretical question
    private List<String> options;
    private int correctAnswerIndex; // 0 to 3
    private String subject; // e.g., "Variables", "Loops"
    private QuestionType type;

    public enum QuestionType {
        THEORETICAL,
        PROGRAMMING
    }

    // Constructor
    public Question(String text, String codeSnippet, List<String> options, int correctAnswerIndex, String subject, QuestionType type) {
        this.text = text;
        this.codeSnippet = codeSnippet;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.subject = subject;
        this.type = type;
    }

    // Getters
    public String getText() { return text; }
    public String getCodeSnippet() { return codeSnippet; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getSubject() { return subject; }
    public QuestionType getType() { return type; }
}