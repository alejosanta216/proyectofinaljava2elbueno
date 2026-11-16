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

//Pantalla del recepcionista: reservas, check-in/out, folio, huespedes y apartamentos
public class RecepcionistaController {
    private final Alojamiento alojamiento = Aplicacion.getAlojamiento();
    private Empleado recepcionista;
    private final List<Ocupante> listaOcupantes = new ArrayList<>();

    @FXML
    private Label lblBienvenida;
    @FXML
    private Label lblFechaHora;

    //Reservas
    @FXML
    private TableView<Reserva> tblReservas;
    @FXML
    private TableColumn<Reserva, String> colCodigo;
    @FXML
    private TableColumn<Reserva, String> colTitular;
    @FXML
    private TableColumn<Reserva, String> colApartamento;
    @FXML
    private TableColumn<Reserva, String> colEntrada;
    @FXML
    private TableColumn<Reserva, String> colSalida;
    @FXML
    private TableColumn<Reserva, String> colCanal;
    @FXML
    private TableColumn<Reserva, String> colEstado;
    @FXML
    private TableColumn<Reserva, String> colValor;
    @FXML
    private TableColumn<Reserva, String> colSaldo;
    @FXML
    private TextArea txtDetalle;
    @FXML
    private TextField txtValorPago;
    @FXML
    private ComboBox<MedioPago> cbMedioPago;
    @FXML
    private TextField txtDescripcionCargo;
    @FXML
    private TextField txtValorCargo;

    //Nueva reserva
    @FXML
    private ComboBox<CanalReserva> cbCanal;
    @FXML
    private TextField txtCodigoExterno;
    @FXML
    private TextField txtPlataforma;
    @FXML
    private TextField txtDocumentoTitular;
    @FXML
    private ComboBox<String> cbApartamento;
    @FXML
    private DatePicker dpEntrada;
    @FXML
    private DatePicker dpSalida;
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
    @FXML
    private TableView<ConflictoCanal> tblConflictos;
    @FXML
    private TableColumn<ConflictoCanal, String> colConfCodigo;
    @FXML
    private TableColumn<ConflictoCanal, String> colConfPlataforma;
    @FXML
    private TableColumn<ConflictoCanal, String> colConfApartamento;
    @FXML
    private TableColumn<ConflictoCanal, String> colConfFechas;
    @FXML
    private TableColumn<ConflictoCanal, String> colConfMotivo;

    //Huespedes
    @FXML
    private TextField txtHuespNombre;
    @FXML
    private TextField txtHuespDocumento;
    @FXML
    private DatePicker dpHuespNacimiento;
    @FXML
    private TextField txtHuespTelefono;
    @FXML
    private TextField txtHuespCorreo;
    @FXML
    private TextField txtHuespUsuario;
    @FXML
    private PasswordField pfHuespContrasena;
    @FXML
    private TableView<Huesped> tblHuespedes;
    @FXML
    private TableColumn<Huesped, String> colHuespDocumento;
    @FXML
    private TableColumn<Huesped, String> colHuespNombre;
    @FXML
    private TableColumn<Huesped, String> colHuespTelefono;
    @FXML
    private TableColumn<Huesped, String> colHuespCorreo;

    //Apartamentos
    @FXML
    private TableView<Apartamento> tblApartamentos;
    @FXML
    private TableColumn<Apartamento, String> colAptoCodigo;
    @FXML
    private TableColumn<Apartamento, String> colAptoNombre;
    @FXML
    private TableColumn<Apartamento, String> colAptoCapacidad;
    @FXML
    private TableColumn<Apartamento, String> colAptoEstado;
    @FXML
    private TableColumn<Apartamento, String> colAptoMotivo;
    @FXML
    private TableColumn<Apartamento, String> colAptoNovedades;
    @FXML
    private ComboBox<EstadoApartamento> cbEstado;
    @FXML
    private TextField txtMotivo;
    @FXML
    private TextField txtNovedad;
    @FXML
    private TextArea txtNovedades;

    //Se ejecuta al abrir la pantalla
    @FXML
    void initialize() {
        recepcionista = (Empleado) Aplicacion.getUsuarioActual();
        lblBienvenida.setText(recepcionista.getNombre() + " (" + recepcionista.getRol() + ")");

        //Columnas de reservas
        colCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colTitular.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitular().getNombre()));
        colApartamento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getApartamento().getCodigo()));
        colEntrada.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().getFechaEntrada())));
        colSalida.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().getFechaSalida())));
        colCanal.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCanal().toString()));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().toString()));
        colValor.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoDinero(d.getValue().getValorEstancia())));
        colSaldo.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoDinero(d.getValue().getSaldo())));
        tblReservas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                txtDetalle.setText(Utilidades.describirReserva(nueva));
            }
        });
        cbMedioPago.setItems(FXCollections.observableArrayList(MedioPago.values()));
        cbMedioPago.setValue(MedioPago.EFECTIVO);

        //Nueva reserva
        cbCanal.setItems(FXCollections.observableArrayList(CanalReserva.DIRECTA, CanalReserva.EXTERNA));
        cbCanal.setValue(CanalReserva.DIRECTA);
        cbCanal.valueProperty().addListener((obs, anterior, nuevo) -> activarCamposExternos());
        activarCamposExternos();
        colOcupNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colOcupNacimiento.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().fechaNacimiento())));
        colConfCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().codigoExterno()));
        colConfPlataforma.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().plataforma()));
        colConfApartamento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().codigoApartamento()));
        colConfFechas.setCellValueFactory(d -> new SimpleStringProperty(
                Utilidades.formatoFecha(d.getValue().fechaEntrada()) + " - "
                        + Utilidades.formatoFecha(d.getValue().fechaSalida())));
        colConfMotivo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().motivo()));

        //Huespedes
        colHuespDocumento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocumento()));
        colHuespNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colHuespTelefono.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTelefono()));
        colHuespCorreo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCorreo()));
        //Al escoger un huesped se copia su documento como titular
        tblHuespedes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                txtDocumentoTitular.setText(nuevo.getDocumento());
            }
        });

        //Apartamentos
        colAptoCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colAptoNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colAptoCapacidad.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCapacidadMaxima())));
        colAptoEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado().toString()));
        colAptoMotivo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getMotivoFueraServicio() == null ? "" : d.getValue().getMotivoFueraServicio()));
        colAptoNovedades.setCellValueFactory(d -> new SimpleStringProperty(
                String.valueOf(d.getValue().getCantidadNovedades())));
        tblApartamentos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                cbEstado.setValue(nuevo.getEstado());
                mostrarNovedades(nuevo);
            }
        });
        cbEstado.setItems(FXCollections.observableArrayList(EstadoApartamento.PREPARADO,
                EstadoApartamento.PENDIENTE_PREPARACION, EstadoApartamento.EN_PREPARACION,
                EstadoApartamento.FUERA_SERVICIO));

        actualizarTodo();
    }

    //Recarga todas las tablas
    @FXML
    void actualizarTodo() {
        LocalDateTime ahora = Aplicacion.ahora();
        alojamiento.procesarVencimientos(ahora);
        lblFechaHora.setText("Fecha del sistema: " + Utilidades.formatoFechaHora(ahora));
        tblReservas.setItems(FXCollections.observableArrayList(alojamiento.getListaReservas()));
        tblReservas.refresh();
        txtDetalle.clear();
        tblConflictos.setItems(FXCollections.observableArrayList(alojamiento.getListaConflictos()));
        tblHuespedes.setItems(FXCollections.observableArrayList(alojamiento.getListaHuespedes()));
        tblApartamentos.setItems(FXCollections.observableArrayList(alojamiento.obtenerApartamentosActivos()));
        tblApartamentos.refresh();
        txtNovedades.clear();
        List<String> opciones = new ArrayList<>();
        for (Apartamento apartamento : alojamiento.obtenerApartamentosActivos()) {
            opciones.add(apartamento.getCodigo() + " - " + apartamento.getNombre());
        }
        cbApartamento.setItems(FXCollections.observableArrayList(opciones));
    }

    // ===================== Reservas =====================

    private Reserva reservaSeleccionada() {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();
        if (reserva == null) {
            Utilidades.mostrarError("Seleccione una reserva de la tabla.");
        }
        return reserva;
    }

    //Muestra el resultado de una operacion y recarga la tabla
    private void mostrarResultado(boolean exito, String mensajeExito, String mensajeError) {
        if (exito) {
            Utilidades.mostrarInfo(mensajeExito);
        } else {
            Utilidades.mostrarError(mensajeError);
        }
        actualizarTodo();
    }

    @FXML
    void confirmarReserva(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        boolean exito = alojamiento.confirmarReserva(reserva.getCodigo(), Aplicacion.ahora());
        mostrarResultado(exito, "Reserva " + reserva.getCodigo() + " confirmada.",
                "No se pudo confirmar. Debe estar Pendiente y, si es de Portal, tener pagado el anticipo del 30 %.");
    }

    @FXML
    void registrarLlegada(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        boolean exito = alojamiento.registrarLlegada(reserva.getCodigo(), Aplicacion.ahora());
        mostrarResultado(exito, "Check-in registrado. El apartamento quedo OCUPADO.",
                "No se pudo hacer el check-in. La reserva debe estar Confirmada, hoy debe estar dentro de la "
                        + "estancia, el apartamento PREPARADO y deben pasar 3 horas desde la ultima salida.");
    }

    @FXML
    void registrarSalida(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        if (reserva.getSaldo() > 0 && !Utilidades.confirmar("La reserva tiene saldo pendiente de "
                + Utilidades.formatoDinero(reserva.getSaldo()) + ". Desea registrar la salida de todos modos?")) {
            return;
        }
        boolean exito = alojamiento.registrarSalida(reserva.getCodigo(), Aplicacion.ahora());
        mostrarResultado(exito, "Check-out registrado. El apartamento quedo PENDIENTE DE PREPARACION.",
                "No se pudo hacer el check-out. La reserva debe estar En curso.");
    }

    @FXML
    void declararNoShow(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        if (!Utilidades.confirmar("Declarar no-show retiene el 100 % del valor. Continuar?")) {
            return;
        }
        boolean exito = alojamiento.declararNoShow(reserva.getCodigo(), Aplicacion.ahora());
        mostrarResultado(exito, "Reserva marcada como NO_SHOW.",
                "No se pudo. La reserva debe estar Confirmada y ser despues de las 10:00 p. m. del dia de entrada.");
    }

    @FXML
    void cancelarReserva(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        LocalDateTime ahora = Aplicacion.ahora();
        long devolucion = alojamiento.calcularDevolucionCancelacion(reserva.getCodigo(), ahora);
        if (!Utilidades.confirmar("Devolucion segun la politica: " + Utilidades.formatoDinero(devolucion)
                + ".\nCancelar la reserva " + reserva.getCodigo() + "?")) {
            return;
        }
        boolean exito = alojamiento.cancelarReserva(reserva.getCodigo(), ahora);
        mostrarResultado(exito, "Reserva cancelada. Devolucion: " + Utilidades.formatoDinero(devolucion),
                "Solo se pueden cancelar reservas Pendientes o Confirmadas.");
    }

    @FXML
    void registrarPago(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        long valor = Utilidades.leerDinero(txtValorPago);
        if (valor <= 0 || cbMedioPago.getValue() == null) {
            Utilidades.mostrarError("Escriba un valor valido y escoja el medio de pago.");
            return;
        }
        boolean exito = alojamiento.registrarPago(reserva.getCodigo(), valor, cbMedioPago.getValue(),
                "Pago en recepcion", Aplicacion.ahora());
        txtValorPago.clear();
        mostrarResultado(exito, "Pago registrado.", "No se pudo registrar el pago (reserva cancelada o no-show).");
    }

    @FXML
    void registrarCargo(ActionEvent event) {
        Reserva reserva = reservaSeleccionada();
        if (reserva == null) {
            return;
        }
        long valor = Utilidades.leerDinero(txtValorCargo);
        if (Utilidades.hayCamposVacios(txtDescripcionCargo) || valor <= 0) {
            Utilidades.mostrarError("Escriba la descripcion del servicio y un valor valido.");
            return;
        }
        boolean exito = alojamiento.registrarCargoServicio(reserva.getCodigo(), txtDescripcionCargo.getText().trim(),
                valor, Aplicacion.ahora());
        txtDescripcionCargo.clear();
        txtValorCargo.clear();
        mostrarResultado(exito, "Servicio adicional cargado al folio.",
                "Solo se cargan servicios a reservas Confirmadas o En curso.");
    }

    // ===================== Nueva reserva =====================

    //Los campos de codigo externo y plataforma solo aplican al canal Externo
    private void activarCamposExternos() {
        boolean externa = cbCanal.getValue() == CanalReserva.EXTERNA;
        txtCodigoExterno.setDisable(!externa);
        txtPlataforma.setDisable(!externa);
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

    //Agrega al titular como ocupante
    @FXML
    void agregarTitularComoOcupante(ActionEvent event) {
        Huesped titular = alojamiento.obtenerHuesped(txtDocumentoTitular.getText().trim());
        if (titular == null) {
            Utilidades.mostrarError("No existe un huesped con ese documento. Registrelo en la pestana Huespedes.");
            return;
        }
        listaOcupantes.add(new Ocupante(titular.getNombre(), titular.getFechaNacimiento()));
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

    //Saca el codigo del texto "A101 - Suite Palma"
    private String codigoApartamentoSeleccionado() {
        if (cbApartamento.getValue() == null) {
            return null;
        }
        return cbApartamento.getValue().split(" - ")[0];
    }

    @FXML
    void cotizar(ActionEvent event) {
        String codigo = codigoApartamentoSeleccionado();
        if (codigo == null || dpEntrada.getValue() == null || dpSalida.getValue() == null || listaOcupantes.isEmpty()) {
            Utilidades.mostrarError("Escoja apartamento, fechas y agregue los ocupantes.");
            return;
        }
        Cotizacion cotizacion = alojamiento.cotizar(codigo, dpEntrada.getValue(), dpSalida.getValue(), listaOcupantes);
        if (cotizacion == null) {
            Utilidades.mostrarError("Fechas no validas para cotizar.");
            return;
        }
        String disponible = alojamiento.estaDisponible(alojamiento.obtenerApartamento(codigo), dpEntrada.getValue(),
                dpSalida.getValue()) ? "SI" : "NO";
        txtCotizacion.setText(Utilidades.describirCotizacion(cotizacion) + "\nDisponible en esas fechas: " + disponible);
    }

    @FXML
    void crearReserva(ActionEvent event) {
        String codigo = codigoApartamentoSeleccionado();
        LocalDate entrada = dpEntrada.getValue();
        LocalDate salida = dpSalida.getValue();
        String documento = txtDocumentoTitular.getText().trim();
        if (codigo == null || entrada == null || salida == null || documento.isEmpty() || listaOcupantes.isEmpty()) {
            Utilidades.mostrarError("Complete titular, apartamento, fechas y ocupantes.");
            return;
        }
        if (alojamiento.obtenerHuesped(documento) == null) {
            Utilidades.mostrarError("El titular no esta registrado. Registrelo en la pestana Huespedes.");
            return;
        }
        LocalDateTime ahora = Aplicacion.ahora();
        Reserva reserva;
        if (cbCanal.getValue() == CanalReserva.EXTERNA) {
            if (Utilidades.hayCamposVacios(txtCodigoExterno, txtPlataforma)) {
                Utilidades.mostrarError("Escriba el codigo externo y la plataforma.");
                return;
            }
            int conflictosAntes = alojamiento.getListaConflictos().size();
            reserva = alojamiento.recibirReservaExterna(txtCodigoExterno.getText().trim(),
                    txtPlataforma.getText().trim(), documento, codigo, entrada, salida, listaOcupantes, ahora);
            if (reserva == null && alojamiento.getListaConflictos().size() > conflictosAntes) {
                Utilidades.mostrarError("Reserva externa rechazada: se registro como CONFLICTO DE CANAL.");
                actualizarTodo();
                return;
            }
        } else {
            reserva = alojamiento.crearReservaDirecta(documento, codigo, entrada, salida, listaOcupantes, ahora);
        }
        if (reserva == null) {
            Utilidades.mostrarError("No se pudo crear la reserva. Revise fechas, capacidad y disponibilidad.");
            return;
        }
        Utilidades.mostrarInfo("Reserva " + reserva.getCodigo() + " creada en estado PENDIENTE.\n"
                + "Confirmela en la pestana Reservas (tiene 24 horas).");
        listaOcupantes.clear();
        refrescarOcupantes();
        txtCotizacion.clear();
        txtCodigoExterno.clear();
        txtPlataforma.clear();
        actualizarTodo();
    }

    // ===================== Huespedes =====================

    @FXML
    void registrarHuesped(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtHuespNombre, txtHuespDocumento, txtHuespTelefono, txtHuespCorreo,
                txtHuespUsuario, pfHuespContrasena) || dpHuespNacimiento.getValue() == null) {
            Utilidades.mostrarError("Complete todos los datos del huesped.");
            return;
        }
        Huesped huesped = new Huesped(txtHuespNombre.getText().trim(), txtHuespDocumento.getText().trim(),
                dpHuespNacimiento.getValue(), txtHuespTelefono.getText().trim(), txtHuespCorreo.getText().trim(),
                txtHuespUsuario.getText().trim(), pfHuespContrasena.getText());
        if (!alojamiento.registrarHuesped(huesped)) {
            Utilidades.mostrarError("El documento o el usuario ya estan registrados.");
            return;
        }
        txtDocumentoTitular.setText(huesped.getDocumento());
        txtHuespNombre.clear();
        txtHuespDocumento.clear();
        dpHuespNacimiento.setValue(null);
        txtHuespTelefono.clear();
        txtHuespCorreo.clear();
        txtHuespUsuario.clear();
        pfHuespContrasena.clear();
        Utilidades.mostrarInfo("Huesped registrado. Su documento quedo puesto como titular en Nueva reserva.");
        actualizarTodo();
    }

    // ===================== Apartamentos =====================

    private Apartamento apartamentoSeleccionado() {
        Apartamento apartamento = tblApartamentos.getSelectionModel().getSelectedItem();
        if (apartamento == null) {
            Utilidades.mostrarError("Seleccione un apartamento de la tabla.");
        }
        return apartamento;
    }

    @FXML
    void cambiarEstadoApartamento(ActionEvent event) {
        Apartamento apartamento = apartamentoSeleccionado();
        if (apartamento == null) {
            return;
        }
        if (cbEstado.getValue() == null) {
            Utilidades.mostrarError("Escoja el nuevo estado.");
            return;
        }
        boolean exito = alojamiento.cambiarEstadoApartamento(apartamento.getCodigo(), cbEstado.getValue(),
                txtMotivo.getText().trim());
        txtMotivo.clear();
        mostrarResultado(exito, "Estado actualizado.",
                "No se pudo cambiar. Un apartamento OCUPADO no se cambia a mano y FUERA_SERVICIO exige motivo.");
    }

    @FXML
    void registrarNovedad(ActionEvent event) {
        Apartamento apartamento = apartamentoSeleccionado();
        if (apartamento == null) {
            return;
        }
        if (Utilidades.hayCamposVacios(txtNovedad)) {
            Utilidades.mostrarError("Describa la novedad (dano, faltante o situacion).");
            return;
        }
        boolean exito = alojamiento.registrarNovedad(apartamento.getCodigo(), txtNovedad.getText().trim(),
                recepcionista.getNombre(), Aplicacion.ahora());
        txtNovedad.clear();
        mostrarResultado(exito, "Novedad registrada.", "No se pudo registrar la novedad.");
    }

    private void mostrarNovedades(Apartamento apartamento) {
        StringBuilder texto = new StringBuilder();
        for (Novedad novedad : apartamento.getListaNovedades()) {
            texto.append(Utilidades.formatoFechaHora(novedad.fecha())).append(" - ")
                    .append(novedad.descripcion()).append(" (").append(novedad.reportadoPor()).append(")\n");
        }
        if (texto.isEmpty()) {
            texto.append("Sin novedades registradas.");
        }
        txtNovedades.setText(texto.toString());
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        Aplicacion.setUsuarioActual(null);
        Aplicacion.cambiarPantalla("login-view.fxml", "Iniciar sesion");
    }
}
