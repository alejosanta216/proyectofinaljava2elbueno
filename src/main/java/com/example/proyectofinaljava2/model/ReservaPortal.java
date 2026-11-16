package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Reserva hecha por el huesped desde la aplicacion
public class ReservaPortal extends Reserva {
    public static final int PORCENTAJE_ANTICIPO = 30;

    //Constructor
    public ReservaPortal(String codigo, Huesped titular, Apartamento apartamento, LocalDate fechaEntrada,
                         LocalDate fechaSalida, List<Ocupante> listaOcupantes, Cotizacion cotizacion,
                         PoliticaCancelacion politica, LocalDateTime fechaCreacion) {
        super(codigo, titular, apartamento, fechaEntrada, fechaSalida, listaOcupantes, cotizacion,
                politica, fechaCreacion);
    }

    @Override
    public CanalReserva getCanal() {
        return CanalReserva.PORTAL;
    }

    //Anticipo del 30 % del valor de la estancia, redondeado hacia arriba
    public long calcularAnticipo() {
        return (valorEstancia * PORCENTAJE_ANTICIPO + 99) / 100;
    }

    //Solo se confirma si ya pago el anticipo
    @Override
    public boolean puedeConfirmarse() {
        return folio.calcularTotalPagos() >= calcularAnticipo();
    }
}