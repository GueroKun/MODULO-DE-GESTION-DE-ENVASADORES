import { http } from "./http";

const BASE_URL = "http://localhost:8080/api/envasado";

export const procesoFinalizadoApi = {

  // 🔥 SOLO FINALIZADOS
  list: () =>
    http(`${BASE_URL}/finalizados`),

};