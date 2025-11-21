package logic;

import java.util.Random;

/**
 * Represents an AI chatbot with subject-specific strengths and weaknesses.
 * Each chatbot has different success rates based on the question subject.
 */
public class Chatbot {
    private String name;
    private String strengthSubject;
    private String weaknessSubject;
    private boolean isRevealed;
    private Random random;

    public Chatbot(String name, String strengthSubject, String weaknessSubject) {
        this.name = name;
        this.strengthSubject = strengthSubject;
        this.weaknessSubject = weaknessSubject;
        this.isRevealed = false;
        this.random = new Random();
    }

    public void revealStats() {
        this.isRevealed = true;
    }

    public boolean calculateSuccess(String currentSubject) {
        double roll = random.nextDouble(); // 0.0 to 1.0

        if (currentSubject.equalsIgnoreCase(strengthSubject)) {
            // Strength: 90% chance of being correct
            return roll < 0.90;
        } else if (currentSubject.equalsIgnoreCase(weaknessSubject)) {
            // Weakness: 10% chance of being correct
            return roll < 0.10;
        } else {
            // Neutral: 50% chance
            return roll < 0.50;
        }
    }

    public String getName() { return name; }
    public String getStrengthSubject() { return strengthSubject; }
    public String getWeaknessSubject() { return weaknessSubject; }
    public boolean isRevealed() { return isRevealed; }
}
