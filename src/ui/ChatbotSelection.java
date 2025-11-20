package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Chatbot;

public class ChatbotSelection {
    private final VBox layout;
    private final WindowManager manager;

    public ChatbotSelection(WindowManager manager) {
        this.manager = manager;

        layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");

        // Header
        Label title = new Label("CHOOSE YOUR ASSISTANT");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        // Grid of Bots
        FlowPane botGrid = new FlowPane();
        botGrid.setAlignment(Pos.CENTER);
        botGrid.setHgap(20);
        botGrid.setVgap(20);
        botGrid.setPadding(new Insets(20));

        // The 7 Chatbots
        String[] botNames = {"CHATGPT", "GEMINI", "GROK", "COPILOT", "CLAUDE", "DEEPSEEK", "PERPLEXITY"};

        for (String name : botNames) {
            VBox botCard = createBotCard(name);
            botGrid.getChildren().add(botCard);
        }

        layout.getChildren().addAll(title, botGrid);
    }

    private VBox createBotCard(String name) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 1px; -fx-background-color: #222;");

        // Placeholder Avatar
        Rectangle avatar = new Rectangle(80, 80, Color.web(Theme.ACCENT_COLOR));

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setFont(Theme.FONT_NORMAL);

        // We still calculate stats here so the Bot object is created correctly...
        String[] stats = assignStats(name);
        // ...BUT we do NOT display them to the user yet. (Hidden as per proposal)

        Button btnSelect = Theme.createStyledButton("SELECT");
        btnSelect.setOnAction(e -> {
            // Create the specific bot with its unique stats
            Chatbot selectedBot = new Chatbot(name, stats[0], stats[1]);

            // Pass it to manager.
            // Note: Manager will call bot.revealStats() ONLY after Subject Selection.
            manager.onChatbotSelected(selectedBot);
        });

        card.getChildren().addAll(avatar, nameLabel, btnSelect);
        return card;
    }

    // Unique Personalities (Hidden until game start)
    private String[] assignStats(String name) {
        switch (name) {
            case "CHATGPT":  return new String[]{"INTRO", "MAPPINGS"};
            case "GEMINI":  return new String[]{"OOP", "FUNCTIONAL"};
            case "GROK": return new String[]{"PROCEDURAL", "EVENT_DRIVEN"};
            case "COPILOT":   return new String[]{"FUNCTIONAL", "OOP"};
            case "CLAUDE":   return new String[]{"IMP_DEC", "INTRO"};
            case "DEEPSEEK":   return new String[]{"EVENT_DRIVEN", "IMP_DEC"};
            case "PERPLEXITY":   return new String[]{"MAPPINGS", "PROCEDURAL"};
            default:       return new String[]{"INTRO", "OOP"};
        }
    }

    public VBox getLayout() {
        return layout;
    }
}