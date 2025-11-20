package PedroWattimo.Obligatorio.models.subsistemas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.Categoria;
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
            Propietario prop = veh.getPropietario();
            prop.registrarTransito(t);
            List<String> mensajes = prop.generarNotificacionesTransito(t);
            for (String mensaje : mensajes) {
                Fachada.getInstancia().registrarNotificacionPropietario(prop, mensaje, fh);
            }
        }
        return t;
    }

    public Transito emularTransito(Long puestoId, String matricula, LocalDateTime fechaHora)
            throws OblException, TarifaNoDefinidaException {

        Puesto puesto = Fachada.getInstancia().buscarPuestoPorId(puestoId);
        Propietario prop = Fachada.getInstancia().buscarPropietarioPorMatriculaInterno(matricula);

        prop.validarPuedeTransitar();

        Vehiculo veh = prop.buscarVehiculoPorMatricula(matricula);

        Categoria cat = veh.getCategoria();
        Tarifa tarifa = puesto.tarifaPara(cat);

        List<Transito> transitosPrevios = transitosDe(veh, puesto, fechaHora.toLocalDate());

        Object[] resultadoPago = prop.procesarPagoTransito(veh, puesto, tarifa, fechaHora, transitosPrevios);
        double montoAPagar = (double) resultadoPago[0];
        double montoBonif = (double) resultadoPago[1];
        Bonificacion bonifAplicada = (Bonificacion) resultadoPago[2];

        Transito transito = registrarTransito(puesto, veh, tarifa, montoBonif, montoAPagar, fechaHora, bonifAplicada);

        Fachada.getInstancia().avisar(Eventos.TRANSITO_REGISTRADO);

        return transito;
    }

}
