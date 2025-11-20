package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import PedroWattimo.Obligatorio.models.Transito;

public class TransitoDto {
    private String puesto;
    private String matricula;
    private String categoria;
    private double montoTarifa;
    private String nombreBonificacion;
    private double montoBonificacion;
    private double montoPagado;
    private LocalDateTime fechaHora;

    public TransitoDto() {
    }

    public TransitoDto(String puesto, String matricula, String categoria, double montoTarifa, String nombreBonificacion,
            double montoBonificacion, double montoPagado, LocalDateTime fechaHora) {
        this.puesto = puesto;
        this.matricula = matricula;
        this.categoria = categoria;
        this.montoTarifa = montoTarifa;
        this.nombreBonificacion = nombreBonificacion;
        this.montoBonificacion = montoBonificacion;
        this.montoPagado = montoPagado;
        this.fechaHora = fechaHora;
    }

    public TransitoDto(Transito t) {
        this.puesto = t.puesto() != null ? t.puesto().getNombre() : null;
        this.matricula = t.vehiculo() != null ? t.vehiculo().getMatricula() : null;
        this.categoria = t.categoriaVehiculo();
        this.montoTarifa = t.costoConTarifa();
        this.nombreBonificacion = t.nombreBonificacion();
        this.montoBonificacion = t.montoBonificacion();
        this.montoPagado = t.totalPagado();
        this.fechaHora = t.fechaHora();
    }

    public static List<TransitoDto> desdeLista(List<Transito> lista) {
        List<TransitoDto> ret = new ArrayList<>();
        for (Transito t : lista) {
            ret.add(new TransitoDto(t));
        }
        return ret;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMontoTarifa() {
        return montoTarifa;
    }

    public void setMontoTarifa(double montoTarifa) {
        this.montoTarifa = montoTarifa;
    }

    public String getNombreBonificacion() {
        return nombreBonificacion;
    }

    public void setNombreBonificacion(String nombreBonificacion) {
        this.nombreBonificacion = nombreBonificacion;
    }

    public double getMontoBonificacion() {
        return montoBonificacion;
    }

    public void setMontoBonificacion(double montoBonificacion) {
        this.montoBonificacion = montoBonificacion;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
