package com.example.proyectofinaljava2.controller;

import com.example.proyectofinaljava2.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

//Metodos de apoyo que usan todos los controladores
public class Utilidades {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    //Mensajes en ventana

    public static void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("Informacion");
        alerta.showAndWait();
    }

    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.showAndWait();
    }

    //Pregunta si o no. Devuelve true si el usuario acepta
    public static boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText(null);
        alerta.setTitle("Confirmar");
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.YES;
    }

    //Formatos

    public static String formatoDinero(long valor) {
        return "$ " + String.format("%,d", valor).replace(',', '.');
    }

    public static String formatoFecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FORMATO_FECHA);
    }

    public static String formatoFechaHora(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FORMATO_FECHA_HORA);
    }

    //Lectura de campos

    //Lee un valor en pesos. Acepta puntos de miles. Devuelve -1 si no es valido
    public static long leerDinero(TextField campo) {
        String texto = campo.getText().trim().replace(".", "").replace("$", "").replace(" ", "");
        try {
            return Long.parseLong(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //Lee un numero entero. Devuelve -1 si no es valido
    public static int leerEntero(TextField campo) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //Dice si algun campo esta vacio
    public static boolean hayCamposVacios(TextField... campos) {
        for (TextField campo : campos) {
            if (campo.getText() == null || campo.getText().isBlank()) {
                return true;
            }
        }
        return false;
    }

    //Textos de detalle

    //Desglose noche por noche de una cotizacion
    public static String describirCotizacion(Cotizacion cotizacion) {
        StringBuilder texto = new StringBuilder();
        texto.append("Noche        Temporada  Tarifa      Personas  Valor\n");
        for (NocheCotizada noche : cotizacion.noches()) {
            texto.append(String.format("%-12s %-10s %-11s %-9d %s%n",
                    formatoFecha(noche.noche()), noche.temporada(),
                    formatoDinero(noche.tarifaPorPersona()), noche.personasQuePagan(),
                    formatoDinero(noche.valor())));
        }
        texto.append("\nNoches: ").append(cotizacion.getCantidadNoches());
        texto.append("\nTotal estancia: ").append(formatoDinero(cotizacion.valorTotal()));
        return texto.toString();
    }

    //Datos de la reserva con su folio (cargos, pagos y saldo)
    public static String describirReserva(Reserva reserva) {
        StringBuilder texto = new StringBuilder();
        texto.append("Reserva ").append(reserva.getCodigo())
                .append("  |  Canal: ").append(reserva.getCanal())
                .append("  |  Estado: ").append(reserva.getEstado()).append("\n");
        if (reserva instanceof ReservaExterna) {
            ReservaExterna externa = (ReservaExterna) reserva;
            texto.append("Codigo externo: ").append(externa.getCodigoExterno())
                    .append(" (").append(externa.getPlataforma()).append(")\n");
        }
        texto.append("Titular: ").append(reserva.getTitular().getNombre())
                .append("  |  Apartamento: ").append(reserva.getApartamento().getCodigo())
                .append(" - ").append(reserva.getApartamento().getNombre()).append("\n");
        texto.append("Entrada: ").append(formatoFecha(reserva.getFechaEntrada()))
                .append("  |  Salida: ").append(formatoFecha(reserva.getFechaSalida()))
                .append("  |  Noches: ").append(reserva.getCantidadNoches()).append("\n");

        texto.append("Ocupantes: ");
        for (Ocupante ocupante : reserva.getListaOcupantes()) {
            texto.append(ocupante.nombre()).append("; ");
        }
        texto.append("\nValor estancia: ").append(formatoDinero(reserva.getValorEstancia()));
        if (reserva instanceof ReservaPortal) {
            ReservaPortal portal = (ReservaPortal) reserva;
            texto.append("  |  Anticipo (30 %): ").append(formatoDinero(portal.calcularAnticipo()));
        }
        if (reserva.calcularComision() > 0) {
            texto.append("  |  Comision plataforma: ").append(formatoDinero(reserva.calcularComision()));
        }

        texto.append("\n\nCARGOS\n");
        for (Cargo cargo : reserva.getFolio().getListaCargos()) {
            texto.append(String.format("%-20s %-20s %-14s %s%n", formatoFechaHora(cargo.fecha()),
                    cargo.tipo(), formatoDinero(cargo.valor()), cargo.descripcion()));
        }
        texto.append("\nPAGOS\n");
        for (Pago pago : reserva.getFolio().getListaPagos()) {
            texto.append(String.format("%-20s %-20s %-14s %s%n", formatoFechaHora(pago.fecha()),
                    pago.medio(), formatoDinero(pago.valor()), pago.descripcion()));
        }
        texto.append("\nTotal cargos: ").append(formatoDinero(reserva.getFolio().calcularTotalCargos()));
        texto.append("\nTotal pagos: ").append(formatoDinero(reserva.getFolio().calcularTotalPagos()));
        texto.append("\nSALDO: ").append(formatoDinero(reserva.getSaldo()));
        if (reserva.getFechaCierre() != null) {
            texto.append("\nDevolucion registrada: ").append(formatoDinero(reserva.getValorDevolucion()));
        }
        return texto.toString();
    }
}