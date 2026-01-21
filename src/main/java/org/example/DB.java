package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    private static final String DB_URL = "jdbc:mysql://10.25.2.145:3306/24chko";
    //Holt die sicheren Login-Daten aus IntelliJ/PC-Einstellungen
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    //Verbindung holen
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static ArrayList<Leistung> alleLeistungenLaden() {
        ArrayList<Leistung> leistungsListe = new ArrayList<>();
        String sqlAbfrage = "SELECT * FROM leistungen";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlAbfrage)) {

            while (resultSet.next()) {
                leistungsListe.add(new Leistung(
                        resultSet.getInt("id"),
                        resultSet.getString("bezeichnung"),
                        resultSet.getDouble("preis"),
                        resultSet.getString("kuerzel")
                ));
            }
            System.out.println(leistungsListe.size() + " Leistungen geladen.");

        } catch (SQLException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }
        return leistungsListe;
    }

    public static boolean leistungHinzufuegen(Leistung leistung) {
        String sqlBefehl = "INSERT INTO leistungen (bezeichnung, preis, kuerzel) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBefehl)) {

            preparedStatement.setString(1, leistung.getBezeichnung());
            preparedStatement.setDouble(2, leistung.getPreis());
            preparedStatement.setString(3, leistung.getKuerzel());

            int zeilen = preparedStatement.executeUpdate();
            System.out.println("Leistung hinzugefügt.");
            return zeilen > 0;

        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen: " + e.getMessage());
            return false;
        }
    }

    public static boolean leistungAktualisieren(Leistung leistung) {
        String sqlBefehl = "UPDATE leistungen SET bezeichnung=?, preis=?, kuerzel=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBefehl)) {

            preparedStatement.setString(1, leistung.getBezeichnung());
            preparedStatement.setDouble(2, leistung.getPreis());
            preparedStatement.setString(3, leistung.getKuerzel());
            preparedStatement.setInt(4, leistung.getId());

            int zeilen = preparedStatement.executeUpdate();
            System.out.println("Leistung aktualisiert.");
            return zeilen > 0;

        } catch (SQLException e) {
            System.out.println("Fehler beim Aktualisieren: " + e.getMessage());
            return false;
        }
    }

    public static boolean leistungLoeschen(int id) {
        String sqlBefehl = "DELETE FROM leistungen WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBefehl)) {

            preparedStatement.setInt(1, id);

            int zeilen = preparedStatement.executeUpdate();
            System.out.println("Leistung gelöscht.");
            return zeilen > 0;

        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen: " + e.getMessage());
            return false;
        }
    }
}