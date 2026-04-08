import { useCallback, useEffect, useState } from "react";
import { usuariosApi } from "../services/usuariosApi";

export function useUsuarios() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await usuariosApi.list();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    reload();
  }, [reload]);

  const create = async (payload) => {
    await usuariosApi.create(payload);
    await reload();
  };

  const update = async (id, payload) => {
    await usuariosApi.update(id, payload);
    await reload();
  };

  const remove = async (id) => {
    await usuariosApi.remove(id);
    await reload();
  };

  return { items, loading, reload, create, update, remove };
}