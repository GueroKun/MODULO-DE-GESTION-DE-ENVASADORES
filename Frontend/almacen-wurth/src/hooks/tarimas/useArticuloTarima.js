// src/hooks/useArticulosTarima.js
import { useState } from "react";
import { articulosTarimaApi } from "../../services/tarimas/articulosTarimaApi";

export function useArticulosTarima() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  // Cargar artículos de una tarima SIN borrar los demás
  const loadByTarima = async (tarimaId) => {
    setLoading(true);
    try {
      const data = await articulosTarimaApi.listByTarima(tarimaId);
      const nuevos = Array.isArray(data) ? data : [];

      setItems((prev) => {
        // Quitar artículos de esa tarima
        const otros = prev.filter((a) => a.tarimaId !== tarimaId);
        // Agregar los nuevos
        return [...otros, ...nuevos];
      });
    } finally {
      setLoading(false);
    }
  };

  const create = async (payload) => {
    await articulosTarimaApi.create(payload);
    await loadByTarima(payload.tarimaId);
  };

  const update = async (id, payload, tarimaId) => {
    await articulosTarimaApi.update(id, payload);

    // Actualizar estado local SIN recargar todo
    setItems((prev) =>
      prev.map((item) =>
        item.id === id ? { ...item, ...payload } : item
      )
    );
  };

  const remove = async (id) => {
    await articulosTarimaApi.remove(id);

    // Eliminar del estado local
    setItems((prev) => prev.filter((item) => item.id !== id));
  };

  return {
    items,
    loading,
    loadByTarima,
    create,
    update,
    remove,
  };
}