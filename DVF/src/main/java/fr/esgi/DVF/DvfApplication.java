package fr.esgi.DVF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class DvfApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvfApplication.class, args);
	}

}
