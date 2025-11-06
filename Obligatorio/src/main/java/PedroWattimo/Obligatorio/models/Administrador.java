package PedroWattimo.Obligatorio.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Administrador {
    private int cedula;
    private String nombreCompleto;
    private String passwordHash;
    private boolean logueado;

    public Administrador() {
    }

    /**
     * Crea un administrador almacenando el hash de la contraseña.
     */
    public Administrador(int cedula, String nombreCompleto, String passwordPlano) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.passwordHash = hash(passwordPlano);
        this.logueado = false;
    }

    public int getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Verifica si la contraseña es correcta comparando el hash almacenado.
     */
    public boolean passwordCorrecta(String pwd) {
        if (pwd == null || pwd.isBlank() || this.passwordHash == null || this.passwordHash.isBlank())
            return false;
        return this.passwordHash.equals(hash(pwd));
    }

    public boolean estaLogueado() {
        return this.logueado;
    }

    /**
     * Marca al administrador como logueado. Es idempotente.
     */
    public void loguear() {
        this.logueado = true;
    }

    /**
     * Marca al administrador como deslogueado. Es idempotente.
     */
    public void desloguear() {
        this.logueado = false;
    }

    // -----------------
    // Helpers de hashing
    // -----------------
    private String hash(String input) {
        if (input == null)
            return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            return input; // fallback defensivo
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
