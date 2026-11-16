package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Clase padre abstracta de los empleados del alojamiento
public abstract class Empleado extends Persona {
    //Atributos
    protected LocalDate fechaContratacion;
    protected boolean activo; // false cuando se desvincula

    //Constructor
    public Empleado(String nombre, String documento, String telefono, String correo,
                    String usuario, String contrasena, LocalDate fechaContratacion) {
        super(nombre, documento, telefono, correo, usuario, contrasena);
        this.fechaContratacion = fechaContratacion;
        this.activo = true;
    }

    //Getters
    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public boolean isActivo() {
        return activo;
    }

    //Desvincula al empleado: no se borra, solo queda inactivo
    public void desvincular() {
        this.activo = false;
    }

    //Un empleado desvinculado no puede entrar al sistema
    @Override
    public boolean validarCredenciales(String usuario, String contrasena) {
        return activo && super.validarCredenciales(usuario, contrasena);
    }
}