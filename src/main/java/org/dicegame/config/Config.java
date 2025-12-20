package org.dicegame.config;

public final class Config {
    private Config() {}

    private static String getenvOr(String key, String def) {
        String v = System.getenv(key);
        if (v != null && !v.isEmpty()) return v;
        v = System.getProperty(key);
        return (v != null && !v.isEmpty()) ? v : def;
    }

    public static String getDbUrl() {
        return getenvOr("DB_URL", "jdbc:postgresql://localhost:5432/dicegame");
    }
    public static String getDbUser() {
        return getenvOr("DB_USER", "postgres");
    }
    public static String getDbPassword() {
        return getenvOr("DB_PASSWORD", "");
    }
    public static String getDbSchema() {
        return getenvOr("DB_SCHEMA", "dicegame");
    }
    public static int getDbPoolSize() {
        String v = getenvOr("DB_POOL_SIZE", "5");
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return 5; }
    }
    public static String getScorePersistence() {
        return getenvOr("SCORE_PERSISTENCE", "DB");
    }
    public static boolean isFlywayEnabled() {
        return "true".equalsIgnoreCase(getenvOr("ENABLE_FLYWAY", "false"));
    }
}

