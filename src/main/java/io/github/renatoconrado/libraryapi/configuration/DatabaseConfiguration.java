package io.github.renatoconrado.libraryapi.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public @Configuration class DatabaseConfiguration {
    private static final int MIN_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 10;
    private static final long MAX_LIFETIME = MINUTES.toMillis(10);
    private static final long MAX_CONNECTION_TIMEOUT = SECONDS.toMillis(60);

    private @Value("${spring.datasource.url}") String url;
    private @Value("${spring.datasource.username}") String username;
    private @Value("${spring.datasource.password}") String password;
    private @Value("${spring.datasource.driver-class-name}") String driver;

//        DataSource Simples
//        return new DriverManagerDataSource(url, username, password);

    public @Bean DataSource hikariDataSource() {
        log.info("Connected to DataBase URL: {}", url);

        var config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMinimumIdle(MIN_POOL_SIZE);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setPoolName("library-db-pool");

        config.setMaxLifetime(MAX_LIFETIME);
        config.setConnectionTimeout(MAX_CONNECTION_TIMEOUT);
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }
}
