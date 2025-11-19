package PedroWattimo.Obligatorio.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import PedroWattimo.Obligatorio.models.Propietario;
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

        fachada.registrarObservador(this);
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
        List<PuestoDto> dtos = new ArrayList<>();
        for (int i = 0; i < puestos.size(); i++) {
            Puesto p = puestos.get(i);
            dtos.add(new PuestoDto((long) i, p.getNombre(), p.getDireccion()));
        }
        return dtos;
    }

    @GetMapping("/puestos/{id}/tarifas")
    public List<TarifaDto> obtenerTarifasPuesto(@PathVariable Long id) throws OblException {
        Puesto puesto = fachada.buscarPuestoPorId(id);
        List<Tarifa> tarifas = puesto.getTablaTarifas();
        List<TarifaDto> dtos = new ArrayList<>();

        for (Tarifa t : tarifas) {
            String categoria = t.getCategoria() != null ? t.getCategoria().getNombre() : "Desconocida";
            dtos.add(new TarifaDto(categoria, t.getMonto()));
        }

        return dtos;
    }

    @PostMapping
    public Respuesta emularTransito(@RequestBody EmularTransitoRequest request)
            throws OblException, TarifaNoDefinidaException {
        LocalDateTime fechaHora = LocalDateTime.parse(request.getFechaHora(), DateTimeFormatter.ISO_DATE_TIME);

        Transito transito = fachada.emularTransito(
                request.getPuestoId(),
                request.getMatricula(),
                fechaHora);

        Propietario prop = transito.vehiculo().getPropietario();
        String nombrePropietario = prop.getNombreCompleto();
        String estadoPropietario = prop.getEstadoActual() != null ? prop.getEstadoActual().nombre() : "HABILITADO";
        String categoria = transito.categoriaVehiculo();
        String nombreBonificacion = transito.nombreBonificacion();
        double montoBase = transito.costoConTarifa();
        double montoBonificacion = transito.montoBonificacion();
        double montoAPagar = transito.totalPagado();
        int saldoActual = prop.getSaldoActual();

        EmularTransitoResultado resultado = new EmularTransitoResultado(
                nombrePropietario,
                estadoPropietario,
                categoria,
                nombreBonificacion,
                montoBase,
                montoBonificacion,
                montoAPagar,
                saldoActual);

        return new Respuesta("ok", resultado);
    }
}
