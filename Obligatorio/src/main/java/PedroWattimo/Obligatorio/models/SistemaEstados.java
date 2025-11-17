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
     * 
     */
    public Estado buscarPorNombre(String nombreEstado) throws OblException {
        return FabricaEstados.crear(nombreEstado);
    }

    /**
     * Lista todos los estados como DTOs.
     * Patrón Experto: el sistema que conoce los estados transforma a DTO.
     */
    public List<PedroWattimo.Obligatorio.dtos.EstadoDto> listarEstadosDto() {
        List<PedroWattimo.Obligatorio.dtos.EstadoDto> estadoDtos = new ArrayList<>();
        for (Estado estado : estados) {
            estadoDtos.add(new PedroWattimo.Obligatorio.dtos.EstadoDto(estado.nombre()));
        }
        return estadoDtos;
    }
}
