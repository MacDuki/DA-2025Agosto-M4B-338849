package PedroWattimo.Obligatorio.dtos;

public class PropietarioResumenDto {
    private String nombreCompleto;
    private String estado;
    private int saldoActual;

    public PropietarioResumenDto() {
    }

    public PropietarioResumenDto(String nombreCompleto, String estado, int saldoActual) {
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.saldoActual = saldoActual;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(int saldoActual) {
        this.saldoActual = saldoActual;
    }
}
