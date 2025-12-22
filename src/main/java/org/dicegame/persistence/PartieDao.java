package org.dicegame.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class PartieDao {
    private final DataSource ds;

    public PartieDao(DataSource ds) {
        this.ds = ds;
    }

    public void updatePartieFinished(long partieId, int scoreFinal) throws Exception {
        final String sql = "UPDATE " + org.dicegame.config.Config.getDbSchema() + ".partie SET scoreFinal = ?, finished_at = now() WHERE \"idPartie\" = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, scoreFinal);
            ps.setLong(2, partieId);
            ps.executeUpdate();
        }
    }

    public long insertPartie(int nbTours, int pointsSi7, Long joueurId, int scoreFinal) throws Exception {
        final String sql = "INSERT INTO " + org.dicegame.config.Config.getDbSchema() + ".partie (nbTours, pointsSi7, joueur_id, scoreFinal) VALUES (?, ?, ?, ?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, nbTours);
            ps.setInt(2, pointsSi7);
            if (joueurId == null) ps.setNull(3, java.sql.Types.BIGINT); else ps.setLong(3, joueurId);
            ps.setInt(4, scoreFinal);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new Exception("No generated key for partie insert");
            }
        }
    }

    public List<PartieSummary> listAll() throws Exception {
        final String sql = "SELECT \"idPartie\", nbTours, pointsSi7, joueur_id, scoreFinal, started_at, finished_at FROM " + org.dicegame.config.Config.getDbSchema() + ".partie ORDER BY started_at DESC";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<PartieSummary> res = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("idPartie");
                int nbTours = rs.getInt("nbTours");
                int pointsSi7 = rs.getInt("pointsSi7");
                Long joueurId = rs.getObject("joueur_id") == null ? null : rs.getLong("joueur_id");
                int scoreFinal = rs.getInt("scoreFinal");
                OffsetDateTime started = rs.getObject("started_at", OffsetDateTime.class);
                OffsetDateTime finished = rs.getObject("finished_at", OffsetDateTime.class);
                res.add(new PartieSummary(id, nbTours, pointsSi7, joueurId, scoreFinal, started, finished));
            }
            return res;
        }
    }

    public List<PartieWithPlayer> listAllWithPlayerName() throws Exception {
        final String sql = "SELECT p.\"idPartie\", p.nbTours, p.pointsSi7, p.joueur_id, j.\"nomJoueur\", j.\"prenomJoueur\", p.scoreFinal, p.started_at, p.finished_at FROM " + org.dicegame.config.Config.getDbSchema() + ".partie p LEFT JOIN " + org.dicegame.config.Config.getDbSchema() + ".joueur j ON p.joueur_id = j.\"idJoueur\" ORDER BY p.started_at DESC";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<PartieWithPlayer> res = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong(1);
                int nbTours = rs.getInt(2);
                int pointsSi7 = rs.getInt(3);
                Long joueurId = rs.getObject(4) == null ? null : rs.getLong(4);
                String nom = rs.getString(5);
                String prenom = rs.getString(6);
                int scoreFinal = rs.getInt(7);
                java.time.OffsetDateTime started = rs.getObject(8, java.time.OffsetDateTime.class);
                java.time.OffsetDateTime finished = rs.getObject(9, java.time.OffsetDateTime.class);
                res.add(new PartieWithPlayer(id, nbTours, pointsSi7, joueurId, nom, prenom, scoreFinal, started, finished));
            }
            return res;
        }
    }

    public List<org.dicegame.model.Lancer> listLancers(long partieId) throws Exception {
        final String sql = "SELECT idDe as id, tour, de1, de2, somme, pointsGagnes, created_at FROM " + org.dicegame.config.Config.getDbSchema() + ".de WHERE partie_id = ? ORDER BY tour";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, partieId);
            try (ResultSet rs = ps.executeQuery()) {
                List<org.dicegame.model.Lancer> res = new ArrayList<>();
                while (rs.next()) {
                    int tour = rs.getInt("tour");
                    int d1 = rs.getInt("de1");
                    int d2 = rs.getInt("de2");
                    int points = rs.getInt("pointsGagnes");
                    res.add(new org.dicegame.model.Lancer(tour, d1, d2, points));
                }
                return res;
            }
        }
    }

    public long insertLancer(long partieId, int tour, int de1, int de2, int somme, int pointsGagnes) throws Exception {
        final String sql = "INSERT INTO " + org.dicegame.config.Config.getDbSchema() + ".de (partie_id, tour, de1, de2, somme, pointsGagnes) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection c = ds.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, partieId);
            ps.setInt(2, tour);
            ps.setInt(3, de1);
            ps.setInt(4, de2);
            ps.setInt(5, somme);
            ps.setInt(6, pointsGagnes);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new Exception("No generated key for lancer insert");
            }
        }
    }
}
