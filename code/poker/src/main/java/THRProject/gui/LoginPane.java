package THRProject.gui;

import THRProject.client.ClientObserver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Pannello di login per l'accesso al gioco
 * 
 * MODIFICHE: - Integrato con metodo tryLogin del Client - Aggiunta gestione
 * stati di connessione - Aggiunto feedback visivo durante il login
 */
public class LoginPane extends VBox implements ClientObserver {
	
    private final SceneManager manager;
    
    Label messageLabel = new Label();
    Button loginButton = new Button("Login");
	Button registerButton = new Button("Registrati");
	TextField usernameField = new TextField();
	PasswordField passwordField = new PasswordField();

	public LoginPane(SceneManager manager) {

		this.manager = manager;
		
		setSpacing(15);
		setPadding(new Insets(20));
		setAlignment(Pos.CENTER);

		Label title = new Label("JustPoker™");
		title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		
		usernameField.setPromptText("Username");
		passwordField.setPromptText("Password");
		messageLabel.setStyle("-fx-text-fill: red;");
		
		manager.getClient().addObserver(this);

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
			
			manager.getClient().tryLogin(username, password);
		});

		registerButton.setOnAction(e -> manager.showRegisterScene());

		getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton, messageLabel);
	}

	@Override
	public void onLoginResult(boolean success) {
		javafx.application.Platform.runLater(() -> {
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Login effettuato con successo!");
                manager.showMainMenu();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Username o password errati.");
                usernameField.setDisable(false);
                passwordField.setDisable(false);
                loginButton.setDisable(false);
                registerButton.setDisable(false);
            }
        });		
	}
	
	@Override
    public void onMessageReceived(String message) {
        javafx.application.Platform.runLater(() -> {
            // gestione messaggi generici
            messageLabel.setText(message);
        });
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTornaMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameViewUpdate() {
		// TODO Auto-generated method stub
		
	}
}
