package PedroWattimo.Obligatorio.controllers;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.BonificacionDto;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PropietarioConBonificacionesDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.models.entidades.Bonificacion;
import PedroWattimo.Obligatorio.models.entidades.ConexionNavegador;
import PedroWattimo.Obligatorio.models.entidades.Propietario;
import PedroWattimo.Obligatorio.models.entidades.Puesto;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.subsistemas.Fachada;
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
    }

    @PostMapping("/vistaConectada")
    public void vistaConectada() {
        fachada.agregarObservador(this);
    }

    @PostMapping("/vistaCerrada")
    public void vistaCerrada() {
        fachada.eliminarObservador(this);
    }

    @Override
    public void actualizar(Observable origen, Object evento) {
        System.out.println("[AsignarBonificacionesController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "evento_sistema",
                "Cambio en el sistema detectado");

        Respuesta respuesta = new Respuesta("sistema_actualizado", notificacion);
        conexionNavegador.enviarJSON(Respuesta.lista(respuesta));
    }

    @PostMapping("/bonificaciones")
    public List<Respuesta> listarBonificaciones() {
        List<Bonificacion> bonificaciones = fachada.listarBonificaciones();
        List<BonificacionDto> bonificacionDtos = BonificacionDto.desdeLista(bonificaciones);
        return Respuesta.lista(new Respuesta("bonificacionesCargadas", bonificacionDtos));
    }

    @PostMapping("/puestos")
    public List<Respuesta> listarPuestos() {
        List<Puesto> puestos = fachada.listarPuestos();
        List<PuestoDto> puestoDtos = PuestoDto.desdeLista(puestos);
        return Respuesta.lista(new Respuesta("puestosCargados", puestoDtos));
    }

    @PostMapping("/propietario")
    public List<Respuesta> buscarPropietario(@RequestParam String cedula) throws OblException {
        Propietario propietario = fachada.buscarPropietarioPorCedula(cedula);
        PropietarioConBonificacionesDto dto = new PropietarioConBonificacionesDto(propietario);
        return Respuesta.lista(new Respuesta("propietarioEncontrado", dto));
    }

    @PostMapping
    public List<Respuesta> asignarBonificacion(
            @RequestParam String cedula,
            @RequestParam String nombreBonificacion,
            @RequestParam String nombrePuesto) throws OblException {
        fachada.asignarBonificacion(
                cedula,
                nombreBonificacion,
                nombrePuesto);

        return Respuesta.lista(new Respuesta("bonificacionAsignada", "Bonificación asignada exitosamente"));
    }
}
