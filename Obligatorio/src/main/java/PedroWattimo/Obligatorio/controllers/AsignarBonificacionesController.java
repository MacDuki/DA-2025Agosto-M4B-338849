package PedroWattimo.Obligatorio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.AsignarBonificacionRequest;
import PedroWattimo.Obligatorio.dtos.BonificacionAsignadaDto;
import PedroWattimo.Obligatorio.dtos.BonificacionDto;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.models.AsignacionBonificacion;
import PedroWattimo.Obligatorio.models.Bonificacion;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Propietario;
import PedroWattimo.Obligatorio.models.Puesto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/admin/asignar-bonificacion")
@Scope("session")
public class AsignarBonificacionesController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public AsignarBonificacionesController(ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
        fachada.registrarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        System.out.println("[AsignarBonificacionesController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "evento_sistema",
                "Cambio en el sistema detectado");

        Respuesta respuesta = new Respuesta("sistema_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    @GetMapping("/bonificaciones")
    public List<BonificacionDto> listarBonificaciones() {
        List<Bonificacion> bonificaciones = fachada.listarBonificaciones();
        List<BonificacionDto> dtos = new ArrayList<>();
        for (Bonificacion b : bonificaciones) {
            dtos.add(new BonificacionDto(b.getNombre(), b.getPorcentaje()));
        }
        return dtos;
    }

    @GetMapping("/puestos")
    public List<PuestoDto> listarPuestos() {
        List<Puesto> puestos = fachada.listarPuestos();
        List<PuestoDto> dtos = new ArrayList<>();
        for (int i = 0; i < puestos.size(); i++) {
            Puesto p = puestos.get(i);
            dtos.add(new PuestoDto((long) i, p.getNombre(), p.getDireccion()));
        }
        return dtos;
    }

    @GetMapping("/propietario")
    public Respuesta buscarPropietario(@RequestParam String cedula) throws OblException {
        Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);

        List<BonificacionAsignadaDto> bonificaciones = new ArrayList<>();
        for (AsignacionBonificacion ab : propietario.getAsignaciones()) {
            String nombreBonif = ab.getBonificacion() != null ? ab.getBonificacion().getNombre() : null;
            String nombrePuesto = ab.getPuesto() != null ? ab.getPuesto().getNombre() : null;
            bonificaciones.add(new BonificacionAsignadaDto(nombreBonif, nombrePuesto, ab.getFechaHora()));
        }

        PropietarioConBonificacionesDto dto = new PropietarioConBonificacionesDto(
                propietario.getNombreCompleto(),
                propietario.getEstadoActual() != null ? propietario.getEstadoActual().nombre() : "HABILITADO",
                bonificaciones);

        return new Respuesta("ok", dto);
    }

    @PostMapping
    public Respuesta asignarBonificacion(@RequestBody AsignarBonificacionRequest request) throws OblException {
        fachada.asignarBonificacion(
                request.getCedula(),
                request.getNombreBonificacion(),
                request.getNombrePuesto());

        return new Respuesta("ok", "Bonificación asignada exitosamente");
    }
}
