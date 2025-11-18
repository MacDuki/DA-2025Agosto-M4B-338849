package PedroWattimo.Obligatorio.models;

public class SeedData {
    public static void initialize() {
        // 1. Precargar Administradores
        cargarAdministradores();

        // 2. Precargar Categorías de vehículos
        cargarCategorias();

        // 3. Precargar Propietarios (después de categorías para poder crear estados)
        cargarPropietarios();

        // 4. Precargar Puestos
        cargarPuestos();

        // 5. Precargar Tarifas de cada puesto
        cargarTarifas();

        // 6. Precargar Vehículos
        cargarVehiculos();

        // 7. Precargar Bonificaciones y Asignaciones a propietarios
        cargarBonificacionesYAsignaciones();

        System.out.println("✓ Datos precargados exitosamente");
    }

    static void cargarAdministradores() {
        Fachada f = Fachada.getInstancia();

        Administrador admin1 = new Administrador(99999999, "Root Admin", "admin123");
        Administrador admin2 = new Administrador(88888888, "Gestor 1", "gestor123");
        Administrador admin3 = new Administrador(12345678, "Usuario Administrador", "admin.123");

        f.obtenerAdministradoresInternos().add(admin1);
        f.obtenerAdministradoresInternos().add(admin2);
        f.obtenerAdministradoresInternos().add(admin3);
    }

    static void cargarCategorias() {
        Fachada f = Fachada.getInstancia();

        Categoria auto = new Categoria("Auto");
        Categoria camioneta = new Categoria("Camioneta");
        Categoria camion = new Categoria("Camión");

        f.obtenerCategoriasInternos().add(auto);
        f.obtenerCategoriasInternos().add(camioneta);
        f.obtenerCategoriasInternos().add(camion);
    }

    static void cargarPuestos() {
        Fachada f = Fachada.getInstancia();

        Puesto p1 = new Puesto("Peaje Ruta 1", "KM 34, Ruta Interbalnearia");
        Puesto p2 = new Puesto("Peaje Ruta 3", "KM 96, San José");
        Puesto p3 = new Puesto("Peaje Ruta 8", "KM 22, Pando");

        f.obtenerPuestosInternos().add(p1);
        f.obtenerPuestosInternos().add(p2);
        f.obtenerPuestosInternos().add(p3);
    }

    static void cargarTarifas() {
        Fachada f = Fachada.getInstancia();

        java.util.function.Function<String, Categoria> cat = nombre -> {
            for (Categoria c : f.obtenerCategorias()) {
                if (c.getNombre().equalsIgnoreCase(nombre))
                    return c;
            }
            return null;
        };

        Categoria auto = cat.apply("Auto");
        Categoria camioneta = cat.apply("Camioneta");
        Categoria camion = cat.apply("Camión");
        if (camion == null)
            camion = cat.apply("Camion"); // tolerar sin tilde

        // Obtener puestos
        java.util.List<Puesto> puestos = f.obtenerPuestos();

        // Peaje Ruta 1 (Interbalnearia) - Tarifas más altas por ser ruta principal
        if (puestos.size() > 0) {
            Puesto peajeRuta1 = puestos.get(0);
            if (auto != null)
                peajeRuta1.obtenerTablaTarifasInterna().add(new Tarifa(150.0, auto));
            if (camioneta != null)
                peajeRuta1.obtenerTablaTarifasInterna().add(new Tarifa(220.0, camioneta));
            if (camion != null)
                peajeRuta1.obtenerTablaTarifasInterna().add(new Tarifa(380.0, camion));
        }

        // Peaje Ruta 3 (San José) - Tarifas estándar
        if (puestos.size() > 1) {
            Puesto peajeRuta3 = puestos.get(1);
            if (auto != null)
                peajeRuta3.obtenerTablaTarifasInterna().add(new Tarifa(120.0, auto));
            if (camioneta != null)
                peajeRuta3.obtenerTablaTarifasInterna().add(new Tarifa(180.0, camioneta));
            if (camion != null)
                peajeRuta3.obtenerTablaTarifasInterna().add(new Tarifa(300.0, camion));
        }

        // Peaje Ruta 8 (Pando) - Tarifas más bajas por ser ruta secundaria
        if (puestos.size() > 2) {
            Puesto peajeRuta8 = puestos.get(2);
            if (auto != null)
                peajeRuta8.obtenerTablaTarifasInterna().add(new Tarifa(100.0, auto));
            if (camioneta != null)
                peajeRuta8.obtenerTablaTarifasInterna().add(new Tarifa(150.0, camioneta));
            if (camion != null)
                peajeRuta8.obtenerTablaTarifasInterna().add(new Tarifa(250.0, camion));
        }
    }

    static void cargarVehiculos() {
        Fachada f = Fachada.getInstancia();

        Categoria catAuto = null;
        Categoria catCamioneta = null;
        Categoria catCamion = null;
        for (Categoria c : f.obtenerCategorias()) {
            if ("Auto".equalsIgnoreCase(c.getNombre()))
                catAuto = c;
            else if ("Camioneta".equalsIgnoreCase(c.getNombre()))
                catCamioneta = c;
            else if ("Camión".equalsIgnoreCase(c.getNombre()) || "Camion".equalsIgnoreCase(c.getNombre()))
                catCamion = c;
        }

        Propietario p1 = f.buscarPropietarioPorCedula(11111111);
        Propietario p2 = f.buscarPropietarioPorCedula(22222222);
        Propietario p3 = f.buscarPropietarioPorCedula(33333333);
        Propietario p4 = f.buscarPropietarioPorCedula(44444444);
        Propietario p5 = f.buscarPropietarioPorCedula(23456789);

        if (p1 != null) {
            Vehiculo v1 = p1.registrarVehiculo("SBA1234", "Ford Fiesta", "Rojo", catAuto);
            Vehiculo v2 = p1.registrarVehiculo("SBA5678", "Hyundai Tucson", "Blanco", catCamioneta);
            f.obtenerVehiculosInternos().add(v1);
            f.obtenerVehiculosInternos().add(v2);
        }

        if (p2 != null) {
            Vehiculo v3 = p2.registrarVehiculo("ABC1234", "Toyota Hilux", "Negro", catCamioneta);
            f.obtenerVehiculosInternos().add(v3);
        }

        if (p3 != null) {
            Vehiculo v4 = p3.registrarVehiculo("TRK9000", "Scania R500", "Azul", catCamion);
            f.obtenerVehiculosInternos().add(v4);
        }

        if (p4 != null) {
            Vehiculo v5 = p4.registrarVehiculo("DEF4321", "Chevrolet Onix", "Gris", catAuto);
            f.obtenerVehiculosInternos().add(v5);
        }

        if (p5 != null) {
            Vehiculo v6 = p5.registrarVehiculo("XYZ7890", "Volkswagen Golf", "Plateado", catAuto);
            f.obtenerVehiculosInternos().add(v6);
        }
    }

    static void cargarPropietarios() {
        Fachada f = Fachada.getInstancia();

        // Propietario 1: habilitado, con saldo y mínimo de alerta
        Propietario p1 = new Propietario(11111111, "Ana Pérez", "ana123")
                .acreditarSaldo(1500)
                .ajustarSaldoMinimoAlerta(200)
                .cambiarEstado(FabricaEstados.crearHabilitado());

        // Propietario 2: penalizado
        Propietario p2 = new Propietario(22222222, "Bruno López", "bruno123")
                .acreditarSaldo(800)
                .ajustarSaldoMinimoAlerta(100)
                .cambiarEstado(FabricaEstados.crearPenalizado());

        // Propietario 3: suspendido, sin saldo
        Propietario p3 = new Propietario(33333333, "Carla Gómez", "carla123")
                .ajustarSaldoMinimoAlerta(50)
                .cambiarEstado(FabricaEstados.crearSuspendido());

        // Propietario 4: habilitado, sin bonificación asignada
        Propietario p4 = new Propietario(44444444, "Diego Ruiz", "diego123")
                .acreditarSaldo(1200)
                .ajustarSaldoMinimoAlerta(150)
                .cambiarEstado(FabricaEstados.crearHabilitado());

        // Propietario 5: nuevo usuario propietario con saldo y alerta configurados
        Propietario p5 = new Propietario(23456789, "Usuario Propietario", "prop.123")
                .acreditarSaldo(2000)
                .ajustarSaldoMinimoAlerta(500)
                .cambiarEstado(FabricaEstados.crearHabilitado());

        // Registrar en el sistema usando método interno
        f.obtenerPropietariosInternos().add(p1);
        f.obtenerPropietariosInternos().add(p2);
        f.obtenerPropietariosInternos().add(p3);
        f.obtenerPropietariosInternos().add(p4);
        f.obtenerPropietariosInternos().add(p5);
    }

    static void cargarBonificacionesYAsignaciones() {
        Fachada f = Fachada.getInstancia();

        // Crear tipos de bonificación disponibles usando la fábrica
        Bonificacion exonerados = FabricaBonificaciones.crearExonerados();
        Bonificacion frecuentes = FabricaBonificaciones.crearFrecuentes();
        Bonificacion trabajadores = FabricaBonificaciones.crearTrabajadores();

        // Registrar bonificaciones en el sistema
        f.obtenerBonificacionesInternas().add(exonerados);
        f.obtenerBonificacionesInternas().add(frecuentes);
        f.obtenerBonificacionesInternas().add(trabajadores);

        // Obtener entidades necesarias
        Propietario p1 = f.buscarPropietarioPorCedula(11111111);
        Propietario p2 = f.buscarPropietarioPorCedula(22222222);
        Propietario p4 = f.buscarPropietarioPorCedula(44444444);

        java.util.List<Puesto> puestos = f.obtenerPuestos();
        if (puestos.isEmpty())
            return;

        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();

        // Asignaciones ejemplo:
        // p1: Frecuentes en Puesto 0
        if (p1 != null) {
            Puesto puesto0 = puestos.get(0);
            AsignacionBonificacion ab1 = p1.asignarBonificacion(frecuentes, puesto0, ahora.minusDays(5));
            if (ab1 != null)
                f.obtenerAsignacionesBonificacionesInternas().add(ab1);
        }

        // p2: Trabajadores en Puesto 1
        if (p2 != null && puestos.size() > 1) {
            Puesto puesto1 = puestos.get(1);
            AsignacionBonificacion ab2 = p2.asignarBonificacion(trabajadores, puesto1, ahora.minusDays(3));
            if (ab2 != null)
                f.obtenerAsignacionesBonificacionesInternas().add(ab2);
        }

        // p4: Exonerados en Puesto 2 (p3 queda SIN bonificación)
        if (p4 != null && puestos.size() > 2) {
            Puesto puesto2 = puestos.get(2);
            AsignacionBonificacion ab3 = p4.asignarBonificacion(exonerados, puesto2, ahora.minusDays(1));
            if (ab3 != null)
                f.obtenerAsignacionesBonificacionesInternas().add(ab3);
        }
    }
}
