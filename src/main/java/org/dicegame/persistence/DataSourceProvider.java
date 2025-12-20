package org.dicegame.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dicegame.config.Config;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.sql.SQLException;

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

        // Run Flyway migrations if enabled
        if (Config.isFlywayEnabled()) {
            Flyway flyway = Flyway.configure()
                    .dataSource(hikari)
                    .schemas(Config.getDbSchema())
                    .locations("classpath:db/migration")
                    .load();
            flyway.migrate();
        }

        return hikari;
    }
}

