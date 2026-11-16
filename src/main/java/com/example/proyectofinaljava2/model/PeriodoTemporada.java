package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Rango de fechas (inclusivo) que pertenece a una temporada media o alta
public record PeriodoTemporada(TipoTemporada tipo, LocalDate fechaInicio, LocalDate fechaFin) {

    //Dice si la fecha cae dentro del periodo
    public boolean contiene(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
}