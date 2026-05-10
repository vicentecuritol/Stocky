package com.cavi.stocky.controller;

import com.cavi.stocky.dto.ClimaResponseDto;
import com.cavi.stocky.service.ClimaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// controller que expone el endpoint para consultar el clima
// llama al ClimaService que internamente llama a la API de Open Meteo
@RestController
@RequestMapping("/api/v1/clima")
@AllArgsConstructor
public class ClimaController {

    private final ClimaService climaService;

    // GET /api/v1/clima?latitud=-33.45&longitud=-70.66
    // los parametros latitud y longitud van en la URL como query params
    // ejemplo en Postman: GET http://localhost:8080/api/v1/clima?latitud=-33.45&longitud=-70.66
    @GetMapping
    public ResponseEntity<ClimaResponseDto> getClima(
            @RequestParam Double latitud,  // @RequestParam toma el valor de ?latitud= en la URL
            @RequestParam Double longitud) {
        ClimaResponseDto clima = climaService.getClimaActual(latitud, longitud);
        return ResponseEntity.ok(clima);
    }

    // GET /api/v1/clima/santiago - acceso rapido al clima de Santiago sin tener que poner coordenadas
    @GetMapping("/santiago")
    public ResponseEntity<ClimaResponseDto> getClimaSantiago() {
        ClimaResponseDto clima = climaService.getClimaActual(-33.45, -70.67); // coordenadas de Santiago
        return ResponseEntity.ok(clima);
    }
}
