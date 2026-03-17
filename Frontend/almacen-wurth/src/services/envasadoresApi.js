// src/services/envasadoresApi.js
import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/envasadores";

export const envasadoresApi = {
  list: () => http(BASE_URL),

  create: (payload) =>
    http(BASE_URL, {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  update: (id, payload) =>
    http(`${BASE_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    }),

  remove: (id) =>
    http(`${BASE_URL}/${id}`, {
      method: "DELETE",
    }),
};
