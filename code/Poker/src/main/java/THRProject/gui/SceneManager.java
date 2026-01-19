
package THRProject.gui;

import THRProject.client.Client;
import THRProject.game.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private Stage stage;
    private Client client;
    
    private GameTablePane gameTablePane;
    private AnimationTimer gameLoop;

    public SceneManager(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    public void showLoginScene() {
        LoginPane loginPane = new LoginPane(this, client);
        Scene scene = new Scene(loginPane, 400, 350);
        stage.setScene(scene);
        stage.setTitle("Login - JustPoker™");
        stage.show();
    }

    public void showRegisterScene() {
        RegisterPane registerPane = new RegisterPane(this, client);
        Scene scene = new Scene(registerPane, 400, 350);
        stage.setScene(scene);
        stage.setTitle("Registrazione - JustPoker™");
    }

    public void showMainMenu() {
        MainMenuPane menuPane = new MainMenuPane(this, client);
        Scene scene = new Scene(menuPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Menù principale - JustPoker™");
    }
    
    public void showLobby() {
        LobbyPane lobbyPane = new LobbyPane(this, client);
        Scene scene = new Scene(lobbyPane, 600, 400);
        stage.setScene(scene);
    }
    
    public void showGameTable() {
        Platform.runLater(() -> {
            /*if (gameTablePane == null) {
                gameTablePane = new GameTablePane(
                        client,
                        client.getGame(),
                        client.getClientId()
                );
            }*/
        	gameTablePane = new GameTablePane(client, null, 0);
            stage.setScene(new Scene(gameTablePane, 900, 600));
        });
    }
    
    /* =====================
     * GAME LOOP GUI
     * ===================== */

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {

            private Game lastGame; // riferimento precedente

            @Override
            public void handle(long now) {

                Game currentGame = client.getGame();
                if (currentGame == null) return;

                // aggiorna solo se cambia riferimento
                if (currentGame != lastGame && gameTablePane != null) {
                    refreshGameTable();
                    lastGame = currentGame;
                }
            }
        };

        gameLoop.start();
    }

    /* =====================
     * REDRAW (senza callback)
     * ===================== */

    public void refreshGameTable() {
        if (gameTablePane != null) {
            Platform.runLater(() -> gameTablePane.refresh());
        }
    }
    
}
