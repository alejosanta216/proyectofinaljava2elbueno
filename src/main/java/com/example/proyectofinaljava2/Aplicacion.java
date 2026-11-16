package com.example.proyectofinaljava2;

import com.example.proyectofinaljava2.model.Alojamiento;
import com.example.proyectofinaljava2.model.IAutenticable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

//Clase principal: arranca la app y cambia entre pantallas
public class Aplicacion extends Application {
    private static Stage ventana;
    private static Alojamiento alojamiento;
    private static IAutenticable usuarioActual;
    private static long horasAdelantadas = 0;

    @Override
    public void start(Stage stage) {
        ventana = stage;
        alojamiento = new Alojamiento("Aparta-Suites UQ");
        DatosPrueba.cargar(alojamiento, ahora());
        cambiarPantalla("login-view.fxml", "Iniciar sesion");
        ventana.show();
    }

    //Carga un archivo FXML y lo pone en la ventana
    public static void cambiarPantalla(String archivo, String titulo) {
        try {
            FXMLLoader cargador = new FXMLLoader(Aplicacion.class.getResource(archivo));
            Scene escena = new Scene(cargador.load());
            ventana.setScene(escena);
            ventana.setTitle("SGA Aparta-Suites UQ - " + titulo);
            ventana.centerOnScreen();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir la pantalla " + archivo, e);
        }
    }

    public static Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public static IAutenticable getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(IAutenticable usuario) {
        usuarioActual = usuario;
    }

    //Fecha y hora del sistema (se puede adelantar para las demostraciones)
    public static LocalDateTime ahora() {
        return LocalDateTime.now().plusHours(horasAdelantadas).withSecond(0).withNano(0);
    }

    public static void adelantarReloj(long horas) {
        horasAdelantadas += horas;
    }

    public static void volverHoraReal() {
        horasAdelantadas = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}