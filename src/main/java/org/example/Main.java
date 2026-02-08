package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

/*
  Projekt: Leistungsverwaltung JavaFX
  Beschreibung: Eine Anwendung zurVerwaltung von medizinischen Leistungen mit MySQL-Anbindung und asynchroner Datenverarbeitung.
  Projekt von Karolina KORVIN-PIOTROVSKA und Angeh Whitney Chu
 */

/**
 *Diese Klasse dient lediglich als Einstiegspunkt und startet die JavaFX-Applikation.
 */
public class Main {

    /**
     * Die Main-Methode startet die JavaFX-Umgebung.
     * @param args Die Befehlszeilenargumente.
     */
    public static void main(String[] args) {
        // Wir starten die eigentliche App über die innere Klasse
        Application.launch(App.class, args); //Startet JavaFX
    }

    /**
     * JavaFX verlangt eine Klasse, die von Application erbt
     *
     */
    public static class App extends Application {
        /**
         * Initialisiert den LeistungsController und zeigt das Hauptfenster an.
         * @param primaryStage Hauptfenster der Anwendung
         */
        @Override
        public void start(Stage primaryStage) { //Methode, zum Starten, primaryStage ist das Hauptfenster
        // Controller erstellen und GUI anzeigen
        LeistungsController controller = new LeistungsController(primaryStage);
        controller.anzeigeErstellen();
        }

        /**
         * Wird aufgerufen, wenn die Anwendung geschlossen wird.
         */
        @Override
        public void stop() {
        System.out.println("Anwendung wird beendet - Ressourcen werden freigegeben...");
        }
    }
}
