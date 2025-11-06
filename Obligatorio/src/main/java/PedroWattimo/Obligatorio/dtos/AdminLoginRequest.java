package PedroWattimo.Obligatorio.dtos;

public class AdminLoginRequest {
    private int cedula;
    private String password;

    public AdminLoginRequest() {
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
