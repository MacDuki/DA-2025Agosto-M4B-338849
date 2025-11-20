package PedroWattimo.Obligatorio.dtos;

import PedroWattimo.Obligatorio.models.SesionPropietario;

public class PropietarioAutenticadoDTO {
    private int cedula;
    private String nombreCompleto;
    private String estado;

    public PropietarioAutenticadoDTO() {
    }

    public PropietarioAutenticadoDTO(int cedula, String nombreCompleto, String estado) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
    }

    public PropietarioAutenticadoDTO(SesionPropietario sesion) {
        this.cedula = sesion.getPropietario().getCedula();
        this.nombreCompleto = sesion.getPropietario().getNombreCompleto();
        this.estado = sesion.getPropietario().getEstadoActual() != null
                ? sesion.getPropietario().getEstadoActual().nombre()
                : "HABILITADO";
    }

    public int getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
