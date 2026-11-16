package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Una noche del desglose de una cotizacion
public record NocheCotizada(LocalDate noche, TipoTemporada temporada, long tarifaPorPersona,
                            int personasQuePagan, long valor) {
}