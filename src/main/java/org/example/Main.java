package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Controller erstellen und GUI anzeigen
        LeistungsController controller = new LeistungsController(primaryStage);
        controller.anzeigeErstellen();
    }

    @Override
    public void stop() {
        System.out.println("Anwendung wird beendet - Ressourcen werden freigegeben...");
    }

    public static void main(String[] args) {
        // JavaFX-Anwendung starten
        launch(args);
    }
}
