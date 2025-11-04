package PedroWattimo.Obligatorio.models;

public class Administrador {
    private String cedula;
    private String contraseña;
    private String nombreCompleto;

    public Administrador() {
    }

    public Administrador(String cedula, String contraseña, String nombreCompleto) {
        this.cedula = cedula;
        this.contraseña = contraseña;
        this.nombreCompleto = nombreCompleto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
