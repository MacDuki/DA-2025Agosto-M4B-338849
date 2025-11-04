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
        SistemaAdministradores sa = f.getSistemaAdministradores();

        // Crear administradores usando solo constructores
        Administrador admin1 = new Administrador("99999999", "admin123", "Root Admin");
        Administrador admin2 = new Administrador("88888888", "gestor123", "Gestor 1");

        // Registrar
        sa.getAdministradores().add(admin1);
        sa.getAdministradores().add(admin2);
    }

    static void cargarCategorias() {
        Fachada f = Fachada.getInstancia();
        SistemaCategorias sc = f.getSistemaCategorias();

        // Categorías base
        Categoria auto = new Categoria("Auto");
        Categoria camioneta = new Categoria("Camioneta");
        Categoria camion = new Categoria("Camión");

        sc.getCategorias().add(auto);
        sc.getCategorias().add(camioneta);
        sc.getCategorias().add(camion);
    }

    static void cargarPuestos() {
        Fachada f = Fachada.getInstancia();
        SistemaPuestos sp = f.getSistemaPuestos();

        Puesto p1 = new Puesto("Peaje Ruta 1", "KM 34, Ruta Interbalnearia");
        Puesto p2 = new Puesto("Peaje Ruta 3", "KM 96, San José");
        Puesto p3 = new Puesto("Peaje Ruta 8", "KM 22, Pando");

        sp.getPuestos().add(p1);
        sp.getPuestos().add(p2);
        sp.getPuestos().add(p3);
    }

    static void cargarTarifas() {
        Fachada f = Fachada.getInstancia();
        SistemaPuestos sp = f.getSistemaPuestos();
        SistemaCategorias sc = f.getSistemaCategorias();

        // Helper: buscar categoría por nombre
        java.util.function.Function<String, Categoria> cat = nombre -> {
            for (Categoria c : sc.getCategorias()) {
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

        // Definir montos por puesto y categoría (ejemplo simple)
        for (Puesto puesto : sp.getPuestos()) {
            if (auto != null)
                puesto.getTablaTarifas().add(new Tarifa(120.0, auto));
            if (camioneta != null)
                puesto.getTablaTarifas().add(new Tarifa(180.0, camioneta));
            if (camion != null)
                puesto.getTablaTarifas().add(new Tarifa(300.0, camion));
        }
    }

    static void cargarVehiculos() {
        Fachada f = Fachada.getInstancia();
        SistemaPropietarios sp = f.getSistemaPropietarios();
        SistemaVehiculos sv = f.getSistemaVehiculos();
        SistemaCategorias sc = f.getSistemaCategorias();

        // Buscar categorías ya precargadas
        Categoria catAuto = null;
        Categoria catCamioneta = null;
        Categoria catCamion = null;
        for (Categoria c : sc.getCategorias()) {
            if ("Auto".equalsIgnoreCase(c.getNombre()))
                catAuto = c;
            else if ("Camioneta".equalsIgnoreCase(c.getNombre()))
                catCamioneta = c;
            else if ("Camión".equalsIgnoreCase(c.getNombre()) || "Camion".equalsIgnoreCase(c.getNombre()))
                catCamion = c;
        }

        // Obtener propietarios ya cargados
        Propietario p1 = sp.buscarPorCedula("11111111");
        Propietario p2 = sp.buscarPorCedula("22222222");
        Propietario p3 = sp.buscarPorCedula("33333333");

        // Crear vehículos mediante métodos de dominio (sin setters)
        if (p1 != null) {
            Vehiculo v1 = p1.registrarVehiculo("SBA1234", "Ford Fiesta", "Rojo", catAuto);
            Vehiculo v2 = p1.registrarVehiculo("SBA5678", "Hyundai Tucson", "Blanco", catCamioneta);
            sv.getVehiculos().add(v1);
            sv.getVehiculos().add(v2);
        }

        if (p2 != null) {
            Vehiculo v3 = p2.registrarVehiculo("ABC1234", "Toyota Hilux", "Negro", catCamioneta);
            sv.getVehiculos().add(v3);
        }

        if (p3 != null) {
            Vehiculo v4 = p3.registrarVehiculo("TRK9000", "Scania R500", "Azul", catCamion);
            sv.getVehiculos().add(v4);
        }
    }

    static void cargarPropietarios() {
        Fachada f = Fachada.getInstancia();
        SistemaPropietarios sp = f.getSistemaPropietarios();

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

        // Registrar en el sistema
        sp.getPropietarios().add(p1);
        sp.getPropietarios().add(p2);
        sp.getPropietarios().add(p3);
    }

}
