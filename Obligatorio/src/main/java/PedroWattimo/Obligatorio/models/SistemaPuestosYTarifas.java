package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * SistemaPuestosYTarifas: fusiona gestión de Puestos y acceso a sus Tarifas.
 * No mantiene Categorias (migradas a SistemaVehiculosYCategorias).
 */
public class SistemaPuestosYTarifas {
    private final List<Puesto> puestos = new ArrayList<>();

    protected SistemaPuestosYTarifas() {
    }

    // ---- Acceso encapsulado ----
    public List<Puesto> getPuestos() {
        return List.copyOf(puestos);
    }

    List<Puesto> obtenerPuestosInternos() {
        return puestos;
    }

    /** Delegación al Puesto para conocer sus tarifas. */
    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto == null ? List.of() : puesto.getTablaTarifas();
    }

    /** Obtener puesto por índice (ID lógico en memoria). */
    public Puesto obtenerPorId(Long id) throws OblException {
        if (id == null || id < 0 || id >= puestos.size()) {
            throw new OblException("Puesto no encontrado con ID: " + id);
        }
        return puestos.get(id.intValue());
    }

    /** Buscar puesto por nombre. */
    public Puesto obtenerPorNombre(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre del puesto no puede estar vacío");
        }
        return puestos.stream()
                .filter(p -> nombre.equalsIgnoreCase(p.getNombre()))
                .findFirst()
                .orElseThrow(() -> new OblException("Puesto no encontrado: " + nombre));
    }
}
