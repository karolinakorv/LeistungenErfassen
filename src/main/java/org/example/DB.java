package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DB{

    //Datenbank-Parameter
    private static final String DB_URL = "jdbc:mysql://10.25.2.145:3306/24chko";
    private static final String DB_USER = "24koka";
    private static final String DB_PASSWORD = "geb24";

    //Erstellt eine Verbindung zur Datenbank. return Connection-Objekt zur Datenbank//
    public static Connection verbindungHerstellen() {
        Connection verbindung = null;
        try {
            // JDBC Treiber laden
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Verbindung zur Datenbank herstellen
            verbindung = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Datenbankverbindung erfolgreich hergestellt!");

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Treiber nicht gefunden: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Fehler bei Datenbankverbindung: " + e.getMessage());
        }
        return verbindung;
    }

    //Lädt alle Leistungen aus der Datenbank. return ArrayList mit allen Leistungen//
    public static ArrayList<Leistung> alleLeistungenLaden() {
        ArrayList<Leistung> leistungsListe = new ArrayList<>();
        Connection verbindung = null;
        Statement statement = null;
        ResultSet ergebnis = null;

        try {
            verbindung = verbindungHerstellen();
            if (verbindung != null) {
                statement = verbindung.createStatement();
                String sqlAbfrage = "SELECT * FROM leistungen";
                ergebnis = statement.executeQuery(sqlAbfrage);

                //Durch alle Datensätze durchgehen
                while (ergebnis.next()) {
                    int id = ergebnis.getInt("id");
                    String bezeichnung = ergebnis.getString("bezeichnung");
                    double preis = ergebnis.getDouble("preis");
                    String kuerzel = ergebnis.getString("kuerzel");

                    Leistung leistung = new Leistung(id, bezeichnung, preis, kuerzel);
                    leistungsListe.add(leistung);
                }
                System.out.println(leistungsListe.size() + " Leistungen geladen.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Laden der Leistungen: " + e.getMessage());
        } finally {
            verbindungenSchliessen(verbindung, statement, ergebnis);
        }

        return leistungsListe;
    }

    //Speichert eine neue Leistung in der Datenbank
    public static boolean leistungHinzufuegen(Leistung leistung) {
        Connection verbindung = null;
        PreparedStatement preparedStatement = null;
        boolean erfolgreich = false;

        try {
            verbindung = verbindungHerstellen();
            if (verbindung != null) {
                String sqlBefehl = "INSERT INTO leistungen (bezeichnung, preis, kuerzel) VALUES (?, ?, ?)";
                preparedStatement = verbindung.prepareStatement(sqlBefehl);

                preparedStatement.setString(1, leistung.getBezeichnung());
                preparedStatement.setDouble(2, leistung.getPreis());
                preparedStatement.setString(3, leistung.getKuerzel());

                int zeilen = preparedStatement.executeUpdate();
                erfolgreich = (zeilen > 0);
                System.out.println("Leistung hinzugefügt: " + leistung.getBezeichnung());
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen der Leistung: " + e.getMessage());
        } finally {
            verbindungenSchliessen(verbindung, preparedStatement, null);
        }

        return erfolgreich;
    }

    //Aktualisiert eine bestehende Leistung in der Datenbank//
    public static boolean leistungAktualisieren(Leistung leistung) {
        Connection verbindung = null;
        PreparedStatement preparedStatement = null;
        boolean erfolgreich = false;

        try {
            verbindung = verbindungHerstellen();
            if (verbindung != null) {
                String sqlBefehl = "UPDATE leistungen SET bezeichnung=?, preis=?, kuerzel=? WHERE id=?";
                preparedStatement = verbindung.prepareStatement(sqlBefehl);

                preparedStatement.setString(1, leistung.getBezeichnung());
                preparedStatement.setDouble(2, leistung.getPreis());
                preparedStatement.setString(3, leistung.getKuerzel());
                preparedStatement.setInt(4, leistung.getId());

                int zeilen = preparedStatement.executeUpdate();
                erfolgreich = (zeilen > 0);
                System.out.println("Leistung aktualisiert: " + leistung.getBezeichnung());
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Aktualisieren der Leistung: " + e.getMessage());
        } finally {
            verbindungenSchliessen(verbindung, preparedStatement, null);
        }

        return erfolgreich;
    }

    //Löscht eine Leistung aus der Datenbank//
    public static boolean leistungLoeschen(int leistungsId) {
        Connection verbindung = null;
        PreparedStatement preparedStatement = null;
        boolean erfolgreich = false;

        try {
            verbindung = verbindungHerstellen();
            if (verbindung != null) {
                String sqlBefehl = "DELETE FROM leistungen WHERE id=?";
                preparedStatement = verbindung.prepareStatement(sqlBefehl);
                preparedStatement.setInt(1, leistungsId);

                int zeilen = preparedStatement.executeUpdate();
                erfolgreich = (zeilen > 0);
                System.out.println("Leistung mit ID " + leistungsId + " gelöscht.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen der Leistung: " + e.getMessage());
        } finally {
            verbindungenSchliessen(verbindung, preparedStatement, null);
        }

        return erfolgreich;
    }

    //Schließt alle Datenbankverbindungen ordnungsgemäß//
    private static void verbindungenSchliessen(Connection verbindung, Statement statement, ResultSet ergebnis) {
        try {
            if (ergebnis != null) {
                ergebnis.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (verbindung != null) {
                verbindung.close();
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Schließen der Verbindungen: " + e.getMessage());
        }
    }
}