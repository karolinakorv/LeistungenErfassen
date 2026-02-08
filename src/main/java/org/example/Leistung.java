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
    /** @return Die ID der Leistung. */
    public int getId() {
        return id;
    }

    /**
     * @param id Die neue ID der Leistung.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** @return Die Bezeichnung der Leistung. */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /** @param bezeichnung Die neue Bezeichnung der Leistung. */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /** @return Preis der Leistung. */
    public double getPreis() {
        return preis;
    }

    /** @param preis Der neue Preis der Leistung. */
    public void setPreis(double preis) {
        this.preis = preis;
    }

    /** @return Kürzel der Leistung. */
    public String getKuerzel() {
        return kuerzel;
    }

    /** @param kuerzel Das neue Kürzel der Leistung. */
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