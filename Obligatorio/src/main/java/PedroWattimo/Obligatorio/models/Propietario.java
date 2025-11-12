package PedroWattimo.Obligatorio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Propietario extends Usuario {
    private int saldoActual;
    private int saldoMinimoAlerta;
    private Estado estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Transito> transitos;
    private List<AsignacionBonificacion> asignaciones;
    private List<Notificacion> notificaciones;

    /**
     * Constructor sencillo: define identidad y credenciales.
     * Estado por defecto: HABILITADO. Saldos en 0.
     */
    public Propietario(int cedula, String nombreCompleto, String contraseña) {
        super(cedula, nombreCompleto, contraseña);
        // Inicialización de colecciones
        this.vehiculos = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();

        // Datos específicos de Propietario
        this.saldoActual = 0;
        this.saldoMinimoAlerta = 0;
        this.estadoActual = Estado.HABILITADO;
    }

    public String getcontraseña() {
        return contraseña;
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

    /** Cambia el estado actual del propietario. */
    public Propietario cambiarEstado(Estado nuevoEstado) {
        this.estadoActual = (nuevoEstado == null) ? Estado.HABILITADO : nuevoEstado;
        return this;
    }

    /** Define el umbral de saldo mínimo para alertas (no negativo). */
    public Propietario ajustarSaldoMinimoAlerta(int nuevoMinimo) {
        this.saldoMinimoAlerta = Math.max(0, nuevoMinimo);
        return this;
    }

    /** Acredita saldo positivo. Ignora montos nulos/negativos. */
    public Propietario acreditarSaldo(int monto) {
        if (monto > 0)
            this.saldoActual += monto;
        return this;
    }

    /**
     * Debita saldo si hay fondos suficientes. Retorna true si se realizó.
     * No permite saldos negativos.
     */
    public boolean debitarSaldo(int monto) {
        if (monto <= 0)
            return false;
        if (this.saldoActual - monto < 0)
            return false;
        this.saldoActual -= monto;
        return true;
    }

    /**
     * Registra un vehículo nuevo asociado a este propietario.
     */
    public Vehiculo registrarVehiculo(String matricula, String modelo, String color, Categoria categoria) {
        Vehiculo v = new Vehiculo(matricula, modelo, color, categoria, this);
        if (this.vehiculos == null)
            this.vehiculos = new ArrayList<>();
        this.vehiculos.add(v);
        return v;
    }

    /**
     * Agrega un vehículo ya construido y asegura la asociación con este
     * propietario.
     */
    public Propietario agregarVehiculo(Vehiculo v) {
        if (v == null)
            return this;
        // Si el vehículo no referencia este propietario, crear uno nuevo consistente
        if (v.getPropietario() != this) {
            v = new Vehiculo(v.getMatricula(), v.getModelo(), v.getColor(), v.getCategoria(), this);
        }
        if (this.vehiculos == null)
            this.vehiculos = new ArrayList<>();
        this.vehiculos.add(v);
        return this;
    }

    // ---------------------------------------------
    // Lógica de dominio (Principio Experto)
    // ---------------------------------------------

    /**
     * Indica si el propietario puede ingresar al sistema según su estado actual.
     *  solo el estado SUSPENDIDO impide el ingreso.
     */
    public boolean puedeIngresar() {
        if (this.estadoActual == null)
            return true; // Por defecto habilitado
        return this.estadoActual.permiteIngresar();
    }

    /**
     * Patrón Experto: el Propietario sabe si puede transitar según su estado.
     * Delega en el Estado la decisión.
     */
    public boolean puedeTransitar() {
        if (this.estadoActual == null)
            return true; // Por defecto habilitado
        return this.estadoActual.permiteTransitar();
    }

    /**
     * Patrón Experto: el Propietario sabe si tiene saldo insuficiente para un
     * monto.
     */
    public boolean saldoInsuficientePara(double monto) {
        return this.saldoActual < monto;
    }

    /**
     * Patrón Experto: el Propietario debita un monto de su saldo.
     * No permite saldo negativo.
     */
    public void debitar(double monto) {
        if (monto <= 0)
            return;
        this.saldoActual -= (int) Math.round(monto);
        if (this.saldoActual < 0)
            this.saldoActual = 0;
    }

    /**
     * Patrón Experto: el Propietario acredita un monto a su saldo.
     */
    public void acreditar(double monto) {
        if (monto > 0) {
            this.saldoActual += (int) Math.round(monto);
        }
    }

    /**
     * Patrón Experto: el Propietario busca su bonificación asignada para un puesto
     * específico.
     */
    public Optional<AsignacionBonificacion> bonificacionAsignadaPara(Puesto p) {
        if (p == null || this.asignaciones == null)
            return Optional.empty();
        return this.asignaciones.stream()
                .filter(ab -> ab.activaPara(p, this))
                .findFirst();
    }

    /**
     * Patrón Experto: el Propietario sabe si debe alertar sobre su saldo.
     */
    public boolean debeAlertarSaldo() {
        return this.saldoActual < this.saldoMinimoAlerta;
    }

    /**
     * Patrón Experto: el Propietario registra una notificación para sí mismo.
     */
    public void registrarNotificacion(String mensaje, LocalDateTime fh) {
        if (mensaje == null || fh == null)
            return;
        if (this.notificaciones == null)
            this.notificaciones = new ArrayList<>();
        Notificacion notif = new Notificacion(fh, mensaje, this);
        this.notificaciones.add(notif);
    }

    /**
     * Patrón Experto: el Propietario registra un tránsito en su historial.
     */
    public void registrarTransito(Transito transito) {
        if (transito == null)
            return;
        if (this.transitos == null)
            this.transitos = new ArrayList<>();
        this.transitos.add(transito);
    }

    /**
     * Patrón Experto: el Propietario registra una asignación de bonificación
     * para un puesto y fecha-hora dados.
     */
    public AsignacionBonificacion asignarBonificacion(Bonificacion bonificacion, Puesto puesto,
            LocalDateTime fechaHora) {
        if (bonificacion == null || puesto == null || fechaHora == null)
            return null;
        if (this.asignaciones == null)
            this.asignaciones = new ArrayList<>();
        AsignacionBonificacion ab = new AsignacionBonificacion(fechaHora, this, puesto, bonificacion);
        this.asignaciones.add(ab);
        return ab;
    }

    /**
     * Patrón Experto: el Propietario busca un vehículo por matrícula entre sus
     * vehículos.
     */
    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        if (matricula == null || this.vehiculos == null)
            return null;
        return this.vehiculos.stream()
                .filter(v -> matricula.equalsIgnoreCase(v.getMatricula()))
                .findFirst()
                .orElse(null);
    }

    // -----------------------------
    // Expert: Tablero del propietario
    // -----------------------------
    public List<AsignacionBonificacion> bonificacionesAsignadas() {
        return this.asignaciones == null ? List.of() : List.copyOf(this.asignaciones);
    }

    public List<Vehiculo> vehiculos() {
        return this.vehiculos == null ? List.of() : List.copyOf(this.vehiculos);
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

    public int saldoActual() {
        return this.saldoActual;
    }

    public String estadoActual() {
        return this.estadoActual != null ? this.estadoActual.nombre() : Estado.HABILITADO.nombre();
    }

    /**
     * Patrón Experto: calcula el total gastado por un vehículo específico
     * a partir de los tránsitos del propietario.
     */
    public double totalGastadoPor(Vehiculo v) {
        if (v == null || this.transitos == null)
            return 0.0;
        return this.transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(v))
                .mapToDouble(Transito::totalPagado)
                .sum();
    }

    /**
     * Patrón Experto: calcula la cantidad de tránsitos de un vehículo específico
     * a partir de los tránsitos del propietario.
     */
    public int cantidadTransitosDe(Vehiculo v) {
        if (v == null || this.transitos == null)
            return 0;
        return (int) this.transitos.stream()
                .filter(t -> t.vehiculo() != null && t.vehiculo().equals(v))
                .count();
    }
}
