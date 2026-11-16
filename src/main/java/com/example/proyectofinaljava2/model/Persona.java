package com.example.proyectofinaljava2.model;

//Clase padre abstracta de todas las personas que entran al sistema
public abstract class Persona implements IAutenticable {
    //Atributos
    protected String nombre;
    protected String documento;
    protected String telefono;
    protected String correo;
    protected String usuario;
    protected String contrasena;

    //Constructor
    public Persona(String nombre, String documento, String telefono, String correo,
                   String usuario, String contrasena) {
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    //Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String getUsuario() {
        return usuario;
    }

    //Cambia la contrasena si la nueva no esta vacia
    public boolean cambiarContrasena(String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.isBlank()) {
            return false;
        }
        this.contrasena = nuevaContrasena;
        return true;
    }

    //Valida usuario y contrasena (la contrasena no tiene getter, se protege)
    @Override
    public boolean validarCredenciales(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    //Cada tipo de persona entra con su propio rol
    @Override
    public abstract Rol getRol();
}