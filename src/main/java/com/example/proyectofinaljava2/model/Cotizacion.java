package com.example.proyectofinaljava2.model;

import java.util.ArrayList;
import java.util.List;

//Resultado de una cotizacion
public record Cotizacion(List<NocheCotizada> noches, long valorTotal) {

    //Guarda una copia de la lista para que no se pueda cambiar desde afuera
    public Cotizacion {
        noches = new ArrayList<>(noches);
    }

    @Override
    public List<NocheCotizada> noches() {
        return new ArrayList<>(noches);
    }

    public int getCantidadNoches() {
        return noches.size();
    }
}