package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.dtos.AdminAutenticadoDto;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaAuth {
    private List<Administrador> administradores = new ArrayList<>();

    public List<Administrador> getAdministradores() {
        return List.copyOf(administradores);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Administrador> obtenerAdministradoresInternos() {
        return administradores;
    }

    protected SistemaAuth() {
    }

    // --------------------------------------------------------------
    // Autenticación de propietarios
    // --------------------------------------------------------------

    public Propietario autenticarPropietario(SistemaPropietarios sistemaPropietarios, int cedula, String password)
            throws OblException {
        if (password == null || password.isBlank()) {
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

    public PropietarioAutenticadoDTO loginPropietario(SistemaPropietarios sistemaPropietarios, int cedula,
            String password)
            throws OblException {
        Propietario p = autenticarPropietario(sistemaPropietarios, cedula, password);
        if (!p.puedeIngresar()) {
            throw new OblException("Usuario deshabilitado, no puede ingresar al sistema");
        }
        return new PropietarioAutenticadoDTO(
                p.getCedula(),
                p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : Estado.HABILITADO.nombre());
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

}
