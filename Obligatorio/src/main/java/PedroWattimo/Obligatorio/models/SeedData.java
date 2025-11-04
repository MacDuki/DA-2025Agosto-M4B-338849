package PedroWattimo.Obligatorio.models;

public class SeedData {
    public static void initialize() {
        Fachada fachada = Fachada.getInstancia();

        // 1. Precargar Administradores
        cargarAdministradores(fachada);

        // 2. Precargar Categorías de vehículos
        cargarCategorias(fachada);

        // 3. Precargar Propietarios (después de categorías para poder crear estados)
        cargarPropietarios(fachada);

        // 4. Precargar Puestos
        cargarPuestos(fachada);

        // 5. Precargar Tarifas de cada puesto
        cargarTarifas(fachada);

        // 6. Precargar Vehículos
        cargarVehiculos(fachada);

        System.out.println("✓ Datos precargados exitosamente");
    }

    private static void cargarAdministradores(Fachada fachada) {
        SistemaAdministradores sistemaAdmin = fachada.getSistemaAdministradores();

        // Administrador requerido
        Administrador admin1 = new Administrador("12345678", "admin.123", "Usuario Administrador");
        sistemaAdmin.getAdministradores().add(admin1);

        // Segundo administrador
        Administrador admin2 = new Administrador("11111111", "admin456", "María Rodríguez");
        sistemaAdmin.getAdministradores().add(admin2);

        System.out.println("  - Administradores cargados: " + sistemaAdmin.getAdministradores().size());
    }

    private static void cargarCategorias(Fachada fachada) {
        SistemaCategorias sistemaCategorias = fachada.getSistemaCategorias();

        // Categorías de vehículos
        Categoria automovil = new Categoria("Automóvil");
        Categoria camion = new Categoria("Camión");
        Categoria motocicleta = new Categoria("Motocicleta");
        Categoria camioneta = new Categoria("Camioneta");

        sistemaCategorias.getCategorias().add(automovil);
        sistemaCategorias.getCategorias().add(camion);
        sistemaCategorias.getCategorias().add(motocicleta);
        sistemaCategorias.getCategorias().add(camioneta);

        System.out.println("  - Categorías cargadas: " + sistemaCategorias.getCategorias().size());
    }

    private static void cargarPropietarios(Fachada fachada) {
        SistemaPropietarios sistemaProp = fachada.getSistemaPropietarios();

        // Propietario requerido
        Propietario prop1 = new Propietario("23456789", "Usuario Propietario", "prop.123");
        prop1.setSaldoActual(2000);
        prop1.setSaldoMinimoAlerta(500);
        // Estado "Habilitado" es el estado por defecto
        // prop1.setEstadoActual(estadoHabilitado); // Se configurará cuando exista
        // SistemaEstados
        sistemaProp.getPropietarios().add(prop1);

        // Segundo propietario
        Propietario prop2 = new Propietario("33333333", "Juan Pérez", "prop456");
        prop2.setSaldoActual(1500);
        prop2.setSaldoMinimoAlerta(300);
        sistemaProp.getPropietarios().add(prop2);

        // Tercer propietario
        Propietario prop3 = new Propietario("44444444", "Ana García", "prop789");
        prop3.setSaldoActual(3000);
        prop3.setSaldoMinimoAlerta(600);
        sistemaProp.getPropietarios().add(prop3);

        // Cuarto propietario (inhabilitado/suspendido) para pruebas de login 403
        Propietario prop4 = new Propietario("55555555", "Carlos Inhabilitado", "disabled123");
        prop4.setSaldoActual(1000);
        prop4.setSaldoMinimoAlerta(200);
        prop4.setEstadoActual(Estado.SUSPENDIDO);
        sistemaProp.getPropietarios().add(prop4);

        System.out.println("  - Propietarios cargados: " + sistemaProp.getPropietarios().size());
    }

    private static void cargarPuestos(Fachada fachada) {
        SistemaPuestos sistemaPuestos = fachada.getSistemaPuestos();

        // Puestos de peaje
        Puesto puesto1 = new Puesto("Peaje Montevideo Este", "Ruta 1 Km 25");
        Puesto puesto2 = new Puesto("Peaje Colonia", "Ruta 1 Km 120");
        Puesto puesto3 = new Puesto("Peaje Canelones", "Ruta 8 Km 30");
        Puesto puesto4 = new Puesto("Peaje Maldonado", "Ruta 9 Km 95");

        sistemaPuestos.getPuestos().add(puesto1);
        sistemaPuestos.getPuestos().add(puesto2);
        sistemaPuestos.getPuestos().add(puesto3);
        sistemaPuestos.getPuestos().add(puesto4);

        System.out.println("  - Puestos cargados: " + sistemaPuestos.getPuestos().size());
    }

    private static void cargarTarifas(Fachada fachada) {
        SistemaPuestos sistemaPuestos = fachada.getSistemaPuestos();
        SistemaCategorias sistemaCategorias = fachada.getSistemaCategorias();

        // Obtener categorías
        Categoria automovil = sistemaCategorias.getCategorias().get(0); // Automóvil
        Categoria camion = sistemaCategorias.getCategorias().get(1); // Camión
        Categoria motocicleta = sistemaCategorias.getCategorias().get(2); // Motocicleta
        Categoria camioneta = sistemaCategorias.getCategorias().get(3); // Camioneta

        int tarifasTotales = 0;

        // Asignar tarifas a cada puesto
        for (Puesto puesto : sistemaPuestos.getPuestos()) {
            // Tarifas varían según el puesto
            double multiplicador = 1.0;
            if (puesto.getNombre().contains("Colonia")) {
                multiplicador = 1.2;
            } else if (puesto.getNombre().contains("Maldonado")) {
                multiplicador = 1.3;
            }

            Tarifa tarifaAuto = new Tarifa(100 * multiplicador, automovil);
            Tarifa tarifaCamion = new Tarifa(300 * multiplicador, camion);
            Tarifa tarifaMoto = new Tarifa(50 * multiplicador, motocicleta);
            Tarifa tarifaCamioneta = new Tarifa(150 * multiplicador, camioneta);

            puesto.getTablaTarifas().add(tarifaAuto);
            puesto.getTablaTarifas().add(tarifaCamion);
            puesto.getTablaTarifas().add(tarifaMoto);
            puesto.getTablaTarifas().add(tarifaCamioneta);

            tarifasTotales += 4;
        }

        System.out.println("  - Tarifas cargadas: " + tarifasTotales);
    }

    private static void cargarVehiculos(Fachada fachada) {
        SistemaVehiculos sistemaVehiculos = fachada.getSistemaVehiculos();
        SistemaPropietarios sistemaProp = fachada.getSistemaPropietarios();
        SistemaCategorias sistemaCategorias = fachada.getSistemaCategorias();

        // Obtener propietarios y categorías
        Propietario prop1 = sistemaProp.getPropietarios().get(0);
        Propietario prop2 = sistemaProp.getPropietarios().get(1);
        Propietario prop3 = sistemaProp.getPropietarios().get(2);

        Categoria automovil = sistemaCategorias.getCategorias().get(0);
        Categoria camion = sistemaCategorias.getCategorias().get(1);
        Categoria motocicleta = sistemaCategorias.getCategorias().get(2);
        Categoria camioneta = sistemaCategorias.getCategorias().get(3);

        // Vehículos del propietario 1
        Vehiculo v1 = new Vehiculo("ABC1234", "Toyota Corolla 2020", "Blanco", automovil, prop1);
        Vehiculo v2 = new Vehiculo("DEF5678", "Honda CB500 2019", "Rojo", motocicleta, prop1);

        // Vehículos del propietario 2
        Vehiculo v3 = new Vehiculo("GHI9012", "Ford Ranger 2021", "Negro", camioneta, prop2);
        Vehiculo v4 = new Vehiculo("JKL3456", "Chevrolet Onix 2022", "Azul", automovil, prop2);

        // Vehículos del propietario 3
        Vehiculo v5 = new Vehiculo("MNO7890", "Mercedes Benz Actros 2018", "Blanco", camion, prop3);
        Vehiculo v6 = new Vehiculo("PQR1234", "Volkswagen Amarok 2020", "Gris", camioneta, prop3);

        // Agregar al sistema
        sistemaVehiculos.getVehiculos().add(v1);
        sistemaVehiculos.getVehiculos().add(v2);
        sistemaVehiculos.getVehiculos().add(v3);
        sistemaVehiculos.getVehiculos().add(v4);
        sistemaVehiculos.getVehiculos().add(v5);
        sistemaVehiculos.getVehiculos().add(v6);

        // Agregar a los propietarios
        prop1.getVehiculos().add(v1);
        prop1.getVehiculos().add(v2);
        prop2.getVehiculos().add(v3);
        prop2.getVehiculos().add(v4);
        prop3.getVehiculos().add(v5);
        prop3.getVehiculos().add(v6);

        System.out.println("  - Vehículos cargados: " + sistemaVehiculos.getVehiculos().size());
    }
}
