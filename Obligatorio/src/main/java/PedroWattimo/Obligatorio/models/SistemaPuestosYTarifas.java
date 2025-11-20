package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaPuestosYTarifas {
    private final List<Puesto> puestos = new ArrayList<>();

    protected SistemaPuestosYTarifas() {
    }

    public List<Puesto> getPuestos() {
        return List.copyOf(puestos);
    }

    public List<Tarifa> tarifasDePuesto(Puesto puesto) {
        return puesto == null ? List.of() : puesto.getTablaTarifas();
    }

    public Puesto buscarPorId(Long id) throws OblException {
        if (id == null || id < 0 || id >= puestos.size()) {
            throw new OblException("Puesto no encontrado con ID: " + id);
        }
        return puestos.get(id.intValue());
    }

    public Puesto buscarPorNombre(String nombre) throws OblException {
        if (nombre == null || nombre.isBlank()) {
            throw new OblException("El nombre del puesto no puede estar vacío");
        }
        return puestos.stream()
                .filter(p -> nombre.equalsIgnoreCase(p.getNombre()))
                .findFirst()
                .orElseThrow(() -> new OblException("Puesto no encontrado: " + nombre));
    }

    public List<Puesto> listarPuestos() {
        return getPuestos();
    }

    public Puesto agregarPuesto(String nombre, String direccion) throws OblException {
        Puesto.validarDatosCreacion(nombre, direccion);

        if (puestos.stream().anyMatch(p -> nombre.equalsIgnoreCase(p.getNombre()))) {
            throw new OblException("Ya existe un puesto con el nombre: " + nombre);
        }

        Puesto nuevoPuesto = new Puesto(nombre, direccion);
        puestos.add(nuevoPuesto);
        return nuevoPuesto;
    }

    public void agregarTarifaAPuesto(String nombrePuesto, double monto, String nombreCategoria) throws OblException {
        Puesto puesto = buscarPorNombre(nombrePuesto);

        Categoria categoria = Fachada.getInstancia().buscarCategoriaPorNombreInterno(nombreCategoria);
        if (categoria == null) {
            throw new OblException("No existe la categoría: " + nombreCategoria);
        }

        Tarifa.validarDatosCreacion(monto, categoria);
        Tarifa nuevaTarifa = new Tarifa(monto, categoria);
        puesto.agregarTarifa(nuevaTarifa);
    }
}
