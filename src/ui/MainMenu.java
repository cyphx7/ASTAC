package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Main menu screen with navigation options for the game.
 */
public class MainMenu {
    private final StackPane layout;

    public MainMenu(WindowManager manager) {
        Image bgImage = new Image(getClass().getResourceAsStream("../res/b.gif"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setSmooth(false);
        bgView.setPreserveRatio(true);

        VBox contentBox = new VBox(350);
        contentBox.setAlignment(Pos.CENTER);

        Image titleImage = new Image(getClass().getResourceAsStream("../res/title.gif"));
        ImageView titleView = new ImageView(titleImage);
        titleView.setSmooth(false);
        titleView.setPreserveRatio(true);

        // Buttons
        Button btnPlay = createSpriteButton("PLAY GAME");
        Button btnGuide = createSpriteButton("GUIDE");
        Button btnSettings = createSpriteButton("SETTINGS");
        Button btnExit = createSpriteButton("EXIT");

        btnPlay.setOnAction(e -> manager.startNewGame());
        btnGuide.setOnAction(e -> manager.showGuide());
        btnExit.setOnAction(e -> System.exit(0));

        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(btnPlay, btnGuide, btnSettings, btnExit);

        contentBox.getChildren().addAll(titleView, buttonRow);

        layout = new StackPane();
        // Add visuals
        layout.getChildren().addAll(bgView, contentBox);
        bgView.fitWidthProperty().bind(layout.widthProperty().multiply(0.50));
        bgView.fitHeightProperty().bind(layout.heightProperty().multiply(0.30));
        layout.setStyle("-fx-background-color: #000000");
    }

    public StackPane getLayout() {
        return layout;
    }

    private Button createSpriteButton(String text) {
    Button btn = new Button(text);

    // 1. Load the Image (128x64)
    // Make sure "button_sprite.png" is in your resources folder
    Image img = new Image(getClass().getResourceAsStream("../res/mainbutton.png"));
    ImageView view = new ImageView(img);

    // 2. Set Size
    view.setFitWidth(128);
    view.setFitHeight(64);
    view.setSmooth(false); // Uncomment if it's pixel art

    // 3. Set the Graphic
    btn.setGraphic(view);

    // 4. CRITICAL: Layer Text on TOP of Image
    btn.setContentDisplay(ContentDisplay.CENTER);

    // 5. CSS: Make the actual button transparent so only Sprite + Text shows
    btn.setStyle(
        "-fx-background-color: transparent; " + // Hide standard gray button                  
        "-fx-padding: 0; " +                    // Remove padding so size matches image exactly
        "-fx-background-radius: 0; " +          
        "-fx-border-width: 0;"
    );

    // 6. Text Styling (Ensure contrast against your sprite)
    btn.setTextFill(Color.WHITE); 
    btn.setFont(Theme.FONT_NORMAL); // Or use a custom font size like Font.font("Consolas", 14)

    return btn;
    }
}
