package THRProject.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EndPane extends VBox {

	private Label statusLabel;

	public EndPane(SceneManager manager) {
		setSpacing(15);
		setPadding(new Insets(25));
		setAlignment(Pos.CENTER);

		Label title = new Label("Chiusura gioco - JustPoker™");
		title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		statusLabel = new Label("La partita è terminata.");
		statusLabel.setStyle("-fx-text-fill: blue;");

		Button exitButton = new Button("Esci dal gioco");

		/*
		 * ---------------- ESCI ----------------
		 */
		exitButton.setOnAction(e -> {
			System.exit(0);
		});

		getChildren().addAll(title, statusLabel, exitButton);
	}
}
