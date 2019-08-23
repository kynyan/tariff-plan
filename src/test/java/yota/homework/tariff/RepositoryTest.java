package yota.homework.tariff;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import yota.homework.tariff.config.DbConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Configuration
@Transactional
@ContextConfiguration(classes = DbConfig.class)
//@PropertySource("classpath:application.properties")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryTest {
}
