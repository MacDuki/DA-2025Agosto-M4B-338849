package PedroWattimo.Obligatorio.dtos;

public class PropietarioLoginRequest {
    private int cedula;
    private String password;

    public PropietarioLoginRequest() {
    }

    public int getCedula() {
        return cedula;
    }

    public String getPassword() {
        return password;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}