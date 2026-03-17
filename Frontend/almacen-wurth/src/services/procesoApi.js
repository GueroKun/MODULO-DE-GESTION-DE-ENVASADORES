// src/services/procesoApi.js
import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/envasado";

export const procesoApi = {
  // obtener procesos en curso
  enProceso: () =>
    http(`${BASE_URL}/en-proceso`),

  finalizados: () =>
    http(`${BASE_URL}/finalizados`),

  // iniciar proceso
  iniciar: (payload) =>
    http(`${BASE_URL}/iniciar`, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  // finalizar proceso
  finalizar: (id) =>
    http(`${BASE_URL}/${id}/finalizar`, {
      method: "PUT",
    }),

  // obtener proceso por id
  getById: (id) =>
    http(`${BASE_URL}/${id}`),
};