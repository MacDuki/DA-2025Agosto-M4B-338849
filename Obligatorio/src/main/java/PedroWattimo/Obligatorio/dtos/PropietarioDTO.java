package PedroWattimo.Obligatorio.dtos;

public class PropietarioDTO {
    private String cedula;
    private String nombreCompleto;
    private String estado;

    public PropietarioDTO() {}

    public PropietarioDTO(String cedula, String nombreCompleto, String estado) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
    }

    public String getCedula() { return cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEstado() { return estado; }

    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setEstado(String estado) { this.estado = estado; }
}
