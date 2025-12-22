package org.dicegame.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dicegame.config.Config;
import org.dicegame.model.Joueur;

public class JoueurDao {
    private final DataSource ds;

    public JoueurDao(DataSource ds) {
        this.ds = ds;
    }

    public void save(Joueur j) throws SQLException {
        final String schema = Config.getDbSchema();
        try (Connection c = ds.getConnection()) {
            try (Statement st = c.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS " + schema + ".joueur (\"idJoueur\" SERIAL PRIMARY KEY, \"nomJoueur\" VARCHAR(100) NOT NULL, \"prenomJoueur\" VARCHAR(100) NOT NULL, \"scoreJoueur\" INTEGER DEFAULT 0, created_at TIMESTAMP WITH TIME ZONE DEFAULT now())");
            } catch (SQLException ignored) {}

            String sql = "INSERT INTO " + schema + ".joueur (\"nomJoueur\", \"prenomJoueur\", \"scoreJoueur\") VALUES (?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, j.getNomJoueur());
                ps.setString(2, j.getPrenomJoueur());
                ps.setInt(3, j.getScoreJoueur());
                ps.executeUpdate();
            }
        }
    }

    public Long insertIfNotExists(String nom, String prenom) throws SQLException {
        final String schema = Config.getDbSchema();
        final String select = "SELECT \"idJoueur\" FROM " + schema + ".joueur WHERE \"nomJoueur\" = ? AND \"prenomJoueur\" = ?";
        final String insert = "INSERT INTO " + schema + ".joueur (\"nomJoueur\", \"prenomJoueur\", \"scoreJoueur\") VALUES (?, ?, ?)";
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
                ps.setInt(3, 0);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getLong(1);
                }
            }
        }
        return null;
    }

    public List<Joueur> findAll() {
        final String schema = Config.getDbSchema();
        List<Joueur> res = new ArrayList<>();
        final String sql = "SELECT \"nomJoueur\", \"prenomJoueur\", \"scoreJoueur\" FROM " + schema + ".joueur";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Joueur j = new Joueur(rs.getString(1), rs.getString(2));
                j.Majscore(rs.getInt(3));
                res.add(j);
            }
        } catch (SQLException ignored) {
        }
        return res;
    }
}

