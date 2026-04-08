import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/usuarios";

export const usuariosApi = {
  list: () => http(BASE_URL),

  create: ({ nombre, password, rol }) =>
    http(`${BASE_URL}?nombre=${encodeURIComponent(nombre)}&password=${encodeURIComponent(password)}&rol=${rol}`, {
      method: "POST",
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