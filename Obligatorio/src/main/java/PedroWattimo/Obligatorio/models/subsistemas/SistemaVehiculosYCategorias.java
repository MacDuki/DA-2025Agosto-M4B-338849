package PedroWattimo.Obligatorio.models.subsistemas;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.entidades.*;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaVehiculosYCategorias {
    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();

    protected SistemaVehiculosYCategorias() {
    }

    public List<Vehiculo> getVehiculos() {
        return List.copyOf(vehiculos);
    }

    public List<Categoria> getCategorias() {
        return List.copyOf(categorias);
    }

    public Categoria buscarCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            return null;
        return categorias.stream()
                .filter(c -> nombre.equalsIgnoreCase(c.getNombre()))
                .findFirst()
                .orElse(null);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        if (matricula == null || matricula.isBlank())
            return null;
        return vehiculos.stream()
                .filter(v -> matricula.equalsIgnoreCase(v.getMatricula()))
                .findFirst()
                .orElse(null);
    }

    public Categoria agregarCategoria(String nombre) throws OblException {
        Categoria.validarDatosCreacion(nombre);

        Categoria existente = buscarCategoriaPorNombre(nombre);
        if (existente != null) {
            throw new OblException("Ya existe una categoría con el nombre: " + nombre);
        }

        Categoria nuevaCategoria = new Categoria(nombre);
        categorias.add(nuevaCategoria);
        return nuevaCategoria;
    }

    public Vehiculo registrarVehiculo(int cedulaPropietario, String matricula, String modelo,
            String color, String nombreCategoria) throws OblException {
        Vehiculo.validarDatosCreacion(matricula, modelo, nombreCategoria);

        Vehiculo existente = buscarVehiculoPorMatricula(matricula);
        if (existente != null) {
            throw new OblException("Ya existe un vehículo con la matrícula: " + matricula);
        }

        Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedulaPropietario);
        if (propietario == null) {
            throw new OblException("No existe el propietario con cédula: " + cedulaPropietario);
        }

        Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);
        if (categoria == null) {
            throw new OblException("No existe la categoría: " + nombreCategoria);
        }

        Vehiculo nuevoVehiculo = propietario.registrarVehiculo(matricula, modelo, color, categoria);
        vehiculos.add(nuevoVehiculo);
        return nuevoVehiculo;
    }
}
