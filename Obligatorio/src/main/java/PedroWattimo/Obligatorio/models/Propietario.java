package PedroWattimo.Obligatorio.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Propietario {
    private String cedula;
    private String nombreCompleto;
    private String contraseña;
    private int saldoActual;
    private int saldoMinimoAlerta;
    private Estado estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Transito> transitos;
    private List<AsignacionBonificacion> asignaciones;
    private List<Notificacion> notificaciones;

    public Propietario() {
        this.vehiculos = new ArrayList<>();
        this.transitos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        // Por defecto, el propietario está habilitado para ingresar
        this.estadoActual = Estado.HABILITADO;
    }

    public Propietario(String cedula, String nombreCompleto, String contraseña) {
        this();
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        // Almacenar siempre el hash de la contraseña
        this.contraseña = hash(contraseña);
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
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

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<Transito> getTransitos() {
        return transitos;
    }

    public List<AsignacionBonificacion> getAsignaciones() {
        return asignaciones;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setContraseña(String contraseña) {
        // Almacenar siempre el hash de la contraseña
        this.contraseña = hash(contraseña);
    }

    public void setSaldoActual(int saldoActual) {
        this.saldoActual = saldoActual;
    }

    public void setSaldoMinimoAlerta(int saldoMinimoAlerta) {
        this.saldoMinimoAlerta = saldoMinimoAlerta;
    }

    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    // ---------------------------------------------
    // Lógica de dominio (Principio Experto)
    // ---------------------------------------------

    /**
     * Verifica si la contraseña ingresada coincide con el hash almacenado.
     * Valida nulos o vacíos devolviendo false.
     */
    public boolean passwordCorrecta(String pwd) {
        if (pwd == null || pwd.isBlank() || this.contraseña == null || this.contraseña.isBlank()) {
            return false;
        }
        return this.contraseña.equals(hash(pwd));
    }

    /**
     * Indica si el propietario puede ingresar al sistema según su estado actual.
     * Por ahora, solo el estado SUSPENDIDO impide el ingreso.
     */
    public boolean puedeIngresar() {
        if (this.estadoActual == null)
            return true; // Por defecto habilitado
        return !Estado.SUSPENDIDO.equals(this.estadoActual);
    }

    // -----------------------------
    // Expert: Tablero del propietario
    // -----------------------------
    public List<AsignacionBonificacion> bonificacionesAsignadas() {
        return this.asignaciones;
    }

    public List<Vehiculo> vehiculos() {
        return this.vehiculos;
    }

    public List<Notificacion> notificacionesOrdenadasDesc() {
        List<Notificacion> out = new ArrayList<>(this.notificaciones);
        out.sort((a, b) -> b.getFechaHora().compareTo(a.getFechaHora()));
        return out;
    }

    public List<Transito> transitosOrdenadosDesc() {
        List<Transito> all = new ArrayList<>(this.transitos);
        if (this.vehiculos != null) {
            for (Vehiculo v : this.vehiculos) {
                if (v.getTransitos() != null) {
                    all.addAll(v.getTransitos());
                }
            }
        }
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

    public double totalGastadoPor(Vehiculo v) {
        if (v == null || v.getTransitos() == null)
            return 0.0;
        return v.getTransitos().stream().mapToDouble(Transito::totalPagado).sum();
    }

    public int cantidadTransitosDe(Vehiculo v) {
        if (v == null || v.getTransitos() == null)
            return 0;
        return v.getTransitos().size();
    }

    // Helpers
    private String hash(String input) {
        if (input == null)
            return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // No debería ocurrir; en caso de error, retornar tal cual (evita NPE)
            return input;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
