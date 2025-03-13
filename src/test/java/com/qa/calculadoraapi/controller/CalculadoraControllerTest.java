package com.qa.calculadoraapi.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qa.calculadoraapi.exception.DivisionPorCeroException;
import com.qa.calculadoraapi.service.CalculadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CalculadoraController.class)
class CalculadoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculadoraService calculadoraService;

    @BeforeEach
    void setUp() {
        when(calculadoraService.sumar(5.0, 3.0)).thenReturn(8.0);
        when(calculadoraService.restar(5.0, 3.0)).thenReturn(2.0);
        when(calculadoraService.multiplicar(5.0, 3.0)).thenReturn(15.0);
        when(calculadoraService.dividir(6.0, 3.0)).thenReturn(2.0);
        when(calculadoraService.dividir(5.0, 0.0)).thenThrow(
                new DivisionPorCeroException("No se puede dividir por cero"));
    }

    @Test
    @DisplayName("GET /api/calculadora/sumar debería retornar respuesta 200 con resultado correcto")
    void sumar_deberiaRetornarRespuestaCorrecta() throws Exception {
        mockMvc.perform(get("/api/calculadora/sumar")
                        .param("a", "5.0")
                        .param("b", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(8.0))
                .andExpect(jsonPath("$.operacion").value("+"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(calculadoraService, times(1)).sumar(5.0, 3.0);
    }

    @Test
    @DisplayName("GET /api/calculadora/restar debería retornar respuesta 200 con resultado correcto")
    void restar_deberiaRetornarRespuestaCorrecta() throws Exception {
        mockMvc.perform(get("/api/calculadora/restar")
                        .param("a", "5.0")
                        .param("b", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(2.0))
                .andExpect(jsonPath("$.operacion").value("-"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(calculadoraService, times(1)).restar(5.0, 3.0);
    }

    @Test
    @DisplayName("GET /api/calculadora/multiplicar debería retornar respuesta 200 con resultado correcto")
    void multiplicar_deberiaRetornarRespuestaCorrecta() throws Exception {
        mockMvc.perform(get("/api/calculadora/multiplicar")
                        .param("a", "5.0")
                        .param("b", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(15.0))
                .andExpect(jsonPath("$.operacion").value("*"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(calculadoraService, times(1)).multiplicar(5.0, 3.0);
    }

    @Test
    @DisplayName("GET /api/calculadora/dividir debería retornar respuesta 200 con resultado correcto")
    void dividir_deberiaRetornarRespuestaCorrecta() throws Exception {
        mockMvc.perform(get("/api/calculadora/dividir")
                        .param("a", "6.0")
                        .param("b", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(2.0))
                .andExpect(jsonPath("$.operacion").value("/"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(calculadoraService, times(1)).dividir(6.0, 3.0);
    }

    @Test
    @DisplayName("GET /api/calculadora/dividir con divisor cero debería retornar respuesta 400 con error")
    void dividir_conDivisorCero_deberiaRetornarRespuestaError() throws Exception {
        mockMvc.perform(get("/api/calculadora/dividir")
                        .param("a", "5.0")
                        .param("b", "0.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No se puede dividir por cero"))
                .andExpect(jsonPath("$.status").value("ERROR"));

        verify(calculadoraService, times(1)).dividir(5.0, 0.0);
    }
}
