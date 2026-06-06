package com.cavi.stocky.service;

import com.cavi.stocky.dto.ClimaResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ClimaService {

    private final RestClient restClient;

    public ClimaService(@Value("${openmeteo.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public ClimaResponseDto getClimaActual(Double latitud, Double longitud) {
        return restClient.get()
                .uri("/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true", latitud, longitud)
                .retrieve()
                .body(ClimaResponseDto.class);
    }
}
