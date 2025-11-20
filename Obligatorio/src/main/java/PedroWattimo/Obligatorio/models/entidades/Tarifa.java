package PedroWattimo.Obligatorio.models.entidades;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class Tarifa {
    private double monto;
    private Categoria categoria;

    public Tarifa() {
    }

    public static void validarDatosCreacion(double monto, Categoria categoria) throws OblException {
        if (monto <= 0) {
            throw new OblException("El monto de la tarifa debe ser mayor a 0");
        }
        if (categoria == null) {
            throw new OblException("La tarifa debe tener una categoría");
        }
    }

    public Tarifa(double monto, Categoria categoria) {
        this.monto = monto;
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
