package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Rango de fechas (inclusivo) en que un apartamento no se puede vender
public record Bloqueo(LocalDate fechaInicio, LocalDate fechaFin, String motivo) {

    //Dice si la noche de esa fecha esta bloqueada
    public boolean incluyeNoche(LocalDate noche) {
        return !noche.isBefore(fechaInicio) && !noche.isAfter(fechaFin);
    }
}