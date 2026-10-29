package com.monja.reporteservice.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * One-time cleanup for local H2 databases created during development.
 * Reporte IDs are primary keys, so this migration is deliberately limited to H2
 * and records a marker after completion. Production PostgreSQL IDs are untouched.
 */
@Component
public class ReporteIdMigration implements ApplicationRunner {
    private static final String VERSION = "V1_COMPACT_LOCAL_REPORTE_IDS";

    private final JdbcTemplate jdbc;
    private final DataSource dataSource;

    public ReporteIdMigration(JdbcTemplate jdbc, DataSource dataSource) {
        this.jdbc = jdbc;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.getMetaData().getDatabaseProductName().toLowerCase().contains("h2")) {
                return;
            }
        }

        jdbc.execute("CREATE TABLE IF NOT EXISTS app_migrations (version VARCHAR(100) PRIMARY KEY)");
        Integer alreadyApplied = jdbc.queryForObject(
                "SELECT COUNT(*) FROM app_migrations WHERE version = ?", Integer.class, VERSION);
        if (alreadyApplied != null && alreadyApplied > 0) {
            return;
        }

        List<Long> ids = jdbc.query("SELECT id FROM reportes ORDER BY id",
                (result, row) -> result.getLong(1));

        // Move every current key out of the positive range first to avoid collisions
        // while compacting values such as 1, 2, 33, 34 into 1, 2, 3, 4.
        for (Long id : ids) {
            jdbc.update("UPDATE reportes SET id = ? WHERE id = ?", -id, id);
        }
        for (int index = 0; index < ids.size(); index++) {
            jdbc.update("UPDATE reportes SET id = ? WHERE id = ?",
                    index + 1L, -ids.get(index));
        }

        jdbc.execute("ALTER TABLE reportes ALTER COLUMN id RESTART WITH " + (ids.size() + 1));
        jdbc.update("INSERT INTO app_migrations(version) VALUES (?)", VERSION);

        if (ids.size() > 0) {
            System.out.println("Reporte IDs compacted for local H2: 1-" + ids.size()
                    + "; next ID is " + (ids.size() + 1));
        }
    }
}
