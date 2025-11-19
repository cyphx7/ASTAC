package ui; // Make sure this is at the top!

import javafx.geometry.Pos; // Added this import to fix 'Pos' error
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainMenu {
    private final VBox layout;

    public MainMenu(WindowManager manager) {
        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");

        // Title Label
        Label title = new Label("ARE YOU SMARTER\nTHAN A CHATBOT?");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_TITLE);
        title.setStyle("-fx-text-alignment: center;");

        // Buttons
        Button btnPlay = Theme.createStyledButton("PLAY GAME");
        Button btnGuide = Theme.createStyledButton("GUIDE");
        Button btnSettings = Theme.createStyledButton("SETTINGS");
        Button btnExit = Theme.createStyledButton("EXIT");

        // --- THE FIX IS HERE ---
        // We changed 'startGame()' to 'startNewGame()' in the Manager
        btnPlay.setOnAction(e -> manager.startNewGame());

        btnExit.setOnAction(e -> System.exit(0));

        // Add visuals
        layout.getChildren().addAll(title, btnPlay, btnGuide, btnSettings, btnExit);
    }

    public VBox getLayout() {
        return layout;
    }
}