package org.dicegame.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class DeDao {
    private final DataSource ds;

    public DeDao(DataSource ds) {
        this.ds = ds;
    }

    
    public int[] getLastRoll() throws Exception {
        final String sql = "SELECT de1, de2 FROM " + org.dicegame.config.Config.getDbSchema() + ".de ORDER BY created_at DESC LIMIT 1";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new int[] { rs.getInt(1), rs.getInt(2) };
                return new int[] {1,1};
            }
        }
    }

    
    public void insertRoll(Integer partieId, Integer tour, int de1, int de2, int somme, int pointsGagnes) throws Exception {
        final String insert = "INSERT INTO " + org.dicegame.config.Config.getDbSchema() + ".de (partie_id, tour, de1, de2, somme, pointsGagnes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(insert)) {
            if (partieId == null) ps.setNull(1, java.sql.Types.INTEGER); else ps.setInt(1, partieId);
            if (tour == null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, tour);
            ps.setInt(3, de1);
            ps.setInt(4, de2);
            ps.setInt(5, somme);
            ps.setInt(6, pointsGagnes);
            ps.executeUpdate();
        }
    }

    
    public int bindTransientRollsToPartie(long partieId, java.time.OffsetDateTime from, java.time.OffsetDateTime to) throws Exception {
        final String update = "UPDATE " + org.dicegame.config.Config.getDbSchema() + ".de SET partie_id = ? WHERE partie_id IS NULL AND created_at >= ? AND created_at <= ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(update)) {
            ps.setLong(1, partieId);
            ps.setObject(2, from);
            ps.setObject(3, to);
            return ps.executeUpdate();
        }
    }

    
    public java.util.List<org.dicegame.model.De> listDe() throws Exception {
        final String sql = "SELECT idDe, partie_id, tour, de1, de2, somme, pointsGagnes, created_at FROM " + org.dicegame.config.Config.getDbSchema() + ".de ORDER BY created_at DESC";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                java.util.List<org.dicegame.model.De> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    org.dicegame.model.De de = new org.dicegame.model.De();
                    de.setIdDe(rs.getLong("idDe"));
                    de.setPartieId(rs.getLong("partie_id"));
                    de.setTour(rs.getInt("tour"));
                    de.setDe1(rs.getInt("de1"));
                    de.setDe2(rs.getInt("de2"));
                    de.setSomme(rs.getInt("somme"));
                    de.setPointsGagnes(rs.getInt("pointsGagnes"));
                    de.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
                    list.add(de);
                }
                return list;
            }
        }
    }

   
    public java.util.List<org.dicegame.model.De> listDeForPartie(long partieId) throws Exception {
        final String sql = "SELECT idDe, partie_id, tour, de1, de2, somme, pointsGagnes, created_at FROM " + org.dicegame.config.Config.getDbSchema() + ".de WHERE partie_id = ? ORDER BY tour ASC";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, partieId);
            try (ResultSet rs = ps.executeQuery()) {
                java.util.List<org.dicegame.model.De> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    org.dicegame.model.De de = new org.dicegame.model.De();
                    de.setIdDe(rs.getLong("idDe"));
                    de.setPartieId(rs.getLong("partie_id"));
                    de.setTour(rs.getInt("tour"));
                    de.setDe1(rs.getInt("de1"));
                    de.setDe2(rs.getInt("de2"));
                    de.setSomme(rs.getInt("somme"));
                    de.setPointsGagnes(rs.getInt("pointsGagnes"));
                    de.setCreatedAt(rs.getObject("created_at", java.time.OffsetDateTime.class));
                    list.add(de);
                }
                return list;
            }
        }
    }
}
