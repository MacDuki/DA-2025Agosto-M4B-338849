package PedroWattimo.Obligatorio.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.Respuesta;
import PedroWattimo.Obligatorio.dtos.EmularTransitoRequest;
import PedroWattimo.Obligatorio.dtos.EmularTransitoResultado;
import PedroWattimo.Obligatorio.dtos.NotificacionSSEDto;
import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.dtos.TarifaDto;
import PedroWattimo.Obligatorio.models.ConexionNavegador;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Puesto;
import PedroWattimo.Obligatorio.models.Tarifa;
import PedroWattimo.Obligatorio.models.Transito;
import PedroWattimo.Obligatorio.models.exceptions.OblException;
import PedroWattimo.Obligatorio.models.exceptions.TarifaNoDefinidaException;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/admin/emular-transito")
@Scope("session")
public class EmularTransitoController implements Observador {

    private final Fachada fachada = Fachada.getInstancia();
    private final ConexionNavegador conexionNavegador;

    public EmularTransitoController(ConexionNavegador conexionNavegador) {
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
        System.out.println("[EmularTransitoController] Evento recibido: " + evento);

        NotificacionSSEDto notificacion = new NotificacionSSEDto(
                "evento_sistema",
                "Cambio en el sistema detectado");

        Respuesta respuesta = new Respuesta("sistema_actualizado", notificacion);
        conexionNavegador.enviarJSON(List.of(respuesta));
    }

    @GetMapping("/puestos")
    public List<PuestoDto> listarPuestos() {
        List<Puesto> puestos = fachada.listarPuestos();
        return PuestoDto.desdeLista(puestos);
    }

    @GetMapping("/puestos/{id}/tarifas")
    public List<TarifaDto> obtenerTarifasPuesto(@PathVariable Long id) throws OblException {
        Puesto puesto = fachada.buscarPuestoPorId(id);
        List<Tarifa> tarifas = puesto.getTablaTarifas();
        return TarifaDto.desdeLista(tarifas);
    }

    @PostMapping
    public Respuesta emularTransito(@RequestBody EmularTransitoRequest request)
            throws OblException, TarifaNoDefinidaException {
        LocalDateTime fechaHora = LocalDateTime.parse(request.getFechaHora(), DateTimeFormatter.ISO_DATE_TIME);

        Transito transito = fachada.emularTransito(
                request.getPuestoId(),
                request.getMatricula(),
                fechaHora);

        EmularTransitoResultado resultado = new EmularTransitoResultado(transito);
        return new Respuesta("ok", resultado);
    }
}
