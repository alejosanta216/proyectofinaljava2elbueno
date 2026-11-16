package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//Clase padre abstracta de las reservas. Cada canal confirma y cobra a su manera
public abstract class Reserva implements ICancelable {
    //Horas fijas del alojamiento
    public static final LocalTime HORA_ENTRADA = LocalTime.of(15, 0);
    public static final LocalTime HORA_SALIDA = LocalTime.of(11, 0);
    public static final LocalTime HORA_LIMITE_NO_SHOW = LocalTime.of(22, 0);
    public static final int HORAS_PARA_CONFIRMAR = 24;

    //Atributos
    protected String codigo;
    protected Huesped titular;
    protected Apartamento apartamento;
    protected LocalDate fechaEntrada;
    protected LocalDate fechaSalida;
    protected List<Ocupante> listaOcupantes;
    protected EstadoReserva estado;
    protected LocalDateTime fechaCreacion;
    protected long valorEstancia;            // Congelado al crear la reserva
    protected List<NocheCotizada> desglose;  // Congelado al crear la reserva
    protected PoliticaCancelacion politica;  // Se congela al crear la reserva
    protected Folio folio;
    protected long valorDevolucion;
    protected LocalDateTime fechaCierre;     // Fecha de cancelacion o no-show
    protected LocalDateTime fechaHoraLlegada;
    protected LocalDateTime fechaHoraSalidaReal;

    //Constructor: nace Pendiente y carga el alojamiento al folio
    public Reserva(String codigo, Huesped titular, Apartamento apartamento, LocalDate fechaEntrada,
                   LocalDate fechaSalida, List<Ocupante> listaOcupantes, Cotizacion cotizacion,
                   PoliticaCancelacion politica, LocalDateTime fechaCreacion) {
        this.codigo = codigo;
        this.titular = titular;
        this.apartamento = apartamento;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.listaOcupantes = new ArrayList<>(listaOcupantes);
        this.fechaCreacion = fechaCreacion;
        this.valorEstancia = cotizacion.valorTotal();
        this.desglose = cotizacion.noches();
        this.politica = politica.copiar();

        this.estado = EstadoReserva.PENDIENTE;
        this.folio = new Folio();
        this.valorDevolucion = 0;
        this.fechaCierre = null;
        this.fechaHoraLlegada = null;
        this.fechaHoraSalidaReal = null;

        folio.registrarCargo(new Cargo("Alojamiento " + desglose.size() + " noche(s)",
                TipoCargo.ALOJAMIENTO, valorEstancia, fechaCreacion));
    }

    //Getters
    public String getCodigo() {
        return codigo;
    }

    public Huesped getTitular() {
        return titular;
    }

    public Apartamento getApartamento() {
        return apartamento;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public List<Ocupante> getListaOcupantes() {
        return new ArrayList<>(listaOcupantes);
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public long getValorEstancia() {
        return valorEstancia;
    }

    public List<NocheCotizada> getDesglose() {
        return new ArrayList<>(desglose);
    }

    public PoliticaCancelacion getPolitica() {
        return politica.copiar();
    }

    public Folio getFolio() {
        return folio;
    }

    public long getValorDevolucion() {
        return valorDevolucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public LocalDateTime getFechaHoraSalidaReal() {
        return fechaHoraSalidaReal;
    }

    //Metodos abstractos: cada canal los define (polimorfismo)
    public abstract CanalReserva getCanal();

    public abstract boolean puedeConfirmarse();

    //Comision que el alojamiento paga por la reserva. Por defecto no hay
    public long calcularComision() {
        return 0;
    }

    //Datos calculados

    public int getCantidadNoches() {
        return desglose.size();
    }

    public long getSaldo() {
        return folio.calcularSaldo();
    }

    public boolean esActiva() {
        return estado.esActiva();
    }

    //Momento en que empieza la estancia (dia de entrada a las 3:00 p. m.)
    public LocalDateTime getInicioEstancia() {
        return fechaEntrada.atTime(HORA_ENTRADA);
    }

    //Dice si esta reserva usa al menos una noche del rango [entrada, salida)
    public boolean comparteNoches(LocalDate entrada, LocalDate salida) {
        return entrada.isBefore(fechaSalida) && salida.isAfter(fechaEntrada);
    }

    //Dice si la reserva ocupa la noche de esa fecha
    public boolean ocupaNoche(LocalDate noche) {
        return !noche.isBefore(fechaEntrada) && noche.isBefore(fechaSalida);
    }

    //Ciclo de vida

    //Pasa a Confirmada si el estado lo permite y el canal lo autoriza
    public boolean confirmar() {
        if (!estado.puedeCambiarA(EstadoReserva.CONFIRMADA)) {
            return false;
        }
        if (!puedeConfirmarse()) {
            return false;
        }
        estado = EstadoReserva.CONFIRMADA;
        return true;
    }

    //Registra la llegada (check-in)
    public boolean registrarLlegada(LocalDateTime ahora) {
        if (!estado.puedeCambiarA(EstadoReserva.EN_CURSO)) {
            return false;
        }
        LocalDate hoy = ahora.toLocalDate();
        if (hoy.isBefore(fechaEntrada) || !hoy.isBefore(fechaSalida)) {
            return false;
        }
        if (apartamento.getEstado() != EstadoApartamento.PREPARADO) {
            return false;
        }
        estado = EstadoReserva.EN_CURSO;
        fechaHoraLlegada = ahora;
        apartamento.ocupar();
        return true;
    }

    //Registra la salida (check-out)
    public boolean registrarSalida(LocalDateTime ahora) {
        if (!estado.puedeCambiarA(EstadoReserva.FINALIZADA)) {
            return false;
        }
        estado = EstadoReserva.FINALIZADA;
        fechaHoraSalidaReal = ahora;
        apartamento.liberarParaPreparacion();
        return true;
    }

    //Cancelacion (interface ICancelable)

    //Calcula la devolucion sin cancelar, para mostrarla antes al huesped
    @Override
    public long calcularDevolucion(LocalDateTime momento) {
        if (!estado.puedeCambiarA(EstadoReserva.CANCELADA)) {
            return 0;
        }
        return politica.calcularDevolucion(valorEstancia, folio.calcularTotalPagos(),
                momento, getInicioEstancia());
    }

    //Cancela una reserva Pendiente o Confirmada y registra la devolucion
    @Override
    public boolean cancelar(LocalDateTime momento) {
        if (!estado.puedeCambiarA(EstadoReserva.CANCELADA)) {
            return false;
        }
        long devolucion = calcularDevolucion(momento);
        cerrarFolio(devolucion, "cancelacion", momento);
        estado = EstadoReserva.CANCELADA;
        return true;
    }

    //Cancela la reserva si pasaron 24 horas sin confirmar
    public boolean vencerSiCorresponde(LocalDateTime ahora) {
        if (estado != EstadoReserva.PENDIENTE) {
            return false;
        }
        if (ahora.isBefore(fechaCreacion.plusHours(HORAS_PARA_CONFIRMAR))) {
            return false;
        }
        cerrarFolio(folio.calcularTotalPagos(), "vencimiento del plazo de confirmacion", ahora);
        estado = EstadoReserva.CANCELADA;
        return true;
    }

    //Declara no-show despues de las 10:00 p. m.
    public boolean declararNoShow(LocalDateTime ahora) {
        if (!estado.puedeCambiarA(EstadoReserva.NO_SHOW)) {
            return false;
        }
        if (ahora.isBefore(fechaEntrada.atTime(HORA_LIMITE_NO_SHOW))) {
            return false;
        }
        long devolucion = folio.calcularTotalPagos() - valorEstancia;
        if (devolucion < 0) {
            devolucion = 0;
        }
        cerrarFolio(devolucion, "no-show", ahora);
        estado = EstadoReserva.NO_SHOW;
        return true;
    }

    //Cierra el folio con movimientos contrarios
    private void cerrarFolio(long devolucion, String motivo, LocalDateTime momento) {
        long totalPagado = folio.calcularTotalPagos();
        long retenido = totalPagado - devolucion;

        folio.registrarCargo(new Cargo("Anulacion del alojamiento por " + motivo,
                TipoCargo.CORRECCION, -valorEstancia, momento));
        if (retenido > 0) {
            folio.registrarCargo(new Cargo("Penalidad por " + motivo,
                    TipoCargo.PENALIDAD, retenido, momento));
        }
        if (devolucion > 0) {
            MedioPago medio = folio.getListaPagos().get(0).medio();
            folio.registrarPago(new Pago("Devolucion por " + motivo, -devolucion, medio, momento));
        }
        valorDevolucion = devolucion;
        fechaCierre = momento;
    }

    //Movimientos del folio

    //Registra un pago o abono. No se aceptan en reservas canceladas ni no-show
    public boolean registrarPago(String descripcion, long valor, MedioPago medio, LocalDateTime fecha) {
        if (valor <= 0 || medio == null) {
            return false;
        }
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.NO_SHOW) {
            return false;
        }
        return folio.registrarPago(new Pago(descripcion, valor, medio, fecha));
    }

    //Registra un servicio adicional
    public boolean registrarCargoServicio(String descripcion, long valor, LocalDateTime fecha) {
        if (valor <= 0) {
            return false;
        }
        if (estado != EstadoReserva.CONFIRMADA && estado != EstadoReserva.EN_CURSO) {
            return false;
        }
        return folio.registrarCargo(new Cargo(descripcion, TipoCargo.SERVICIO_ADICIONAL, valor, fecha));
    }

    //Corrige un cargo mal registrado con un cargo contrario (negativo)
    public boolean corregirCargo(String descripcion, long valor, LocalDateTime fecha) {
        if (valor <= 0) {
            return false;
        }
        return folio.registrarCargo(new Cargo("Correccion: " + descripcion, TipoCargo.CORRECCION, -valor, fecha));
    }

    //Corrige un pago mal registrado con un pago contrario (negativo)
    public boolean corregirPago(String descripcion, long valor, MedioPago medio, LocalDateTime fecha) {
        if (valor <= 0 || medio == null) {
            return false;
        }
        return folio.registrarPago(new Pago("Correccion: " + descripcion, -valor, medio, fecha));
    }
}