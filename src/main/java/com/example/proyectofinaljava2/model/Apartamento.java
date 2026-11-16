package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Apartamento que se vende completo a un solo grupo
public class Apartamento {
    //Atributos
    private String codigo;
    private String nombre;
    private int numeroDormitorios; // Solo descriptivo
    private int capacidadMaxima;
    private List<String> caracteristicas;
    private long tarifaBaja;  // Tarifa por persona por noche en cada temporada
    private long tarifaMedia;
    private long tarifaAlta;
    private EstadoApartamento estado;
    private String motivoFueraServicio;
    private boolean activo; // false = eliminado logicamente
    private List<Bloqueo> listaBloqueos;
    private List<Novedad> listaNovedades;

    //Constructor: inicia Preparado y activo
    public Apartamento(String codigo, String nombre, int numeroDormitorios, int capacidadMaxima,
                       long tarifaBaja, long tarifaMedia, long tarifaAlta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.capacidadMaxima = capacidadMaxima;
        this.tarifaBaja = tarifaBaja;
        this.tarifaMedia = tarifaMedia;
        this.tarifaAlta = tarifaAlta;

        this.caracteristicas = new ArrayList<>();
        this.estado = EstadoApartamento.PREPARADO;
        this.motivoFueraServicio = "";
        this.activo = true;
        this.listaBloqueos = new ArrayList<>();
        this.listaNovedades = new ArrayList<>();
    }

    //Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }

    public void setNumeroDormitorios(int numeroDormitorios) {
        this.numeroDormitorios = numeroDormitorios;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    //Devuelve una copia para proteger la lista original
    public List<String> getCaracteristicas() {
        return new ArrayList<>(caracteristicas);
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = new ArrayList<>(caracteristicas);
    }

    public void agregarCaracteristica(String caracteristica) {
        caracteristicas.add(caracteristica);
    }

    public long getTarifaBaja() {
        return tarifaBaja;
    }

    public long getTarifaMedia() {
        return tarifaMedia;
    }

    public long getTarifaAlta() {
        return tarifaAlta;
    }

    //Cambia las tarifas de las tres temporadas. No afecta reservas ya creadas
    public boolean actualizarTarifas(long tarifaBaja, long tarifaMedia, long tarifaAlta) {
        if (tarifaBaja <= 0 || tarifaMedia <= 0 || tarifaAlta <= 0) {
            return false;
        }
        this.tarifaBaja = tarifaBaja;
        this.tarifaMedia = tarifaMedia;
        this.tarifaAlta = tarifaAlta;
        return true;
    }

    //Devuelve la tarifa por persona de la temporada indicada
    public long obtenerTarifa(TipoTemporada temporada) {
        switch (temporada) {
            case ALTA:
                return tarifaAlta;
            case MEDIA:
                return tarifaMedia;
            default:
                return tarifaBaja;
        }
    }

    public EstadoApartamento getEstado() {
        return estado;
    }

    public String getMotivoFueraServicio() {
        return motivoFueraServicio;
    }

    public boolean isActivo() {
        return activo;
    }

    //Eliminacion logica: el apartamento deja de venderse pero no se borra
    public void eliminar() {
        this.activo = false;
    }

    //Cambia el estado del apartamento a mano
    public boolean cambiarEstado(EstadoApartamento nuevoEstado, String motivo) {
        if (nuevoEstado == null || nuevoEstado == EstadoApartamento.OCUPADO) {
            return false;
        }
        if (estado == EstadoApartamento.OCUPADO) {
            return false;
        }
        if (nuevoEstado == EstadoApartamento.FUERA_SERVICIO) {
            if (motivo == null || motivo.isBlank()) {
                return false;
            }
            motivoFueraServicio = motivo;
        } else {
            motivoFueraServicio = "";
        }
        estado = nuevoEstado;
        return true;
    }

    //Lo usa el check-in
    public void ocupar() {
        estado = EstadoApartamento.OCUPADO;
    }

    //Lo usa el check-out: queda pendiente de preparacion (limpieza)
    public void liberarParaPreparacion() {
        estado = EstadoApartamento.PENDIENTE_PREPARACION;
    }

    //Bloqueos

    public List<Bloqueo> getListaBloqueos() {
        return new ArrayList<>(listaBloqueos);
    }

    //Registra un bloqueo si las fechas son validas y tiene motivo
    public boolean agregarBloqueo(Bloqueo bloqueo) {
        if (bloqueo == null || bloqueo.fechaFin().isBefore(bloqueo.fechaInicio())) {
            return false;
        }
        if (bloqueo.motivo() == null || bloqueo.motivo().isBlank()) {
            return false;
        }
        listaBloqueos.add(bloqueo);
        return true;
    }

    //Dice si la noche de esa fecha tiene algun bloqueo
    public boolean estaBloqueado(LocalDate noche) {
        for (Bloqueo bloqueo : listaBloqueos) {
            if (bloqueo.incluyeNoche(noche)) {
                return true;
            }
        }
        return false;
    }

    //Novedades

    public List<Novedad> getListaNovedades() {
        return new ArrayList<>(listaNovedades);
    }

    public boolean registrarNovedad(Novedad novedad) {
        if (novedad == null || novedad.descripcion() == null || novedad.descripcion().isBlank()) {
            return false;
        }
        listaNovedades.add(novedad);
        return true;
    }

    public int getCantidadNovedades() {
        return listaNovedades.size();
    }
}
