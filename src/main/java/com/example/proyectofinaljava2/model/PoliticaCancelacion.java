package com.example.proyectofinaljava2.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//Politica de cancelacion configurable
public class PoliticaCancelacion {
    //Atributos
    private int diasDevolucionTotal;        // Mas dias: devuelve todo
    private int horasRetencionTotal;        // Menos horas: retiene todo
    private int porcentajeRetencionParcial; // Entre ambos: retiene este %

    //Constructor con la politica que pide el documento: 7 dias, 48 horas, 50 %
    public PoliticaCancelacion() {
        this(7, 48, 50);
    }

    public PoliticaCancelacion(int diasDevolucionTotal, int horasRetencionTotal,
                               int porcentajeRetencionParcial) {
        this.diasDevolucionTotal = diasDevolucionTotal;
        this.horasRetencionTotal = horasRetencionTotal;
        this.porcentajeRetencionParcial = porcentajeRetencionParcial;
    }

    //Getters
    public int getDiasDevolucionTotal() {
        return diasDevolucionTotal;
    }

    public int getHorasRetencionTotal() {
        return horasRetencionTotal;
    }

    public int getPorcentajeRetencionParcial() {
        return porcentajeRetencionParcial;
    }

    //Valida que la configuracion tenga sentido
    public boolean esValida() {
        return horasRetencionTotal >= 0
                && diasDevolucionTotal * 24 >= horasRetencionTotal
                && porcentajeRetencionParcial >= 0 && porcentajeRetencionParcial <= 100;
    }

    //Copia para congelar la politica dentro de una reserva
    public PoliticaCancelacion copiar() {
        return new PoliticaCancelacion(diasDevolucionTotal, horasRetencionTotal, porcentajeRetencionParcial);
    }

    //Calcula cuanto se retiene segun la antelacion
    public long calcularRetencion(long valorEstancia, LocalDateTime momento, LocalDateTime inicioEstancia) {
        long horasAntelacion = ChronoUnit.HOURS.between(momento, inicioEstancia);

        if (horasAntelacion > diasDevolucionTotal * 24L) {
            return 0;
        }
        if (horasAntelacion >= horasRetencionTotal) {
            return valorEstancia * porcentajeRetencionParcial / 100;
        }
        return valorEstancia;
    }

    //Calcula la devolucion (nunca deja deuda)
    public long calcularDevolucion(long valorEstancia, long totalPagado,
                                   LocalDateTime momento, LocalDateTime inicioEstancia) {
        long retencion = calcularRetencion(valorEstancia, momento, inicioEstancia);
        long devolucion = totalPagado - retencion;
        if (devolucion < 0) {
            return 0;
        }
        return devolucion;
    }
}