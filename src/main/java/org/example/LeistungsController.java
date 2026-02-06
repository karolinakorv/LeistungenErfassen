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

public class LeistungsController {

    private final Stage stage;

    // Datenstruktur für Tabelle (noch ohne DB-Anbindung)
    private final ObservableList<Leistung> datenListe = FXCollections.observableArrayList();
    private final Queue<Runnable> dbTaskQueue = new LinkedList<>();

    private TableView<Leistung> tabelle;
    private TextField txtBez, txtPreis, txtKuerzel;

    public LeistungsController(Stage stage) {
        this.stage = stage;
        startBackgroundWorker();
    }

    public void anzeigeErstellen() {
        BorderPane root = new BorderPane();

        // Tabelle
        tabelle = new TableView<>(datenListe);
        tabelle.getColumns().add(spalteErstellen("ID", "id", 50));
        tabelle.getColumns().add(spalteErstellen("Bezeichnung", "bezeichnung", 200));
        tabelle.getColumns().add(spalteErstellen("Preis", "preis", 100));
        tabelle.getColumns().add(spalteErstellen("Kürzel", "kuerzel", 100));

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

        unten.getChildren().addAll(
                new Label("Leistungsverwaltung"),
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

    private <T> TableColumn<Leistung, T> spalteErstellen(String titel, String property, double breite) {
        TableColumn<Leistung, T> col = new TableColumn<>(titel);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(breite);
        return col;
    }

    private void fuelleFelder(Leistung l) {
        txtBez.setText(l.getBezeichnung());
        txtPreis.setText(String.valueOf(l.getPreis()));
        txtKuerzel.setText(l.getKuerzel());
    }
    
    private boolean validiereEingabe() {
        if (txtBez.getText().isEmpty() || txtKuerzel.getText().isEmpty()) return false;

        // Regex: Erlaubt z.B. "10", "10.5", "10.50"
        String preisRegex = "\\d+(\\.\\d+)?";
        return txtPreis.getText().matches(preisRegex);
    }

    // KONZEPT 4: Sortieren (Comparator Logik)
    private void aktionSortieren() {
        // Wir sortieren die JavaFX Liste direkt mit einem Comparator
        datenListe.sort((l1, l2) -> l1.getBezeichnung().compareToIgnoreCase(l2.getBezeichnung()));
    }

    // --- Datenbank Queue System ---

    private void ladeDaten() {
        addTaskToQueue(() -> {
            ArrayList<Leistung> liste = DB.alleLeistungenLaden();
            Platform.runLater(() -> {
                datenListe.setAll(liste);
                System.out.println("Daten geladen.");
            });
        });
    }

    private void aktionHinzufuegen() {
        if (!validiereEingabe()) return;
        Leistung neu = new Leistung(txtBez.getText(), Double.parseDouble(txtPreis.getText()), txtKuerzel.getText());

        addTaskToQueue(() -> {
            boolean ok = DB.leistungHinzufuegen(neu);
            if (ok) ladeDaten();
        });
        txtBez.clear(); txtPreis.clear(); txtKuerzel.clear();
    }

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

    private void aktionLoeschen() {
        Leistung auswahl = tabelle.getSelectionModel().getSelectedItem();
        if (auswahl == null) return;

        addTaskToQueue(() -> {
            boolean ok = DB.leistungLoeschen(auswahl.getId());
            if (ok) ladeDaten();
        });
    }

    private void addTaskToQueue(Runnable task) {
        synchronized (dbTaskQueue) {
            dbTaskQueue.add(task); // Queue: Enqueue
            dbTaskQueue.notify();  // Weckt den Worker Thread auf
        }
    }

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
