package PedroWattimo.Obligatorio.models;

import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SistemaEstados {
    private final List<Estado> estados;

    protected SistemaEstados() {
        this.estados = new ArrayList<>();

        this.estados.add(FabricaEstados.crearHabilitado());
        this.estados.add(FabricaEstados.crearPenalizado());
        this.estados.add(FabricaEstados.crearSuspendido());
        this.estados.add(FabricaEstados.crearDeshabilitado());
    }

    public List<Estado> listarEstados() {
        return List.copyOf(estados);
    }

    public Estado buscarPorNombre(String nombreEstado) throws OblException {
        return FabricaEstados.crear(nombreEstado);
    }

    public List<PedroWattimo.Obligatorio.dtos.EstadoDto> listarEstadosDto() {
        List<PedroWattimo.Obligatorio.dtos.EstadoDto> estadoDtos = new ArrayList<>();
        for (Estado estado : estados) {
            estadoDtos.add(new PedroWattimo.Obligatorio.dtos.EstadoDto(estado.nombre()));
        }
        return estadoDtos;
    }
}
