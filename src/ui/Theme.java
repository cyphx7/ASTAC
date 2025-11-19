package ui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Theme {
    // Color Palette
    public static final String BG_COLOR = "#1a1a1a";
    public static final String ACCENT_COLOR = "#00ff00"; // Neon Green
    public static final String BTN_BG_COLOR = "#333333";
    public static final String TEXT_COLOR = "#ffffff";
    public static final String CODE_BG_COLOR = "#000000";
    public static final String ERROR_COLOR = "#ff3333";

    // Fonts
    public static final Font FONT_TITLE = Font.font("Courier New", FontWeight.BOLD, 40);
    public static final Font FONT_HEADER = Font.font("Courier New", FontWeight.BOLD, 28);
    public static final Font FONT_NORMAL = Font.font("Courier New", FontWeight.NORMAL, 16);
    public static final Font FONT_CODE = Font.font("Consolas", 18);

    // Helper to style buttons consistently
    public static Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        String baseStyle = "-fx-background-color: " + BTN_BG_COLOR + "; " +
                "-fx-text-fill: " + ACCENT_COLOR + "; " +
                "-fx-border-color: " + ACCENT_COLOR + "; " +
                "-fx-border-width: 2px;";
        btn.setStyle(baseStyle);

        // Hover Effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: black; -fx-border-color: " + ACCENT_COLOR + "; -fx-border-width: 2px;"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }
}