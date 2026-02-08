package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Der LeistungsController steuert die Benutzeroberfläche zur Verwaltung von Leistungen.
 * Er fungiert als Bindeglied zwischen der View (JavaFX), dem Datenmodell (Leistung)
 * und der Datenbank-Logik (DB).
 * Enthält ein eigenes Warteschlangen-System (Background Worker),
 * um Datenbankzugriffe asynchron vom UI-Thread durchzuführen.
 */
public class LeistungsController {

    private final Stage stage; //Referenz zum Hauptfenster der Anwendung

    // Datenstruktur für Tabelle (noch ohne DB-Anbindung)
    /** Eine beobachtbare Liste der Leistungen.
     * Änderungen an dieser Liste werden automatisch in der TableView reflektiert.
     */
    private final ObservableList<Leistung> datenListe = FXCollections.observableArrayList(); //JavaFX-spezifische Liste, Änderungen werden automatisch in der TableView angezeigt

    /**
     * Warteschlange für Datenbankoperationen.
     * Thread-sichere, asynchrone DB-Zugriffe
     */
    private final Queue<Runnable> dbTaskQueue = new LinkedList<>(); //Warteschlange für Datenbankoperationen, sorgt für thread-sichere, asynchrone DB-Zugriffe

    private TableView<Leistung> tabelle;
    private TextField txtBez, txtPreis, txtKuerzel;

    /**
     * Konstruktor des Controllers.
     * @param stage Die primaryStage, auf der die GUI gemacht werden soll.
     */
    public LeistungsController(Stage stage) { //speichert die Stage
        this.stage = stage;
        startBackgroundWorker(); //Startet Hintergrund-Thread, wichitg damit nicht alles einfiert
    }

    /**
     * Erstellt den gesamten Aufbau der Benutzeroberfläche (Tabelle, Eingabefelder, Buttons)
     * und zeigt das Fenster an.
     */
    public void anzeigeErstellen() {
        BorderPane root = new BorderPane();

        // Tabelle
        tabelle = new TableView<>(datenListe);
        tabelle.getColumns().add(spalteErstellen("ID", "id", 50));
        tabelle.getColumns().add(spalteErstellen("Bezeichnung", "bezeichnung", 200));
        tabelle.getColumns().add(spalteErstellen("Preis", "preis", 100));
        tabelle.getColumns().add(spalteErstellen("Kürzel", "kuerzel", 100));

        tabelle.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> { //reagirt auf klicks, füllt textfelder, update funktion
            if (neu != null) {
                fuelleFelder(neu);
            }
        });

        // Eingabebereich
        VBox unten = new VBox(10);
        unten.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        HBox inputs = new HBox(10);
        txtBez = new TextField();
        txtBez.setPromptText("Bezeichnung");

        txtPreis = new TextField();
        txtPreis.setPromptText("Preis (0.00)");

        txtKuerzel = new TextField();
        txtKuerzel.setPromptText("Kürzel");

        inputs.getChildren().addAll(txtBez, txtPreis, txtKuerzel);

        // KORRIGIERT: Buttons erstellen und Aktionen zuweisen
        Button btnAdd = new Button("Hinzufügen");
        btnAdd.setOnAction(e -> aktionHinzufuegen());

        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> aktionUpdate());

        Button btnDelete = new Button("Löschen");
        btnDelete.setOnAction(e -> aktionLoeschen());

        Button btnSort = new Button("Sortieren (A-Z)");
        btnSort.setOnAction(e -> aktionSortieren());

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnSort);

        // "Neu / Leeren"
        // Damit löschst du die Eingabefelder und hebst die Auswahl in der Tabelle auf
        Button btnReset = new Button("Neue Eingabe");
        btnReset.setOnAction(e -> {
            //Textfelder leeren
            txtBez.clear();
            txtPreis.clear();
            txtKuerzel.clear();

            tabelle.getSelectionModel().clearSelection();
        });

        buttons = new HBox(10);
        buttons.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnSort, btnReset);

        unten.getChildren().addAll(
                new Label("Geben Sie Ihre Leistung ein:"),
                inputs,
                buttons
        );

        root.setCenter(tabelle);
        root.setBottom(unten);

        stage.setTitle("Leistungsverwaltung");
        stage.setScene(new Scene(root, 700, 500));
        stage.show();

        ladeDaten();
    }

    /**
     * Hilfsmethode zum Erstellen einer Tabellenspalte.
     * @param titel Der Anzeigename der Spalte
     * @param property Der Name des Attributs in der Klasse (Leistung)
     * @param breite Die bevorzugte Breite der Spalte
     * @return Eine konfigurierte TableColumn
     * @param <T> Datentyp der Spalte
     */
    private <T> TableColumn<Leistung, T> spalteErstellen(String titel, String property, double breite) {
        TableColumn<Leistung, T> col = new TableColumn<>(titel);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(breite);
        return col;
    }

    /**
     * Füllt die Eingabefelder mit den Daten einer gewählten Leistung
     * @param l Die gewählte Leistung
     */
    private void fuelleFelder(Leistung l) {
        txtBez.setText(l.getBezeichnung());
        txtPreis.setText(String.valueOf(l.getPreis()));
        txtKuerzel.setText(l.getKuerzel());
    }

    /**
     * Prüft, ob die Benutzereingaben in den Textfeldern gültig sind.
     * @return true, wenn die Eingabe korrekt ist, sonst false
     */
    private boolean validiereEingabe() {
        if (txtBez.getText().isEmpty() || txtKuerzel.getText().isEmpty()) return false;

        // Regex: Erlaubt z.B. "10", "10.5", "10.50"
        String preisRegex = "\\d+(\\.\\d+)?";
        return txtPreis.getText().matches(preisRegex);
    }

    // KONZEPT 4: Sortieren (Comparator Logik)
    /**
     * Sortiert die Liste der Leistungen alphabetisch nach Bezeichnung.
     */
    private void aktionSortieren() {
        // Wir sortieren die JavaFX Liste direkt mit einem Comparator
        datenListe.sort((l1, l2) -> l1.getBezeichnung().compareToIgnoreCase(l2.getBezeichnung()));
    }

    // --- Datenbank Queue System ---

    /**
     * Lädt alle Leistungen asynchron aus der Datenbank und aktualisiert die Liste
     */
    private void ladeDaten() {
        addTaskToQueue(() -> {
            ArrayList<Leistung> liste = DB.alleLeistungenLaden();
            Platform.runLater(() -> {
                datenListe.setAll(liste);
                System.out.println("Daten geladen.");
            });
        });
    }

    /**
     * Liest die Felder aus und fügt eine neue Leistung zur DB hinzu
     */
    private void aktionHinzufuegen() {
        if (!validiereEingabe()) return;
        Leistung neu = new Leistung(txtBez.getText(), Double.parseDouble(txtPreis.getText()), txtKuerzel.getText());

        addTaskToQueue(() -> {
            boolean ok = DB.leistungHinzufuegen(neu);
            if (ok) ladeDaten();
        });
        txtBez.clear(); txtPreis.clear(); txtKuerzel.clear();
    }

    /**
     * Aktualisiert eine bestehende Leistung in der Datenbank
     */
    private void aktionUpdate() {
        Leistung auswahl = tabelle.getSelectionModel().getSelectedItem();
        if (auswahl == null || !validiereEingabe()) return;

        auswahl.setBezeichnung(txtBez.getText());
        auswahl.setPreis(Double.parseDouble(txtPreis.getText()));
        auswahl.setKuerzel(txtKuerzel.getText());

        addTaskToQueue(() -> {
            boolean ok = DB.leistungAktualisieren(auswahl);
            if (ok) {
                Platform.runLater(() -> tabelle.refresh());
            }
        });
    }

    /**
     * Löscht die aktuell in der Tabelle markierte Leistung aus der Datenbank
     */
    private void aktionLoeschen() {
        Leistung auswahl = tabelle.getSelectionModel().getSelectedItem();
        if (auswahl == null) return;

        addTaskToQueue(() -> {
            boolean ok = DB.leistungLoeschen(auswahl.getId());
            if (ok) ladeDaten();
        });
    }

    /**
     * Fügt einen Datenbank-Task zur Warteschlange hinzu und benachrichtigt den Worker
     * @param task Ein Runnable, das die Datenbank-Operation enthält
     */
    private void addTaskToQueue(Runnable task) {
        synchronized (dbTaskQueue) {
            dbTaskQueue.add(task); // Queue: Enqueue
            dbTaskQueue.notify();  // Weckt den Worker Thread auf
        }
    }

    /**
     * Startet einen Thread, der permanent die dbTaskQueue abarbeitet.
     * Verhindert das Blockieren der Benutzeroberfläche (UI-Freeze) bei langen DB-Abfragen
     */
    private void startBackgroundWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                Runnable task;
                synchronized (dbTaskQueue) {
                    while (dbTaskQueue.isEmpty()) {
                        try {
                            dbTaskQueue.wait(); // Wartet auf neue Aufgaben
                        } catch (InterruptedException e) { return; }
                    }
                    task = dbTaskQueue.poll(); // Queue: Dequeue (FIFO)
                }
                try {
                    task.run(); // Führt die DB-Operation aus
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        worker.setDaemon(true); // Thread stirbt, wenn Programm beendet wird
        worker.start();
    }
}
