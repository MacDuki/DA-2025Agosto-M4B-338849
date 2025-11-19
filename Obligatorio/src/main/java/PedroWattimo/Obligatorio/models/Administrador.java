package PedroWattimo.Obligatorio.models;

public class Administrador extends Usuario {
    private boolean logueado;

    public Administrador() {
        super();
    }

    /**
     * Valida los datos para crear un administrador.
     * Patrón Experto: el Administrador conoce sus reglas de validación.
     */
    public static void validarDatosCreacion(int cedula, String nombreCompleto, String password)
            throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        if (cedula <= 0) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La cédula debe ser mayor a 0");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException(
                    "El nombre completo no puede estar vacío");
        }
        if (password == null || password.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La contraseña no puede estar vacía");
        }
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
