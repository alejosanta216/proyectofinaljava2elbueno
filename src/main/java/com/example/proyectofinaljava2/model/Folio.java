package com.example.proyectofinaljava2.model;

import java.util.ArrayList;
import java.util.List;

//Cuenta de una reserva: acumula cargos y pagos. Nada se modifica ni se elimina
public class Folio {
    //Atributos
    private List<Cargo> listaCargos;
    private List<Pago> listaPagos;

    //Constructor
    public Folio() {
        this.listaCargos = new ArrayList<>();
        this.listaPagos = new ArrayList<>();
    }

    //Devuelven copias: los movimientos solo entran por los metodos de registro
    public List<Cargo> getListaCargos() {
        return new ArrayList<>(listaCargos);
    }

    public List<Pago> getListaPagos() {
        return new ArrayList<>(listaPagos);
    }

    //Registra un cargo. Solo los de correccion pueden ser negativos
    public boolean registrarCargo(Cargo cargo) {
        if (cargo == null || cargo.valor() == 0) {
            return false;
        }
        if (cargo.valor() < 0 && cargo.tipo() != TipoCargo.CORRECCION) {
            return false;
        }
        listaCargos.add(cargo);
        return true;
    }

    //Registra un pago. Los negativos son devoluciones o correcciones
    public boolean registrarPago(Pago pago) {
        if (pago == null || pago.valor() == 0) {
            return false;
        }
        listaPagos.add(pago);
        return true;
    }

    public long calcularTotalCargos() {
        long total = 0;
        for (Cargo cargo : listaCargos) {
            total += cargo.valor();
        }
        return total;
    }

    public long calcularTotalPagos() {
        long total = 0;
        for (Pago pago : listaPagos) {
            total += pago.valor();
        }
        return total;
    }

    //Saldo = cargos - pagos. Positivo: el huesped debe
    public long calcularSaldo() {
        return calcularTotalCargos() - calcularTotalPagos();
    }
}