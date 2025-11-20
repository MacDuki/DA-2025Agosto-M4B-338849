package PedroWattimo.Obligatorio.models.entidades;

/**
 * Estado Deshabilitado: no puede ingresar al sistema, no puede realizar
 * tránsitos,
 * no se le pueden asignar bonificaciones.
 */
public class EstadoDeshabilitado extends Estado {

    public EstadoDeshabilitado() {
        super("Deshabilitado");
    }

    @Override
    public boolean permiteTransitar() {
        return false;
    }

    @Override
    public boolean permiteBonificaciones() {
        return false;
    }

    @Override
    public boolean permiteNotificaciones() {
        return true;
    }

    @Override
    public boolean permiteIngresar() {
        return false;
    }
}
