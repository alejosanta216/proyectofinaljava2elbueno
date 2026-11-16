package com.example.proyectofinaljava2.controller;

import com.example.proyectofinaljava2.Aplicacion;
import com.example.proyectofinaljava2.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Pantalla del administrador: apartamentos, recepcionistas, temporadas, politica y reportes
public class AdministradorController {
    private final Alojamiento alojamiento = Aplicacion.getAlojamiento();
    private LocalDate inicioReporte;
    private LocalDate finReporte;

    @FXML
    private Label lblBienvenida;

    //Apartamentos
    @FXML
    private TableView<Apartamento> tblApartamentos;
    @FXML
    private TableColumn<Apartamento, String> colCodigo;
    @FXML
    private TableColumn<Apartamento, String> colNombre;
    @FXML
    private TableColumn<Apartamento, String> colDormitorios;
    @FXML
    private TableColumn<Apartamento, String> colCapacidad;
    @FXML
    private TableColumn<Apartamento, String> colBaja;
    @FXML
    private TableColumn<Apartamento, String> colMedia;
    @FXML
    private TableColumn<Apartamento, String> colAlta;
    @FXML
    private TableColumn<Apartamento, String> colEstado;
    @FXML
    private TableColumn<Apartamento, String> colCaracteristicas;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDormitorios;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private TextField txtCaracteristicas;
    @FXML
    private TextField txtTarifaBaja;
    @FXML
    private TextField txtTarifaMedia;
    @FXML
    private TextField txtTarifaAlta;
    @FXML
    private DatePicker dpBloqueoInicio;
    @FXML
    private DatePicker dpBloqueoFin;
    @FXML
    private TextField txtBloqueoMotivo;
    @FXML
    private TextArea txtBloqueos;

    //Recepcionistas
    @FXML
    private TableView<Empleado> tblRecepcionistas;
    @FXML
    private TableColumn<Empleado, String> colRecDocumento;
    @FXML
    private TableColumn<Empleado, String> colRecNombre;
    @FXML
    private TableColumn<Empleado, String> colRecTelefono;
    @FXML
    private TableColumn<Empleado, String> colRecCorreo;
    @FXML
    private TableColumn<Empleado, String> colRecUsuario;
    @FXML
    private TableColumn<Empleado, String> colRecContratacion;
    @FXML
    private TableColumn<Empleado, String> colRecActivo;
    @FXML
    private TextField txtRecNombre;
    @FXML
    private TextField txtRecDocumento;
    @FXML
    private TextField txtRecTelefono;
    @FXML
    private TextField txtRecCorreo;
    @FXML
    private TextField txtRecUsuario;
    @FXML
    private PasswordField pfRecContrasena;

    //Temporadas, politica y reloj
    @FXML
    private TableView<PeriodoTemporada> tblTemporadas;
    @FXML
    private TableColumn<PeriodoTemporada, String> colTempTipo;
    @FXML
    private TableColumn<PeriodoTemporada, String> colTempInicio;
    @FXML
    private TableColumn<PeriodoTemporada, String> colTempFin;
    @FXML
    private ComboBox<TipoTemporada> cbTipoTemporada;
    @FXML
    private DatePicker dpTempInicio;
    @FXML
    private DatePicker dpTempFin;
    @FXML
    private TextField txtDiasDevolucion;
    @FXML
    private TextField txtHorasRetencion;
    @FXML
    private TextField txtPorcentajeRetencion;
    @FXML
    private Label lblPoliticaActual;
    @FXML
    private Label lblReloj;

    //Reportes
    @FXML
    private DatePicker dpRepInicio;
    @FXML
    private DatePicker dpRepFin;
    @FXML
    private TableView<Apartamento> tblOcupacion;
    @FXML
    private TableColumn<Apartamento, String> colOcupApartamento;
    @FXML
    private TableColumn<Apartamento, String> colOcupNoches;
    @FXML
    private TableColumn<Apartamento, String> colOcupPorcentaje;
    @FXML
    private Label lblIngresos;
    @FXML
    private Label lblComisiones;
    @FXML
    private TableView<CanalReserva> tblCanales;
    @FXML
    private TableColumn<CanalReserva, String> colCanal;
    @FXML
    private TableColumn<CanalReserva, String> colCanTotal;
    @FXML
    private TableColumn<CanalReserva, String> colCanPendiente;
    @FXML
    private TableColumn<CanalReserva, String> colCanConfirmada;
    @FXML
    private TableColumn<CanalReserva, String> colCanEnCurso;
    @FXML
    private TableColumn<CanalReserva, String> colCanFinalizada;
    @FXML
    private TableColumn<CanalReserva, String> colCanCancelada;
    @FXML
    private TableColumn<CanalReserva, String> colCanNoShow;
    @FXML
    private TableView<Reserva> tblSaldos;
    @FXML
    private TableColumn<Reserva, String> colSalCodigo;
    @FXML
    private TableColumn<Reserva, String> colSalTitular;
    @FXML
    private TableColumn<Reserva, String> colSalEstado;
    @FXML
    private TableColumn<Reserva, String> colSalSaldo;
    @FXML
    private TableView<Apartamento> tblNovedades;
    @FXML
    private TableColumn<Apartamento, String> colNovApartamento;
    @FXML
    private TableColumn<Apartamento, String> colNovCantidad;

    //Se ejecuta al abrir la pantalla
    @FXML
    void initialize() {
        lblBienvenida.setText(((Empleado) Aplicacion.getUsuarioActual()).getNombre() + " (ADMINISTRADOR)");

        //Apartamentos
        colCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colDormitorios.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getNumeroDormitorios())));
        colCapacidad.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCapacidadMaxima())));
        colBaja.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoDinero(d.getValue().getTarifaBaja())));
        colMedia.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoDinero(d.getValue().getTarifaMedia())));
        colAlta.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoDinero(d.getValue().getTarifaAlta())));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().toString()));
        colCaracteristicas.setCellValueFactory(d -> new SimpleStringProperty(
                String.join(", ", d.getValue().getCaracteristicas())));
        tblApartamentos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                llenarFormularioApartamento(nuevo);
            }
        });

        //Recepcionistas
        colRecDocumento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocumento()));
        colRecNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colRecTelefono.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTelefono()));
        colRecCorreo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCorreo()));
        colRecUsuario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsuario()));
        colRecContratacion.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().getFechaContratacion())));
        colRecActivo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isActivo() ? "Activo" : "Desvinculado"));
        tblRecepcionistas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                txtRecNombre.setText(nuevo.getNombre());
                txtRecDocumento.setText(nuevo.getDocumento());
                txtRecTelefono.setText(nuevo.getTelefono());
                txtRecCorreo.setText(nuevo.getCorreo());
                txtRecUsuario.setText(nuevo.getUsuario());
                pfRecContrasena.clear();
            }
        });

        //Temporadas
        colTempTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipo().toString()));
        colTempInicio.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoFecha(d.getValue().fechaInicio())));
        colTempFin.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoFecha(d.getValue().fechaFin())));
        cbTipoTemporada.setItems(FXCollections.observableArrayList(TipoTemporada.MEDIA, TipoTemporada.ALTA));

        //Reportes
        colOcupApartamento.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCodigo() + " - " + d.getValue().getNombre()));
        colOcupNoches.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(
                alojamiento.contarNochesOcupadas(d.getValue().getCodigo(), inicioReporte, finReporte))));
        colOcupPorcentaje.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.1f %%",
                alojamiento.calcularPorcentajeOcupacion(d.getValue().getCodigo(), inicioReporte, finReporte))));
        colCanal.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().toString()));
        colCanTotal.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(alojamiento.contarReservasPorCanal(d.getValue(), null))));
        columnaPorEstado(colCanPendiente, EstadoReserva.PENDIENTE);
        columnaPorEstado(colCanConfirmada, EstadoReserva.CONFIRMADA);
        columnaPorEstado(colCanEnCurso, EstadoReserva.EN_CURSO);
        columnaPorEstado(colCanFinalizada, EstadoReserva.FINALIZADA);
        columnaPorEstado(colCanCancelada, EstadoReserva.CANCELADA);
        columnaPorEstado(colCanNoShow, EstadoReserva.NO_SHOW);
        colSalCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colSalTitular.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitular().getNombre()));
        colSalEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().toString()));
        colSalSaldo.setCellValueFactory(d -> new SimpleStringProperty(Utilidades.formatoDinero(d.getValue().getSaldo())));
        colNovApartamento.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCodigo() + " - " + d.getValue().getNombre()));
        colNovCantidad.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCantidadNovedades())));

        //Periodo del reporte por defecto: el mes actual
        LocalDate hoy = Aplicacion.ahora().toLocalDate();
        dpRepInicio.setValue(hoy.withDayOfMonth(1));
        dpRepFin.setValue(hoy.withDayOfMonth(hoy.lengthOfMonth()));

        actualizarTodo();
    }

    //Columna que cuenta reservas de un canal en un estado
    private void columnaPorEstado(TableColumn<CanalReserva, String> columna, EstadoReserva estado) {
        columna.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(alojamiento.contarReservasPorCanal(d.getValue(), estado))));
    }

    @FXML
    void actualizarTodo() {
        alojamiento.procesarVencimientos(Aplicacion.ahora());
        tblApartamentos.setItems(FXCollections.observableArrayList(alojamiento.obtenerApartamentosActivos()));
        tblApartamentos.refresh();
        actualizarRecepcionistas();
        tblTemporadas.setItems(FXCollections.observableArrayList(alojamiento.getListaPeriodosTemporada()));
        mostrarPolitica();
        lblReloj.setText("Fecha y hora del sistema: " + Utilidades.formatoFechaHora(Aplicacion.ahora()));
        generarReportes(null);
    }

    // ===================== Apartamentos =====================

    private void llenarFormularioApartamento(Apartamento apartamento) {
        txtCodigo.setText(apartamento.getCodigo());
        txtNombre.setText(apartamento.getNombre());
        txtDormitorios.setText(String.valueOf(apartamento.getNumeroDormitorios()));
        txtCapacidad.setText(String.valueOf(apartamento.getCapacidadMaxima()));
        txtCaracteristicas.setText(String.join(", ", apartamento.getCaracteristicas()));
        txtTarifaBaja.setText(String.valueOf(apartamento.getTarifaBaja()));
        txtTarifaMedia.setText(String.valueOf(apartamento.getTarifaMedia()));
        txtTarifaAlta.setText(String.valueOf(apartamento.getTarifaAlta()));
        StringBuilder texto = new StringBuilder();
        for (Bloqueo bloqueo : apartamento.getListaBloqueos()) {
            texto.append(Utilidades.formatoFecha(bloqueo.fechaInicio())).append(" a ")
                    .append(Utilidades.formatoFecha(bloqueo.fechaFin())).append(" - ")
                    .append(bloqueo.motivo()).append("\n");
        }
        txtBloqueos.setText(texto.isEmpty() ? "Sin bloqueos." : texto.toString());
    }

    //Convierte "Balcon, Jacuzzi" en una lista
    private List<String> leerCaracteristicas() {
        List<String> caracteristicas = new ArrayList<>();
        for (String parte : txtCaracteristicas.getText().split(",")) {
            if (!parte.isBlank()) {
                caracteristicas.add(parte.trim());
            }
        }
        return caracteristicas;
    }

    @FXML
    void registrarApartamento(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtCodigo, txtNombre)) {
            Utilidades.mostrarError("Escriba el codigo y el nombre.");
            return;
        }
        int dormitorios = Utilidades.leerEntero(txtDormitorios);
        int capacidad = Utilidades.leerEntero(txtCapacidad);
        long baja = Utilidades.leerDinero(txtTarifaBaja);
        long media = Utilidades.leerDinero(txtTarifaMedia);
        long alta = Utilidades.leerDinero(txtTarifaAlta);
        if (dormitorios <= 0 || capacidad <= 0 || baja <= 0 || media <= 0 || alta <= 0) {
            Utilidades.mostrarError("Dormitorios, capacidad y tarifas deben ser numeros mayores que cero.");
            return;
        }
        Apartamento apartamento = new Apartamento(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                dormitorios, capacidad, baja, media, alta);
        apartamento.setCaracteristicas(leerCaracteristicas());
        if (alojamiento.registrarApartamento(apartamento)) {
            Utilidades.mostrarInfo("Apartamento registrado.");
            limpiarApartamento(null);
            actualizarTodo();
        } else {
            Utilidades.mostrarError("Ya existe un apartamento con ese codigo.");
        }
    }

    //Actualiza datos descriptivos y tarifas del apartamento seleccionado
    @FXML
    void actualizarApartamento(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        int dormitorios = Utilidades.leerEntero(txtDormitorios);
        int capacidad = Utilidades.leerEntero(txtCapacidad);
        long baja = Utilidades.leerDinero(txtTarifaBaja);
        long media = Utilidades.leerDinero(txtTarifaMedia);
        long alta = Utilidades.leerDinero(txtTarifaAlta);
        if (dormitorios <= 0 || capacidad <= 0 || baja <= 0 || media <= 0 || alta <= 0) {
            Utilidades.mostrarError("Dormitorios, capacidad y tarifas deben ser numeros mayores que cero.");
            return;
        }
        boolean datos = alojamiento.actualizarApartamento(codigo, txtNombre.getText().trim(), dormitorios,
                capacidad, leerCaracteristicas());
        boolean tarifas = alojamiento.actualizarTarifas(codigo, baja, media, alta);
        if (datos && tarifas) {
            Utilidades.mostrarInfo("Apartamento actualizado. Las reservas ya hechas conservan su valor.");
            actualizarTodo();
        } else {
            Utilidades.mostrarError("No se pudo actualizar. Seleccione un apartamento existente.");
        }
    }

    @FXML
    void eliminarApartamento(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty() || !Utilidades.confirmar("Eliminar el apartamento " + codigo + "?")) {
            return;
        }
        if (alojamiento.eliminarApartamento(codigo)) {
            Utilidades.mostrarInfo("Apartamento eliminado (queda en el historial).");
            limpiarApartamento(null);
            actualizarTodo();
        } else {
            Utilidades.mostrarError("No se puede eliminar: no existe o tiene reservas activas.");
        }
    }

    @FXML
    void limpiarApartamento(ActionEvent event) {
        txtCodigo.clear();
        txtNombre.clear();
        txtDormitorios.clear();
        txtCapacidad.clear();
        txtCaracteristicas.clear();
        txtTarifaBaja.clear();
        txtTarifaMedia.clear();
        txtTarifaAlta.clear();
        txtBloqueos.clear();
        tblApartamentos.getSelectionModel().clearSelection();
    }

    @FXML
    void registrarBloqueo(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty() || dpBloqueoInicio.getValue() == null || dpBloqueoFin.getValue() == null
                || Utilidades.hayCamposVacios(txtBloqueoMotivo)) {
            Utilidades.mostrarError("Seleccione un apartamento y escriba fechas y motivo del bloqueo.");
            return;
        }
        if (alojamiento.registrarBloqueo(codigo, dpBloqueoInicio.getValue(), dpBloqueoFin.getValue(),
                txtBloqueoMotivo.getText().trim())) {
            Utilidades.mostrarInfo("Bloqueo registrado. Esas noches ya no se pueden reservar.");
            dpBloqueoInicio.setValue(null);
            dpBloqueoFin.setValue(null);
            txtBloqueoMotivo.clear();
            actualizarTodo();
        } else {
            Utilidades.mostrarError("No se pudo bloquear: fechas no validas o choca con una reserva activa.");
        }
    }

    // ===================== Recepcionistas =====================

    private void actualizarRecepcionistas() {
        List<Empleado> recepcionistas = new ArrayList<>();
        for (Empleado empleado : alojamiento.getListaEmpleados()) {
            if (empleado instanceof Recepcionista) {
                recepcionistas.add(empleado);
            }
        }
        tblRecepcionistas.setItems(FXCollections.observableArrayList(recepcionistas));
        tblRecepcionistas.refresh();
    }

    @FXML
    void contratarRecepcionista(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtRecNombre, txtRecDocumento, txtRecTelefono, txtRecCorreo,
                txtRecUsuario, pfRecContrasena)) {
            Utilidades.mostrarError("Complete todos los datos del recepcionista.");
            return;
        }
        Recepcionista recepcionista = new Recepcionista(txtRecNombre.getText().trim(), txtRecDocumento.getText().trim(),
                txtRecTelefono.getText().trim(), txtRecCorreo.getText().trim(), txtRecUsuario.getText().trim(),
                pfRecContrasena.getText(), Aplicacion.ahora().toLocalDate());
        if (alojamiento.registrarEmpleado(recepcionista)) {
            Utilidades.mostrarInfo("Recepcionista contratado.");
            limpiarRecepcionista(null);
            actualizarRecepcionistas();
        } else {
            Utilidades.mostrarError("El documento o el usuario ya estan registrados.");
        }
    }

    @FXML
    void actualizarRecepcionista(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtRecNombre, txtRecDocumento, txtRecTelefono, txtRecCorreo)) {
            Utilidades.mostrarError("Seleccione un recepcionista y complete sus datos.");
            return;
        }
        if (alojamiento.actualizarEmpleado(txtRecDocumento.getText().trim(), txtRecNombre.getText().trim(),
                txtRecTelefono.getText().trim(), txtRecCorreo.getText().trim())) {
            Utilidades.mostrarInfo("Datos actualizados.");
            actualizarRecepcionistas();
        } else {
            Utilidades.mostrarError("No se pudo actualizar: no existe o esta desvinculado.");
        }
    }

    @FXML
    void desvincularRecepcionista(ActionEvent event) {
        String documento = txtRecDocumento.getText().trim();
        if (documento.isEmpty() || !Utilidades.confirmar("Desvincular al recepcionista " + documento + "?")) {
            return;
        }
        if (alojamiento.desvincularRecepcionista(documento)) {
            Utilidades.mostrarInfo("Recepcionista desvinculado. Ya no podra iniciar sesion.");
            limpiarRecepcionista(null);
            actualizarRecepcionistas();
        } else {
            Utilidades.mostrarError("No se pudo desvincular: no existe o ya estaba desvinculado.");
        }
    }

    @FXML
    void limpiarRecepcionista(ActionEvent event) {
        txtRecNombre.clear();
        txtRecDocumento.clear();
        txtRecTelefono.clear();
        txtRecCorreo.clear();
        txtRecUsuario.clear();
        pfRecContrasena.clear();
        tblRecepcionistas.getSelectionModel().clearSelection();
    }

    // ===================== Temporadas, politica y reloj =====================

    @FXML
    void agregarTemporada(ActionEvent event) {
        if (alojamiento.agregarPeriodoTemporada(cbTipoTemporada.getValue(), dpTempInicio.getValue(),
                dpTempFin.getValue())) {
            dpTempInicio.setValue(null);
            dpTempFin.setValue(null);
            actualizarTodo();
        } else {
            Utilidades.mostrarError("Escoja MEDIA o ALTA y un rango de fechas valido.");
        }
    }

    @FXML
    void eliminarTemporada(ActionEvent event) {
        int indice = tblTemporadas.getSelectionModel().getSelectedIndex();
        if (alojamiento.eliminarPeriodoTemporada(indice)) {
            actualizarTodo();
        } else {
            Utilidades.mostrarError("Seleccione el periodo que quiere eliminar.");
        }
    }

    private void mostrarPolitica() {
        PoliticaCancelacion politica = alojamiento.getPoliticaCancelacion();
        lblPoliticaActual.setText("Actual: mas de " + politica.getDiasDevolucionTotal()
                + " dias devuelve todo; desde " + politica.getHorasRetencionTotal() + " horas retiene el "
                + politica.getPorcentajeRetencionParcial() + " %; con menos horas retiene el 100 %.");
        txtDiasDevolucion.setText(String.valueOf(politica.getDiasDevolucionTotal()));
        txtHorasRetencion.setText(String.valueOf(politica.getHorasRetencionTotal()));
        txtPorcentajeRetencion.setText(String.valueOf(politica.getPorcentajeRetencionParcial()));
    }

    @FXML
    void guardarPolitica(ActionEvent event) {
        int dias = Utilidades.leerEntero(txtDiasDevolucion);
        int horas = Utilidades.leerEntero(txtHorasRetencion);
        int porcentaje = Utilidades.leerEntero(txtPorcentajeRetencion);
        if (alojamiento.configurarPoliticaCancelacion(dias, horas, porcentaje)) {
            Utilidades.mostrarInfo("Politica guardada. Aplica solo a las reservas nuevas.");
            mostrarPolitica();
        } else {
            Utilidades.mostrarError("Valores no validos. Revise que el porcentaje este entre 0 y 100 "
                    + "y que los dias cubran mas horas que el limite de retencion.");
        }
    }

    //Mueve la hora del sistema para demostrar vencimientos y no-show
    @FXML
    void adelantarUnaHora(ActionEvent event) {
        Aplicacion.adelantarReloj(1);
        actualizarTodo();
    }

    @FXML
    void adelantarUnDia(ActionEvent event) {
        Aplicacion.adelantarReloj(24);
        actualizarTodo();
    }

    @FXML
    void volverHoraReal(ActionEvent event) {
        Aplicacion.volverHoraReal();
        actualizarTodo();
    }

    // ===================== Reportes =====================

    @FXML
    void generarReportes(ActionEvent event) {
        LocalDate inicio = dpRepInicio.getValue();
        LocalDate fin = dpRepFin.getValue();
        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            Utilidades.mostrarError("Escoja un periodo valido para los reportes.");
            return;
        }
        inicioReporte = inicio;
        finReporte = fin;
        tblOcupacion.setItems(FXCollections.observableArrayList(alojamiento.obtenerApartamentosActivos()));
        tblOcupacion.refresh();
        long ingresos = alojamiento.calcularIngresosPeriodo(inicio, fin);
        long comisiones = alojamiento.calcularComisionesPeriodo(inicio, fin);
        lblIngresos.setText("Ingresos recibidos: " + Utilidades.formatoDinero(ingresos));
        lblComisiones.setText("Comisiones a plataformas: " + Utilidades.formatoDinero(comisiones)
                + "   |   Neto: " + Utilidades.formatoDinero(ingresos - comisiones));
        tblCanales.setItems(FXCollections.observableArrayList(CanalReserva.values()));
        tblCanales.refresh();
        tblSaldos.setItems(FXCollections.observableArrayList(alojamiento.obtenerReservasConSaldoPendiente()));
        tblNovedades.setItems(FXCollections.observableArrayList(alojamiento.obtenerApartamentosConMasNovedades()));
        tblNovedades.refresh();
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        Aplicacion.setUsuarioActual(null);
        Aplicacion.cambiarPantalla("login-view.fxml", "Iniciar sesion");
    }
}