package com.cavi.stocky;

import com.cavi.stocky.model.Categoria;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class StockyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockyApplication.class, args);
	}

}
