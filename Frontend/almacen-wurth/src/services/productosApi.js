import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/productos";

export const productosApi = {
  list: () => http(BASE_URL),

  create: (payload) =>
    http(BASE_URL, {
      method: "POST",
      body: JSON.stringify(payload),
    }),


};
