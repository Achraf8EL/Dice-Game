package org.dicegame.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dicegame.model.Saisie;

public class SaisieDao {
    private final DataSource ds;

    public SaisieDao(DataSource ds) {
        this.ds = ds;
    }

    public long insert(String nomAffiche, int score, Long joueurId) {
        final String sql = "INSERT INTO " + org.dicegame.config.Config.getDbSchema() + ".saisie (joueur_id, \"nomSaisie\", \"scoreSaisie\") VALUES (?, ?, ?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (joueurId == null) ps.setNull(1, Types.BIGINT); else ps.setLong(1, joueurId);
            ps.setString(2, nomAffiche);
            ps.setInt(3, score);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("No generated key for saisie insert");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Saisie> listTop(int limit) {
        final String sql = "SELECT \"nomSaisie\", \"scoreSaisie\" FROM " + org.dicegame.config.Config.getDbSchema() + ".saisie ORDER BY \"scoreSaisie\" DESC LIMIT ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Saisie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new Saisie(rs.getString("nomSaisie"), rs.getInt("scoreSaisie")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Saisie> listAll() {
        final String sql = "SELECT \"nomSaisie\", \"scoreSaisie\" FROM " + org.dicegame.config.Config.getDbSchema() + ".saisie ORDER BY \"scoreSaisie\" DESC";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Saisie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new Saisie(rs.getString("nomSaisie"), rs.getInt("scoreSaisie")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

