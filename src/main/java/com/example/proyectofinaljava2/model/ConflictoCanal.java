package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Reserva externa rechazada porque chocaba con otra o repetia su codigo
public record ConflictoCanal(String codigoExterno, String plataforma, String codigoApartamento,
                             LocalDate fechaEntrada, LocalDate fechaSalida,
                             LocalDateTime fechaRegistro, String motivo) {
}