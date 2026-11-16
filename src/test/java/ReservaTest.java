import com.example.proyectofinaljava2.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas del ciclo de vida de la reserva y su folio
class ReservaTest {
    private final LocalDateTime ahora = LocalDateTime.of(2027, 3, 1, 9, 0);
    private final LocalDate entrada = LocalDate.of(2027, 3, 5);
    private final LocalDate salida = LocalDate.of(2027, 3, 7);

    private Huesped huesped() {
        return new Huesped("Ana", "111", LocalDate.of(1990, 1, 1), "300", "ana@correo.com", "ana", "123");
    }

    private Apartamento apartamento() {
        return new Apartamento("A1", "Suite Palma", 2, 4, 100000, 150000, 200000);
    }

    //Cotizacion fija: 2 noches de 2 personas a 100000 = 400000
    private Cotizacion cotizacion() {
        List<NocheCotizada> noches = new ArrayList<>();
        noches.add(new NocheCotizada(entrada, TipoTemporada.BAJA, 100000, 2, 200000));
        noches.add(new NocheCotizada(entrada.plusDays(1), TipoTemporada.BAJA, 100000, 2, 200000));
        return new Cotizacion(noches, 400000);
    }

    private List<Ocupante> ocupantes() {
        return List.of(new Ocupante("Ana", LocalDate.of(1990, 1, 1)), new Ocupante("Luis", LocalDate.of(1988, 4, 2)));
    }

    private ReservaPortal portal(Apartamento apartamento) {
        return new ReservaPortal("R-0001", huesped(), apartamento, entrada, salida, ocupantes(),
                cotizacion(), new PoliticaCancelacion(), ahora);
    }

    //La reserva nace Pendiente, con el alojamiento cargado en el folio
    @Test
    void reservaNacePendiente() {
        Reserva reserva = portal(apartamento());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(400000, reserva.getSaldo());
        assertEquals(2, reserva.getCantidadNoches());
        assertNull(reserva.getFechaHoraLlegada());
    }

    //Portal solo se confirma con el anticipo del 30 %
    @Test
    void portalExigeAnticipo() {
        ReservaPortal reserva = portal(apartamento());
        assertEquals(120000, reserva.calcularAnticipo());
        reserva.registrarPago("Abono", 119999, MedioPago.TARJETA, ahora);
        assertFalse(reserva.confirmar());
        reserva.registrarPago("Abono", 1, MedioPago.EFECTIVO, ahora);
        assertTrue(reserva.confirmar());
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    //Directa y Externa se confirman sin anticipo; la externa paga comision del 15 %
    @Test
    void polimorfismoPorCanal() {
        Apartamento apartamento = apartamento();
        Reserva directa = new ReservaDirecta("R-0002", huesped(), apartamento, entrada, salida, ocupantes(),
                cotizacion(), new PoliticaCancelacion(), ahora);
        Reserva externa = new ReservaExterna("R-0003", "BK-1", "Booking", huesped(), apartamento, entrada, salida,
                ocupantes(), cotizacion(), new PoliticaCancelacion(), ahora);
        assertTrue(directa.puedeConfirmarse());
        assertTrue(externa.puedeConfirmarse());
        assertEquals(0, directa.calcularComision());
        assertEquals(60000, externa.calcularComision());
        assertNotEquals(directa.getCanal(), externa.getCanal());
    }

    //Solo se permiten los cambios de estado de la tabla
    @Test
    void transicionesInvalidasSeRechazan() {
        ReservaPortal reserva = portal(apartamento());
        assertFalse(reserva.registrarSalida(ahora));
        assertFalse(reserva.registrarLlegada(entrada.atTime(15, 0)));
        assertFalse(EstadoReserva.FINALIZADA.puedeCambiarA(EstadoReserva.EN_CURSO));
        assertTrue(EstadoReserva.PENDIENTE.puedeCambiarA(EstadoReserva.CANCELADA));
    }

    //Check-in y check-out cambian la reserva y el apartamento
    @Test
    void checkInYCheckOut() {
        Apartamento apartamento = apartamento();
        ReservaPortal reserva = portal(apartamento);
        reserva.registrarPago("Anticipo", 120000, MedioPago.TARJETA, ahora);
        reserva.confirmar();

        assertFalse(reserva.registrarLlegada(entrada.minusDays(1).atTime(15, 0)));
        apartamento.cambiarEstado(EstadoApartamento.EN_PREPARACION, "");
        assertFalse(reserva.registrarLlegada(entrada.atTime(15, 0)));
        apartamento.cambiarEstado(EstadoApartamento.PREPARADO, "");

        assertTrue(reserva.registrarLlegada(entrada.atTime(15, 0)));
        assertEquals(EstadoApartamento.OCUPADO, apartamento.getEstado());
        assertNotNull(reserva.getFechaHoraLlegada());

        assertTrue(reserva.registrarSalida(salida.atTime(11, 0)));
        assertEquals(EstadoReserva.FINALIZADA, reserva.getEstado());
        assertEquals(EstadoApartamento.PENDIENTE_PREPARACION, apartamento.getEstado());
    }

    //A las 24 horas sin confirmar se cancela sola y devuelve lo pagado
    @Test
    void vencimientoDeVeinticuatroHoras() {
        ReservaPortal reserva = portal(apartamento());
        reserva.registrarPago("Abono", 10000, MedioPago.EFECTIVO, ahora);
        assertFalse(reserva.vencerSiCorresponde(ahora.plusHours(23)));
        assertTrue(reserva.vencerSiCorresponde(ahora.plusHours(24)));
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(10000, reserva.getValorDevolucion());
        assertEquals(0, reserva.getSaldo());
    }

    //No-show solo desde las 10:00 p. m. y retiene el valor de la estancia
    @Test
    void noShow() {
        ReservaPortal reserva = portal(apartamento());
        reserva.registrarPago("Pago", 450000, MedioPago.EFECTIVO, ahora);
        reserva.confirmar();
        assertFalse(reserva.declararNoShow(entrada.atTime(21, 59)));
        assertTrue(reserva.declararNoShow(entrada.atTime(22, 0)));
        assertEquals(EstadoReserva.NO_SHOW, reserva.getEstado());
        assertEquals(50000, reserva.getValorDevolucion());
        assertEquals(0, reserva.getSaldo());
    }

    //Los pagos no se modifican: una correccion agrega un movimiento contrario
    @Test
    void foliosInalterables() {
        ReservaPortal reserva = portal(apartamento());
        reserva.registrarPago("Abono", 100000, MedioPago.TRANSFERENCIA, ahora);
        Pago primerPago = reserva.getFolio().getListaPagos().get(0);
        reserva.corregirPago("Abono duplicado", 100000, MedioPago.TRANSFERENCIA, ahora);
        assertEquals(2, reserva.getFolio().getListaPagos().size());
        assertEquals(primerPago, reserva.getFolio().getListaPagos().get(0));
        assertEquals(400000, reserva.getSaldo());
        reserva.getFolio().getListaPagos().clear();
        assertEquals(2, reserva.getFolio().getListaPagos().size());
    }
}
