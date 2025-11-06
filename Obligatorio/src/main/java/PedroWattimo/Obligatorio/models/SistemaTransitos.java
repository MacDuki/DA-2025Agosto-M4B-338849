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

public class SistemaTransitos {
    private List<Transito> transitos = new ArrayList<Transito>();

    // Referencias a otros sistemas (inyectados desde Fachada)
    private SistemaPropietarios sistemaPropietarios;
    private SistemaPuestos sistemaPuestos;
    private SistemaBonificaciones sistemaBonificaciones;
    private SistemaNotificaciones sistemaNotificaciones;

    protected SistemaTransitos() {
    }

    // Métodos para inyectar dependencias (llamados desde Fachada)
    void setSistemaPropietarios(SistemaPropietarios sistemaPropietarios) {
        this.sistemaPropietarios = sistemaPropietarios;
    }

    void setSistemaPuestos(SistemaPuestos sistemaPuestos) {
        this.sistemaPuestos = sistemaPuestos;
    }

    void setSistemaBonificaciones(SistemaBonificaciones sistemaBonificaciones) {
        this.sistemaBonificaciones = sistemaBonificaciones;
    }

    void setSistemaNotificaciones(SistemaNotificaciones sistemaNotificaciones) {
        this.sistemaNotificaciones = sistemaNotificaciones;
    }

    // Encapsulamiento: retornar copia inmutable para prevenir modificaciones
    // externas
    public List<Transito> getTransitos() {
        return List.copyOf(transitos);
    }

    // Método interno para acceso directo (package-private para uso de SeedData)
    List<Transito> obtenerTransitosInternos() {
        return transitos;
    }

    /**
     * Consulta los tránsitos de un vehículo en un puesto específico en una fecha.
     * Necesario para calcular bonificación de Frecuentes.
     */
    public List<Transito> transitosDe(Vehiculo veh, Puesto puesto, LocalDate fecha) {
        if (veh == null || puesto == null || fecha == null)
            return List.of();

        return transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(veh))
                .filter(t -> t.puesto() != null && t.puesto().equals(puesto))
                .filter(t -> t.fechaHora() != null && t.fechaHora().toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }

    /**
     * Registra un tránsito en el sistema y en las entidades relacionadas.
     * Patrón Experto: el Sistema coordina el registro en todas las colecciones.
     */
    public Transito registrarTransito(Puesto puesto, Vehiculo veh, Tarifa tarifa,
            double montoBonif, double montoPagado,
            LocalDateTime fh, Bonificacion bonifAplicada) {
        Transito t = new Transito(puesto, veh, tarifa, montoBonif, montoPagado, fh, bonifAplicada);

        // Registrar en el sistema
        this.transitos.add(t);

        // Registrar en las entidades relacionadas (delegación)
        if (puesto != null)
            puesto.registrarTransito(t);
        if (veh != null) {
            veh.registrarTransito(t);
            if (veh.getPropietario() != null) {
                veh.getPropietario().registrarTransito(t);
            }
        }

        return t;
    }

    /**
     * CU: Emular tránsito.
     * Orquesta todo el proceso: validaciones, cálculo de bonificación, débito,
     * registro de tránsito y notificaciones.
     * 
     * Patrón Experto: el SistemaTransitos coordina el CU completo, pero delega
     * cada responsabilidad específica en la entidad experta correspondiente.
     */
    public EmularTransitoResultado emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {

        // 1. Obtener puesto
        Puesto puesto = sistemaPuestos.obtenerPorId(puestoId);

        // 2. Obtener propietario por matrícula del vehículo
        Propietario prop = sistemaPropietarios.propietarioPorMatricula(matricula);

        // 3. Validar estado del propietario - Delega en Estado
        Estado estado = prop.getEstadoActual();
        if (estado != null && !estado.permiteTransitar()) {
            if (estado.equals(Estado.DESHABILITADO)) {
                throw new OblException("El propietario del vehículo está deshabilitado, no puede realizar tránsitos");
            }
            if (estado.equals(Estado.SUSPENDIDO)) {
                throw new OblException("El propietario del vehículo está suspendido, no puede realizar tránsitos");
            }
        }

        // 4. Obtener vehículo del propietario - Delega en Propietario
        Vehiculo veh = prop.buscarVehiculoPorMatricula(matricula);
        if (veh == null) {
            throw new OblException("El vehículo no pertenece al propietario");
        }

        // 5. Obtener categoría y tarifa - Delega en Vehículo y Puesto
        Categoria cat = veh.getCategoria();
        Tarifa tarifa = puesto.tarifaPara(cat);
        double montoBase = tarifa.getMonto();

        // 6. Calcular bonificación - Delega en Estado, Bonificacion y Propietario
        double montoBonif = 0.0;
        Bonificacion bonifAplicada = null;

        if (estado == null || estado.permiteBonificaciones()) {
            Optional<Bonificacion> bonifOpt = sistemaBonificaciones.bonificacionVigente(prop, puesto);
            if (bonifOpt.isPresent()) {
                bonifAplicada = bonifOpt.get();
                montoBonif = bonifAplicada.calcularDescuento(prop, veh, puesto, tarifa, fechaHora, this);
                // Clamp: no puede ser mayor que el monto base
                montoBonif = Math.min(montoBonif, montoBase);
                montoBonif = Math.max(montoBonif, 0.0);
            }
        }

        // 7. Calcular monto a pagar
        double montoAPagar = montoBase - montoBonif;

        // 8. Verificar saldo - Delega en Propietario
        if (prop.saldoInsuficientePara(montoAPagar)) {
            throw new OblException(String.format("Saldo insuficiente: $%.2f", prop.getSaldoActual()));
        }

        // 9. Debitar saldo - Delega en Propietario
        prop.debitar(montoAPagar);

        // 10. Registrar tránsito
        registrarTransito(puesto, veh, tarifa, montoBonif, montoAPagar, fechaHora, bonifAplicada);

        // 11. Notificaciones - Delega en Estado y Propietario
        if (estado == null || estado.permiteNotificaciones()) {
            // Notificación de tránsito
            String mensajeTransito = String.format("[%s] Pasaste por el puesto %s con el vehículo %s",
                    fechaHora.toString(), puesto.getNombre(), veh.getMatricula());
            sistemaNotificaciones.registrar(prop, mensajeTransito, fechaHora);

            // Alerta de saldo bajo - Delega en Propietario
            if (prop.debeAlertarSaldo()) {
                String mensajeSaldo = String.format("[%s] Tu saldo actual es $%d. Te recomendamos hacer una recarga",
                        fechaHora.toString(), prop.getSaldoActual());
                sistemaNotificaciones.registrar(prop, mensajeSaldo, fechaHora);
            }
        }

        // 12. Construir DTO de respuesta
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

    // Este metodo debe retornar transito (mantenido por compatibilidad, ya
    // obsoleto)
    @Deprecated
    public void emularTransito(Puesto puesto, String matriculaVehiculo, LocalDateTime fechaHora) {
        // Lógica para emular un tránsito (simulación)
        return;
    }
}
