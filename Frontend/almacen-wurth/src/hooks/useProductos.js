// src/hooks/useProductos.js
import { useCallback, useEffect, useState } from "react";
import { productosApi } from "../services/productosApi";

export function useProductos() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await productosApi.list();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    reload();
  }, [reload]);

  const create = async (payload) => {
    await productosApi.create(payload);
    await reload();
  };

  return { items, loading, reload, create };
}
