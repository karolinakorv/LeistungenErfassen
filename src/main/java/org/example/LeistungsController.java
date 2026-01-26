package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeistungsController {

    private final Stage stage;

    // Datenstruktur für Tabelle (noch ohne DB-Anbindung)
    private final ObservableList<Leistung> datenListe = FXCollections.observableArrayList();

    private TableView<Leistung> tabelle;
    private TextField txtBez, txtPreis, txtKuerzel;

    public LeistungsController(Stage stage) {
        this.stage = stage;
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

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(
                new Button("Hinzufügen"),
                new Button("Update"),
                new Button("Löschen"),
                new Button("Sortieren (A-Z)")
        );

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
}
