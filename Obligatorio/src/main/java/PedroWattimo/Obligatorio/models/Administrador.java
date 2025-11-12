package PedroWattimo.Obligatorio.models;

public class Administrador extends Usuario {
    private boolean logueado;

    public Administrador() {
        super();
    }

    /**
     * Crea un administrador almacenando el hash de la contraseña.
     */
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

    /**
     * Marca al administrador como logueado. Es idempotente.
     */
    public void loguear() {
        this.logueado = true;
    }

    /**
     * Marca al administrador como deslogueado. Es idempotente.
     */
    public void desloguear() {
        this.logueado = false;
    }
}
