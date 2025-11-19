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

    /**
     * Valida los datos para crear un puesto.
     * Patrón Experto: el Puesto conoce sus reglas de validación.
     */
    public static void validarDatosCreacion(String nombre, String direccion)
            throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException(
                    "El nombre del puesto no puede estar vacío");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException(
                    "La dirección del puesto no puede estar vacía");
        }
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

    public List<Tarifa> getTablaTarifas() {
        return tablaTarifas == null ? List.of() : List.copyOf(tablaTarifas);
    }

    // Patrón Experto: el Puesto conoce sus propias tarifas
    // Método interno para agregar tarifas (uso package-private para SeedData)
    List<Tarifa> obtenerTablaTarifasInterna() {
        return tablaTarifas;
    }

    /**
     * Verifica si ya existe una tarifa para una categoría específica.
     * Patrón Experto: el Puesto conoce sus tarifas.
     */
    public boolean tieneTarifaPara(Categoria categoria) {
        if (categoria == null || tablaTarifas == null) {
            return false;
        }
        return tablaTarifas.stream()
                .anyMatch(t -> t.getCategoria() != null && t.getCategoria().equals(categoria));
    }

    /**
     * Agrega una tarifa al puesto.
     * Patrón Experto: el Puesto gestiona sus propias tarifas.
     */
    public void agregarTarifa(Tarifa tarifa) throws PedroWattimo.Obligatorio.models.exceptions.OblException {
        if (tarifa == null) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La tarifa no puede ser nula");
        }
        if (tarifa.getCategoria() == null) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException("La tarifa debe tener una categoría");
        }
        if (tieneTarifaPara(tarifa.getCategoria())) {
            throw new PedroWattimo.Obligatorio.models.exceptions.OblException(
                    "Ya existe una tarifa para la categoría " + tarifa.getCategoria().getNombre() + " en el puesto "
                            + nombre);
        }
        tablaTarifas.add(tarifa);
    }

    /**
     * 
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
