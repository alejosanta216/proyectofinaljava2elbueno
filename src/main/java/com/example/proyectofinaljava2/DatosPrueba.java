package com.example.proyectofinaljava2;

import com.example.proyectofinaljava2.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Datos iniciales para probar la aplicacion
public class DatosPrueba {

    public static void cargar(Alojamiento alojamiento, LocalDateTime ahora) {
        LocalDate hoy = ahora.toLocalDate();

        //Usuarios
        alojamiento.registrarEmpleado(new Administrador("Laura Gomez", "1001", "3001112233",
                "admin@apartasuites.com", "admin", "admin123", hoy.minusYears(3)));
        alojamiento.registrarEmpleado(new Recepcionista("Carlos Ruiz", "1002", "3004445566",
                "recepcion@apartasuites.com", "recepcion", "recep123", hoy.minusYears(1)));
        alojamiento.registrarHuesped(new Huesped("Ana Torres", "1094000111", LocalDate.of(1998, 4, 12),
                "3105556677", "ana@correo.com", "ana", "ana123"));
        alojamiento.registrarHuesped(new Huesped("Pedro Diaz", "1094000222", LocalDate.of(1985, 9, 3),
                "3118889900", "pedro@correo.com", "pedro", "pedro123"));
        alojamiento.registrarHuesped(new Huesped("Marta Lopez", "1094000333", LocalDate.of(1990, 1, 25),
                "3122223344", "marta@correo.com", "marta", "marta123"));

        //Apartamentos
        Apartamento palma = new Apartamento("A101", "Suite Palma", 1, 2, 120000, 150000, 190000);
        palma.agregarCaracteristica("Balcon");
        palma.agregarCaracteristica("Vista al valle");
        Apartamento cafetal = new Apartamento("A102", "Suite Cafetal", 2, 4, 100000, 130000, 170000);
        cafetal.agregarCaracteristica("Cocina");
        cafetal.agregarCaracteristica("Wifi");
        Apartamento cocora = new Apartamento("A201", "Loft Cocora", 1, 3, 110000, 140000, 180000);
        cocora.agregarCaracteristica("Jacuzzi");
        Apartamento familiar = new Apartamento("A202", "Familiar Salento", 3, 6, 90000, 120000, 150000);
        familiar.agregarCaracteristica("Cocina");
        familiar.agregarCaracteristica("Parqueadero");
        alojamiento.registrarApartamento(palma);
        alojamiento.registrarApartamento(cafetal);
        alojamiento.registrarApartamento(cocora);
        alojamiento.registrarApartamento(familiar);

        //Temporadas
        int anio = hoy.getYear();
        alojamiento.agregarPeriodoTemporada(TipoTemporada.ALTA, LocalDate.of(anio, 12, 15),
                LocalDate.of(anio + 1, 1, 15));
        alojamiento.agregarPeriodoTemporada(TipoTemporada.MEDIA, LocalDate.of(anio + 1, 6, 15),
                LocalDate.of(anio + 1, 7, 31));

        //Reserva directa en curso (Pedro con su hijo de 3 anios)
        List<Ocupante> ocupantes1 = new ArrayList<>();
        ocupantes1.add(new Ocupante("Pedro Diaz", LocalDate.of(1985, 9, 3)));
        ocupantes1.add(new Ocupante("Tomas Diaz", hoy.minusYears(3)));
        Reserva enCurso = alojamiento.crearReservaDirecta("1094000222", "A102", hoy, hoy.plusDays(3),
                ocupantes1, ahora);
        if (enCurso != null) {
            alojamiento.confirmarReserva(enCurso.getCodigo(), ahora);
            alojamiento.registrarPago(enCurso.getCodigo(), 100000, MedioPago.EFECTIVO, "Abono inicial", ahora);
            alojamiento.registrarLlegada(enCurso.getCodigo(), ahora);
        }

        //Reserva de portal pendiente de anticipo
        List<Ocupante> ocupantes2 = new ArrayList<>();
        ocupantes2.add(new Ocupante("Ana Torres", LocalDate.of(1998, 4, 12)));
        ocupantes2.add(new Ocupante("Juan Torres", LocalDate.of(1997, 2, 8)));
        alojamiento.crearReservaPortal("1094000111", "A101", hoy.plusDays(10), hoy.plusDays(13),
                ocupantes2, ahora);

        //Reserva directa confirmada que llega hoy (lista para check-in)
        List<Ocupante> ocupantes3 = new ArrayList<>();
        ocupantes3.add(new Ocupante("Marta Lopez", LocalDate.of(1990, 1, 25)));
        Reserva llegaHoy = alojamiento.crearReservaDirecta("1094000333", "A201", hoy, hoy.plusDays(2),
                ocupantes3, ahora);
        if (llegaHoy != null) {
            alojamiento.confirmarReserva(llegaHoy.getCodigo(), ahora);
        }

        //Reserva externa confirmada
        List<Ocupante> ocupantes4 = new ArrayList<>();
        ocupantes4.add(new Ocupante("Marta Lopez", LocalDate.of(1990, 1, 25)));
        ocupantes4.add(new Ocupante("Luis Lopez", LocalDate.of(1988, 6, 30)));
        ocupantes4.add(new Ocupante("Sara Lopez", hoy.minusYears(8)));
        Reserva externa = alojamiento.recibirReservaExterna("BK-55821", "Booking", "1094000333", "A202",
                hoy.plusDays(20), hoy.plusDays(24), ocupantes4, ahora);
        if (externa != null) {
            alojamiento.confirmarReserva(externa.getCodigo(), ahora);
        }

        //Conflicto de canal: el mismo codigo externo llega otra vez
        alojamiento.recibirReservaExterna("BK-55821", "Booking", "1094000333", "A101",
                hoy.plusDays(40), hoy.plusDays(42), ocupantes4, ahora);

        //Bloqueo y novedad
        alojamiento.registrarBloqueo("A201", hoy.plusDays(30), hoy.plusDays(33), "Mantenimiento del jacuzzi");
        alojamiento.registrarNovedad("A102", "Control del televisor danado", "Carlos Ruiz", ahora);
    }
}