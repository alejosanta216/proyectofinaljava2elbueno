package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Reserva enviada por una plataforma externa
public class ReservaExterna extends Reserva {
    public static final int PORCENTAJE_COMISION = 15;

    //Atributos
    private String codigoExterno;
    private String plataforma;

    //Constructor
    public ReservaExterna(String codigo, String codigoExterno, String plataforma, Huesped titular,
                          Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida,
                          List<Ocupante> listaOcupantes, Cotizacion cotizacion,
                          PoliticaCancelacion politica, LocalDateTime fechaCreacion) {
        super(codigo, titular, apartamento, fechaEntrada, fechaSalida, listaOcupantes, cotizacion,
                politica, fechaCreacion);
        this.codigoExterno = codigoExterno;
        this.plataforma = plataforma;
    }

    //Getters
    public String getCodigoExterno() {
        return codigoExterno;
    }

    public String getPlataforma() {
        return plataforma;
    }

    @Override
    public CanalReserva getCanal() {
        return CanalReserva.EXTERNA;
    }

    //La plataforma ya cobro al huesped, se confirma sin anticipo
    @Override
    public boolean puedeConfirmarse() {
        return true;
    }

    //Comision del 15 % sobre el valor de la estancia
    @Override
    public long calcularComision() {
        return valorEstancia * PORCENTAJE_COMISION / 100;
    }
}