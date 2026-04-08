import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
  Box,
  IconButton,
  Divider,
} from "@mui/material";

import { Plus, Package, Trash2, Pencil, Check, X } from "lucide-react";

export default function ArticulosTarimaDialog({
  open,
  onClose,
  tarima,
  modo,
  articulos = [],
  onAgregar,
  onEditarCantidad,
  onEliminar,
}) {
  const [codigo, setCodigo] = useState("");
  const [cantidad, setCantidad] = useState(1);
  const [editandoId, setEditandoId] = useState(null);
  const [editCantidad, setEditCantidad] = useState(1);

  if (!tarima) return null;

  const handleAgregar = (e) => {
    e.preventDefault();
    onAgregar({
      tarimaId: tarima.id,
      codigo,
      cantidad: parseInt(cantidad) || 0,
    });
    setCodigo("");
    setCantidad(1);
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        <Box display="flex" alignItems="center" gap={1}>
          <Package size={18} />
          {modo === "agregar" ? "Agregar Artículo" : "Artículos"} — Tarima{" "}
          {tarima.numeroReferencia}
        </Box>
      </DialogTitle>

      <DialogContent>
        {modo === "agregar" && (
          <Box component="form" onSubmit={handleAgregar} mb={2}>
            <Box display="grid" gridTemplateColumns="1fr 1fr" gap={2}>
              <TextField
                label="Código de Artículo"
                value={codigo}
                onChange={(e) => setCodigo(e.target.value)}
                required
                size="small"
              />
              <TextField
                label="Cantidad"
                type="number"
                value={cantidad}
                onChange={(e) => setCantidad(e.target.value)}
                inputProps={{ min: 1 }}
                required
                size="small"
              />
            </Box>

            <Button
              type="submit"
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              startIcon={<Plus size={16} />}
            >
              Agregar
            </Button>

            <Divider sx={{ mt: 2 }} />
          </Box>
        )}
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cerrar</Button>
      </DialogActions>
    </Dialog>
  );
}