package org.dicegame.persistence;

import org.dicegame.model.Joueur;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.dicegame.config.Config;

public class JoueurDao {
    private final DataSource ds;

    public JoueurDao(DataSource ds) {
        this.ds = ds;
    }

    // Sauvegarde basique : crée la table si besoin puis insert
    public void save(Joueur j) throws SQLException {
        final String schema = Config.getDbSchema();
        try (Connection c = ds.getConnection()) {
            // tentative de création de table (si le SGBD le permet)
            try (Statement st = c.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS " + schema + ".joueur (id SERIAL PRIMARY KEY, nom VARCHAR(100) NOT NULL, prenom VARCHAR(100) NOT NULL, created_at TIMESTAMP WITH TIME ZONE DEFAULT now())");
            } catch (SQLException ignored) {}

            String sql = "INSERT INTO " + schema + ".joueur (nom, prenom) VALUES (?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, j.getNomJoueur());
                ps.setString(2, j.getPrenomJoueur());
                ps.executeUpdate();
            }
        }
    }

    // Insère le joueur si absent et retourne son id
    public Long insertIfNotExists(String nom, String prenom) throws SQLException {
        final String schema = Config.getDbSchema();
        final String select = "SELECT id FROM " + schema + ".joueur WHERE nom = ? AND prenom = ?";
        final String insert = "INSERT INTO " + schema + ".joueur (nom, prenom) VALUES (?, ?)";
        try (Connection c = ds.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(select)) {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                }
            }
            try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getLong(1);
                }
            }
        }
        return null;
    }

    // Récupère tous les joueurs (si table absente renvoie liste vide)
    public List<Joueur> findAll() {
        final String schema = Config.getDbSchema();
        List<Joueur> res = new ArrayList<>();
        final String sql = "SELECT nom, prenom FROM " + schema + ".joueur";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Joueur j = new Joueur(rs.getString(1), rs.getString(2));
                res.add(j);
            }
        } catch (SQLException ignored) {
            // table manquante ou autre -> renvoyer liste vide
        }
        return res;
    }
}

