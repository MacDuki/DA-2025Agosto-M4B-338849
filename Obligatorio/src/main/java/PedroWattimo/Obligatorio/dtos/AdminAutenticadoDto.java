package PedroWattimo.Obligatorio.dtos;

public class AdminAutenticadoDto {
    private int cedula;
    private String nombreCompleto;

    public AdminAutenticadoDto() {}

    public AdminAutenticadoDto(int cedula, String nombreCompleto) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
    }

    public int getCedula() { return cedula; }
    public String getNombreCompleto() { return nombreCompleto; }

    public void setCedula(int cedula) { this.cedula = cedula; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
}
