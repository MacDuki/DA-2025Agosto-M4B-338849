package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * SistemaEstados: gestiona los estados posibles de los propietarios.
 * Proporciona operaciones para listar estados y buscar por nombre.
 */
public class SistemaEstados {
    private final List<Estado> estados;

    protected SistemaEstados() {
        this.estados = new ArrayList<>();
        // Inicializar con los estados disponibles usando la fábrica
        this.estados.add(FabricaEstados.crearHabilitado());
        this.estados.add(FabricaEstados.crearPenalizado());
        this.estados.add(FabricaEstados.crearSuspendido());
        this.estados.add(FabricaEstados.crearDeshabilitado());
    }

    /**
     * Lista todos los estados disponibles en el sistema.
     */
    public List<Estado> listarEstados() {
        return List.copyOf(estados);
    }

    /**
     * Busca un estado por su nombre usando la fábrica.
     * Lanza excepción si no existe.
     */
    public Estado buscarPorNombre(String nombreEstado) throws OblException {
        return FabricaEstados.crear(nombreEstado);
    }
}
