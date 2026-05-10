package com.cavi.stocky;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// clase principal del proyecto, desde aca arranca todo
@SpringBootApplication // le dice a spring que escanee y configure toda la aplicacion
public class StockyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockyApplication.class, args);
	}// enciende el servidor

}
