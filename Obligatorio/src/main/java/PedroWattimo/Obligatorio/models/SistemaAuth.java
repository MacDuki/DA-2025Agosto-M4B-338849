package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

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
}
