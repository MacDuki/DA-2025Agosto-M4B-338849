// Minimal client for dashboard endpoints and polling

async function apiGet(url) {
  const res = await fetch(url, { headers: { Accept: "application/json" } });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

async function apiDelete(url) {
  const res = await fetch(url, {
    method: "DELETE",
    headers: { Accept: "application/json" },
  });
  if (!res.ok) {
    // Try to parse body for message even on OK mapping in global handler
    let msg = await res.text();
    throw new Error(msg || `HTTP ${res.status}`);
  }
  return res.json();
}

export async function getDashboard(cedula) {
  return apiGet(`/propietarios/${cedula}/dashboard`);
}

export async function getDashboardVersion(cedula) {
  return apiGet(`/propietarios/${cedula}/dashboard/version`);
}

export async function borrarNotificaciones(cedula) {
  return apiDelete(`/propietarios/${cedula}/notificaciones`);
}
