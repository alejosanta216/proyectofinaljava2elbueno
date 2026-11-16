import com.example.proyectofinaljava2.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas del apartamento: estados, tarifas, bloqueos y novedades
class ApartamentoTest {

    private Apartamento apartamento() {
        return new Apartamento("A1", "Suite Palma", 2, 4, 100000, 150000, 200000);
    }

    //Cada temporada devuelve su tarifa
    @Test
    void tarifaSegunTemporada() {
        Apartamento apartamento = apartamento();
        long[] esperadas = {100000, 150000, 200000};
        long[] obtenidas = {apartamento.obtenerTarifa(TipoTemporada.BAJA),
                apartamento.obtenerTarifa(TipoTemporada.MEDIA), apartamento.obtenerTarifa(TipoTemporada.ALTA)};
        assertArrayEquals(esperadas, obtenidas);
        assertFalse(apartamento.actualizarTarifas(0, 1, 1));
    }

    //Fuera de servicio exige motivo y Ocupado no se pone a mano
    @Test
    void cambiosDeEstadoManuales() {
        Apartamento apartamento = apartamento();
        assertFalse(apartamento.cambiarEstado(EstadoApartamento.FUERA_SERVICIO, ""));
        assertFalse(apartamento.cambiarEstado(EstadoApartamento.OCUPADO, ""));
        assertTrue(apartamento.cambiarEstado(EstadoApartamento.FUERA_SERVICIO, "Dano en la tuberia"));
        assertEquals("Dano en la tuberia", apartamento.getMotivoFueraServicio());
        assertTrue(apartamento.cambiarEstado(EstadoApartamento.PREPARADO, ""));
        assertEquals("", apartamento.getMotivoFueraServicio());
    }

    //Un apartamento ocupado no se puede cambiar a mano
    @Test
    void apartamentoOcupadoNoCambiaAMano() {
        Apartamento apartamento = apartamento();
        apartamento.ocupar();
        assertFalse(apartamento.cambiarEstado(EstadoApartamento.PREPARADO, ""));
        apartamento.liberarParaPreparacion();
        assertEquals(EstadoApartamento.PENDIENTE_PREPARACION, apartamento.getEstado());
    }

    //El bloqueo incluye sus dos extremos
    @Test
    void bloqueoIncluyeExtremos() {
        Apartamento apartamento = apartamento();
        assertTrue(apartamento.agregarBloqueo(new Bloqueo(LocalDate.of(2027, 5, 1), LocalDate.of(2027, 5, 3), "Pintura")));
        assertTrue(apartamento.estaBloqueado(LocalDate.of(2027, 5, 1)));
        assertTrue(apartamento.estaBloqueado(LocalDate.of(2027, 5, 3)));
        assertFalse(apartamento.estaBloqueado(LocalDate.of(2027, 5, 4)));
        assertFalse(apartamento.agregarBloqueo(new Bloqueo(LocalDate.of(2027, 5, 9), LocalDate.of(2027, 5, 8), "Mal")));
    }

    //Las listas que devuelve no alteran las originales
    @Test
    void listasProtegidas() {
        Apartamento apartamento = apartamento();
        apartamento.agregarCaracteristica("Balcon");
        apartamento.registrarNovedad(new Novedad(LocalDateTime.of(2027, 3, 1, 9, 0), "Grifo roto", "Recepcion"));
        apartamento.getCaracteristicas().clear();
        apartamento.getListaNovedades().clear();
        assertIterableEquals(List.of("Balcon"), apartamento.getCaracteristicas());
        assertEquals(1, apartamento.getCantidadNovedades());
    }
}
