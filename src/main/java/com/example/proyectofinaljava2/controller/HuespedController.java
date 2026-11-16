package com.example.proyectofinaljava2.controller;

import com.example.proyectofinaljava2.Aplicacion;
import com.example.proyectofinaljava2.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Pantalla del huesped: buscar, cotizar, reservar, pagar, cancelar y perfil
public class HuespedController {
    private final Alojamiento alojamiento = Aplicacion.getAlojamiento();
    private Huesped huesped;
    private final List<Ocupante> listaOcupantes = new ArrayList<>();

    @FXML
    private Label lblBienvenida;

    //Buscar y reservar
    @FXML
    private DatePicker dpEntrada;
    @FXML
    private DatePicker dpSalida;
    @FXML
    private Spinner<Integer> spPersonas;
    @FXML
    private TableView<Apartamento> tblDisponibles;
    @FXML
    private TableColumn<Apartamento, String> colDispCodigo;
    @FXML
    private TableColumn<Apartamento, String> colDispNombre;
    @FXML
    private TableColumn<Apartamento, String> colDispDormitorios;
    @FXML
    private TableColumn<Apartamento, String> colDispCapacidad;
    @FXML
    private TableColumn<Apartamento, String> colDispCaracteristicas;
    @FXML
    private TextField txtNombreOcupante;
    @FXML
    private DatePicker dpNacimientoOcupante;
    @FXML
    private TableView<Ocupante> tblOcupantes;
    @FXML
    private TableColumn<Ocupante, String> colOcupNombre;
    @FXML
    private TableColumn<Ocupante, String> colOcupNacimiento;
    @FXML
    private TextArea txtCotizacion;

    //Mis reservas
    @FXML
    private TableView<Reserva> tblMisReservas;
    @FXML
    private TableColumn<Reserva, String> colResCodigo;
    @FXML
    private TableColumn<Reserva, String> colResApartamento;
    @FXML
    private TableColumn<Reserva, String> colResEntrada;
    @FXML
    private TableColumn<Reserva, String> colResSalida;
    @FXML
    private TableColumn<Reserva, String> colResEstado;
    @FXML
    private TableColumn<Reserva, String> colResValor;
    @FXML
    private TableColumn<Reserva, String> colResSaldo;
    @FXML
    private TextArea txtFolio;
    @FXML
    private TextField txtValorPago;
    @FXML
    private ComboBox<MedioPago> cbMedioPago;

    //Perfil
    @FXML
    private TextField txtPerfilNombre;
    @FXML
    private TextField txtPerfilDocumento;
    @FXML
    private DatePicker dpPerfilNacimiento;
    @FXML
    private TextField txtPerfilTelefono;
    @FXML
    private TextField txtPerfilCorreo;
    @FXML
    private TextField txtPerfilUsuario;
    @FXML
    private PasswordField pfNuevaContrasena;

    //Se ejecuta al abrir la pantalla
    @FXML
    void initialize() {
        huesped = (Huesped) Aplicacion.getUsuarioActual();
        lblBienvenida.setText("Hola, " + huesped.getNombre());

        //Columnas de apartamentos disponibles
        colDispCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colDispNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colDispDormitorios.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getNumeroDormitorios())));
        colDispCapacidad.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCapacidadMaxima())));
        colDispCaracteristicas.setCellValueFactory(d -> new SimpleStringProperty(
                String.join(", ", d.getValue().getCaracteristicas())));

        //Columnas de ocupantes
        colOcupNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colOcupNacimiento.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().fechaNacimiento())));

        //Columnas de mis reservas
        colResCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colResApartamento.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getApartamento().getNombre()));
        colResEntrada.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().getFechaEntrada())));
        colResSalida.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().getFechaSalida())));
        colResEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().toString()));
        colResValor.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoDinero(d.getValue().getValorEstancia())));
        colResSaldo.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoDinero(d.getValue().getSaldo())));

        //Al escoger una reserva se muestra su folio
        tblMisReservas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                txtFolio.setText(Utilidades.describirReserva(nueva));
            }
        });

        cbMedioPago.setItems(FXCollections.observableArrayList(MedioPago.values()));
        cbMedioPago.setValue(MedioPago.TARJETA);

        cargarPerfil();
        actualizarMisReservas();
    }

    // ===================== Buscar y reservar =====================

    @FXML
    void buscarDisponibles(ActionEvent event) {
        LocalDate entrada = dpEntrada.getValue();
        LocalDate salida = dpSalida.getValue();
        LocalDateTime ahora = Aplicacion.ahora();
        if (!alojamiento.sonFechasValidas(entrada, salida, ahora)) {
            Utilidades.mostrarError("Fechas no validas: la entrada debe ser desde hoy y la salida despues de la entrada.");
            return;
        }
        List<Apartamento> disponibles = alojamiento.buscarApartamentosDisponibles(entrada, salida,
                spPersonas.getValue(), ahora);
        tblDisponibles.setItems(FXCollections.observableArrayList(disponibles));
        txtCotizacion.clear();
        if (disponibles.isEmpty()) {
            Utilidades.mostrarInfo("No hay apartamentos disponibles para esas fechas y personas.");
        }
    }

    //Agrega al propio huesped como ocupante
    @FXML
    void agregarmeComoOcupante(ActionEvent event) {
        listaOcupantes.add(new Ocupante(huesped.getNombre(), huesped.getFechaNacimiento()));
        refrescarOcupantes();
    }

    @FXML
    void agregarOcupante(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtNombreOcupante) || dpNacimientoOcupante.getValue() == null) {
            Utilidades.mostrarError("Escriba el nombre y la fecha de nacimiento del ocupante.");
            return;
        }
        if (dpNacimientoOcupante.getValue().isAfter(Aplicacion.ahora().toLocalDate())) {
            Utilidades.mostrarError("La fecha de nacimiento no puede ser futura.");
            return;
        }
        listaOcupantes.add(new Ocupante(txtNombreOcupante.getText().trim(), dpNacimientoOcupante.getValue()));
        txtNombreOcupante.clear();
        dpNacimientoOcupante.setValue(null);
        refrescarOcupantes();
    }

    @FXML
    void quitarOcupante(ActionEvent event) {
        Ocupante seleccionado = tblOcupantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Utilidades.mostrarError("Seleccione el ocupante que quiere quitar.");
            return;
        }
        listaOcupantes.remove(seleccionado);
        refrescarOcupantes();
    }

    private void refrescarOcupantes() {
        tblOcupantes.setItems(FXCollections.observableArrayList(listaOcupantes));
    }

    //Muestra el valor noche por noche del apartamento seleccionado
    @FXML
    void cotizar(ActionEvent event) {
        Cotizacion cotizacion = obtenerCotizacion();
        if (cotizacion != null) {
            long anticipo = (cotizacion.valorTotal() * ReservaPortal.PORCENTAJE_ANTICIPO + 99) / 100;
            txtCotizacion.setText(Utilidades.describirCotizacion(cotizacion)
                    + "\nAnticipo para confirmar (30 %): " + Utilidades.formatoDinero(anticipo)
                    + "\nLos menores de 6 anios no pagan.");
        }
    }

    //Valida la seleccion y calcula la cotizacion. Devuelve null si falta algo
    private Cotizacion obtenerCotizacion() {
        Apartamento apartamento = tblDisponibles.getSelectionModel().getSelectedItem();
        if (apartamento == null) {
            Utilidades.mostrarError("Primero busque y seleccione un apartamento de la tabla.");
            return null;
        }
        if (listaOcupantes.isEmpty()) {
            Utilidades.mostrarError("Agregue los ocupantes (todas las personas que se alojan).");
            return null;
        }
        if (listaOcupantes.size() > apartamento.getCapacidadMaxima()) {
            Utilidades.mostrarError("Hay mas ocupantes que la capacidad del apartamento ("
                    + apartamento.getCapacidadMaxima() + ").");
            return null;
        }
        return alojamiento.cotizar(apartamento.getCodigo(), dpEntrada.getValue(), dpSalida.getValue(),
                listaOcupantes);
    }

    @FXML
    void reservar(ActionEvent event) {
        Cotizacion cotizacion = obtenerCotizacion();
        if (cotizacion == null) {
            return;
        }
        Apartamento apartamento = tblDisponibles.getSelectionModel().getSelectedItem();
        String mensaje = "Reservar " + apartamento.getNombre() + " por "
                + Utilidades.formatoDinero(cotizacion.valorTotal()) + "?";
        if (!Utilidades.confirmar(mensaje)) {
            return;
        }
        Reserva reserva = alojamiento.crearReservaPortal(huesped.getDocumento(), apartamento.getCodigo(),
                dpEntrada.getValue(), dpSalida.getValue(), listaOcupantes, Aplicacion.ahora());
        if (reserva == null) {
            Utilidades.mostrarError("No se pudo crear la reserva. Revise fechas, capacidad y disponibilidad.");
            return;
        }
        ReservaPortal portal = (ReservaPortal) reserva;
        Utilidades.mostrarInfo("Reserva " + reserva.getCodigo() + " creada en estado PENDIENTE.\n"
                + "Pague el anticipo de " + Utilidades.formatoDinero(portal.calcularAnticipo())
                + " en las proximas 24 horas para confirmarla (pestana Mis reservas).");
        listaOcupantes.clear();
        refrescarOcupantes();
        tblDisponibles.getItems().clear();
        txtCotizacion.clear();
        actualizarMisReservas();
    }

    // ===================== Mis reservas =====================

    @FXML
    void actualizarMisReservas() {
        alojamiento.procesarVencimientos(Aplicacion.ahora());
        tblMisReservas.setItems(FXCollections.observableArrayList(
                alojamiento.consultarReservasHuesped(huesped.getDocumento())));
        tblMisReservas.refresh();
        txtFolio.clear();
    }

    private Reserva reservaSeleccionada() {
        Reserva reserva = tblMisReservas.getSelectionModel().getSelectedItem();
        if (reserva == null) {
            Utilidades.mostrarError("Seleccione una reserva de la tabla.");
        }
        return reserva;
    }

    //Registra un pago. Si cubre el anticipo, la reserva se confirma
    @FXML
    void pagar(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        long valor = Utilidades.leerDinero(txtValorPago);
        if (valor <= 0 || cbMedioPago.getValue() == null) {
            Utilidades.mostrarError("Escriba un valor valido y escoja el medio de pago.");
            return;
        }
        LocalDateTime ahora = Aplicacion.ahora();
        if (!alojamiento.registrarPago(reserva.getCodigo(), valor, cbMedioPago.getValue(),
                "Pago desde el portal", ahora)) {
            Utilidades.mostrarError("No se pudo registrar el pago. La reserva puede estar cancelada o vencida.");
            actualizarMisReservas();
            return;
        }
        String mensaje = "Pago registrado.";
        if (reserva.getEstado() == EstadoReserva.PENDIENTE) {
            if (alojamiento.confirmarReserva(reserva.getCodigo(), ahora)) {
                mensaje += " Se cubrio el anticipo: la reserva quedo CONFIRMADA.";
            } else {
                mensaje += " Aun no cubre el anticipo del 30 %.";
            }
        }
        Utilidades.mostrarInfo(mensaje);
        txtValorPago.clear();
        actualizarMisReservas();
    }

    //Muestra la devolucion y cancela si el huesped acepta
    @FXML
    void cancelarReserva(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        if (reserva.getEstado() != EstadoReserva.PENDIENTE && reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            Utilidades.mostrarError("Solo se pueden cancelar reservas Pendientes o Confirmadas.");
            return;
        }
        LocalDateTime ahora = Aplicacion.ahora();
        long devolucion = alojamiento.calcularDevolucionCancelacion(reserva.getCodigo(), ahora);
        String mensaje = "Ha pagado " + Utilidades.formatoDinero(reserva.getFolio().calcularTotalPagos())
                + ".\nSi cancela ahora se le devuelven " + Utilidades.formatoDinero(devolucion)
                + ".\nDesea cancelar la reserva " + reserva.getCodigo() + "?";
        if (!Utilidades.confirmar(mensaje)) {
            return;
        }
        if (alojamiento.cancelarReserva(reserva.getCodigo(), ahora)) {
            Utilidades.mostrarInfo("Reserva cancelada. Devolucion registrada: " + Utilidades.formatoDinero(devolucion));
        } else {
            Utilidades.mostrarError("No se pudo cancelar la reserva.");
        }
        actualizarMisReservas();
    }

    // ===================== Perfil =====================

    private void cargarPerfil() {
        txtPerfilNombre.setText(huesped.getNombre());
        txtPerfilDocumento.setText(huesped.getDocumento());
        dpPerfilNacimiento.setValue(huesped.getFechaNacimiento());
        txtPerfilTelefono.setText(huesped.getTelefono());
        txtPerfilCorreo.setText(huesped.getCorreo());
        txtPerfilUsuario.setText(huesped.getUsuario());
    }

    @FXML
    void guardarPerfil(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtPerfilNombre, txtPerfilTelefono, txtPerfilCorreo)
                || dpPerfilNacimiento.getValue() == null) {
            Utilidades.mostrarError("Complete todos los datos del perfil.");
            return;
        }
        if (alojamiento.actualizarHuesped(huesped.getDocumento(), txtPerfilNombre.getText().trim(),
                dpPerfilNacimiento.getValue(), txtPerfilTelefono.getText().trim(), txtPerfilCorreo.getText().trim())) {
            lblBienvenida.setText("Hola, " + huesped.getNombre());
            Utilidades.mostrarInfo("Perfil actualizado.");
        } else {
            Utilidades.mostrarError("No se pudo actualizar el perfil.");
        }
    }

    @FXML
    void cambiarContrasena(ActionEvent event) {
        if (huesped.cambiarContrasena(pfNuevaContrasena.getText())) {
            pfNuevaContrasena.clear();
            Utilidades.mostrarInfo("Contrasena actualizada.");
        } else {
            Utilidades.mostrarError("La contrasena no es valida.");
        }
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        Aplicacion.setUsuarioActual(null);
        Aplicacion.cambiarPantalla("login-view.fxml", "Iniciar sesion");
    }
}