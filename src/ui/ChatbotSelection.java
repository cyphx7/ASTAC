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

        // Define the 7 Chatbots
        // In a real app, you might give them different stats here
        String[] botNames = {"CYBER", "PIXEL", "GLITCH", "NANO", "TERA", "GIGA", "MEGA"};

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

        Button btnSelect = Theme.createStyledButton("SELECT");
        btnSelect.setOnAction(e -> {
            // Logic: Create the bot with random stats for now (hidden logic)
            Chatbot selectedBot = new Chatbot(name, "PROGRAMMING", "THEORETICAL");
            manager.onChatbotSelected(selectedBot);
        });

        card.getChildren().addAll(avatar, nameLabel, btnSelect);
        return card;
    }

    public VBox getLayout() {
        return layout;
    }
}