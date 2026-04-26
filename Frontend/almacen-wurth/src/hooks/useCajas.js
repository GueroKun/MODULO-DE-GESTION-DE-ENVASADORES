import { useCallback, useEffect, useState } from "react";
import { cajasApi } from "../services/cajasApi";
import { movimientosCajaApi } from "../services/movimientosCajaApi";
import { stockCajasApi } from "../services/stockCajasApi";

export function useCajas() {
  const [cajas, setCajas] = useState([]);
  const [stock, setStock] = useState([]);
  const [entradas, setEntradas] = useState([]);
  const [salidas, setSalidas] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadAll = useCallback(async () => {
    setLoading(true);
    try {
      const [cajasData, stockData, entradasData, salidasData] = await Promise.all([
        cajasApi.getAll(),
        stockCajasApi.getAll(),
        movimientosCajaApi.getEntradas(),
        movimientosCajaApi.getSalidas(),
      ]);

      setCajas(cajasData || []);
      setStock(stockData || []);
      setEntradas(entradasData || []);
      setSalidas(salidasData || []);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadAll();
  }, [loadAll]);

  const createCaja = async (payload) => {
    const created = await cajasApi.create(payload);
    await loadAll();
    return created;
  };

  const updateCaja = async (id, payload) => {
    const updated = await cajasApi.update(id, payload);
    await loadAll();
    return updated;
  };

  const deleteCaja = async (id) => {
    await cajasApi.remove(id);
    await loadAll();
  };

  const registrarEntrada = async (payload) => {
    const created = await movimientosCajaApi.registrarEntrada(payload);
    await loadAll();
    return created;
  };

  const registrarSalida = async (payload) => {
    const created = await movimientosCajaApi.registrarSalida(payload);
    await loadAll();
    return created;
  };

  const historialPorCaja = async (cajaId) => {
    return await movimientosCajaApi.getHistorialPorCaja(cajaId);
  };

  return {
    cajas,
    stock,
    entradas,
    salidas,
    loading,
    reload: loadAll,
    createCaja,
    updateCaja,
    deleteCaja,
    registrarEntrada,
    registrarSalida,
    historialPorCaja,
  };
}