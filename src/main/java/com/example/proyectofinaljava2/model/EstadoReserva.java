package com.example.proyectofinaljava2.model;

//Estados de una reserva y los cambios permitidos entre ellos
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    EN_CURSO,
    FINALIZADA,
    CANCELADA,
    NO_SHOW;

    //Dice si se permite pasar al nuevo estado
    public boolean puedeCambiarA(EstadoReserva nuevo) {
        switch (this) {
            case PENDIENTE:
                return nuevo == CONFIRMADA || nuevo == CANCELADA;
            case CONFIRMADA:
                return nuevo == EN_CURSO || nuevo == CANCELADA || nuevo == NO_SHOW;
            case EN_CURSO:
                return nuevo == FINALIZADA;
            default:
                return false; // FINALIZADA, CANCELADA y NO_SHOW son finales
        }
    }

    //Una reserva es activa si esta Pendiente, Confirmada o En curso
    public boolean esActiva() {
        return this == PENDIENTE || this == CONFIRMADA || this == EN_CURSO;
    }
}