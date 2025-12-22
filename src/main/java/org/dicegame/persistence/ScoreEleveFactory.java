package org.dicegame.persistence;

import org.dicegame.model.ScoreEleve;

public final class ScoreEleveFactory {
    private ScoreEleveFactory() {}

    public static ScoreEleve create() {
        String mode = org.dicegame.config.Config.getScorePersistence();
        System.out.println("[DB DEBUG] Score persistence mode = " + mode);
        if ("DB".equalsIgnoreCase(mode)) {
            try {
                javax.sql.DataSource ds = DataSourceProvider.getDataSource();
                if (org.dicegame.config.Config.isFlywayEnabled()) {
                    try {
                        org.flywaydb.core.Flyway flyway = org.flywaydb.core.Flyway.configure()
                                .dataSource(ds)
                                .schemas(org.dicegame.config.Config.getDbSchema())
                                .load();
                        flyway.migrate();
                    } catch (NoClassDefFoundError | Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return new PersistentScoreEleve(ds);
            } catch (Exception e) {
                e.printStackTrace();
                return new ScoreEleve();
            }
        }
        return new ScoreEleve();
    }
}

