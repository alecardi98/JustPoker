package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterPane extends VBox {

    private SceneManager manager;
    private Client client;

    public RegisterPane(SceneManager manager, Client client) {
        this.manager = manager;
        this.client = client;

        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label title = new Label("Registrazione - JustPoker™");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Registrati");
        Button backButton = new Button("Indietro");

        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Inserisci username e password.");
                return;
            }

            // Per ora simuliamo la registrazione
            messageLabel.setText("Registrazione completata!");
            
            // Torna al login
            manager.showLoginScene();
        });

        backButton.setOnAction(e -> manager.showLoginScene());

        getChildren().addAll(title, usernameField, passwordField, registerButton, backButton, messageLabel);
    }
}

