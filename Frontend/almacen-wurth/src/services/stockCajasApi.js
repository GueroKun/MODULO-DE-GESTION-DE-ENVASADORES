import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/stock-cajas";

export const stockCajasApi = {
  getAll: () => http(BASE_URL),

  getByCaja: (cajaId) =>
    http(`${BASE_URL}/caja/${cajaId}`),

  buscarPorUbicacion: (ubicacion) =>
    http(`${BASE_URL}/buscar?ubicacion=${encodeURIComponent(ubicacion)}`),
};