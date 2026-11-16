import com.example.proyectofinaljava2.model.PoliticaCancelacion;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas de la politica de cancelacion
class PoliticaCancelacionTest {
    private final LocalDateTime inicio = LocalDateTime.of(2027, 3, 20, 15, 0);
    private final PoliticaCancelacion politica = new PoliticaCancelacion();

    //Mas de 7 dias antes: devuelve todo lo pagado
    @Test
    void masDeSieteDiasDevuelveTodo() {
        assertEquals(300000, politica.calcularDevolucion(400000, 300000, inicio.minusDays(8), inicio));
        assertEquals(0, politica.calcularRetencion(400000, inicio.minusDays(8), inicio));
    }

    //Entre 2 y 7 dias: retiene el 50 % del valor de la estancia
    @Test
    void entreDosYSieteDiasRetieneLaMitad() {
        assertEquals(200000, politica.calcularRetencion(400000, inicio.minusDays(5), inicio));
        assertEquals(100000, politica.calcularDevolucion(400000, 300000, inicio.minusDays(5), inicio));
    }

    //Menos de 48 horas: retiene todo
    @Test
    void menosDeCuarentaYOchoHorasNoDevuelve() {
        assertEquals(400000, politica.calcularRetencion(400000, inicio.minusHours(30), inicio));
        assertEquals(0, politica.calcularDevolucion(400000, 300000, inicio.minusHours(30), inicio));
    }

    //Si pago menos de lo retenido, la devolucion es 0 (nunca negativa)
    @Test
    void devolucionNuncaEsNegativa() {
        long devolucion = politica.calcularDevolucion(400000, 50000, inicio.minusDays(3), inicio);
        assertEquals(0, devolucion);
        assertFalse(devolucion < 0);
    }

    //La copia es otro objeto con los mismos valores
    @Test
    void copiarCreaOtroObjetoIgual() {
        PoliticaCancelacion copia = politica.copiar();
        assertNotSame(politica, copia);
        assertEquals(politica.getDiasDevolucionTotal(), copia.getDiasDevolucionTotal());
        assertEquals(politica.getPorcentajeRetencionParcial(), copia.getPorcentajeRetencionParcial());
    }

    //Una configuracion sin sentido no es valida
    @Test
    void configuracionInvalida() {
        assertTrue(politica.esValida());
        assertFalse(new PoliticaCancelacion(1, 72, 50).esValida());
        assertFalse(new PoliticaCancelacion(7, 48, 150).esValida());
    }
}
