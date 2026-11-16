package com.example.proyectofinaljava2.model;

import java.time.LocalDateTime;

//Pago del folio (no se modifica)
public record Pago(String descripcion, long valor, MedioPago medio, LocalDateTime fecha) {
}