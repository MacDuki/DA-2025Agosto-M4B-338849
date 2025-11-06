package PedroWattimo.Obligatorio.dtos;

/**
 * DTO para representar una tarifa de un puesto.
 */
public class TarifaDto {
    private String categoria;
    private double monto;

    public TarifaDto() {
    }

    public TarifaDto(String categoria, double monto) {
        this.categoria = categoria;
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
