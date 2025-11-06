package PedroWattimo.Obligatorio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PedroWattimo.Obligatorio.dtos.PuestoDto;
import PedroWattimo.Obligatorio.dtos.TarifaDto;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.Puesto;
import PedroWattimo.Obligatorio.models.Tarifa;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

/**
 * Controlador REST para operaciones relacionadas con puestos de peaje.
 * Sin lógica de negocio: solo coordina request/response.
 */
@RestController
@RequestMapping("/puestos")
public class PuestosController {

    private final Fachada fachada = Fachada.getInstancia();

    /**
     * GET /puestos
     * Lista todos los puestos disponibles.
     * Respuesta: [ {id, nombre, direccion}, ... ]
     */
    @GetMapping
    public ResponseEntity<List<PuestoDto>> listarPuestos() {
        try {
            List<Puesto> puestos = fachada.obtenerPuestos();
            List<PuestoDto> dtos = new ArrayList<>();

            // Mapear a DTOs (sin lógica, solo transformación)
            for (int i = 0; i < puestos.size(); i++) {
                Puesto p = puestos.get(i);
                dtos.add(new PuestoDto((long) i, p.getNombre(), p.getDireccion()));
            }

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /puestos/{id}/tarifas
     * Obtiene las tarifas de un puesto específico.
     * Respuesta: [ {categoria, monto}, ... ]
     */
    @GetMapping("/{id}/tarifas")
    public ResponseEntity<List<TarifaDto>> obtenerTarifasPuesto(@PathVariable Long id) {
        try {
            Puesto puesto = fachada.obtenerPuestoPorId(id);
            List<Tarifa> tarifas = puesto.getTablaTarifas();
            List<TarifaDto> dtos = new ArrayList<>();

            // Mapear a DTOs (sin lógica, solo transformación)
            for (Tarifa t : tarifas) {
                String categoria = t.getCategoria() != null ? t.getCategoria().getNombre() : "Desconocida";
                dtos.add(new TarifaDto(categoria, t.getMonto()));
            }

            return ResponseEntity.ok(dtos);
        } catch (OblException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
