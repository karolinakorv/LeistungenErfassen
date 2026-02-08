package org.example;


//Speichert die Informationen über eine medizinische Leistung//
/**
 * Repräsentiert eine medizinische Leistung im System.
 * Diese Klasse dient für die Speicherung und Anzeige von Leistungsinformationen wie
 * Bezeichnung, Preis und Kürzel.
 */
public class Leistung {

    private int id;
    private String bezeichnung;
    private double preis;
    private String kuerzel;

    /**
     * Standard-Konstruktor ohne Parameter.
     */
    public Leistung() {
    }

    /**
     * Konstruktor zum Erstellen einer neuen Leistung (noch ohne Datenbank-ID).
     * @param bezeichnung Die Bezeichnung der Leistung.
     * @param preis Der Preis der Leistung.
     * @param kuerzel Das Kürzel der Leistung.
     */
    public Leistung(String bezeichnung, double preis, String kuerzel) {
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.kuerzel = kuerzel;
    }

    /**
     * Konstruktor zum Laden einer existierenden Leistung mit bekannter ID.
     * @param id Die ID aus der Datenbank.
     * @param bezeichnung Die Bezeichnung der Leistung.
     * @param preis Der Preis der Leistung.
     * @param kuerzel Das Kürzel der Leistung.
     */
    public Leistung(int id, String bezeichnung, double preis, String kuerzel) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.kuerzel = kuerzel;
    }

    //Getter und Setter Methoden
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    /**
     * Gibt eine textuelle Zusammenfassung der Leistung zurück, als String.
     * @return Ein String im Format "Leistung: Name (Kürzel) - Preis EUR".
     */
    @Override
    public String toString() {
        return "Leistung: " + bezeichnung + " (" + kuerzel + ") - " + preis + " EUR";
    }
}