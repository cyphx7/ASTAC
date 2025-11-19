package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameUI {
    private final BorderPane root;
    private Label lblSubject;
    private Label lblQuestion;
    private TextArea txtCode;
    private Button[] btnOptions;
    private Label lblChatbotName;
    private Label lblStats;
    private Label lblDialogText;
    private Button btnAsk, btnCopy, btnSave;

    public GameUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + Theme.BG_COLOR + ";");
        root.setPadding(new Insets(20));
        createLayout();
    }

    private void createLayout() {
        // TOP
        BorderPane topBar = new BorderPane();
        lblSubject = new Label("Subject: LOADING...");
        lblSubject.setTextFill(Color.web(Theme.ACCENT_COLOR));
        lblSubject.setFont(Theme.FONT_HEADER);

        HBox commandBox = new HBox(15);
        btnAsk = Theme.createStyledButton("ASK BOT");
        btnCopy = Theme.createStyledButton("COPY PASTE");
        btnSave = Theme.createStyledButton("SAVE");
        btnSave.setDisable(true);
        commandBox.getChildren().addAll(btnAsk, btnCopy, btnSave);
        commandBox.setAlignment(Pos.CENTER_RIGHT);
        topBar.setLeft(lblSubject);
        topBar.setRight(commandBox);
        root.setTop(topBar);

        // LEFT
        VBox leftPanel = new VBox(15);
        leftPanel.setPrefWidth(220);
        leftPanel.setStyle("-fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 0 2 0 0; -fx-padding: 20;");
        Rectangle botAvatar = new Rectangle(180, 180, Color.web(Theme.ACCENT_COLOR));
        lblChatbotName = new Label("Bot: ...");
        lblChatbotName.setTextFill(Color.WHITE);
        lblChatbotName.setFont(Theme.FONT_NORMAL);
        lblStats = new Label("Stats:\n[HIDDEN]");
        lblStats.setTextFill(Color.LIGHTGRAY);
        lblStats.setFont(Theme.FONT_NORMAL);
        leftPanel.getChildren().addAll(botAvatar, lblChatbotName, lblStats);
        root.setLeft(leftPanel);

        // CENTER
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(0, 20, 20, 40));
        centerPanel.setAlignment(Pos.CENTER_LEFT);
        lblQuestion = new Label("...");
        lblQuestion.setTextFill(Color.WHITE);
        lblQuestion.setFont(Theme.FONT_HEADER); // Reusing header font for large text
        lblQuestion.setWrapText(true);

        txtCode = new TextArea();
        txtCode.setEditable(false);
        txtCode.setFont(Theme.FONT_CODE);
        txtCode.setPrefRowCount(6);
        txtCode.setStyle("-fx-control-inner-background: " + Theme.CODE_BG_COLOR + "; -fx-text-fill: " + Theme.ACCENT_COLOR + ";");
        txtCode.setVisible(false);
        centerPanel.getChildren().addAll(lblQuestion, txtCode);
        root.setCenter(centerPanel);

        // BOTTOM
        HBox bottomPanel = new HBox(30);
        bottomPanel.setPrefHeight(200);

        VBox dialogBox = new VBox(10);
        dialogBox.setPrefWidth(400);
        dialogBox.setStyle("-fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 2; -fx-padding: 15;");
        Label dialogHeader = new Label("logic.Chatbot says:");
        dialogHeader.setTextFill(Color.GRAY);
        lblDialogText = new Label("...");
        lblDialogText.setTextFill(Color.WHITE);
        lblDialogText.setFont(Theme.FONT_NORMAL);
        lblDialogText.setWrapText(true);
        dialogBox.getChildren().addAll(dialogHeader, lblDialogText);

        GridPane answerGrid = new GridPane();
        answerGrid.setHgap(15);
        answerGrid.setVgap(15);
        HBox.setHgrow(answerGrid, Priority.ALWAYS);

        btnOptions = new Button[4];
        for (int i = 0; i < 4; i++) {
            btnOptions[i] = Theme.createStyledButton("Option " + (i + 1));
            btnOptions[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btnOptions[i].setWrapText(true);
            answerGrid.add(btnOptions[i], i % 2, i / 2);
            GridPane.setHgrow(btnOptions[i], Priority.ALWAYS);
            GridPane.setVgrow(btnOptions[i], Priority.ALWAYS);
        }

        // Constraints
        ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(50);
        answerGrid.getColumnConstraints().addAll(cc, cc);
        RowConstraints rc = new RowConstraints(); rc.setPercentHeight(50);
        answerGrid.getRowConstraints().addAll(rc, rc);

        bottomPanel.getChildren().addAll(dialogBox, answerGrid);
        root.setBottom(bottomPanel);
    }

    // Getters for the Controller
    public BorderPane getRoot() { return root; }
    public Label getSubjectLabel() { return lblSubject; }
    public Label getQuestionLabel() { return lblQuestion; }
    public TextArea getCodeArea() { return txtCode; }
    public Button[] getOptionButtons() { return btnOptions; }
    public Label getBotNameLabel() { return lblChatbotName; }
    public Label getStatsLabel() { return lblStats; }
    public Label getDialogLabel() { return lblDialogText; }
    public Button getBtnAsk() { return btnAsk; }
    public Button getBtnCopy() { return btnCopy; }
    public Button getBtnSave() { return btnSave; }
}