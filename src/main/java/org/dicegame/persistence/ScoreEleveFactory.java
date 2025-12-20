package org.dicegame.persistence;

import org.dicegame.model.ScoreEleve;

public final class ScoreEleveFactory {
    private ScoreEleveFactory() {}

    public static ScoreEleve create() {
        String mode = org.dicegame.config.Config.getScorePersistence();
        if ("DB".equalsIgnoreCase(mode)) {
            try {
                javax.sql.DataSource ds = DataSourceProvider.getDataSource();
                // Lancer Flyway si activé
                if (org.dicegame.config.Config.isFlywayEnabled()) {
                    try {
                        org.flywaydb.core.Flyway flyway = org.flywaydb.core.Flyway.configure()
                                .dataSource(ds)
                                .schemas(org.dicegame.config.Config.getDbSchema())
                                .load();
                        flyway.migrate();
                    } catch (NoClassDefFoundError | Exception ex) {
                        // Flyway non présent ou migration échouée -> on continue sans migration
                        ex.printStackTrace();
                    }
                }
                return new PersistentScoreEleve(ds);
            } catch (Exception e) {
                e.printStackTrace();
                // fallback
                return new ScoreEleve();
            }
        }
        return new ScoreEleve();
    }
}

