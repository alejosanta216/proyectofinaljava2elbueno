package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Recepcionista: maneja reservas, check-in, check-out, pagos y novedades
public class Recepcionista extends Empleado {

    //Constructor
    public Recepcionista(String nombre, String documento, String telefono, String correo,
                         String usuario, String contrasena, LocalDate fechaContratacion) {
        super(nombre, documento, telefono, correo, usuario, contrasena, fechaContratacion);
    }

    @Override
    public Rol getRol() {
        return Rol.RECEPCIONISTA;
    }
}