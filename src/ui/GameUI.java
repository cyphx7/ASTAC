package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameUI {
    private final BorderPane root;

    // Top Elements
    private Label subjectLabel;
    private Rectangle botAvatar;

    // Center Elements (Now includes Question)
    private Label questionLabel;
    private TextArea codeArea;
    private Button[] optionButtons;

    // Bottom Elements
    private Label dialogLabel;
    private Label botNameLabelBottom;
    private ProgressBar progressBar;
    private Label progressLabel;

    // Command Buttons
    private Button btnAsk;
    private Button btnCopy;
    private Button btnSave;

    public GameUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");

        createTopPanel();
        createCenterPanel();
        createBottomPanel();
    }

    public Parent getRoot() { return root; }

    private void createTopPanel() {
        BorderPane topContainer = new BorderPane();
        topContainer.setPadding(new Insets(20));

        // --- LEFT: Chatbot Icon ---
        VBox topLeft = new VBox(10);
        topLeft.setAlignment(Pos.TOP_LEFT);

        botAvatar = new Rectangle(80, 80, Color.web(Theme.ACCENT_COLOR));
        botAvatar.setArcWidth(15);
        botAvatar.setArcHeight(15);

        topLeft.getChildren().addAll(botAvatar);

        // --- RIGHT: Tools & Subject ---
        VBox topRight = new VBox(10);
        topRight.setAlignment(Pos.TOP_RIGHT);

        // 1. Tools
        HBox toolsBox = new HBox(5);
        toolsBox.setAlignment(Pos.CENTER_RIGHT);

        btnAsk = Theme.createStyledButton("ASK");
        btnAsk.setPrefWidth(60);
        btnAsk.setFont(Font.font("Consolas", 10));

        btnCopy = Theme.createStyledButton("COPY");
        btnCopy.setPrefWidth(60);
        btnCopy.setFont(Font.font("Consolas", 10));

        btnSave = new Button("SAVE");
        Theme.applyCyberpunkStyle(btnSave);
        btnSave.setPrefWidth(60);
        btnSave.setFont(Font.font("Consolas", 10));
        btnSave.setDisable(true);
        btnSave.setStyle("-fx-text-fill: #00ff00; -fx-border-color: #00ff00; -fx-background-color: transparent;");

        toolsBox.getChildren().addAll(btnAsk, btnCopy, btnSave);

        // 2. Subject Label
        subjectLabel = new Label("Subject: LOADING...");
        subjectLabel.setFont(Theme.FONT_NORMAL);
        subjectLabel.setTextFill(Color.WHITE);

        topRight.getChildren().addAll(toolsBox, subjectLabel);

        topContainer.setLeft(topLeft);
        // Removed Center from Top Panel (Question Box Moved)
        topContainer.setRight(topRight);

        root.setTop(topContainer);
    }

    private void createCenterPanel() {
        // Main container for Question + Options to ensure alignment
        VBox centerContainer = new VBox(30); // Spacing between Question and Options
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setPadding(new Insets(10, 40, 20, 40));

        // --- 1. QUESTION BOX (Moved here) ---
        VBox questionBox = new VBox(15);
        questionBox.setAlignment(Pos.TOP_CENTER);
        questionBox.setMaxWidth(800);
        questionBox.setStyle("-fx-background-color: #111; -fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 1px; -fx-padding: 20; -fx-background-radius: 10; -fx-border-radius: 10;");

        questionLabel = new Label("Loading Question...");
        questionLabel.setTextFill(Color.WHITE);
        questionLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setWrapText(false);
        codeArea.setMaxHeight(150);
        codeArea.setMaxWidth(700);
        codeArea.setStyle("-fx-control-inner-background: #000; -fx-font-family: 'Consolas'; -fx-text-fill: #00ff00; -fx-border-color: #333;");
        codeArea.setVisible(false);

        questionBox.getChildren().addAll(questionLabel, codeArea);

        // --- 2. OPTIONS GRID ---
        GridPane optionsGrid = new GridPane();
        optionsGrid.setAlignment(Pos.CENTER);
        optionsGrid.setHgap(20);
        optionsGrid.setVgap(20);

        optionButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = Theme.createStyledButton("Option " + (i + 1));

            optionButtons[i].setWrapText(true);
            optionButtons[i].setTextAlignment(TextAlignment.LEFT);
            optionButtons[i].setPrefWidth(400);
            optionButtons[i].setPrefHeight(90);
            optionButtons[i].setAlignment(Pos.CENTER_LEFT);

            optionsGrid.add(optionButtons[i], i % 2, i / 2);
        }

        centerContainer.getChildren().addAll(questionBox, optionsGrid);
        root.setCenter(centerContainer);
    }

    private void createBottomPanel() {
        BorderPane bottomContainer = new BorderPane();
        bottomContainer.setPadding(new Insets(20));
        bottomContainer.setStyle("-fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 2px 0 0 0; -fx-background-color: #111;");

        // --- LEFT: Dialogue Box ---
        VBox dialogueBox = new VBox(5);
        dialogueBox.setAlignment(Pos.CENTER_LEFT);
        dialogueBox.setMaxWidth(800);

        botNameLabelBottom = new Label("ASSISTANT SAYS:");
        botNameLabelBottom.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        botNameLabelBottom.setTextFill(Color.web(Theme.ACCENT_COLOR));

        dialogLabel = new Label("Waiting for input...");
        dialogLabel.setTextFill(Color.WHITE);
        dialogLabel.setFont(Theme.FONT_NORMAL);
        dialogLabel.setWrapText(true);

        dialogueBox.getChildren().addAll(botNameLabelBottom, dialogLabel);

        // --- RIGHT: Progress Bar ---
        VBox progressBox = new VBox(5);
        progressBox.setAlignment(Pos.CENTER_RIGHT);
        progressBox.setPrefWidth(300);

        progressLabel = new Label("Progress");
        progressLabel.setTextFill(Color.WHITE);
        progressLabel.setFont(Font.font("Consolas", 12));

        progressBar = new ProgressBar(0.0);
        progressBar.setPrefWidth(280);
        progressBar.setStyle("-fx-accent: " + Theme.ACCENT_COLOR + ";");

        progressBox.getChildren().addAll(progressLabel, progressBar);

        bottomContainer.setLeft(dialogueBox);
        bottomContainer.setRight(progressBox);

        root.setBottom(bottomContainer);
    }

    // Getters
    public Label getSubjectLabel() { return subjectLabel; }
    public Label getQuestionLabel() { return questionLabel; }
    public TextArea getCodeArea() { return codeArea; }
    public Button[] getOptionButtons() { return optionButtons; }
    public Label getDialogLabel() { return dialogLabel; }
    public Label getBotNameLabel() { return botNameLabelBottom; }
    public Button getBtnAsk() { return btnAsk; }
    public Button getBtnCopy() { return btnCopy; }
    public Button getBtnSave() { return btnSave; }
    public Label getStatsLabel() { return null; }
    public ProgressBar getProgressBar() { return progressBar; }
    public Label getProgressLabel() { return progressLabel; }
}