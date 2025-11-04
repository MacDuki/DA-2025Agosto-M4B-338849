package PedroWattimo.Obligatorio.dtos;

public class VehiculoResumenDto {
    private String matricula;
    private String modelo;
    private String color;
    private int cantidadTransitos;
    private double montoTotalGastado;

    public VehiculoResumenDto() {
    }

    public VehiculoResumenDto(String matricula, String modelo, String color, int cantidadTransitos,
            double montoTotalGastado) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.cantidadTransitos = cantidadTransitos;
        this.montoTotalGastado = montoTotalGastado;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCantidadTransitos() {
        return cantidadTransitos;
    }

    public void setCantidadTransitos(int cantidadTransitos) {
        this.cantidadTransitos = cantidadTransitos;
    }

    public double getMontoTotalGastado() {
        return montoTotalGastado;
    }

    public void setMontoTotalGastado(double montoTotalGastado) {
        this.montoTotalGastado = montoTotalGastado;
    }
}
