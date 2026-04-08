import { useState } from "react";
import { analisisApi } from "../services/analisisApi";

export const useAnalisis = () => {

  const [embarques, setEmbarques] = useState([]);
  const [preview, setPreview] = useState(null);
  const [loading, setLoading] = useState(false);

  // 🔍 GENERAR PREVIEW
  const generarPreview = async (file) => {
    setLoading(true);
    try {
      const data = await analisisApi.preview(file);
      setPreview(data);
      return data;
    } finally {
      setLoading(false);
    }
  };

  // 💾 CONFIRMAR
  const confirmarPreview = async (previewId) => {
    setLoading(true);
    try {
      const data = await analisisApi.confirmar(previewId);
      await getEmbarques();
      setPreview(null);
      return data;
    } finally {
      setLoading(false);
    }
  };

  // ❌ CANCELAR
  const cancelarPreview = async (previewId) => {
    if (!previewId) return;
    await analisisApi.cancelarPreview(previewId);
    setPreview(null);
  };

  // 📄 LISTAR
  const getEmbarques = async () => {
    setLoading(true);
    try {
      const data = await analisisApi.list();
      setEmbarques(data);
    } finally {
      setLoading(false);
    }
  };

  return {
    generarPreview,
    confirmarPreview,
    cancelarPreview,
    preview,
    embarques,
    loading
  };
};