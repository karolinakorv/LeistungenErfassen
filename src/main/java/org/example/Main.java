package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        // Wir starten die eigentliche App über die innere Klasse
        Application.launch(App.class, args);
    }

        public static class App extends Application {

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
    }
}
