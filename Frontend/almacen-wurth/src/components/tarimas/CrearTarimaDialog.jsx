import React, { useState, useEffect, useMemo } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Box,
  Typography,
} from "@mui/material";
import { Package } from "lucide-react";

export default function CrearTarimaDialog({
  open,
  onClose,
  onCrear,
  racks = [],
  tarimasPorRack = {},
  rackPreseleccionado,
}) {
  const [referencia, setReferencia] = useState("");
  const [rackId, setRackId] = useState("");

  useEffect(() => {
    if (open) {
      setReferencia("");
      setRackId(rackPreseleccionado || "");
    }
  }, [open, rackPreseleccionado]);

  const racksDisponibles = useMemo(() => {
    return racks.filter((r) => {
      const ocupadas = (tarimasPorRack[r.id] || []).length;
      return ocupadas < r.limiteTarimas;
    });
  }, [racks, tarimasPorRack]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onCrear({
      numeroReferencia: referencia,
      rackId: rackId,
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        <Box display="flex" alignItems="center" gap={1}>
          <Package size={18} />
          Crear Tarima
        </Box>
      </DialogTitle>

      <DialogContent>
        <Box component="form" onSubmit={handleSubmit} mt={1}>
          <TextField
            label="Número de Referencia"
            value={referencia}
            onChange={(e) => setReferencia(e.target.value)}
            fullWidth
            required
            margin="normal"
          />

          {racksDisponibles.length === 0 ? (
            <Typography color="error" mt={2}>
              Todos los racks están llenos.
            </Typography>
          ) : (
            <TextField
              select
              label="Rack"
              value={rackId}
              onChange={(e) => setRackId(e.target.value)}
              fullWidth
              required
              margin="normal"
            >
              {racksDisponibles.map((r) => {
                const ocupadas = (tarimasPorRack[r.id] || []).length;
                return (
                  <MenuItem key={r.id} value={r.id}>
                    {r.ubicacion} ({ocupadas}/{r.limiteTarimas})
                  </MenuItem>
                );
              })}
            </TextField>
          )}

          <DialogActions>
            <Button onClick={onClose}>Cancelar</Button>
            <Button
              type="submit"
              variant="contained"
              disabled={!rackId || racksDisponibles.length === 0}
            >
              Crear Tarima
            </Button>
          </DialogActions>
        </Box>
      </DialogContent>
    </Dialog>
  );
}