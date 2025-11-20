package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class Propietario extends Usuario {
    private int saldoActual;
    private int saldoMinimoAlerta;
    private Estado estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Transito> transitos;
    private List<AsignacionBonificacion> asignaciones;
    private List<Notificacion> notificaciones;

    public static void validarDatosCreacion(int cedula, String nombreCompleto, String password)
            throws OblException {
        Usuario.validarDatosCreacionBase(cedula, nombreCompleto, password);
    }

    public Propietario(int cedula, String nombreCompleto, String contraseña) {
        super(cedula, nombreCompleto, contraseña);
        this.vehiculos = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.saldoActual = 0;
        this.saldoMinimoAlerta = 0;
        this.estadoActual = FabricaEstados.crearHabilitado();
    }

    public int getSaldoActual() {
        return saldoActual;
    }

    public int getSaldoMinimoAlerta() {
        return saldoMinimoAlerta;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    // Encapsulamiento: retornar copias inmutables para prevenir modificaciones
    // externas
    public List<Vehiculo> getVehiculos() {
        return vehiculos == null ? List.of() : List.copyOf(vehiculos);
    }

    public List<Transito> getTransitos() {
        return transitos == null ? List.of() : List.copyOf(transitos);
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones == null ? List.of() : List.copyOf(asignaciones);
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones == null ? List.of() : List.copyOf(notificaciones);
    }

    public Propietario cambiarEstado(Estado nuevoEstado) {
        this.estadoActual = (nuevoEstado == null) ? FabricaEstados.crearHabilitado() : nuevoEstado;
        return this;
    }

    public Propietario ajustarSaldoMinimoAlerta(int nuevoMinimo) {
        this.saldoMinimoAlerta = Math.max(0, nuevoMinimo);
        return this;
    }

    public Propietario acreditarSaldo(double monto) {
        if (monto > 0) {
            this.saldoActual += (int) Math.round(monto);
        }
        return this;
    }

    public boolean debitarSaldo(double monto) {
        if (monto <= 0)
            return false;
        int montoRedondeado = (int) Math.round(monto);
        if (this.saldoActual - montoRedondeado < 0)
            return false;
        this.saldoActual -= montoRedondeado;
        if (this.saldoActual < 0)
            this.saldoActual = 0;
        return true;
    }

    public Vehiculo registrarVehiculo(String matricula, String modelo, String color, Categoria categoria) {
        Vehiculo v = new Vehiculo(matricula, modelo, color, categoria, this);
        if (this.vehiculos == null)
            this.vehiculos = new ArrayList<>();
        this.vehiculos.add(v);
        return v;
    }

    public boolean puedeIngresar() {
        if (this.estadoActual == null)
            return true;
        return this.estadoActual.permiteIngresar();
    }

    public boolean puedeTransitar() {
        if (this.estadoActual == null)
            return true;
        return this.estadoActual.permiteTransitar();
    }

    public boolean saldoInsuficientePara(double monto) {
        return this.saldoActual < monto;
    }

    public Optional<AsignacionBonificacion> bonificacionAsignadaPara(Puesto p) {
        if (p == null || this.asignaciones == null)
            return Optional.empty();
        return this.asignaciones.stream()
                .filter(ab -> ab.activaPara(p, this))
                .findFirst();
    }

    public boolean debeAlertarSaldo() {
        return this.saldoActual < this.saldoMinimoAlerta;
    }

    public void registrarNotificacion(String mensaje, LocalDateTime fh) {
        if (mensaje == null || fh == null)
            return;
        if (this.notificaciones == null)
            this.notificaciones = new ArrayList<>();
        Notificacion notif = new Notificacion(fh, mensaje, this);
        this.notificaciones.add(notif);
    }

    public void registrarNotificacionCambioEstado(Estado estadoActual, LocalDateTime fechaHora) {
        if (estadoActual == null || fechaHora == null)
            return;
        String mensaje = "Se ha cambiado tu estado en el sistema. Tu estado actual es " + estadoActual.nombre();
        registrarNotificacion(mensaje, fechaHora);
    }

    public void registrarTransito(Transito transito) {
        if (transito == null)
            return;
        if (this.transitos == null)
            this.transitos = new ArrayList<>();
        this.transitos.add(transito);
    }

    public boolean tieneBonificacionPara(Puesto puesto) {
        if (puesto == null || this.asignaciones == null)
            return false;
        return this.asignaciones.stream()
                .anyMatch(ab -> ab.activaPara(puesto, this));
    }

    public void validarAsignacionBonificacion(Bonificacion bonificacion, Puesto puesto)
            throws OblException {
        if (bonificacion == null) {
            throw new OblException("Debe especificar una bonificación");
        }
        if (puesto == null) {
            throw new OblException("Debe especificar un puesto");
        }
        if (this.estadoActual != null && !this.estadoActual.permiteIngresar()) {
            throw new OblException(
                    "El propietario esta deshabilitado. No se pueden asignar bonificaciones");
        }
        if (this.tieneBonificacionPara(puesto)) {
            throw new OblException(
                    "Ya tiene una bonificación asignada para ese puesto");
        }
    }

    public AsignacionBonificacion asignarBonificacion(Bonificacion bonificacion, Puesto puesto) throws OblException {
        return asignarBonificacion(bonificacion, puesto, LocalDateTime.now());
    }

    public AsignacionBonificacion asignarBonificacion(Bonificacion bonificacion, Puesto puesto,
            LocalDateTime fechaHora) throws OblException {
        if (bonificacion == null || puesto == null || fechaHora == null) {
            throw new OblException("Bonificación, puesto y fecha no pueden ser nulos");
        }
        if (this.asignaciones == null)
            this.asignaciones = new ArrayList<>();
        AsignacionBonificacion ab = new AsignacionBonificacion(fechaHora, this, puesto, bonificacion);
        this.asignaciones.add(ab);
        return ab;
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        if (matricula == null || this.vehiculos == null)
            return null;
        return this.vehiculos.stream()
                .filter(v -> matricula.equalsIgnoreCase(v.getMatricula()))
                .findFirst()
                .orElse(null);
    }

    public List<Notificacion> notificacionesOrdenadasDesc() {
        List<Notificacion> out = new ArrayList<>(this.notificaciones);
        out.sort((a, b) -> b.getFechaHora().compareTo(a.getFechaHora()));
        return out;
    }

    public List<Transito> transitosOrdenadosDesc() {
        // Solo retornar los tránsitos del propietario (ya registrados directamente)
        // No agregar los de vehículos porque causaría duplicación
        List<Transito> all = new ArrayList<>(this.transitos);
        all.sort((a, b) -> b.fechaHora().compareTo(a.fechaHora()));
        return all;
    }

    public int borrarNotificaciones() {
        int cant = this.notificaciones == null ? 0 : this.notificaciones.size();
        if (this.notificaciones != null)
            this.notificaciones.clear();
        return cant;
    }

    public double totalGastadoPor(Vehiculo v) {
        if (v == null || this.transitos == null)
            return 0.0;
        return this.transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(v))
                .mapToDouble(Transito::totalPagado)
                .sum();
    }

    public int cantidadTransitosDe(Vehiculo v) {
        if (v == null || this.transitos == null)
            return 0;
        return (int) this.transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(v))
                .count();
    }
}
