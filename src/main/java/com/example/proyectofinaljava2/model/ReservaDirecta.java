package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Reserva registrada por la recepcion
public class ReservaDirecta extends Reserva {

    //Constructor
    public ReservaDirecta(String codigo, Huesped titular, Apartamento apartamento, LocalDate fechaEntrada,
                          LocalDate fechaSalida, List<Ocupante> listaOcupantes, Cotizacion cotizacion,
                          PoliticaCancelacion politica, LocalDateTime fechaCreacion) {
        super(codigo, titular, apartamento, fechaEntrada, fechaSalida, listaOcupantes, cotizacion,
                politica, fechaCreacion);
    }

    @Override
    public CanalReserva getCanal() {
        return CanalReserva.DIRECTA;
    }

    @Override
    public boolean puedeConfirmarse() {
        return true;
    }
}