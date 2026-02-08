package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

//Main macht nur Start und Übergabe, keine Logik
public class Main {

    public static void main(String[] args) {
        // Wir starten die eigentliche App über die innere Klasse
        Application.launch(App.class, args); //Startet JavaFX
    }
        /*JavaFX verlangt eine Klasse, die von Application erbt
       Die innere Klasse spart eine extra Datei*/
        public static class App extends Application {

        @Override
        public void start(Stage primaryStage) { //Methode, zum Starten, primaryStage ist das Hauptfenster
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
