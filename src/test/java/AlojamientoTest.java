import com.example.proyectofinaljava2.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas de las reglas de negocio principales del alojamiento
class AlojamientoTest {
    private final LocalDateTime ahora = LocalDateTime.of(2027, 3, 1, 9, 0);

    //Crea el alojamiento con 2 apartamentos y 2 huespedes
    private Alojamiento alojamiento() {
        Alojamiento alojamiento = new Alojamiento("Aparta-Suites UQ");
        alojamiento.registrarApartamento(new Apartamento("A1", "Suite Palma", 2, 4, 100000, 150000, 200000));
        alojamiento.registrarApartamento(new Apartamento("A2", "Suite Cafe", 1, 2, 80000, 120000, 160000));
        alojamiento.registrarHuesped(new Huesped("Ana", "111", LocalDate.of(1990, 1, 1), "300", "ana@correo.com", "ana", "123"));
        alojamiento.registrarHuesped(new Huesped("Luis", "222", LocalDate.of(1985, 5, 5), "301", "luis@correo.com", "luis", "123"));
        return alojamiento;
    }

    private List<Ocupante> adultos(int cantidad) {
        List<Ocupante> ocupantes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            ocupantes.add(new Ocupante("Adulto " + i, LocalDate.of(1990, 1, 1)));
        }
        return ocupantes;
    }

    private LocalDate dia(int mes, int dia) {
        return LocalDate.of(2027, mes, dia);
    }

    //Regla principal: no se vende dos veces la misma noche, sin importar el canal
    @Test
    void sinDobleVenta() {
        Alojamiento alojamiento = alojamiento();
        assertNotNull(alojamiento.crearReservaDirecta("111", "A1", dia(3, 10), dia(3, 12), adultos(2), ahora));
        assertNull(alojamiento.crearReservaPortal("222", "A1", dia(3, 11), dia(3, 14), adultos(1), ahora));
        assertNotNull(alojamiento.crearReservaPortal("222", "A1", dia(3, 12), dia(3, 14), adultos(1), ahora));
        assertEquals(2, alojamiento.getListaReservas().size());
    }

    //Cotizacion noche por noche con temporadas y menores de 6 anios
    @Test
    void cotizacionPorTemporadaYEdad() {
        Alojamiento alojamiento = alojamiento();
        alojamiento.agregarPeriodoTemporada(TipoTemporada.MEDIA, dia(3, 11), dia(3, 20));
        alojamiento.agregarPeriodoTemporada(TipoTemporada.ALTA, dia(3, 11), dia(3, 11));
        List<Ocupante> grupo = List.of(new Ocupante("Adulto", LocalDate.of(1990, 1, 1)),
                new Ocupante("Nino 5", dia(3, 11).minusYears(5)),
                new Ocupante("Nino 6", dia(3, 10).minusYears(6)));

        Cotizacion cotizacion = alojamiento.cotizar("A1", dia(3, 10), dia(3, 13), grupo);

        assertEquals(3, cotizacion.getCantidadNoches());
        List<TipoTemporada> temporadas = new ArrayList<>();
        for (NocheCotizada noche : cotizacion.noches()) {
            temporadas.add(noche.temporada());
        }
        assertIterableEquals(List.of(TipoTemporada.BAJA, TipoTemporada.ALTA, TipoTemporada.MEDIA), temporadas);
        assertEquals(2, cotizacion.noches().get(0).personasQuePagan());
        assertEquals(900000, cotizacion.valorTotal());
    }

    //Capacidad, fechas y bloqueos
    @Test
    void validacionesAlReservar() {
        Alojamiento alojamiento = alojamiento();
        assertNull(alojamiento.crearReservaDirecta("222", "A2", dia(4, 1), dia(4, 2), adultos(3), ahora));
        assertNull(alojamiento.crearReservaDirecta("222", "A2", dia(4, 1), dia(4, 1), adultos(1), ahora));
        assertNull(alojamiento.crearReservaDirecta("222", "A2", dia(2, 1), dia(2, 3), adultos(1), ahora));
        assertTrue(alojamiento.registrarBloqueo("A2", dia(5, 1), dia(5, 3), "Pintura"));
        assertNull(alojamiento.crearReservaDirecta("222", "A2", dia(5, 3), dia(5, 4), adultos(1), ahora));
        assertTrue(alojamiento.getListaReservas().isEmpty());
    }

    //Externas: codigo repetido o choque quedan como conflicto, nunca reemplazan
    @Test
    void conflictosDeCanal() {
        Alojamiento alojamiento = alojamiento();
        Reserva externa = alojamiento.recibirReservaExterna("BK-1", "Booking", "111", "A1", dia(4, 1), dia(4, 3), adultos(2), ahora);
        assertNotNull(externa);
        assertNull(alojamiento.recibirReservaExterna("BK-1", "Booking", "222", "A2", dia(4, 1), dia(4, 3), adultos(1), ahora));
        assertNull(alojamiento.recibirReservaExterna("BK-2", "Booking", "222", "A1", dia(4, 2), dia(4, 4), adultos(1), ahora));
        assertEquals(2, alojamiento.getListaConflictos().size());
        assertSame(externa, alojamiento.buscarReservaPorCodigoExterno("BK-1"));
        assertEquals(1, alojamiento.getListaReservas().size());
    }

    //Cancelacion con la politica congelada al crear la reserva
    @Test
    void cancelacionConPoliticaCongelada() {
        Alojamiento alojamiento = alojamiento();
        Reserva reserva = alojamiento.crearReservaDirecta("111", "A1", dia(3, 20), dia(3, 22), adultos(2), ahora);
        alojamiento.confirmarReserva(reserva.getCodigo(), ahora);
        alojamiento.registrarPago(reserva.getCodigo(), 300000, MedioPago.TRANSFERENCIA, "Abono", ahora);
        LocalDateTime cancelacion = dia(3, 20).atTime(15, 0).minusDays(5);

        alojamiento.actualizarTarifas("A1", 1, 1, 1);
        alojamiento.configurarPoliticaCancelacion(1, 1, 100);

        assertEquals(400000, reserva.getValorEstancia());
        assertEquals(100000, alojamiento.calcularDevolucionCancelacion(reserva.getCodigo(), cancelacion));
        assertTrue(alojamiento.cancelarReserva(reserva.getCodigo(), cancelacion));
        assertEquals(0, reserva.getSaldo());
        assertFalse(alojamiento.cancelarReserva(reserva.getCodigo(), cancelacion));
    }

    //Check-in exige 3 horas desde la salida anterior del mismo apartamento
    @Test
    void tresHorasDePreparacion() {
        Alojamiento alojamiento = alojamiento();
        Reserva primera = alojamiento.crearReservaDirecta("111", "A1", dia(3, 5), dia(3, 7), adultos(1), ahora);
        Reserva segunda = alojamiento.crearReservaDirecta("222", "A1", dia(3, 7), dia(3, 8), adultos(1), ahora);
        alojamiento.confirmarReserva(primera.getCodigo(), ahora);
        alojamiento.confirmarReserva(segunda.getCodigo(), ahora);
        alojamiento.registrarLlegada(primera.getCodigo(), dia(3, 5).atTime(15, 0));
        alojamiento.registrarSalida(primera.getCodigo(), dia(3, 7).atTime(11, 0));
        alojamiento.cambiarEstadoApartamento("A1", EstadoApartamento.PREPARADO, "");

        assertFalse(alojamiento.registrarLlegada(segunda.getCodigo(), dia(3, 7).atTime(13, 0)));
        assertTrue(alojamiento.registrarLlegada(segunda.getCodigo(), dia(3, 7).atTime(15, 0)));
    }

    //Inicio de sesion por rol y empleado desvinculado
    @Test
    void seguridadPorRol() {
        Alojamiento alojamiento = alojamiento();
        alojamiento.registrarEmpleado(new Administrador("Admin", "900", "1", "admin@correo.com", "admin", "admin", dia(1, 1)));
        alojamiento.registrarEmpleado(new Recepcionista("Rece", "901", "1", "rece@correo.com", "rece", "123", dia(1, 1)));

        assertEquals(Rol.ADMINISTRADOR, alojamiento.iniciarSesion("admin", "admin").getRol());
        assertEquals(Rol.HUESPED, alojamiento.iniciarSesion("ana", "123").getRol());
        assertNull(alojamiento.iniciarSesion("ana", "mala"));
        assertTrue(alojamiento.desvincularRecepcionista("901"));
        assertNull(alojamiento.iniciarSesion("rece", "123"));
        assertFalse(alojamiento.registrarHuesped(new Huesped("Otro", "333", dia(1, 1), "1", "o@correo.com", "ana", "1")));
    }

    //Reportes del administrador
    @Test
    void reportes() {
        Alojamiento alojamiento = alojamiento();
        Reserva reserva = alojamiento.crearReservaDirecta("111", "A1", dia(3, 10), dia(3, 13), adultos(1), ahora);
        alojamiento.confirmarReserva(reserva.getCodigo(), ahora);
        alojamiento.registrarPago(reserva.getCodigo(), 50000, MedioPago.EFECTIVO, "Abono", ahora);
        alojamiento.registrarNovedad("A2", "Grifo roto", "Recepcion", ahora);
        alojamiento.registrarNovedad("A2", "Falta toalla", "Recepcion", ahora);
        alojamiento.registrarNovedad("A1", "Bombillo", "Recepcion", ahora);

        assertEquals(3, alojamiento.contarNochesOcupadas("A1", dia(3, 1), dia(3, 31)));
        assertEquals(50000, alojamiento.calcularIngresosPeriodo(dia(3, 1), dia(3, 31)));
        assertEquals(1, alojamiento.contarReservasPorCanal(CanalReserva.DIRECTA, null));
        assertSame(reserva, alojamiento.obtenerReservasConSaldoPendiente().get(0));

        List<String> ordenados = new ArrayList<>();
        for (Apartamento apartamento : alojamiento.obtenerApartamentosConMasNovedades()) {
            ordenados.add(apartamento.getCodigo() + " " + apartamento.getCantidadNovedades());
        }
        assertLinesMatch(List.of("A2 2", "A1 \\d"), ordenados);
    }
}
