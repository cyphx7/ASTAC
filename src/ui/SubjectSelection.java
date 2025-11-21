package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Set;

/**
 * Subject selection screen showing available programming topics.
 * Completed subjects are disabled and marked as done.
 */
public class SubjectSelection {
    private final VBox layout;
    private final WindowManager manager;


    private final String[] subjects = {
            "INTRO",           // Intro to Paradigms
            "PROCEDURAL",      // Procedural Programming
            "FUNCTIONAL",      // Functional Programming
            "OOP",             // Object-Oriented Programming
            "IMP_DEC",         // Imperative vs Declarative
            "EVENT_DRIVEN",    // Event-Driven Programming
            "MAPPINGS"         // Component Mappings
    };

    public SubjectSelection(WindowManager manager, Set<String> completedSubjects) {
        this.manager = manager;

        layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");

        Label title = new Label("SELECT A SUBJECT");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        // Create buttons for 7 subjects
        for (int i = 0; i < subjects.length; i++) {
            String subjectName = subjects[i];
            Button btn = Theme.createStyledButton(subjectName);
            btn.setPrefSize(200, 60);


            if (completedSubjects.contains(subjectName)) {
                btn.setDisable(true);
                btn.setText(subjectName + " (DONE)");
                btn.setStyle("-fx-background-color: #111; -fx-text-fill: #555; -fx-border-color: #555;");
            } else {
                btn.setOnAction(e -> manager.onSubjectSelected(subjectName));
            }


            grid.add(btn, i % 3, i / 3);
        }

        layout.getChildren().addAll(title, grid);
    }

    public VBox getLayout() {
        return layout;
    }
}
