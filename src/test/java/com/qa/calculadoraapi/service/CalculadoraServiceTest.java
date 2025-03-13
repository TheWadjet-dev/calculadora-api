package com.qa.calculadoraapi.service;

import static org.junit.jupiter.api.Assertions.*;

import com.qa.calculadoraapi.exception.DivisionPorCeroException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

class CalculadoraServiceTest {

    private CalculadoraService calculadoraService;

    @BeforeEach
    void setUp() {
        calculadoraService = new CalculadoraServiceImpl();
    }

    @Test
    @DisplayName("Suma de dos números debería retornar el resultado correcto")
    void sumar_deberiaRetornarSumaCorrecta() {
        // Arrange
        double a = 5.0;
        double b = 3.0;
        double expectedResult = 8.0;
        // Act
        double actualResult = calculadoraService.sumar(a, b);

        assertEquals(expectedResult, actualResult, 0.0001, "5.0 + 3.0 debería ser 8.0");
    }

    @Test
    @DisplayName("Resta de dos números debería retornar el resultado correcto")
    void restar_deberiaRetornarRestaCorrecta() {
        // Arrange
        double a = 5.0;
        double b = 3.0;
        double expectedResult = 2.0;

        // Act
        double actualResult = calculadoraService.restar(a, b);

        // Assert
        assertEquals(expectedResult, actualResult, 0.0001, "5.0 - 3.0 debería ser 2.0");
    }

    @Test
    @DisplayName("Multiplicación de dos números debería retornar el resultado correcto")
    void multiplicar_deberiaRetornarMultiplicacionCorrecta() {
        // Arrange
        double a = 5.0;
        double b = 3.0;
        double expectedResult = 15.0;

        // Act
        double actualResult = calculadoraService.multiplicar(a, b);

        // Assert
        assertEquals(expectedResult, actualResult, 0.0001, "5.0 * 3.0 debería ser 15.0");
    }

    @Test
    @DisplayName("División de dos números debería retornar el resultado correcto")
    void dividir_deberiaRetornarDivisionCorrecta() {
        // Arrange
        double a = 6.0;
        double b = 3.0;
        double expectedResult = 2.0;

        // Act
        double actualResult = calculadoraService.dividir(a, b);

        // Assert
        assertEquals(expectedResult, actualResult, 0.0001, "6.0 / 3.0 debería ser 2.0");
    }

    @Test
    @DisplayName("División por cero debería lanzar DivisionPorCeroException")
    void dividir_conDivisorCero_deberiaLanzarException() {
        // Arrange
        double a = 5.0;
        double b = 0.0;

        // Act & Assert
        DivisionPorCeroException exception = assertThrows(
                DivisionPorCeroException.class,
                () -> calculadoraService.dividir(a, b),
                "Dividir por cero debería lanzar DivisionPorCeroException"
        );

        assertTrue(exception.getMessage().contains("No se puede dividir por cero"),
                "El mensaje de excepción debe mencionar la división por cero");
    }

    @ParameterizedTest
    @CsvSource({
            "10.0, 5.0, 15.0",
            "0.0, 0.0, 0.0",
            "-5.0, 5.0, 0.0",
            "-5.0, -5.0, -10.0"
    })
    @DisplayName("Suma con múltiples valores debería funcionar correctamente")
    void sumar_conMultiplesValores_deberiaFuncionarCorrectamente(double a, double b, double expectedResult) {
        double actualResult = calculadoraService.sumar(a, b);
        assertEquals(expectedResult, actualResult, 0.0001,
                () -> a + " + " + b + " debería ser " + expectedResult);
    }

    @Nested
    @DisplayName("Pruebas de operaciones con valores extremos")
    class OperacionesConValoresExtremos {

        private CalculadoraService calculadoraService;

        @BeforeEach
        void setUp() {
            calculadoraService = new CalculadoraServiceImpl();
        }

        @Test
        @DisplayName("Suma con valores muy grandes")
        void sumar_conValoresGrandes_deberiaFuncionar() {
            double a = Double.MAX_VALUE / 2;
            double b = Double.MAX_VALUE / 4;

            double resultado = calculadoraService.sumar(a, b);

            assertTrue(resultado > a, "El resultado debe ser mayor que a");
            assertTrue(resultado > b, "El resultado debe ser mayor que b");
        }

        @Test
        @DisplayName("Multiplicación con valores muy pequeños")
        void multiplicar_conValoresPequenos_deberiaFuncionar() {
            double a = Double.MIN_VALUE;
            double b = 2.0;

            double resultado = calculadoraService.multiplicar(a, b);

            assertEquals(a * b, resultado, 0.0000001,
                    "La multiplicación con valores muy pequeños debe ser precisa");
        }
    }

    @Nested
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Pruebas de rendimiento")
    class PruebasRendimientoCalculadora {

        private CalculadoraService calculadoraService = new CalculadoraServiceImpl();

        @Test
        @DisplayName("La suma debe completarse en menos de 100ms")
        void sumar_debeSerRapido() {
            for (int i = 0; i < 1000; i++) {
                calculadoraService.sumar(i, i + 1);
            }
        }
    }
}
