package ui;

import javafx.scene.Scene;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

public class CursorManager {

    private ImageCursor defaultCursor;
    private ImageCursor pressedCursor;

    private Scene scene;

    public CursorManager(Scene scene) {
        this.scene = scene;
        loadCursors();
        setupGlobalEvents();
    }

    private void loadCursors() {
        // 1. Load the full sprite sheet
        Image cursorAtlas = new Image(getClass().getResourceAsStream("../res/mouse.png"));
        PixelReader reader = cursorAtlas.getPixelReader();

        // 2. Slice the images
        // OPTION A: Use "Normal" (Index 0) as default
        WritableImage defaultImg = new WritableImage(reader, 32, 0, 32, 32);
        
        // OPTION B: If you prefer the "Hand" (Index 32) to be the default always:
        // WritableImage defaultImg = new WritableImage(reader, 32, 0, 32, 32);

        WritableImage pressedImg = new WritableImage(reader, 64, 0, 32, 32);

        // 3. Create Cursors
        defaultCursor = new ImageCursor(defaultImg, 0, 0);
        pressedCursor = new ImageCursor(pressedImg, 0, 0);

        // Set initial state
        scene.setCursor(defaultCursor);
    }

    private void setupGlobalEvents() {
        // Simple Logic: 
        // Mouse Down -> Show Pressed Cursor
        // Mouse Up   -> Show Default Cursor
        
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> scene.setCursor(pressedCursor));
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> scene.setCursor(defaultCursor));
    }

    // (Optional) Kept empty or removed if you don't need manual overrides anymore
    public void addHoverEffect(javafx.scene.Node node) {
        // No-op: We removed specific hover logic to keep it simple
    }
}