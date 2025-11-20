package PedroWattimo.Obligatorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import PedroWattimo.Obligatorio.models.subsistemas.SeedData;

@SpringBootApplication
public class ObligatorioApplication {

	public static void main(String[] args) {
		SeedData.cargar();

		SpringApplication.run(ObligatorioApplication.class, args);
	}

}
