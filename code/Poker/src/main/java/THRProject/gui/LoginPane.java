package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginPane extends VBox {

    private SceneManager manager;
    private Client client;

    public LoginPane(SceneManager manager, Client client) {
        this.manager = manager;
        this.client = client;

        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label title = new Label("JustPoker™");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Registrati");

        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Inserisci username e password.");
                return;
            }

            // LOGIN tramite Client
            //client.tryLogin(username, password);			//Da implementare!!!!

            messageLabel.setText("Login effettuato. In attesa del server...");

            // Per ora passiamo direttamente al menù principale
            manager.showMainMenu();
        });

        registerButton.setOnAction(e -> manager.showRegisterScene());

        getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton, messageLabel);
    }
}
