package THRProject.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Pannello di registrazione per nuovi utenti
 * 
 * MODIFICHE:
 * - Aggiunta gestione stati durante la registrazione
 * - Aggiunto feedback visivo
 * - Preparato per integrazione con Client
 */
public class RegisterPane extends VBox {

    private SceneManager manager;

    public RegisterPane(SceneManager manager) {
        this.manager = manager;

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
        messageLabel.setStyle("-fx-text-fill: red;");

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Inserisci username e password.");
                return;
            }

            if (username.length() < 3) {
                messageLabel.setText("L'username deve essere di almeno 3 caratteri.");
                return;
            }

            if (password.length() < 4) {
                messageLabel.setText("La password deve essere di almeno 4 caratteri.");
                return;
            }

            // Disabilita i campi durante la registrazione
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            registerButton.setDisable(true);
            backButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: blue;");
            messageLabel.setText("Registrazione in corso...");

            // Registrazione in thread separato
            new Thread(() -> {
                try {
                    // TODO: Implementare registrazione tramite Client
                    // client.tryRegister(username, password);
                    
                    // PER ORA: Simulazione registrazione con successo
                    Thread.sleep(1000); // Simula attesa server
                    
                    javafx.application.Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("Registrazione completata con successo!");
                        
                        // Torna al login dopo 1 secondo
                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                javafx.application.Platform.runLater(() -> manager.showLoginScene());
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    });
                    
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("Errore durante la registrazione: " + ex.getMessage());
                        // Riabilita i campi
                        usernameField.setDisable(false);
                        passwordField.setDisable(false);
                        registerButton.setDisable(false);
                        backButton.setDisable(false);
                    });
                }
            }).start();
        });

        backButton.setOnAction(e -> manager.showLoginScene());

        getChildren().addAll(title, usernameField, passwordField, registerButton, backButton, messageLabel);
    }
}
