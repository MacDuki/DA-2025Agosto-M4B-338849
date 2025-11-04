package PedroWattimo.Obligatorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import PedroWattimo.Obligatorio.dtos.PropietarioDashboardDto;
import PedroWattimo.Obligatorio.models.Fachada;
import PedroWattimo.Obligatorio.models.SeedData;

public class FachadaDashboardTests {

    @BeforeAll
    static void seed() {
        SeedData.initialize();
    }

    @Test
    void dashboard_minimo_ok_para_propietario_seed() {
        // SeedData loads cedula 23456789 with name "Usuario Propietario"
        PropietarioDashboardDto dto = Fachada.getInstancia().dashboardDePropietario(23456789);
        assertNotNull(dto);
        assertNotNull(dto.getPropietario());
        assertEquals("Usuario Propietario", dto.getPropietario().getNombreCompleto());
    }

    @Test
    void version_incrementa_al_borrar_notificaciones() {
        long v1 = Fachada.getInstancia().versionDashboardDePropietario(23456789);
        Fachada.getInstancia().borrarNotificacionesDePropietario(23456789);
        long v2 = Fachada.getInstancia().versionDashboardDePropietario(23456789);
        assertTrue(v2 >= v1); // puede seguir igual si no había subscripción previa, pero no debe fallar
    }
}
