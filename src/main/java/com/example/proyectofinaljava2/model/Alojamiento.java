package com.example.proyectofinaljava2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Clase central: administra todo el alojamiento
public class Alojamiento {
    public static final int HORAS_MINIMAS_PREPARACION = 3;

    //Atributos
    private String nombre;
    private List<Apartamento> listaApartamentos;
    private List<Huesped> listaHuespedes;
    private List<Empleado> listaEmpleados;
    private List<Reserva> listaReservas;
    private List<PeriodoTemporada> listaPeriodosTemporada;
    private List<ConflictoCanal> listaConflictos;
    private PoliticaCancelacion politicaCancelacion;
    private int consecutivoReservas;

    //Constructor
    public Alojamiento(String nombre) {
        this.nombre = nombre;
        this.listaApartamentos = new ArrayList<>();
        this.listaHuespedes = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
        this.listaPeriodosTemporada = new ArrayList<>();
        this.listaConflictos = new ArrayList<>();
        this.politicaCancelacion = new PoliticaCancelacion();
        this.consecutivoReservas = 0;
    }

    //Getters (las listas salen como copia)
    public String getNombre() {
        return nombre;
    }

    public List<Apartamento> getListaApartamentos() {
        return new ArrayList<>(listaApartamentos);
    }

    public List<Huesped> getListaHuespedes() {
        return new ArrayList<>(listaHuespedes);
    }

    public List<Empleado> getListaEmpleados() {
        return new ArrayList<>(listaEmpleados);
    }

    public List<Reserva> getListaReservas() {
        return new ArrayList<>(listaReservas);
    }

    public List<PeriodoTemporada> getListaPeriodosTemporada() {
        return new ArrayList<>(listaPeriodosTemporada);
    }

    public List<ConflictoCanal> getListaConflictos() {
        return new ArrayList<>(listaConflictos);
    }

    public PoliticaCancelacion getPoliticaCancelacion() {
        return politicaCancelacion.copiar();
    }

    // ===================== Seguridad =====================

    //Inicia sesion. Devuelve null si los datos no coinciden
    public IAutenticable iniciarSesion(String usuario, String contrasena) {
        for (Empleado empleado : listaEmpleados) {
            if (empleado.validarCredenciales(usuario, contrasena)) {
                return empleado;
            }
        }
        for (Huesped huesped : listaHuespedes) {
            if (huesped.validarCredenciales(usuario, contrasena)) {
                return huesped;
            }
        }
        return null;
    }

    //Dice si un nombre de usuario ya esta en uso por cualquier persona
    public boolean existeUsuario(String usuario) {
        for (Empleado empleado : listaEmpleados) {
            if (empleado.getUsuario().equals(usuario)) {
                return true;
            }
        }
        for (Huesped huesped : listaHuespedes) {
            if (huesped.getUsuario().equals(usuario)) {
                return true;
            }
        }
        return false;
    }

    // ===================== Huespedes =====================

    //Busca la posicion de un huesped por documento, -1 si no existe
    public int buscarHuesped(String documento) {
        for (int i = 0; i < listaHuespedes.size(); i++) {
            if (listaHuespedes.get(i).getDocumento().equals(documento)) {
                return i;
            }
        }
        return -1;
    }

    public Huesped obtenerHuesped(String documento) {
        int indice = buscarHuesped(documento);
        if (indice == -1) {
            return null;
        }
        return listaHuespedes.get(indice);
    }

    //Registra un huesped si el documento y el usuario no estan repetidos
    public boolean registrarHuesped(Huesped huesped) {
        if (huesped == null || buscarHuesped(huesped.getDocumento()) != -1) {
            return false;
        }
        if (existeUsuario(huesped.getUsuario())) {
            return false;
        }
        listaHuespedes.add(huesped);
        return true;
    }

    //Actualiza los datos personales del perfil del huesped
    public boolean actualizarHuesped(String documento, String nombre, LocalDate fechaNacimiento,
                                     String telefono, String correo) {
        Huesped huesped = obtenerHuesped(documento);
        if (huesped == null) {
            return false;
        }
        huesped.setNombre(nombre);
        huesped.setFechaNacimiento(fechaNacimiento);
        huesped.setTelefono(telefono);
        huesped.setCorreo(correo);
        return true;
    }

    // ===================== Empleados =====================

    //Busca la posicion de un empleado por documento, -1 si no existe
    public int buscarEmpleado(String documento) {
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getDocumento().equals(documento)) {
                return i;
            }
        }
        return -1;
    }

    public Empleado obtenerEmpleado(String documento) {
        int indice = buscarEmpleado(documento);
        if (indice == -1) {
            return null;
        }
        return listaEmpleados.get(indice);
    }

    //Contrata un empleado si no esta repetido
    public boolean registrarEmpleado(Empleado empleado) {
        if (empleado == null || buscarEmpleado(empleado.getDocumento()) != -1) {
            return false;
        }
        if (existeUsuario(empleado.getUsuario())) {
            return false;
        }
        listaEmpleados.add(empleado);
        return true;
    }

    //Edita los datos de un empleado activo
    public boolean actualizarEmpleado(String documento, String nombre, String telefono, String correo) {
        Empleado empleado = obtenerEmpleado(documento);
        if (empleado == null || !empleado.isActivo()) {
            return false;
        }
        empleado.setNombre(nombre);
        empleado.setTelefono(telefono);
        empleado.setCorreo(correo);
        return true;
    }

    //Desvincula un recepcionista (queda inactivo y ya no puede entrar)
    public boolean desvincularRecepcionista(String documento) {
        Empleado empleado = obtenerEmpleado(documento);
        if (!(empleado instanceof Recepcionista) || !empleado.isActivo()) {
            return false;
        }
        empleado.desvincular();
        return true;
    }

    public List<Recepcionista> obtenerRecepcionistasActivos() {
        List<Recepcionista> recepcionistas = new ArrayList<>();
        for (Empleado empleado : listaEmpleados) {
            if (empleado instanceof Recepcionista && empleado.isActivo()) {
                recepcionistas.add((Recepcionista) empleado);
            }
        }
        return recepcionistas;
    }

    // ===================== Apartamentos =====================

    //Busca la posicion de un apartamento por codigo, -1 si no existe
    public int buscarApartamento(String codigo) {
        for (int i = 0; i < listaApartamentos.size(); i++) {
            if (listaApartamentos.get(i).getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    //Devuelve el apartamento si existe y no fue eliminado, si no null
    public Apartamento obtenerApartamento(String codigo) {
        int indice = buscarApartamento(codigo);
        if (indice == -1 || !listaApartamentos.get(indice).isActivo()) {
            return null;
        }
        return listaApartamentos.get(indice);
    }

    //Registra un apartamento con codigo unico y datos validos
    public boolean registrarApartamento(Apartamento apartamento) {
        if (apartamento == null || buscarApartamento(apartamento.getCodigo()) != -1) {
            return false;
        }
        if (apartamento.getCapacidadMaxima() <= 0 || apartamento.getNumeroDormitorios() <= 0) {
            return false;
        }
        if (apartamento.getTarifaBaja() <= 0 || apartamento.getTarifaMedia() <= 0
                || apartamento.getTarifaAlta() <= 0) {
            return false;
        }
        listaApartamentos.add(apartamento);
        return true;
    }

    //Modifica los datos descriptivos de un apartamento
    public boolean actualizarApartamento(String codigo, String nombre, int numeroDormitorios,
                                         int capacidadMaxima, List<String> caracteristicas) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null || numeroDormitorios <= 0 || capacidadMaxima <= 0) {
            return false;
        }
        apartamento.setNombre(nombre);
        apartamento.setNumeroDormitorios(numeroDormitorios);
        apartamento.setCapacidadMaxima(capacidadMaxima);
        apartamento.setCaracteristicas(caracteristicas);
        return true;
    }

    //Eliminacion logica. No se permite si tiene reservas activas
    public boolean eliminarApartamento(String codigo) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null) {
            return false;
        }
        for (Reserva reserva : listaReservas) {
            if (reserva.getApartamento() == apartamento && reserva.esActiva()) {
                return false;
            }
        }
        apartamento.eliminar();
        return true;
    }

    public List<Apartamento> obtenerApartamentosActivos() {
        List<Apartamento> activos = new ArrayList<>();
        for (Apartamento apartamento : listaApartamentos) {
            if (apartamento.isActivo()) {
                activos.add(apartamento);
            }
        }
        return activos;
    }

    //Define las tarifas por temporada de un apartamento
    public boolean actualizarTarifas(String codigo, long tarifaBaja, long tarifaMedia, long tarifaAlta) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null) {
            return false;
        }
        return apartamento.actualizarTarifas(tarifaBaja, tarifaMedia, tarifaAlta);
    }

    //Cambio del estado operativo (por ejemplo de En preparacion a Preparado)
    public boolean cambiarEstadoApartamento(String codigo, EstadoApartamento nuevoEstado, String motivo) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null) {
            return false;
        }
        return apartamento.cambiarEstado(nuevoEstado, motivo);
    }

    //Registra un bloqueo. Se rechaza si choca con una reserva activa
    public boolean registrarBloqueo(String codigo, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null || fechaInicio == null || fechaFin == null) {
            return false;
        }
        for (Reserva reserva : listaReservas) {
            if (reserva.getApartamento() == apartamento && reserva.esActiva()
                    && reserva.comparteNoches(fechaInicio, fechaFin.plusDays(1))) {
                return false;
            }
        }
        return apartamento.agregarBloqueo(new Bloqueo(fechaInicio, fechaFin, motivo));
    }

    //Registra un dano, faltante o situacion en un apartamento
    public boolean registrarNovedad(String codigo, String descripcion, String reportadoPor, LocalDateTime fecha) {
        Apartamento apartamento = obtenerApartamento(codigo);
        if (apartamento == null) {
            return false;
        }
        return apartamento.registrarNovedad(new Novedad(fecha, descripcion, reportadoPor));
    }

    // ===================== Temporadas y politica =====================

    //Agrega un periodo de temporada media o alta
    public boolean agregarPeriodoTemporada(TipoTemporada tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        if (tipo == null || tipo == TipoTemporada.BAJA || fechaInicio == null || fechaFin == null) {
            return false;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            return false;
        }
        listaPeriodosTemporada.add(new PeriodoTemporada(tipo, fechaInicio, fechaFin));
        return true;
    }

    //Elimina un periodo por su posicion en la lista
    public boolean eliminarPeriodoTemporada(int indice) {
        if (indice < 0 || indice >= listaPeriodosTemporada.size()) {
            return false;
        }
        listaPeriodosTemporada.remove(indice);
        return true;
    }

    //Devuelve la temporada de una noche (alta gana a media)
    public TipoTemporada obtenerTemporada(LocalDate noche) {
        boolean esMedia = false;
        for (PeriodoTemporada periodo : listaPeriodosTemporada) {
            if (periodo.contiene(noche)) {
                if (periodo.tipo() == TipoTemporada.ALTA) {
                    return TipoTemporada.ALTA;
                }
                esMedia = true;
            }
        }
        if (esMedia) {
            return TipoTemporada.MEDIA;
        }
        return TipoTemporada.BAJA;
    }

    //Cambia la politica para las reservas nuevas
    public boolean configurarPoliticaCancelacion(int diasDevolucionTotal, int horasRetencionTotal,
                                                 int porcentajeRetencionParcial) {
        PoliticaCancelacion nueva = new PoliticaCancelacion(diasDevolucionTotal, horasRetencionTotal,
                porcentajeRetencionParcial);
        if (!nueva.esValida()) {
            return false;
        }
        politicaCancelacion = nueva;
        return true;
    }

    // ===================== Disponibilidad y cotizacion =====================

    //Dice si el apartamento se puede vender en esas fechas
    public boolean estaDisponible(Apartamento apartamento, LocalDate entrada, LocalDate salida) {
        if (apartamento == null || !apartamento.isActivo()) {
            return false;
        }
        if (apartamento.getEstado() == EstadoApartamento.FUERA_SERVICIO) {
            return false;
        }
        for (LocalDate noche = entrada; noche.isBefore(salida); noche = noche.plusDays(1)) {
            if (apartamento.estaBloqueado(noche)) {
                return false;
            }
        }
        //Regla mas importante: sin doble venta, sin importar el canal
        for (Reserva reserva : listaReservas) {
            if (reserva.getApartamento() == apartamento && reserva.esActiva()
                    && reserva.comparteNoches(entrada, salida)) {
                return false;
            }
        }
        return true;
    }

    //Apartamentos disponibles para unas fechas y un numero de personas
    public List<Apartamento> buscarApartamentosDisponibles(LocalDate entrada, LocalDate salida,
                                                           int numeroPersonas, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        List<Apartamento> disponibles = new ArrayList<>();
        if (!sonFechasValidas(entrada, salida, ahora) || numeroPersonas <= 0) {
            return disponibles;
        }
        for (Apartamento apartamento : listaApartamentos) {
            if (apartamento.getCapacidadMaxima() >= numeroPersonas
                    && estaDisponible(apartamento, entrada, salida)) {
                disponibles.add(apartamento);
            }
        }
        return disponibles;
    }

    //Salida posterior a la entrada (minimo una noche) y entrada desde hoy
    public boolean sonFechasValidas(LocalDate entrada, LocalDate salida, LocalDateTime ahora) {
        if (entrada == null || salida == null) {
            return false;
        }
        if (!salida.isAfter(entrada)) {
            return false;
        }
        return !entrada.isBefore(ahora.toLocalDate());
    }

    //Calcula el valor noche por noche segun la temporada
    public Cotizacion cotizar(String codigoApartamento, LocalDate entrada, LocalDate salida,
                              List<Ocupante> ocupantes) {
        Apartamento apartamento = obtenerApartamento(codigoApartamento);
        if (apartamento == null || entrada == null || salida == null || !salida.isAfter(entrada)) {
            return null;
        }
        if (ocupantes == null || ocupantes.isEmpty()) {
            return null;
        }

        int personasQuePagan = 0;
        for (Ocupante ocupante : ocupantes) {
            if (ocupante.pagaTarifa(entrada)) {
                personasQuePagan++;
            }
        }

        List<NocheCotizada> noches = new ArrayList<>();
        long valorTotal = 0;
        for (LocalDate noche = entrada; noche.isBefore(salida); noche = noche.plusDays(1)) {
            TipoTemporada temporada = obtenerTemporada(noche);
            long tarifa = apartamento.obtenerTarifa(temporada);
            long valorNoche = tarifa * personasQuePagan;
            noches.add(new NocheCotizada(noche, temporada, tarifa, personasQuePagan, valorNoche));
            valorTotal += valorNoche;
        }
        return new Cotizacion(noches, valorTotal);
    }

    // ===================== Creacion de reservas =====================

    //Busca la posicion de una reserva por codigo, -1 si no existe
    public int buscarReserva(String codigo) {
        for (int i = 0; i < listaReservas.size(); i++) {
            if (listaReservas.get(i).getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    public Reserva obtenerReserva(String codigo) {
        int indice = buscarReserva(codigo);
        if (indice == -1) {
            return null;
        }
        return listaReservas.get(indice);
    }

    //Busca una reserva externa por el codigo de la plataforma
    public ReservaExterna buscarReservaPorCodigoExterno(String codigoExterno) {
        for (Reserva reserva : listaReservas) {
            if (reserva instanceof ReservaExterna) {
                ReservaExterna externa = (ReservaExterna) reserva;
                if (externa.getCodigoExterno().equals(codigoExterno)) {
                    return externa;
                }
            }
        }
        return null;
    }

    //Valida todo lo comun a cualquier canal antes de crear una reserva
    private boolean validarDatosReserva(Huesped titular, Apartamento apartamento, LocalDate entrada,
                                        LocalDate salida, List<Ocupante> ocupantes, LocalDateTime ahora) {
        if (titular == null || apartamento == null) {
            return false;
        }
        if (!sonFechasValidas(entrada, salida, ahora)) {
            return false;
        }
        //Todos los ocupantes cuentan para la capacidad
        if (ocupantes == null || ocupantes.isEmpty() || ocupantes.size() > apartamento.getCapacidadMaxima()) {
            return false;
        }
        return estaDisponible(apartamento, entrada, salida);
    }

    private String generarCodigoReserva() {
        consecutivoReservas++;
        return "R-" + String.format("%04d", consecutivoReservas);
    }

    //Crea una reserva por el canal Portal
    public Reserva crearReservaPortal(String documentoTitular, String codigoApartamento, LocalDate entrada,
                                      LocalDate salida, List<Ocupante> ocupantes, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Huesped titular = obtenerHuesped(documentoTitular);
        Apartamento apartamento = obtenerApartamento(codigoApartamento);
        if (!validarDatosReserva(titular, apartamento, entrada, salida, ocupantes, ahora)) {
            return null;
        }
        Cotizacion cotizacion = cotizar(codigoApartamento, entrada, salida, ocupantes);
        Reserva reserva = new ReservaPortal(generarCodigoReserva(), titular, apartamento, entrada, salida,
                ocupantes, cotizacion, politicaCancelacion, ahora);
        listaReservas.add(reserva);
        return reserva;
    }

    //Crea una reserva por el canal Directo
    public Reserva crearReservaDirecta(String documentoTitular, String codigoApartamento, LocalDate entrada,
                                       LocalDate salida, List<Ocupante> ocupantes, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Huesped titular = obtenerHuesped(documentoTitular);
        Apartamento apartamento = obtenerApartamento(codigoApartamento);
        if (!validarDatosReserva(titular, apartamento, entrada, salida, ocupantes, ahora)) {
            return null;
        }
        Cotizacion cotizacion = cotizar(codigoApartamento, entrada, salida, ocupantes);
        Reserva reserva = new ReservaDirecta(generarCodigoReserva(), titular, apartamento, entrada, salida,
                ocupantes, cotizacion, politicaCancelacion, ahora);
        listaReservas.add(reserva);
        return reserva;
    }

    //Recibe una reserva externa. Si choca, se registra como conflicto
    public Reserva recibirReservaExterna(String codigoExterno, String plataforma, String documentoTitular,
                                         String codigoApartamento, LocalDate entrada, LocalDate salida,
                                         List<Ocupante> ocupantes, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        if (codigoExterno == null || codigoExterno.isBlank()) {
            return null;
        }
        if (buscarReservaPorCodigoExterno(codigoExterno) != null) {
            listaConflictos.add(new ConflictoCanal(codigoExterno, plataforma, codigoApartamento, entrada,
                    salida, ahora, "Codigo externo repetido"));
            return null;
        }

        Huesped titular = obtenerHuesped(documentoTitular);
        Apartamento apartamento = obtenerApartamento(codigoApartamento);
        if (apartamento != null && entrada != null && salida != null && salida.isAfter(entrada)
                && !estaDisponible(apartamento, entrada, salida)) {
            listaConflictos.add(new ConflictoCanal(codigoExterno, plataforma, codigoApartamento, entrada,
                    salida, ahora, "Choca con una reserva vigente o un bloqueo"));
            return null;
        }
        if (!validarDatosReserva(titular, apartamento, entrada, salida, ocupantes, ahora)) {
            return null;
        }

        Cotizacion cotizacion = cotizar(codigoApartamento, entrada, salida, ocupantes);
        Reserva reserva = new ReservaExterna(generarCodigoReserva(), codigoExterno, plataforma, titular,
                apartamento, entrada, salida, ocupantes, cotizacion, politicaCancelacion, ahora);
        listaReservas.add(reserva);
        return reserva;
    }

    // ===================== Ciclo de vida de la reserva =====================

    //Cancela automaticamente las Pendientes que pasaron 24 horas sin confirmar
    public int procesarVencimientos(LocalDateTime ahora) {
        int vencidas = 0;
        for (Reserva reserva : listaReservas) {
            if (reserva.vencerSiCorresponde(ahora)) {
                vencidas++;
            }
        }
        return vencidas;
    }

    public boolean confirmarReserva(String codigo, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return false;
        }
        return reserva.confirmar();
    }

    //Registra el check-in (exige 3 horas de preparacion)
    public boolean registrarLlegada(String codigo, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return false;
        }
        for (Reserva otra : listaReservas) {
            if (otra != reserva && otra.getApartamento() == reserva.getApartamento()
                    && otra.getFechaHoraSalidaReal() != null
                    && ahora.isBefore(otra.getFechaHoraSalidaReal().plusHours(HORAS_MINIMAS_PREPARACION))) {
                return false;
            }
        }
        return reserva.registrarLlegada(ahora);
    }

    public boolean registrarSalida(String codigo, LocalDateTime ahora) {
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return false;
        }
        return reserva.registrarSalida(ahora);
    }

    //Calcula cuanto se devolveria, para mostrarlo antes de cancelar
    public long calcularDevolucionCancelacion(String codigo, LocalDateTime ahora) {
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return 0;
        }
        return reserva.calcularDevolucion(ahora);
    }

    public boolean cancelarReserva(String codigo, LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return false;
        }
        return reserva.cancelar(ahora);
    }

    public boolean declararNoShow(String codigo, LocalDateTime ahora) {
        Reserva reserva = obtenerReserva(codigo);
        if (reserva == null) {
            return false;
        }
        return reserva.declararNoShow(ahora);
    }

    // ===================== Folio =====================

    public boolean registrarPago(String codigoReserva, long valor, MedioPago medio, String descripcion,
                                 LocalDateTime ahora) {
        procesarVencimientos(ahora);
        Reserva reserva = obtenerReserva(codigoReserva);
        if (reserva == null) {
            return false;
        }
        return reserva.registrarPago(descripcion, valor, medio, ahora);
    }

    public boolean registrarCargoServicio(String codigoReserva, String descripcion, long valor,
                                          LocalDateTime ahora) {
        Reserva reserva = obtenerReserva(codigoReserva);
        if (reserva == null) {
            return false;
        }
        return reserva.registrarCargoServicio(descripcion, valor, ahora);
    }

    //Reservas de un huesped (historial y consulta de estado y saldo)
    public List<Reserva> consultarReservasHuesped(String documento) {
        List<Reserva> reservas = new ArrayList<>();
        for (Reserva reserva : listaReservas) {
            if (reserva.getTitular().getDocumento().equals(documento)) {
                reservas.add(reserva);
            }
        }
        return reservas;
    }

    // ===================== Reportes =====================

    //Cuenta las noches ocupadas de un apartamento
    public int contarNochesOcupadas(String codigoApartamento, LocalDate inicio, LocalDate fin) {
        int noches = 0;
        for (LocalDate noche = inicio; !noche.isAfter(fin); noche = noche.plusDays(1)) {
            for (Reserva reserva : listaReservas) {
                EstadoReserva estado = reserva.getEstado();
                boolean cuenta = estado == EstadoReserva.CONFIRMADA || estado == EstadoReserva.EN_CURSO
                        || estado == EstadoReserva.FINALIZADA;
                if (cuenta && reserva.getApartamento().getCodigo().equals(codigoApartamento)
                        && reserva.ocupaNoche(noche)) {
                    noches++;
                    break;
                }
            }
        }
        return noches;
    }

    //Porcentaje de ocupacion de un apartamento en el rango [inicio, fin]
    public double calcularPorcentajeOcupacion(String codigoApartamento, LocalDate inicio, LocalDate fin) {
        int totalNoches = 0;
        for (LocalDate noche = inicio; !noche.isAfter(fin); noche = noche.plusDays(1)) {
            totalNoches++;
        }
        if (totalNoches == 0) {
            return 0;
        }
        return contarNochesOcupadas(codigoApartamento, inicio, fin) * 100.0 / totalNoches;
    }

    //Suma los pagos recibidos en el periodo
    public long calcularIngresosPeriodo(LocalDate inicio, LocalDate fin) {
        long total = 0;
        for (Reserva reserva : listaReservas) {
            for (Pago pago : reserva.getFolio().getListaPagos()) {
                LocalDate fecha = pago.fecha().toLocalDate();
                if (!fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
                    total += pago.valor();
                }
            }
        }
        return total;
    }

    //Suma las comisiones de reservas externas del periodo
    public long calcularComisionesPeriodo(LocalDate inicio, LocalDate fin) {
        long total = 0;
        for (Reserva reserva : listaReservas) {
            LocalDate fecha = reserva.getFechaCreacion().toLocalDate();
            if (!fecha.isBefore(inicio) && !fecha.isAfter(fin) && reserva.getEstado() != EstadoReserva.CANCELADA) {
                total += reserva.calcularComision();
            }
        }
        return total;
    }

    //Cuenta reservas por canal (estado null = todas)
    public int contarReservasPorCanal(CanalReserva canal, EstadoReserva estado) {
        int cantidad = 0;
        for (Reserva reserva : listaReservas) {
            if (reserva.getCanal() == canal && (estado == null || reserva.getEstado() == estado)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    //Reservas que tienen saldo pendiente (el huesped debe)
    public List<Reserva> obtenerReservasConSaldoPendiente() {
        List<Reserva> pendientes = new ArrayList<>();
        for (Reserva reserva : listaReservas) {
            if (reserva.getSaldo() > 0) {
                pendientes.add(reserva);
            }
        }
        return pendientes;
    }

    //Apartamentos ordenados de mayor a menor cantidad de novedades
    public List<Apartamento> obtenerApartamentosConMasNovedades() {
        List<Apartamento> ordenados = new ArrayList<>(listaApartamentos);
        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                if (ordenados.get(j).getCantidadNovedades() < ordenados.get(j + 1).getCantidadNovedades()) {
                    Apartamento temporal = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temporal);
                }
            }
        }
        return ordenados;
    }
}
