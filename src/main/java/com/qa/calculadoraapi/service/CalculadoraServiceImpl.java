package com.qa.calculadoraapi.service;

import com.qa.calculadoraapi.exception.DivisionPorCeroException;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraServiceImpl implements CalculadoraService {

    @Override
    public double sumar(double a, double b) {
        return a + b;
    }

    @Override
    public double restar(double a, double b) {
        return a - b;
    }

    @Override
    public double multiplicar(double a, double b) {
        return a * b;
    }

    @Override
    public double dividir(double a, double b) {
        if (b == 0) {
            throw new DivisionPorCeroException("No se puede dividir por cero");
        }
        return a / b;
    }
}
