package PedroWattimo.Obligatorio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.NotificacionDto;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.NotificacionesBorradasDto;
import PedroWattimo.Obligatorio.dtos.PropietarioAutenticadoDTO;
import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.dtos.PropietarioResumenDto;
import PedroWattimo.Obligatorio.dtos.TransitoDto;
import PedroWattimo.Obligatorio.dtos.VehiculoResumenDto;
import PedroWattimo.Obligatorio.models.AsignacionBonificacion;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Notificacion;
import PedroWattimo.Obligatorio.models.Propietario;
import PedroWattimo.Obligatorio.models.Transito;
import PedroWattimo.Obligatorio.models.Vehiculo;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import jakarta.servlet.http.HttpSession;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/propietarios/dashboard")
@Scope("session")
public class DashboardPropietarioController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public DashboardPropietarioController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;

        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "dashboard_actualizado",
                "Hay cambios en tu dashboard");

        Respuesta respuesta = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    @PostMapping
    public List<Respuesta> obtenerDashboard(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        Propietario p = fachada.buscarPropietarioPorCedula(propietario.getCedula());
        if (p == null) {
            throw new OblException("El propietario no existe");
        }

        // Convertir a DTO en el controlador
        PropietarioDashboardDto dto = new PropietarioDashboardDto();
        dto.setPropietario(new PropietarioResumenDto(
                p.getNombreCompleto(),
                p.getEstadoActual() != null ? p.getEstadoActual().nombre() : "HABILITADO",
                p.getSaldoActual()));

        List<BonificacionAsignadaDto> bonos = new ArrayList<>();
        for (AsignacionBonificacion ab : p.bonificacionesAsignadas()) {
            String nombre = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
            String puesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
            bonos.add(new BonificacionAsignadaDto(nombre, puesto, ab.getFechaHora()));
        }
        dto.setBonificaciones(bonos);

        List<VehiculoResumenDto> vehs = new ArrayList<>();
        for (Vehiculo v : p.vehiculos()) {
            vehs.add(new VehiculoResumenDto(
                    v.getMatricula(), v.getModelo(), v.getColor(),
                    p.cantidadTransitosDe(v), p.totalGastadoPor(v)));
        }
        dto.setVehiculos(vehs);

        List<TransitoDto> transDtos = new ArrayList<>();
        for (Transito t : p.transitosOrdenadosDesc()) {
            transDtos.add(new TransitoDto(
                    t.puesto() != null ? t.puesto().getNombre() : null,
                    t.vehiculo() != null ? t.vehiculo().getMatricula() : null,
                    t.categoriaVehiculo(),
                    t.costoConTarifa(),
                    t.nombreBonificacion(),
                    t.montoBonificacion(),
                    t.totalPagado(),
                    t.fechaHora()));
        }
        dto.setTransitos(transDtos);

        List<NotificacionDto> notifDtos = new ArrayList<>();
        for (Notificacion n : p.notificacionesOrdenadasDesc()) {
            notifDtos.add(new NotificacionDto(n.getFechaHora(), n.getMensaje()));
        }
        dto.setNotificaciones(notifDtos);

        dto.setVersion(fachada.versionDashboardDePropietario(propietario.getCedula()));

        Respuesta respuesta = new Respuesta("dashboard", dto);
        return List.of(respuesta);
    }

    @PostMapping("/notificaciones/borrar")
    public List<Respuesta> borrarNotificaciones(HttpSession session) throws OblException {
        PropietarioAutenticadoDTO propietario = (PropietarioAutenticadoDTO) session.getAttribute("propietario");

        if (propietario == null) {
            throw new OblException("No hay sesión activa. Por favor, inicie sesión.");
        }

        int borradas = fachada.borrarNotificacionesDePropietario(propietario.getCedula());

        NotificacionesBorradasDto resultado = new NotificacionesBorradasDto(
                borradas,
                borradas == 0
                        ? "No hay notificaciones para borrar"
                        : "Se borraron " + borradas + " notificaciones");

        Respuesta respuesta = new Respuesta("notificacionesBorradas", resultado);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "notificaciones_borradas",
                "Notificaciones borradas");
        Respuesta notifSSE = new Respuesta("dashboard_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(notifSSE));

        return List.of(respuesta);
    }
}
