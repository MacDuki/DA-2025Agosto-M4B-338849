package PedroWattimo.Obligatorio.dtos;

import PedroWattimo.Obligatorio.models.SesionAdmin;

public class AdminAutenticadoDto {
    private int cedula;
    private String nombreCompleto;

    public AdminAutenticadoDto() {
    }

    public AdminAutenticadoDto(int cedula, String nombreCompleto) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
    }

    public AdminAutenticadoDto(SesionAdmin sesion) {
        this.cedula = sesion.getAdministrador().getCedula();
        this.nombreCompleto = sesion.getAdministrador().getNombreCompleto();
    }

    public int getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
