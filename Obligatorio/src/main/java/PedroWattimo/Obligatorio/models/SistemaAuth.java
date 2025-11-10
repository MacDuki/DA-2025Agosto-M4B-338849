package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.PropietarioDTO;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaAuth {
    private List<Administrador> administradores = new ArrayList<>();

    protected SistemaAuth() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Administrador> obtenerAdministradoresInternos() {
        return administradores;
    }

    /**
     * Autentica un propietario validando existencia y contraseña.
     * - Valida nulos/vacíos en credenciales
     * - Si no existe o la contraseña no coincide, lanza OblException
     */
    public Propietario autenticarPropietario(SistemaPropietarios sistemaPropietarios, String cedula, String password)
            throws OblException {
        if (cedula == null || cedula.isBlank() || password == null || password.isBlank()) {
            throw new OblException("Acceso denegado");
        }

        Propietario dueño = sistemaPropietarios.buscarPorCedula(cedula);
        if (dueño == null) {
            throw new OblException("Acceso denegado");
        }

        if (!dueño.passwordCorrecta(password)) {
            throw new OblException("Acceso denegado");
        }

        return dueño;
    }

    // --------------------------------------------------------------
    // Autenticación de Administradores
    // --------------------------------------------------------------
    public AdminAutenticadoDto loginAdmin(int cedula, String password) throws OblException {
        // Validaciones de entrada
        if (password == null || password.isBlank()) {
            throw new OblException("Acceso denegado");
        }

        Administrador admin = null;
        for (Administrador a : this.administradores) {
            if (a != null && a.getCedula() == cedula) {
                admin = a;
                break;
            }
        }

        if (admin == null || !admin.passwordCorrecta(password)) {
            throw new OblException("Acceso denegado");
        }

        if (admin.estaLogueado()) {
            throw new OblException("Ud. Ya está logueado");
        }

        admin.loguear();
        return new AdminAutenticadoDto(admin.getCedula(), admin.getNombreCompleto());
    }

    public void logoutAdmin(int cedula) throws OblException {
        Administrador admin = null;
        for (Administrador a : this.administradores) {
            if (a != null && a.getCedula() == cedula) {
                admin = a;
                break;
            }
        }
        if (admin == null) {
            throw new OblException("Acceso denegado");
        }
        admin.desloguear();
    }

    // --------------------------------------------------------------
    // Login de Propietario (DTO) - para que Fachada delegue en 1 línea
    // --------------------------------------------------------------
    public PropietarioDTO loginPropietario(SistemaPropietarios sistemaPropietarios, int cedula, String password)
            throws OblException {
        Propietario p = autenticarPropietario(sistemaPropietarios, String.valueOf(cedula), password);
        if (!p.puedeIngresar()) {
            throw new OblException("Usuario deshabilitado, no puede ingresar al sistema");
        }
        return new PropietarioDTO(
                p.getCedula(),
                p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : Estado.HABILITADO.nombre());
    }
}
