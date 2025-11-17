package PedroWattimo.Obligatorio.dtos;

public class CambiarEstadoRequest {
    private String cedula;
    private String nuevoEstado;

    public CambiarEstadoRequest() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
