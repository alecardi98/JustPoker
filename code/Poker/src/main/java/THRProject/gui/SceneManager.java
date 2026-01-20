package THRProject.gui;

import THRProject.client.Client;
import THRProject.game.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gestore delle scene per l'applicazione GUI
 * 
 * MODIFICHE:
 * - Aggiunto game loop per aggiornamenti automatici
 * - Corretto passaggio parametri a GameTablePane
 * - Aggiunti metodi per gestione finestra di gioco
 */
public class SceneManager {

    private Stage stage;
    private Client client;
    
    private GameTablePane gameTablePane;
    private AnimationTimer gameLoop;

    public SceneManager(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
        
        // Configurazione finestra principale
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            if (client != null) {
                client.quit();
            }
            Platform.exit();
        });
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
        stage.setTitle("Lobby - JustPoker™");
    }
    
    public void showGameTable() {
        Platform.runLater(() -> {
            // Creazione GameTablePane con parametri corretti
            gameTablePane = new GameTablePane(client, client.getGame(), client.getClientId());
            stage.setScene(new Scene(gameTablePane, 1000, 700));
            stage.setTitle("Tavolo da gioco - JustPoker™");
            
            // Avvia il game loop per aggiornamenti automatici
            startGameLoop();
        });
    }
    
    /* =====================
     * GAME LOOP GUI
     * ===================== */

    private void startGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
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
    
    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }

    /* =====================
     * REDRAW (senza callback)
     * ===================== */

    public void refreshGameTable() {
        if (gameTablePane != null) {
            Platform.runLater(() -> gameTablePane.refresh());
        }
    }
    
    /* =====================
     * GETTER
     * ===================== */
    
    public Stage getStage() {
        return stage;
    }
}
