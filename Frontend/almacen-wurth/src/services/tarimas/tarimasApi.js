// src/services/tarimas/tarimasApi.js
import { http } from "../http";

const BASE_URL = "http://localhost:8080/api/tarimas";

export const tarimasApi = {
  list: () => http(BASE_URL),

  listByRack: (rackId) =>
    http(`${BASE_URL}/rack/${rackId}`),

  create: (payload) =>
    http(BASE_URL, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  move: (tarimaId, nuevoRackId) =>
    http(`${BASE_URL}/${tarimaId}/mover/${nuevoRackId}`, {
      method: "PUT",
    }),

  remove: (id) =>
    http(`${BASE_URL}/${id}`, {
      method: "DELETE",
    }),
};