// src/hooks/tarimas/useRacks.js
import { useCallback, useEffect, useState } from "react";
import { racksApi } from "../../services/tarimas/racksApi";

export function useRacks() {
  const [items, setItems] = useState([]);
  const [detalle, setDetalle] = useState([]);
  const [loading, setLoading] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await racksApi.list();
      setItems(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  }, []);

  const loadDetalle = async () => {
    setLoading(true);
    try {
      const data = await racksApi.listDetalle();
      setDetalle(Array.isArray(data) ? data : []);
    } finally {
      setLoading(false);
    }
  };

  const getDetalle = async (id) => {
    return await racksApi.getDetalle(id);
  };

  useEffect(() => {
    reload();
  }, [reload]);

  const create = async (payload) => {
    await racksApi.create(payload);
    await reload();
  };

  const update = async (id, payload) => {
    await racksApi.update(id, payload);
    await reload();
  };

  const remove = async (id) => {
    await racksApi.remove(id);
    await reload();
  };

  return {
    items,
    detalle,
    loading,
    reload,
    loadDetalle,
    getDetalle,
    create,
    update,
    remove,
  };
}