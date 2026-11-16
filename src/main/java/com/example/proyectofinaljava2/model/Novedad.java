package com.example.proyectofinaljava2.model;

import java.time.LocalDateTime;

//Dano, faltante o situacion reportada en un apartamento
public record Novedad(LocalDateTime fecha, String descripcion, String reportadoPor) {
}