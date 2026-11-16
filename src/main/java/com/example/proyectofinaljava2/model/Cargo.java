package com.example.proyectofinaljava2.model;

import java.time.LocalDateTime;

//Cargo del folio (no se modifica)
public record Cargo(String descripcion, TipoCargo tipo, long valor, LocalDateTime fecha) {
}