import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/movimientos-caja";

export const movimientosCajaApi = {
  registrarEntrada: (payload) =>
    http(`${BASE_URL}/entrada`, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  registrarSalida: (payload) =>
    http(`${BASE_URL}/salida`, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  getEntradas: () => http(`${BASE_URL}/entradas`),

  getSalidas: () => http(`${BASE_URL}/salidas`),

  getHistorialPorCaja: (cajaId) =>
    http(`${BASE_URL}/caja/${cajaId}`),
};