package com.cavi.stocky.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
//configuracion del cliente GTPP para llamar a la API externa de clima
@Configuration //indica que esta clase tiene configuraciones que spring debe cargar
public class WebClientConfig {

    @Value("${openmeteo.base-url}")//lee la URL base desde application.properties
    private String openMeteoBaseUrl;

    @Bean// registra este objeto en spring para que se pueda inyectar en otros lados
    public WebClient weatherWebClient() {
        return WebClient.builder()
                .baseUrl(openMeteoBaseUrl)// define la URL base de la API del clima
                .defaultHeader("Accept", "application/json")//le dice que esperamos respuesta en JSON
                .build();
    }
}