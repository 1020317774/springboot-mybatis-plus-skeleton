package com.knox.uranus.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Flyway配置
 *
 * @author knox
 * @date 2020-12-27
 */
@Configuration
public class FlywayConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration", "filesystem:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        logger.info("flyway update successful");
    }
}
