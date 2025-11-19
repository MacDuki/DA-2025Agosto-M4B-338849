package PedroWattimo.Obligatorio.models;

public class Administrador extends Usuario {
    private boolean logueado;

    public Administrador() {
        super();
    }

    public static void validarDatosCreacion(int cedula, String nombreCompleto, String password)
            throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        Usuario.validarDatosCreacionBase(cedula, nombreCompleto, password);
    }

    public Administrador(int cedula, String nombreCompleto, String passwordPlano) {
        super(cedula, nombreCompleto, passwordPlano);
        this.logueado = false;
    }

    public String getPasswordHash() {
        return contraseña;
    }

    public boolean estaLogueado() {
        return this.logueado;
    }

    public void loguear() {
        this.logueado = true;
    }

    public void desloguear() {
        this.logueado = false;
    }
}
