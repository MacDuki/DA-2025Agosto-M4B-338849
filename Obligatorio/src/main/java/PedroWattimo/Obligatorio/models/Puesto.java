package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

public class Puesto {
    private String nombre;
    private String direccion;
    private List<Tarifa> tablaTarifas;

    public Puesto() {
        this.tablaTarifas = new ArrayList<>();
    }

    public Puesto(String nombre, String direccion) {
        this();
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    // Encapsulamiento: retornar copias inmutables para prevenir modificaciones
    // externas
    public List<Tarifa> getTablaTarifas() {
        return tablaTarifas == null ? List.of() : List.copyOf(tablaTarifas);
    }

    // Patrón Experto: el Puesto conoce sus propias tarifas
    // Método interno para agregar tarifas (uso package-private para SeedData)
    List<Tarifa> obtenerTablaTarifasInterna() {
        return tablaTarifas;
    }

    /**
     * Patrón Experto: el Puesto sabe encontrar la tarifa para una categoría
     * específica.
     * Lanza TarifaNoDefinidaException si no existe tarifa para la categoría.
     */
    public Tarifa tarifaPara(Categoria cat) throws TarifaNoDefinidaException {
        if (cat == null) {
            throw new TarifaNoDefinidaException("La categoría no puede ser nula");
        }
        if (tablaTarifas == null || tablaTarifas.isEmpty()) {
            throw new TarifaNoDefinidaException(
                    "No hay tarifas definidas en el puesto " + (nombre != null ? nombre : "desconocido"));
        }
        for (Tarifa t : tablaTarifas) {
            if (t.getCategoria() != null &&
                    (t.getCategoria().equals(cat) ||
                            (t.getCategoria().getNombre() != null
                                    && t.getCategoria().getNombre().equals(cat.getNombre())))) {
                return t;
            }
        }
        throw new TarifaNoDefinidaException(
                "No existe tarifa para la categoría " + cat.getNombre() +
                        " en el puesto " + (nombre != null ? nombre : "desconocido"));
    }

}
