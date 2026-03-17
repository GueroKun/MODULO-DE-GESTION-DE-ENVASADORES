// src/hooks/useProceso.js
import { useCallback, useEffect, useState } from "react";
import { procesoApi } from "../services/procesoApi";

export function useProceso() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await procesoApi.enProceso();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);

  const loadFinalizados = useCallback(async () => {
    setLoading(true);
    try {
      const data = await procesoApi.finalizados();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);


  useEffect(() => {
    reload();
  }, [reload]);


  const iniciar = async (payload) => {
    await procesoApi.iniciar(payload);
    await reload();
  };

  const finalizar = async (id) => {
    await procesoApi.finalizar(id);
    await reload();
  };

  return {
    items,
    loading,
    reload,
    loadFinalizados,
    iniciar,
    finalizar,
  };
}