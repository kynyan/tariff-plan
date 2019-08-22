package yota.homework.tariff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import yota.homework.tariff.config.DbConfig;
import yota.homework.tariff.config.SwaggerConfig;
import yota.homework.tariff.util.Merger;

@SpringBootApplication
@Import({DbConfig.class, SwaggerConfig.class, Merger.class})
public class TariffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TariffApplication.class, args);
	}

}
