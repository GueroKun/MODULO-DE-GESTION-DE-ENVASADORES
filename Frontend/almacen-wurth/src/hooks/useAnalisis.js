import { useState } from "react";
import { analisisApi } from "../services/analisisApi";

export const useAnalisis = () => {

  const [embarques, setEmbarques] = useState([]);
  const [detalles, setDetalles] = useState([]);
  const [loading, setLoading] = useState(false);

  // 📤 SUBIR
  const upload = async (file) => {
    setLoading(true);
    try {
      await analisisApi.upload(file);

      // 🔥 refrescar lista automáticamente
      await getEmbarques();

    } finally {
      setLoading(false);
    }
  };

  // 📄 LISTAR EMBARQUES
  const getEmbarques = async () => {
    setLoading(true);
    try {
      const data = await analisisApi.list();
      setEmbarques(data);
    } finally {
      setLoading(false);
    }
  };

  // 📊 DETALLES
  const getDetalles = async (id) => {
    setLoading(true);
    try {
      const data = await analisisApi.getDetalles(id);
      setDetalles(data);
    } finally {
      setLoading(false);
    }
  };

  return {
    upload,
    getEmbarques,
    getDetalles,
    embarques,
    detalles,
    loading,
  };
};