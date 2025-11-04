package PedroWattimo.Obligatorio.dtos;

import java.time.LocalDateTime;

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
