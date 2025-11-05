package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

public class SistemaPuestos {
    private List<Puesto> puestos = new ArrayList<>();

    protected SistemaPuestos() {
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Puesto> getPuestos() {
        return List.copyOf(puestos);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Puesto> obtenerPuestosInternos() {
        return puestos;
    }

    /**
     * Patrón Experto: delega en el Puesto la consulta de sus tarifas.
     * Retorna copia inmutable para proteger el encapsulamiento.
     */
    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto == null ? List.of() : puesto.getTablaTarifas();
    }

}
