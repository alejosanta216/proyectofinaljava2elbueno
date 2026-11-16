package com.example.proyectofinaljava2.model;

import java.time.LocalDate;

//Huesped con su perfil. El que hace la reserva queda como titular
public class Huesped extends Persona {
    //Atributos
    private LocalDate fechaNacimiento;
    private String rutaFotografia; // Opcional, para verificar identidad

    //Constructor
    public Huesped(String nombre, String documento, LocalDate fechaNacimiento, String telefono,
                   String correo, String usuario, String contrasena) {
        super(nombre, documento, telefono, correo, usuario, contrasena);
        this.fechaNacimiento = fechaNacimiento;
        this.rutaFotografia = null;
    }

    //Getters y setters
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRutaFotografia() {
        return rutaFotografia;
    }

    public void setRutaFotografia(String rutaFotografia) {
        this.rutaFotografia = rutaFotografia;
    }

    public boolean tieneFotografia() {
        return rutaFotografia != null && !rutaFotografia.isBlank();
    }

    @Override
    public Rol getRol() {
        return Rol.HUESPED;
    }
}