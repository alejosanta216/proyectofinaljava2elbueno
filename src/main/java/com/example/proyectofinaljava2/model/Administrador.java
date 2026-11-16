package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Administrador del alojamiento: configura el sistema y consulta reportes
public class Administrador extends Empleado {

    //Constructor
    public Administrador(String nombre, String documento, String telefono, String correo,
                         String usuario, String contrasena, LocalDate fechaContratacion) {
        super(nombre, documento, telefono, correo, usuario, contrasena, fechaContratacion);
    }

    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }
}