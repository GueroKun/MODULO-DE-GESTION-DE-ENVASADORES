// src/hooks/tarimas/useTarimas.js
import { useCallback, useEffect, useState } from "react";
import { tarimasApi } from "../../services/tarimas/tarimasApi";

export function useTarimas() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await tarimasApi.list();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    reload();
  }, [reload]);

  const create = async (payload) => {
    await tarimasApi.create(payload);
    await reload();
  };

  const move = async (tarimaId, nuevoRackId) => {
    await tarimasApi.move(tarimaId, nuevoRackId);
    await reload();
  };

  const remove = async (id) => {
    await tarimasApi.remove(id);
    await reload();
  };

  return { items, loading, reload, create, move, remove };
}