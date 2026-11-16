package com.example.proyectofinaljava2.controller;

import com.example.proyectofinaljava2.Aplicacion;
import com.example.proyectofinaljava2.model.Alojamiento;
import com.example.proyectofinaljava2.model.Huesped;
import com.example.proyectofinaljava2.model.IAutenticable;
import com.example.proyectofinaljava2.model.Rol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;

//Pantalla de inicio de sesion y registro de huespedes
public class LoginController {
    private final Alojamiento alojamiento = Aplicacion.getAlojamiento();

    //Inicio de sesion
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pfContrasena;
    @FXML
    private Label lblMensaje;

    //Registro
    @FXML
    private TextField txtRegNombre;
    @FXML
    private TextField txtRegDocumento;
    @FXML
    private DatePicker dpRegNacimiento;
    @FXML
    private TextField txtRegTelefono;
    @FXML
    private TextField txtRegCorreo;
    @FXML
    private TextField txtRegUsuario;
    @FXML
    private PasswordField pfRegContrasena;
    @FXML
    private Label lblMensajeRegistro;

    //Valida el usuario y abre la pantalla segun su rol
    @FXML
    void iniciarSesion(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtUsuario, pfContrasena)) {
            lblMensaje.setText("Ingrese usuario y contrasena");
            return;
        }
        IAutenticable usuario = alojamiento.iniciarSesion(txtUsuario.getText().trim(), pfContrasena.getText());
        if (usuario == null) {
            lblMensaje.setText("Usuario o contrasena incorrectos");
            return;
        }
        Aplicacion.setUsuarioActual(usuario);
        Rol rol = usuario.getRol();
        if (rol == Rol.HUESPED) {
            Aplicacion.cambiarPantalla("huesped-view.fxml", "Huesped");
        } else if (rol == Rol.RECEPCIONISTA) {
            Aplicacion.cambiarPantalla("recepcionista-view.fxml", "Recepcion");
        } else {
            Aplicacion.cambiarPantalla("administrador-view.fxml", "Administracion");
        }
    }

    //Crea la cuenta de un huesped nuevo
    @FXML
    void registrarse(ActionEvent event) {
        if (Utilidades.hayCamposVacios(txtRegNombre, txtRegDocumento, txtRegTelefono, txtRegCorreo,
                txtRegUsuario, pfRegContrasena) || dpRegNacimiento.getValue() == null) {
            lblMensajeRegistro.setText("Complete todos los campos");
            return;
        }
        LocalDate nacimiento = dpRegNacimiento.getValue();
        if (!nacimiento.isBefore(Aplicacion.ahora().toLocalDate())) {
            lblMensajeRegistro.setText("La fecha de nacimiento no es valida");
            return;
        }
        Huesped huesped = new Huesped(txtRegNombre.getText().trim(), txtRegDocumento.getText().trim(),
                nacimiento, txtRegTelefono.getText().trim(), txtRegCorreo.getText().trim(),
                txtRegUsuario.getText().trim(), pfRegContrasena.getText());
        if (!alojamiento.registrarHuesped(huesped)) {
            lblMensajeRegistro.setText("El documento o el usuario ya estan registrados");
            return;
        }
        Utilidades.mostrarInfo("Cuenta creada. Ya puede iniciar sesion con el usuario " + huesped.getUsuario());
        txtUsuario.setText(huesped.getUsuario());
        limpiarRegistro();
    }

    private void limpiarRegistro() {
        txtRegNombre.clear();
        txtRegDocumento.clear();
        dpRegNacimiento.setValue(null);
        txtRegTelefono.clear();
        txtRegCorreo.clear();
        txtRegUsuario.clear();
        pfRegContrasena.clear();
        lblMensajeRegistro.setText("");
    }
}

