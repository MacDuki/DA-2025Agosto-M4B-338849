package PedroWattimo.Obligatorio.models.subsistemas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.Categoria;
import PedroWattimo.Obligatorio.models.entidades.Estado;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Puesto;
import PedroWattimo.Obligatorio.models.entidades.Tarifa;
import PedroWattimo.Obligatorio.models.entidades.Transito;
import PedroWattimo.Obligatorio.models.entidades.Vehiculo;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

public class SistemaTransitos {

    public enum Eventos {
        TRANSITO_REGISTRADO
    }

    private final List<Transito> transitos = new ArrayList<>();

    protected SistemaTransitos() {
    }

    public List<Transito> getTransitos() {
        return List.copyOf(transitos);
    }

    public List<Transito> transitosDe(Vehiculo veh, Puesto puesto, LocalDate fecha) {
        if (veh == null || puesto == null || fecha == null)
            return List.of();
        return transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(veh))
                .filter(t -> t.puesto() != null && t.puesto().equals(puesto))
                .filter(t -> t.fechaHora() != null && t.fechaHora().toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }

    public Transito registrarTransito(Puesto puesto, Vehiculo veh, Tarifa tarifa,
            double montoBonif, double montoPagado,
            LocalDateTime fh, Bonificacion bonifAplicada) {
        Transito t = new Transito(puesto, veh, tarifa, montoBonif, montoPagado, fh, bonifAplicada);
        this.transitos.add(t);
        if (veh != null && veh.getPropietario() != null) {
            veh.getPropietario().registrarTransito(t);
        }
        return t;
    }

    public Transito emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {

        Puesto puesto = Fachada.getInstancia().buscarPuestoPorId(puestoId);
        Propietario prop = Fachada.getInstancia().buscarPropietarioPorMatriculaInterno(matricula);

        Estado estado = prop.getEstadoActual();
        if (estado != null && !estado.permiteTransitar()) {
            throw new OblException("El propietario del vehículo no puede realizar tránsitos en su estado actual");
        }

        Vehiculo veh = prop.buscarVehiculoPorMatricula(matricula);
        if (veh == null) {
            throw new OblException("El vehículo no pertenece al propietario");
        }

        Categoria cat = veh.getCategoria();
        Tarifa tarifa = puesto.tarifaPara(cat);
        double montoBase = tarifa.getMonto();

        double montoBonif = 0.0;
        Bonificacion bonifAplicada = null;
        if (estado == null || estado.permiteBonificaciones()) {
            Optional<Bonificacion> bonifOpt = Fachada.getInstancia().obtenerBonificacionVigenteInterno(prop, puesto);
            if (bonifOpt.isPresent()) {
                bonifAplicada = bonifOpt.get();
                montoBonif = bonifAplicada.calcularDescuento(prop, veh, puesto, tarifa, fechaHora, this);
                montoBonif = Math.min(montoBonif, montoBase);
                montoBonif = Math.max(montoBonif, 0.0);
            }
        }

        double montoAPagar = montoBase - montoBonif;
        if (prop.saldoInsuficientePara(montoAPagar)) {
            throw new OblException(String.format("Saldo insuficiente: $%d", prop.getSaldoActual()));
        }
        prop.debitarSaldo(montoAPagar);

        Transito transito = registrarTransito(puesto, veh, tarifa, montoBonif, montoAPagar, fechaHora, bonifAplicada);

        if (estado == null || estado.permiteNotificaciones()) {
            String mensajeTransito = String.format("[%s] Pasaste por el puesto %s con el vehículo %s",
                    fechaHora.toString(), puesto.getNombre(), veh.getMatricula());
            Fachada.getInstancia().registrarNotificacionInterno(prop, mensajeTransito, fechaHora);
            if (prop.debeAlertarSaldo()) {
                String mensajeSaldo = String.format("[%s] Tu saldo actual es $%d. Te recomendamos hacer una recarga",
                        fechaHora.toString(), prop.getSaldoActual());
                Fachada.getInstancia().registrarNotificacionInterno(prop, mensajeSaldo, fechaHora);
            }
        }

        // Notificar a través de la Fachada
        Fachada.getInstancia().avisar(Eventos.TRANSITO_REGISTRADO);

        return transito;
    }

}
