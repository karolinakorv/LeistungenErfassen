package org.example;

/**
 * Datenmodell für eine Krankenhausleistung.
 * Speichert die Informationen über eine medizinische Leistung.
 */
public class Leistung {

    private int id;
    private String bezeichnung;
    private double preis;
    private String kuerzel;

    //Standard-Konstruktor ohne Parameter//
    public Leistung() {
    }

    //Konstruktor mit allen Parametern außer ID//
    public Leistung(String bezeichnung, double preis, String kuerzel) {
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.kuerzel = kuerzel;
    }

    // Konstruktor mit allen Parametern inklusive ID//
    public Leistung(int id, String bezeichnung, double preis, String kuerzel) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.kuerzel = kuerzel;
    }

    // Getter und Setter Methoden
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

    //String-Darstellung der Leistung//
    @Override
    public String toString() {
        return "Leistung: " + bezeichnung + " (" + kuerzel + ") - " + preis + " EUR";
    }
}

