import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import db.DatabaseConnection;


class Main{
    public static void main(String[] args) {
        System.out.println("--- Test de Connexion JDBC vers PostgreSQL ---");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // 1. Appel de votre Singleton pour obtenir la connexion
            if (conn != null && !conn.isClosed()) {
                System.out.println(" SUCCÈS : Connexion établie avec la base de données !");

                // 2. Exécution d'une requête SQL de test pour vérifier l'accès aux données
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT current_database(), current_user")) {

                    if (rs.next()) {
                        System.out.println(" Base de données connectée : " + rs.getString(1));
                        System.out.println(" Utilisateur connecté : " + rs.getString(2));
                    }
                }

                // 3. Vérification de la présence des tables que vous avez créées
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT count(*) FROM rooms")) {
                    if (rs.next()) {
                        System.out.println("  Nombre de chambres actuellement dans la table 'rooms' : " + rs.getInt(1));
                    }
                }

            } else {
                System.out.println(" ÉCHEC : La connexion est nulle ou fermée.");
            }

        } catch (Exception e) {
            System.err.println(" ÉCHEC CRITIQUE : Impossible de se connecter à la base de données.");
            e.printStackTrace();
        }

    }
}