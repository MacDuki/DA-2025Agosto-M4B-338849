package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaPuestos {
    private List<Puesto> puestos = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();

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

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Categoria> getCategorias() {
        return List.copyOf(categorias);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Categoria> obtenerCategoriasInternas() {
        return categorias;
    }

    /**
     * Patrón Experto: delega en el Puesto la consulta de sus tarifas.
     * Retorna copia inmutable para proteger el encapsulamiento.
     */
    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto == null ? List.of() : puesto.getTablaTarifas();
    }

    /**
     * Busca un puesto por su ID (para este caso usamos el nombre como identificador
     * único).
     * En un sistema real con BD, sería por Long id.
     */
    public Puesto obtenerPorId(Long id) throws OblException {
        // Como no hay IDs numéricos en el modelo actual, usamos el índice como ID
        if (id == null || id < 0 || id >= puestos.size()) {
            throw new OblException("Puesto no encontrado con ID: " + id);
        }
        return puestos.get(id.intValue());
    }

    /**
     * Busca un puesto por su nombre.
     */
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
