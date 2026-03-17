import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/embarques";
const DETALLE_URL = "http://localhost:8080/api/embarque-detalles";

export const analisisApi = {

  // 📤 SUBIR ARCHIVO (fetch porque es FormData)
  upload: (file) => {

    const formData = new FormData();
    formData.append("file", file);

    return fetch(`${BASE_URL}/upload`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: formData
    }).then(res => {
      if (!res.ok) throw new Error("Error al subir archivo");
      return res.json();
    });

  },

  // 📄 LISTAR EMBARQUES
  list: () => http(BASE_URL),

  // 📄 OBTENER UNO
  get: (id) => http(`${BASE_URL}/${id}`),

  // ❌ ELIMINAR
  remove: (id) =>
    http(`${BASE_URL}/${id}`, {
      method: "DELETE"
    }),

  // 📊 DETALLES POR EMBARQUE
  getDetalles: (embarqueId) =>
    http(`${DETALLE_URL}/embarque/${embarqueId}`),

};