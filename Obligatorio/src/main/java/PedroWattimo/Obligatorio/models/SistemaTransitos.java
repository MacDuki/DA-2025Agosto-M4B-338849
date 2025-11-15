package PedroWattimo.Obligatorio.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;

/**
 * SistemaTransitos: se mantiene con responsabilidades de orquestación del CU
 * Emular Tránsito, sin absorber cálculos que pertenecen a entidades. Se ajusta
 * para utilizar el nuevo SistemaPropietarios (que maneja notificaciones) y
 * SistemaPuestosYTarifas.
 */
public class SistemaTransitos {
    private final List<Transito> transitos = new ArrayList<>();

    private SistemaPropietariosYAdmin sistemaPropietariosYAdmin;
    private SistemaPuestosYTarifas sistemaPuestosYTarifas;
    private SistemaBonificaciones sistemaBonificaciones;

    protected SistemaTransitos() {
    }

    void setSistemaPropietariosYAdmin(SistemaPropietariosYAdmin sistemaPropietariosYAdmin) {
        this.sistemaPropietariosYAdmin = sistemaPropietariosYAdmin;
    }

    void setSistemaPuestosYTarifas(SistemaPuestosYTarifas sistemaPuestosYTarifas) {
        this.sistemaPuestosYTarifas = sistemaPuestosYTarifas;
    }

    void setSistemaBonificaciones(SistemaBonificaciones sistemaBonificaciones) {
        this.sistemaBonificaciones = sistemaBonificaciones;
    }

    public List<Transito> getTransitos() {
        return List.copyOf(transitos);
    }

    List<Transito> obtenerTransitosInternos() {
        return transitos;
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

    public EmularTransitoResultado emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {

        Puesto puesto = sistemaPuestosYTarifas.obtenerPorId(puestoId);
        Propietario prop = sistemaPropietariosYAdmin.propietarioPorMatricula(matricula);

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
            Optional<Bonificacion> bonifOpt = sistemaBonificaciones.bonificacionVigente(prop, puesto);
            if (bonifOpt.isPresent()) {
                bonifAplicada = bonifOpt.get();
                montoBonif = bonifAplicada.calcularDescuento(prop, veh, puesto, tarifa, fechaHora, this);
                montoBonif = Math.min(montoBonif, montoBase);
                montoBonif = Math.max(montoBonif, 0.0);
            }
        }

        double montoAPagar = montoBase - montoBonif;
        if (prop.saldoInsuficientePara(montoAPagar)) {
            throw new OblException(String.format("Saldo insuficiente: $%.2f", prop.getSaldoActual()));
        }
        prop.debitar(montoAPagar);

        registrarTransito(puesto, veh, tarifa, montoBonif, montoAPagar, fechaHora, bonifAplicada);

        if (estado == null || estado.permiteNotificaciones()) {
            String mensajeTransito = String.format("[%s] Pasaste por el puesto %s con el vehículo %s",
                    fechaHora.toString(), puesto.getNombre(), veh.getMatricula());
            sistemaPropietariosYAdmin.registrarNotificacion(prop, mensajeTransito, fechaHora);
            if (prop.debeAlertarSaldo()) {
                String mensajeSaldo = String.format("[%s] Tu saldo actual es $%d. Te recomendamos hacer una recarga",
                        fechaHora.toString(), prop.getSaldoActual());
                sistemaPropietariosYAdmin.registrarNotificacion(prop, mensajeSaldo, fechaHora);
            }
        }

        return new EmularTransitoResultado(
                prop.getNombreCompleto(),
                estado != null ? estado.nombre() : Estado.HABILITADO.nombre(),
                cat != null ? cat.getNombre() : "Desconocida",
                bonifAplicada != null ? bonifAplicada.getNombre() : null,
                montoBase,
                montoBonif,
                montoAPagar,
                prop.getSaldoActual());
    }

    @Deprecated
    public void emularTransito(Puesto puesto, String matriculaVehiculo, LocalDateTime fechaHora) {
        return; // método obsoleto
    }
}
