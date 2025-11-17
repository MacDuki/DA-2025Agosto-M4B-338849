package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * SistemaPuestosYTarifas: fusiona gestión de Puestos y acceso a sus Tarifas.
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

    /** Obtener puesto por índice */
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

    /**
     * Lista todos los puestos del sistema.
     */
    public List<Puesto> listarPuestos() {
        return List.copyOf(puestos);
    }

    /**
     * Obtiene las tarifas de un puesto como DTOs.
     * Patrón Experto: el sistema que conoce los puestos transforma a DTO.
     */
    public List<PedroWattimo.Obligatorio.dtos.TarifaDto> obtenerTarifasDePuesto(Long puestoId) throws OblException {
        Puesto puesto = obtenerPorId(puestoId);
        List<Tarifa> tarifas = puesto.getTablaTarifas();
        List<PedroWattimo.Obligatorio.dtos.TarifaDto> dtos = new ArrayList<>();

        for (Tarifa t : tarifas) {
            String categoria = t.getCategoria() != null ? t.getCategoria().getNombre() : "Desconocida";
            dtos.add(new PedroWattimo.Obligatorio.dtos.TarifaDto(categoria, t.getMonto()));
        }

        return dtos;
    }

    /**
     * Lista todos los puestos como DTOs.
     * Patrón Experto: el sistema que conoce los puestos transforma a DTO.
     */
    public List<PedroWattimo.Obligatorio.dtos.PuestoDto> listarPuestosDto() {
        List<PedroWattimo.Obligatorio.dtos.PuestoDto> dtos = new ArrayList<>();
        for (int i = 0; i < puestos.size(); i++) {
            Puesto p = puestos.get(i);
            dtos.add(new PedroWattimo.Obligatorio.dtos.PuestoDto((long) i, p.getNombre(), p.getDireccion()));
        }
        return dtos;
    }
}
