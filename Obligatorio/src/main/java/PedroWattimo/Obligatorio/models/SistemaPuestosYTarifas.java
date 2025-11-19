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

    /** Delegación al Puesto para conocer sus tarifas. */
    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto == null ? List.of() : puesto.getTablaTarifas();
    }

    /** Busca un puesto por índice/ID */
    public Puesto buscarPorId(Long id) throws OblException {
        if (id == null || id < 0 || id >= puestos.size()) {
            throw new OblException("Puesto no encontrado con ID: " + id);
        }
        return puestos.get(id.intValue());
    }

    /** Busca un puesto por nombre. */
    public Puesto buscarPorNombre(String nombre) throws OblException {
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
        return getPuestos();
    }

    // ---- Casos de uso: Alta de entidades ----

    /**
     * Agrega un nuevo puesto al sistema.
     * Valida que no exista un puesto con el mismo nombre.
     */
    public Puesto agregarPuesto(String nombre, String direccion) throws OblException {
        // Validar datos de creación (delegado al experto)
        Puesto.validarDatosCreacion(nombre, direccion);

        // Validar unicidad (responsabilidad del sistema)
        try {
            buscarPorNombre(nombre);
            throw new OblException("Ya existe un puesto con el nombre: " + nombre);
        } catch (OblException e) {
            // No existe, podemos agregarlo
        }

        Puesto nuevoPuesto = new Puesto(nombre, direccion);
        puestos.add(nuevoPuesto);
        return nuevoPuesto;
    }

    /**
     * Agrega una tarifa a un puesto específico.
     * Orquesta la resolución de objetos y delega al puesto.
     */
    public void agregarTarifaAPuesto(String nombrePuesto, double monto, String nombreCategoria) throws OblException {
        // Validar monto
        if (monto <= 0) {
            throw new OblException("El monto de la tarifa debe ser mayor a 0");
        }

        // Resolver puesto
        Puesto puesto = buscarPorNombre(nombrePuesto);

        // Resolver categoría a través de Fachada
        Categoria categoria = Fachada.getInstancia().buscarCategoriaPorNombreInterno(nombreCategoria);
        if (categoria == null) {
            throw new OblException("No existe la categoría: " + nombreCategoria);
        }

        // Delegar al puesto la adición de la tarifa (Patrón Experto)
        Tarifa nuevaTarifa = new Tarifa(monto, categoria);
        puesto.agregarTarifa(nuevaTarifa);
    }
}
