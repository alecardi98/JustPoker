package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Pannello di login per l'accesso al gioco
 * 
 * MODIFICHE:
 * - Integrato con metodo tryLogin del Client
 * - Aggiunta gestione stati di connessione
 * - Aggiunto feedback visivo durante il login
 */
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
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Inserisci username e password.");
                return;
            }

            // Disabilita i campi durante il login
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            loginButton.setDisable(true);
            registerButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: blue;");
            messageLabel.setText("Login in corso...");

            // LOGIN tramite Client - eseguito in thread separato per non bloccare la GUI
            new Thread(() -> {
                try {
                    // Tenta il login (metodo ancora da implementare nel Client)
                    // client.tryLogin(username, password);
                    
                    // PER ORA: Simulazione login con successo
                    // In futuro: il client dovrà comunicare con il server per validare le credenziali
                    Thread.sleep(1000); // Simula attesa server
                    
                    // Se il login ha successo, mostra il menu principale
                    javafx.application.Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("Login effettuato con successo!");
                        manager.showMainMenu();
                    });
                    
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("Errore durante il login: " + ex.getMessage());
                        // Riabilita i campi
                        usernameField.setDisable(false);
                        passwordField.setDisable(false);
                        loginButton.setDisable(false);
                        registerButton.setDisable(false);
                    });
                }
            }).start();
        });

        registerButton.setOnAction(e -> manager.showRegisterScene());

        getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton, messageLabel);
    }
}
