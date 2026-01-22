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
public class LoginPane extends VBox {

	private final SceneManager manager;

	private Label messageLabel = new Label();
	private Button loginButton = new Button("Login");
	private Button registerButton = new Button("Registrati");
	private TextField usernameField = new TextField();
	private PasswordField passwordField = new PasswordField();

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

	public Label getMessageLabel() {
		return messageLabel;
	}

	public void setMessageLabel(Label messageLabel) {
		this.messageLabel = messageLabel;
	}

	public Button getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(Button loginButton) {
		this.loginButton = loginButton;
	}

	public Button getRegisterButton() {
		return registerButton;
	}

	public void setRegisterButton(Button registerButton) {
		this.registerButton = registerButton;
	}

	public TextField getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(TextField usernameField) {
		this.usernameField = usernameField;
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(PasswordField passwordField) {
		this.passwordField = passwordField;
	}

	public SceneManager getManager() {
		return manager;
	}
}
