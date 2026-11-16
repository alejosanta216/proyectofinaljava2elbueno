package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.Period;

//Persona que se aloja en la reserva
public record Ocupante(String nombre, LocalDate fechaNacimiento) {

    //Edad minima para pagar la tarifa
    public static final int EDAD_MINIMA_PAGO = 6;

    //Calcula la edad cumplida en una fecha dada
    public int calcularEdad(LocalDate fecha) {
        return Period.between(fechaNacimiento, fecha).getYears();
    }

    //Los menores de 6 anios a la fecha de entrada no pagan
    public boolean pagaTarifa(LocalDate fechaEntrada) {
        return calcularEdad(fechaEntrada) >= EDAD_MINIMA_PAGO;
    }
}