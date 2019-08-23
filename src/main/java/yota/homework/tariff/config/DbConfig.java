package yota.homework.tariff.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import yota.homework.tariff.util.PhysicalNamingStrategyImpl;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@ComponentScan({"yota.homework.tariff.model", "yota.homework.tariff.util"})
@EnableJpaRepositories("yota.homework.tariff.repository")
@Slf4j
public class DbConfig {

    @Value("${hibernate.dialect}") String hibernateDialect;
    @Value("${hibernate.show_sql:false}") boolean showSql;

    private final ApplicationContext appContext;
    private final Environment env;

    public DbConfig(ApplicationContext appContext, Environment env) {
        this.appContext = appContext;
        this.env = env;
    }

    /**
     * This looks stupid that instead of returning EntityManagerFactory we return its FactoryBean, but it appears
     * that it implements exception translation mechanism (to translate native Hibernate or JPA exceptions into
     * Spring's exceptions). So this factory not just returns the object that it creates, but also participates in
     * exception handling mechanism. This is probably a design mistake from Spring side, but we have to live with it.
     */

    @Bean
    PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(appContext.getBean(EntityManagerFactory.class));
        return txManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.implicit_naming_strategy", ImplicitNamingStrategyLegacyHbmImpl.class);
        props.put("hibernate.physical_naming_strategy", PhysicalNamingStrategyImpl.class);
        props.put("hibernate.id.new_generator_mappings", true);
        props.put("hibernate.dialect", hibernateDialect);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("yota.homework.tariff.model");
        factory.setDataSource(dataSource());
        factory.setJpaPropertyMap(props);
        return factory;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setDriverClassName(env.getProperty("db.driver_class_name"));
        dataSource.setMinimumIdle(env.getProperty("db.pool.min_size", int.class));
        dataSource.setMaximumPoolSize(env.getProperty("db.pool.max_size", int.class));
        /**
         * HikariCP mostly bypasses the connection validation check. If a connection has been used
         * within the last 1000ms, HikariCP will bypass the validation check automatically in getConnection()
         */
        dataSource.setValidationTimeout(env.getProperty("db.pool.validation_period_millisec", long.class));
        dataSource.setLeakDetectionThreshold(env.getProperty("db.pool.unreturned_connection_timeout_millisec", long.class));
        dataSource.setConnectionTimeout(env.getProperty("db.pool.connection_timeout_millisec", long.class));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Bean public Flyway flyway(@Value("${db.url}") String dbUrl, DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("classpath:/migration");
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
        return flyway;
    }

}
