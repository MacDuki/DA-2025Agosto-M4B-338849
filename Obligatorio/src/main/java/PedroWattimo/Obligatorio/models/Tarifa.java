package PedroWattimo.Obligatorio.models;

public class Tarifa {
    private double monto;
    private Categoria categoria;

    public Tarifa() {
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
