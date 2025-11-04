package PedroWattimo.Obligatorio.models;

import PedroWattimo.Obligatorio.models.exceptions.CredencialesInvalidasException;

public class SistemaAuth {

    /**
     * Autentica un propietario validando existencia y contraseña.
     * - Valida nulos/vacíos en credenciales
     * - Si no existe o la contraseña no coincide, lanza
     * CredencialesInvalidasException
     */
    public Propietario autenticarPropietario(SistemaPropietarios sistemaPropietarios, String cedula, String password) {
        if (cedula == null || cedula.isBlank() || password == null || password.isBlank()) {
            throw new CredencialesInvalidasException();
        }

        Propietario dueño = sistemaPropietarios.buscarPorCedula(cedula);
        if (dueño == null) {
            throw new CredencialesInvalidasException();
        }

        if (!dueño.passwordCorrecta(password)) {
            throw new CredencialesInvalidasException();
        }

        return dueño;
    }
}
