import javafx.application.Application;
import javafx.stage.Stage;
import ui.WindowManager;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        WindowManager windowManager = new WindowManager(primaryStage);
        windowManager.showMainMenu(); // Start at the menu
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}