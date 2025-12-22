package org.dicegame.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

public class JoueurDaoTest {

    private DataSource ds;
    private JoueurDao joueurDao;

    @BeforeEach
    public void setUp() throws Exception {
        HikariDataSource hds = new HikariDataSource();
        hds.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        hds.setUsername("sa");
        hds.setPassword("");
        ds = hds;

        // Create schema and tables
        try (var c = ds.getConnection(); var ps = c.prepareStatement("""
            CREATE SCHEMA dicegame;
            CREATE TABLE dicegame.joueur (
                "idJoueur" BIGINT AUTO_INCREMENT PRIMARY KEY,
                "nomJoueur" VARCHAR(100) NOT NULL,
                "prenomJoueur" VARCHAR(100) NOT NULL,
                "scoreJoueur" INTEGER DEFAULT 0
            );
            """)) {
            ps.execute();
        }

        joueurDao = new JoueurDao(ds);
    }

    @Test
    public void testInsertIfNotExists() throws Exception {
        Long id1 = joueurDao.insertIfNotExists("Doe", "John");
        assertNotNull(id1);

        Long id2 = joueurDao.insertIfNotExists("Doe", "John");
        assertEquals(id1, id2); // Should return same id
    }
}