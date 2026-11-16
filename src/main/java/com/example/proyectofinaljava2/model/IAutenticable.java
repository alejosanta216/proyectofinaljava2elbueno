package com.example.proyectofinaljava2.model;

//Contrato para los usuarios que entran al sistema con usuario y contrasena
public interface IAutenticable {

    public String getUsuario();

    //Valida si el usuario y la contrasena coinciden
    public boolean validarCredenciales(String usuario, String contrasena);

    //Rol con el que entra al sistema
    public Rol getRol();
}