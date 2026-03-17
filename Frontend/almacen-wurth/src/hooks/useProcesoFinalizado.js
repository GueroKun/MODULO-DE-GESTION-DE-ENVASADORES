import { useEffect, useState } from "react";
import { procesoFinalizadoApi } from "../services/procesoFinalizadoApi";

export function useProcesoFinalizado() {

  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const data = await procesoFinalizadoApi.list();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  return {
    items,
    loading,
    reload: load,
  };
}