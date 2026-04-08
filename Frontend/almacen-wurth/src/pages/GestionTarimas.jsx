import React, { useState, useMemo, useEffect } from "react";
import {
  Box,
  Typography,
  Button,
  IconButton,
  TextField,
  MenuItem,
  Select,
  Paper,
  LinearProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";

import {
  ChevronLeft,
  ChevronRight,
  Delete,
} from "@mui/icons-material";

import AppButton from "../components/AppButton";
import TarimasTable from "../components/tarimas/TarimaTable";
import MoverArticuloDialog from "../components/tarimas/MoverArticuloDialog";

import CrearRackDialog from "../components/tarimas/CrearRackDialog";
import CrearTarimaDialog from "../components/tarimas/CrearTarimaDialog";

import { useRacks } from "../hooks/tarimas/useRacks";
import { useTarimas } from "../hooks/tarimas/useTarimas";
import { useArticulosTarima } from "../hooks/tarimas/useArticuloTarima";

export default function GestionTarimas() {
  const { items: racks, create: crearRack, remove: eliminarRack } = useRacks();
  const { items: tarimas, create: crearTarima, remove: eliminarTarima } = useTarimas();
  const {
    items: articulos,
    create: crearArticulo,
    update: editarArticulo,
    remove: eliminarArticulo,
    loadByTarima, // ✅ IMPORTANTE
  } = useArticulosTarima();

  const [rackIndex, setRackIndex] = useState(0);
  const [busqueda, setBusqueda] = useState("");
  const [resultados, setResultados] = useState([]);

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [itemEliminar, setItemEliminar] = useState(null);

  const [openCrearRack, setOpenCrearRack] = useState(false);
  const [openCrearTarima, setOpenCrearTarima] = useState(false);

  const [openMover, setOpenMover] = useState(false);
  const [articuloMover, setArticuloMover] = useState(null);
  const [tarimaOrigenId, setTarimaOrigenId] = useState(null);

  // =========================
  // DERIVADOS
  // =========================
  const rackActual = racks[rackIndex];

  const tarimasActuales = useMemo(() => {
    return tarimas.filter((t) => t.rackId === rackActual?.id);
  }, [tarimas, rackActual]);

  // =========================
  // 🔥 CARGAR ARTICULOS CUANDO CAMBIA EL RACK
  // =========================
  useEffect(() => {
    if (tarimasActuales.length > 0) {
      tarimasActuales.forEach((t) => {
        loadByTarima(t.id);
      });
    }
  }, [tarimasActuales]);

  // =========================
  // NAVEGACIÓN
  // =========================
  const irAnterior = () => setRackIndex((i) => Math.max(0, i - 1));
  const irSiguiente = () =>
    setRackIndex((i) => Math.min(racks.length - 1, i + 1));

  // =========================
  // BÚSQUEDA
  // =========================
  useEffect(() => {
    if (!busqueda) return setResultados([]);

    const q = busqueda.toLowerCase();

    const encontrados = articulos.filter((a) =>
      a.codigo.toLowerCase().includes(q)
    );

    setResultados(encontrados.slice(0, 5));
  }, [busqueda, articulos]);

  // =========================
  // OCUPACIÓN
  // =========================
  const ocupacion = rackActual
    ? (tarimasActuales.length / rackActual.limiteTarimas) * 100
    : 0;

  // =========================
  // HANDLERS
  // =========================
  const handleEliminarRackConfirmado = async () => {
    await eliminarRack(itemEliminar.id);
    setConfirmOpen(false);
  };

  const handleEliminarTarima = async (tarima) => {
    await eliminarTarima(tarima.id);
  };

  const handleAgregarArticulo = async (data) => {
    await crearArticulo(data);
    await loadByTarima(data.tarimaId); // 🔥 recargar
  };

  const handleEditarCantidad = async (id, cantidad, tarimaId) => {
    await editarArticulo(id, { cantidad }, tarimaId);
  };

  const handleEliminarArticulo = async (id, tarimaId) => {
    await eliminarArticulo(id, tarimaId);
  };

  const handleCrearRack = async (data) => {
    await crearRack(data);
    setOpenCrearRack(false);
  };

  const handleCrearTarima = async (data) => {
    await crearTarima({
      ...data,
      rackId: rackActual.id,
    });
    setOpenCrearTarima(false);
  };

  const handleAbrirMover = (articulo, tarimaId) => {
    setArticuloMover(articulo);
    setTarimaOrigenId(tarimaId);
    setOpenMover(true);
  };

  const tarimasPorRack = useMemo(() => {
    const map = {};
    tarimas.forEach((t) => {
      if (!map[t.rackId]) map[t.rackId] = [];
      map[t.rackId].push(t);
    });
    return map;
  }, [tarimas]);

  // =========================
  // RENDER
  // =========================
  return (
    <Box sx={{ p: 3 }}>
      <Paper sx={{ p: 2, mb: 2 }}>
        <Box display="flex" alignItems="center" gap={2} flexWrap="wrap">
          <IconButton onClick={irAnterior} disabled={rackIndex === 0}>
            <ChevronLeft />
          </IconButton>

          <IconButton
            onClick={irSiguiente}
            disabled={rackIndex >= racks.length - 1}
          >
            <ChevronRight />
          </IconButton>

          <Typography fontWeight={600}>
            {rackActual?.ubicacion || "Sin racks"}
          </Typography>

          <Select
            size="small"
            value={rackActual?.id || ""}
            onChange={(e) => {
              const idx = racks.findIndex((r) => r.id === e.target.value);
              if (idx >= 0) setRackIndex(idx);
            }}
          >
            {racks.map((r) => (
              <MenuItem key={r.id} value={r.id}>
                {r.ubicacion}
              </MenuItem>
            ))}
          </Select>

          <Box flex={1} />

          <TextField
            size="small"
            placeholder="Buscar artículo"
            value={busqueda}
            onChange={(e) => setBusqueda(e.target.value)}
          />

          <Button
            variant="outlined"
            color="error"
            startIcon={<Delete />}
            onClick={() => {
              setItemEliminar(rackActual);
              setConfirmOpen(true);
            }}
          >
            Eliminar Rack
          </Button>

          <AppButton variant="contained" onClick={() => setOpenCrearRack(true)}>
            Rack
          </AppButton>

          <AppButton variant="contained" onClick={() => setOpenCrearTarima(true)}>
            Tarima
          </AppButton>
        </Box>

        {/* SEPARADOR + BARRA */}
{rackActual && (
  <Box sx={{ borderTop: "1px solid #e2e8f0", mt: 2, pt: 1 }}>
    
    <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
      
      {/* TEXTO IZQUIERDA */}
      <Typography variant="body2" sx={{ minWidth: 120 }}>
        <strong>{tarimasActuales.length}</strong> / {rackActual.limiteTarimas} tarimas
      </Typography>

      {/* BARRA PROGRESO */}
      <Box sx={{ flex: 1 }}>
        <LinearProgress
          variant="determinate"
          value={Math.min(ocupacion, 100)}
          sx={{
            height: 8,
            borderRadius: 5,
            backgroundColor: "#e5e7eb",
            "& .MuiLinearProgress-bar": {
              backgroundColor:
                ocupacion >= 100
                  ? "#ef4444"
                  : ocupacion >= 75
                  ? "#f59e0b"
                  : "#22c55e",
            },
          }}
        />
      </Box>

      {/* LEYENDA DERECHA */}
      <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
        <Box sx={{ display: "flex", alignItems: "center", gap: 0.5 }}>
          <Box sx={{ width: 10, height: 10, borderRadius: "50%", bgcolor: "#22c55e" }} />
          <Typography variant="caption">Disponible</Typography>
        </Box>

        <Box sx={{ display: "flex", alignItems: "center", gap: 0.5 }}>
          <Box sx={{ width: 10, height: 10, borderRadius: "50%", bgcolor: "#f59e0b" }} />
          <Typography variant="caption">Casi llena</Typography>
        </Box>

        <Box sx={{ display: "flex", alignItems: "center", gap: 0.5 }}>
          <Box sx={{ width: 10, height: 10, borderRadius: "50%", bgcolor: "#ef4444" }} />
          <Typography variant="caption">Llena</Typography>
        </Box>
      </Box>

    </Box>
  </Box>
)}
      </Paper>

      <TarimasTable
        tarimas={tarimasActuales}
        todosArticulos={articulos}
        racks={racks}
        todasTarimas={tarimas}
        onEliminarTarima={handleEliminarTarima}
        onAgregarArticulo={handleAgregarArticulo}
        onEditarCantidad={handleEditarCantidad}
        onEliminarArticulo={handleEliminarArticulo}
        onMoverArticulo={handleAbrirMover}
      />

      <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
        <DialogTitle>Confirmar eliminación</DialogTitle>
        <DialogContent>
          ¿Seguro que deseas eliminar "{itemEliminar?.ubicacion}"?
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmOpen(false)}>Cancelar</Button>
          <Button
            color="error"
            variant="contained"
            onClick={handleEliminarRackConfirmado}
          >
            Eliminar
          </Button>
        </DialogActions>
      </Dialog>

      <CrearRackDialog
        open={openCrearRack}
        onClose={() => setOpenCrearRack(false)}
        onCrear={handleCrearRack}
      />

      <CrearTarimaDialog
        open={openCrearTarima}
        onClose={() => setOpenCrearTarima(false)}
        onCrear={handleCrearTarima}
        racks={racks}
        tarimasPorRack={tarimasPorRack}
        rackPreseleccionado={rackActual?.id}
      />

      <MoverArticuloDialog
        open={openMover}
        onClose={() => setOpenMover(false)}
        articulo={articuloMover}
        racks={racks}
        tarimas={tarimas}
        tarimaActualId={tarimaOrigenId}
      />
    </Box>
  );
}