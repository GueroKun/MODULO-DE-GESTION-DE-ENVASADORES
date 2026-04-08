import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/embarques";

export const analisisApi = {

  // 🔍 GENERAR PREVIEW
  preview: (file) => {
    const formData = new FormData();
    formData.append("file", file);

    return fetch(`${BASE_URL}/preview`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: formData
    }).then(res => {
      if (!res.ok) throw new Error("Error al generar preview");
      return res.json();
    });
  },

  // 💾 CONFIRMAR (GUARDAR)
  confirmar: (previewId) =>
    http(`${BASE_URL}/confirmar/${previewId}`, {
      method: "POST"
    }),

  // ❌ CANCELAR PREVIEW
  cancelarPreview: (previewId) =>
    http(`${BASE_URL}/preview/${previewId}`, {
      method: "DELETE"
    }),

  // 📄 LISTAR EMBARQUES
  list: () => http(BASE_URL),

  // 📄 OBTENER UNO
  get: (id) => http(`${BASE_URL}/${id}`),

};