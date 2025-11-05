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

        System.out.println("✓ Datos precargados exitosamente");
    }

    static void cargarAdministradores() {
        Fachada f = Fachada.getInstancia();

        Administrador admin1 = new Administrador("99999999", "admin123", "Root Admin");
        Administrador admin2 = new Administrador("88888888", "gestor123", "Gestor 1");

        f.obtenerAdministradoresInternos().add(admin1);
        f.obtenerAdministradoresInternos().add(admin2);
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

        for (Puesto puesto : f.obtenerPuestos()) {
            if (auto != null)
                puesto.obtenerTablaTarifasInterna().add(new Tarifa(120.0, auto));
            if (camioneta != null)
                puesto.obtenerTablaTarifasInterna().add(new Tarifa(180.0, camioneta));
            if (camion != null)
                puesto.obtenerTablaTarifasInterna().add(new Tarifa(300.0, camion));
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

        Propietario p1 = f.buscarPropietarioPorCedula("11111111");
        Propietario p2 = f.buscarPropietarioPorCedula("22222222");
        Propietario p3 = f.buscarPropietarioPorCedula("33333333");

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
    }

    static void cargarPropietarios() {
        Fachada f = Fachada.getInstancia();

        // Propietario 1: habilitado, con saldo y mínimo de alerta
        Propietario p1 = new Propietario("11111111", "Ana Pérez", "ana123")
                .acreditarSaldo(1500)
                .ajustarSaldoMinimoAlerta(200)
                .cambiarEstado(Estado.HABILITADO);

        // Propietario 2: penalizado
        Propietario p2 = new Propietario("22222222", "Bruno López", "bruno123")
                .acreditarSaldo(800)
                .ajustarSaldoMinimoAlerta(100)
                .cambiarEstado(Estado.PENALIZADO);

        // Propietario 3: suspendido, sin saldo
        Propietario p3 = new Propietario("33333333", "Carla Gómez", "carla123")
                .ajustarSaldoMinimoAlerta(50)
                .cambiarEstado(Estado.SUSPENDIDO);

        // Registrar en el sistema usando método interno
        f.obtenerPropietariosInternos().add(p1);
        f.obtenerPropietariosInternos().add(p2);
        f.obtenerPropietariosInternos().add(p3);
    }

}
