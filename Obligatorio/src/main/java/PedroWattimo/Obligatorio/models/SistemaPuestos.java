package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

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

}
