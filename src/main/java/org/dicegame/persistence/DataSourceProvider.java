package org.dicegame.persistence;

import javax.sql.DataSource;

import org.dicegame.config.Config;
import org.flywaydb.core.Flyway;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DataSourceProvider {
    private DataSourceProvider() {}

    private static volatile HikariDataSource ds;

    public static DataSource getDataSource() {
        if (ds == null) {
            synchronized (DataSourceProvider.class) {
                if (ds == null) {
                    ds = createDataSource();
                }
            }
        }
        return ds;
    }

    private static HikariDataSource createDataSource() {
        String url = Config.getDbUrl();
        String user = Config.getDbUser();
        String pass = Config.getDbPassword();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setMaximumPoolSize(Config.getDbPoolSize());
        cfg.setPoolName("dicegame-hikari");

        HikariDataSource hikari = new HikariDataSource(cfg);

        
        if (Config.isFlywayEnabled()) {
            try {
                Flyway flyway = Flyway.configure()
                        .dataSource(hikari)
                        .schemas(Config.getDbSchema())
                        .locations("classpath:db/migration")
                        .baselineOnMigrate(true)
                        .load();
                flyway.migrate();
            } catch (org.flywaydb.core.api.FlywayException ex) {
                System.out.println("[DB DEBUG] Flyway migration skipped or failed: " + ex.getMessage());
            } catch (NoClassDefFoundError | Exception ex) {
                System.out.println("[DB DEBUG] Flyway not available or migration error: " + ex.getMessage());
            }
        }

        return hikari;
    }
}

