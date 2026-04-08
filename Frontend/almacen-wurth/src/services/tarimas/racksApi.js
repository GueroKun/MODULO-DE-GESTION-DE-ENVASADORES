// src/services/tarimas/racksApi.js
import { http } from "../http";

const BASE_URL = "http://localhost:8080/api/racks";

export const racksApi = {
  // Lista simple
  list: () => http(BASE_URL),

  // Lista con tarimas y artículos (estructura completa)
  listDetalle: () => http(`${BASE_URL}/detalle`),

  // Detalle de un rack
  getDetalle: (id) => http(`${BASE_URL}/${id}/detalle`),

  // Crear rack
  create: (payload) =>
    http(BASE_URL, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  // Editar rack
  update: (id, payload) =>
    http(`${BASE_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    }),

  // Eliminar rack
  remove: (id) =>
    http(`${BASE_URL}/${id}`, {
      method: "DELETE",
    }),
};