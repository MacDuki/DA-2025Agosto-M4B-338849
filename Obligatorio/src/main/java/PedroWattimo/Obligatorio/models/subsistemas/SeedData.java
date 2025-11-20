package PedroWattimo.Obligatorio.models.subsistemas;

import PedroWattimo.Obligatorio.models.entidades.*;
import PedroWattimo.Obligatorio.models.fabricas.*;
import PedroWattimo.Obligatorio.models.exceptions.OblException;

public class SeedData {

    public static void cargar() {
        try {

            cargarCategorias();

            cargarPuestos();

            cargarTarifas();

            cargarAdministradores();

            cargarPropietarios();

            cargarVehiculos();

            cargarBonificaciones();

            asignarBonificaciones();

            System.out.println("Datos precargados exitosamente");
        } catch (OblException e) {
            System.err.println("Error al precargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void cargarCategorias() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        fachada.agregarCategoria("Auto");
        fachada.agregarCategoria("Camioneta");
        fachada.agregarCategoria("Camión");
    }

    static void cargarPuestos() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        fachada.agregarPuesto("Peaje Ruta 1", "KM 34, Ruta Interbalnearia");
        fachada.agregarPuesto("Peaje Ruta 3", "KM 96, San José");
        fachada.agregarPuesto("Peaje Ruta 8", "KM 22, Pando");
    }

    static void cargarTarifas() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        // Peaje Ruta 1 - Tarifas más altas
        fachada.agregarTarifaAPuesto("Peaje Ruta 1", 150.0, "Auto");
        fachada.agregarTarifaAPuesto("Peaje Ruta 1", 220.0, "Camioneta");
        fachada.agregarTarifaAPuesto("Peaje Ruta 1", 380.0, "Camión");

        // Peaje Ruta 3 - Tarifas estándar
        fachada.agregarTarifaAPuesto("Peaje Ruta 3", 120.0, "Auto");
        fachada.agregarTarifaAPuesto("Peaje Ruta 3", 180.0, "Camioneta");
        fachada.agregarTarifaAPuesto("Peaje Ruta 3", 300.0, "Camión");

        // Peaje Ruta 8 - Tarifas más bajas
        fachada.agregarTarifaAPuesto("Peaje Ruta 8", 100.0, "Auto");
        fachada.agregarTarifaAPuesto("Peaje Ruta 8", 150.0, "Camioneta");
        fachada.agregarTarifaAPuesto("Peaje Ruta 8", 250.0, "Camión");
    }

    static void cargarAdministradores() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        fachada.agregarAdministrador(99999999, "Root Admin", "admin123");
        fachada.agregarAdministrador(88888888, "Gestor 1", "gestor123");
        fachada.agregarAdministrador(12345678, "Usuario Administrador", "admin.123");
    }

    static void cargarPropietarios() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        // Propietario 1: habilitado, con saldo y mínimo de alerta
        Propietario p1 = fachada.registrarPropietario(11111111, "Ana Pérez", "ana123");
        p1.acreditarSaldo(1500);
        p1.ajustarSaldoMinimoAlerta(200);
        p1.cambiarEstado(FabricaEstados.crearHabilitado());

        // Propietario 2: penalizado
        Propietario p2 = fachada.registrarPropietario(22222222, "Bruno López", "bruno123");
        p2.acreditarSaldo(800);
        p2.ajustarSaldoMinimoAlerta(100);
        p2.cambiarEstado(FabricaEstados.crearPenalizado());

        // Propietario 3: suspendido, sin saldo
        Propietario p3 = fachada.registrarPropietario(33333333, "Carla Gómez", "carla123");
        p3.ajustarSaldoMinimoAlerta(50);
        p3.cambiarEstado(FabricaEstados.crearSuspendido());

        // Propietario 4: habilitado, sin bonificación asignada
        Propietario p4 = fachada.registrarPropietario(44444444, "Diego Ruiz", "diego123");
        p4.acreditarSaldo(1200);
        p4.ajustarSaldoMinimoAlerta(150);
        p4.cambiarEstado(FabricaEstados.crearHabilitado());

        // Propietario 5: Requerido
        Propietario p5 = fachada.registrarPropietario(23456789, "Usuario Propietario", "prop.123");
        p5.acreditarSaldo(2000);
        p5.ajustarSaldoMinimoAlerta(500);
        p5.cambiarEstado(FabricaEstados.crearHabilitado());
    }

    static void cargarVehiculos() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        // Vehículos del propietario Ana Pérez
        fachada.registrarVehiculo(11111111, "SBA1234", "Ford Fiesta", "Rojo", "Auto");
        fachada.registrarVehiculo(11111111, "SBA5678", "Hyundai Tucson", "Blanco", "Camioneta");

        // Vehículo del propietario Bruno López
        fachada.registrarVehiculo(22222222, "ABC1234", "Toyota Hilux", "Negro", "Camioneta");

        // Vehículo del propietario Carla Gómez
        fachada.registrarVehiculo(33333333, "TRK9000", "Scania R500", "Azul", "Camión");

        // Vehículo del propietario Diego Ruiz
        fachada.registrarVehiculo(44444444, "DEF4321", "Chevrolet Onix", "Gris", "Auto");

        // Vehículo del Usuario Propietario
        fachada.registrarVehiculo(23456789, "XYZ7890", "Volkswagen Golf", "Plateado", "Auto");
    }

    static void cargarBonificaciones() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        fachada.agregarBonificacion(FabricaBonificaciones.crearExonerados());
        fachada.agregarBonificacion(FabricaBonificaciones.crearFrecuentes());
        fachada.agregarBonificacion(FabricaBonificaciones.crearTrabajadores());
    }

    static void asignarBonificaciones() throws OblException {
        Fachada fachada = Fachada.getInstancia();

        // Ana Pérez: Frecuentes en Peaje Ruta 1
        fachada.asignarBonificacion("11111111", "Frecuentes", "Peaje Ruta 1");

        // Bruno López: Trabajadores en Peaje Ruta 3
        fachada.asignarBonificacion("22222222", "Trabajadores", "Peaje Ruta 3");

        // Diego Ruiz: Exonerados en Peaje Ruta 8
        fachada.asignarBonificacion("44444444", "Exonerados", "Peaje Ruta 8");

        // Carla Gómez queda sin bonificación asignada
    }
}
