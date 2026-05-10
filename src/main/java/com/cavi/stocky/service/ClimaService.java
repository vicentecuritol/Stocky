package com.cavi.stocky.service;

import com.cavi.stocky.dto.ClimaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

// service que se comunica con la API externa de Open Meteo para obtener el clima
// usamos WebClient que es el cliente HTTP reactivo de Spring, mas moderno que RestTemplate
@Service
public class ClimaService {

    @Autowired
    private WebClient weatherWebClient; // spring inyecta el WebClient que configuramos en WebClientConfig

    // consulta el clima actual para una latitud y longitud especifica
    // ejemplo: getClimaActual(-33.45, -70.66) trae el clima de Santiago
    public ClimaResponseDto getClimaActual(Double latitud, Double longitud) {

        return weatherWebClient
                .get() // tipo de peticion HTTP GET
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast") // ruta del endpoint en la API
                        .queryParam("latitude", latitud)   // parametro de latitud en la URL
                        .queryParam("longitude", longitud) // parametro de longitud en la URL
                        .queryParam("current_weather", true) // le pedimos que incluya el clima actual
                        .build()
                )
                .retrieve() // ejecuta la peticion y espera la respuesta
                .bodyToMono(ClimaResponseDto.class) // convierte el JSON de respuesta al DTO
                .block(); // espera a que llegue la respuesta antes de continuar (modo sincrono)
    }
}
