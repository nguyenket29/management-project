package com.hau.ketnguyen.it;

import com.hau.ketnguyen.it.config.propertise.ApplicationPropertise;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationPropertise.class})
public class ItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItApplication.class, args);
	}

}
