package com.monja.reporteservice.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/** Configures the Render-managed PostgreSQL connection when its host is provided. */
@Configuration
@ConditionalOnProperty(name = "reportes.db.host")
public class RenderPostgresConfiguration {

    @Bean
    HikariDataSource dataSource(Environment environment) {
        String host = environment.getRequiredProperty("reportes.db.host");
        String port = environment.getProperty("reportes.db.port", "5432");
        String database = environment.getRequiredProperty("reportes.db.name");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
        dataSource.setUsername(environment.getRequiredProperty("reportes.db.user"));
        dataSource.setPassword(environment.getRequiredProperty("reportes.db.password"));
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
