package com.example.proyectofinaljava2.model;

import java.time.LocalDateTime;

//Contrato para lo que se puede cancelar calculando antes la devolucion
public interface ICancelable {

    //Calcula cuanto se devolveria si se cancela en ese momento, sin cancelar
    public long calcularDevolucion(LocalDateTime momento);

    //Cancela y registra la devolucion. Devuelve false si no se puede cancelar
    public boolean cancelar(LocalDateTime momento);
}