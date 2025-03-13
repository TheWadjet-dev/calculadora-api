package com.qa.calculadoraapi.controller;

import com.qa.calculadoraapi.exception.DivisionPorCeroException;
import com.qa.calculadoraapi.service.CalculadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calculadora")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    @Autowired
    public CalculadoraController(CalculadoraService calculadoraService) {
        this.calculadoraService = calculadoraService;
    }

    @GetMapping("/sumar")
    public ResponseEntity<Map<String, Object>> sumar(
            @RequestParam("a") double a,
            @RequestParam("b") double b) {

        double resultado = calculadoraService.sumar(a, b);
        return crearRespuesta(a, b, "+", resultado);
    }

    @GetMapping("/restar")
    public ResponseEntity<Map<String, Object>> restar(
            @RequestParam("a") double a,
            @RequestParam("b") double b) {

        double resultado = calculadoraService.restar(a, b);
        return crearRespuesta(a, b, "-", resultado);
    }

    @GetMapping("/multiplicar")
    public ResponseEntity<Map<String, Object>> multiplicar(
            @RequestParam("a") double a,
            @RequestParam("b") double b) {

        double resultado = calculadoraService.multiplicar(a, b);
        return crearRespuesta(a, b, "*", resultado);
    }

    @GetMapping("/dividir")
    public ResponseEntity<Map<String, Object>> dividir(
            @RequestParam("a") double a,
            @RequestParam("b") double b) {

        try {
            double resultado = calculadoraService.dividir(a, b);
            return crearRespuesta(a, b, "/", resultado);
        } catch (DivisionPorCeroException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Map<String, Object>> crearRespuesta(
            double a, double b, String operacion, double resultado) {

        Map<String, Object> response = new HashMap<>();
        response.put("operando1", a);
        response.put("operando2", b);
        response.put("operacion", operacion);
        response.put("resultado", resultado);
        response.put("status", "SUCCESS");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcion(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error al procesar la solicitud: " + e.getMessage());
        response.put("status", "ERROR");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
