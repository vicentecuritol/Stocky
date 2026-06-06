package com.cavi.stocky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// DTO que mapea la respuesta JSON que devuelve la API de Open Meteo
// la API devuelve los datos en este formato:
// {
//   "latitude": -33.45,
//   "longitude": -70.66,
//   "current_weather": {
//     "temperature": 18.5,
//     "windspeed": 12.3,
//     "weathercode": 1,
//     "time": "2024-01-15T14:00"
//   }
// }
@Data
public class ClimaResponseDto {

    private Double latitude;   // latitud de la ubicacion consultada
    private Double longitude;  // longitud de la ubicacion consultada

    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;// objeto con el clima actual, el nombre debe coincidir exactamente con el JSON

    // clase interna que representa el bloque current_weather del JSON
    @Data
    public static class CurrentWeather {
        private Double temperature;  // temperatura en grados celsius
        private Double windspeed;    // velocidad del viento en km/h
        private Integer weathercode; // codigo del estado del clima (1=despejado, 3=nublado, etc)
        private String time;         // hora de la medicion
    }
}
