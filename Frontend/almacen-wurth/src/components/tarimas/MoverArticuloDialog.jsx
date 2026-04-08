import React, { useState, useMemo } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Divider,
  CircularProgress,
} from "@mui/material";

import { ArrowRightLeft } from "lucide-react";

export default function MoverArticuloDialog({
  open,
  onClose,
  articulo,
  racks,
  tarimas,
  tarimaActualId,
  onMoverArticulo,
  loading = false,
}) {
  const [tarimaDestinoId, setTarimaDestinoId] = useState("");

  const handleClose = () => {
    setTarimaDestinoId("");
    onClose();
  };

  const handleMover = () => {
    if (!tarimaDestinoId || !articulo) return;
    onMoverArticulo(articulo.id, tarimaDestinoId);
    setTarimaDestinoId("");
  };

  // Agrupar tarimas por rack
  const rackGroups = useMemo(() => {
    const grupos = {};

    tarimas
      .filter((t) => t.id !== tarimaActualId)
      .forEach((t) => {
        const rack = racks.find((r) => r.id === t.rackId);
        const nombreRack = rack?.ubicacion || "Sin rack";

        if (!grupos[nombreRack]) grupos[nombreRack] = [];
        grupos[nombreRack].push(t);
      });

    return grupos;
  }, [tarimas, racks, tarimaActualId]);

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        <Box display="flex" alignItems="center" gap={1}>
          <ArrowRightLeft size={18} />
          Mover artículo
        </Box>
      </DialogTitle>

      <DialogContent>
        {articulo && (
          <Box
            sx={{
              bgcolor: "#f1f5f9",
              borderRadius: 2,
              px: 2,
              py: 1,
              mb: 2,
            }}
          >
            <Typography variant="caption" color="text.secondary">
              Artículo a mover
            </Typography>

            <Typography fontFamily="monospace" fontWeight="bold">
              {articulo.codigo}
            </Typography>

            <Typography variant="caption" color="text.secondary">
              Cantidad: <strong>{articulo.cantidad}</strong>
            </Typography>
          </Box>
        )}

        <FormControl fullWidth size="small">
          <InputLabel>Tarima destino</InputLabel>
          <Select
            value={tarimaDestinoId}
            label="Tarima destino"
            onChange={(e) => setTarimaDestinoId(e.target.value)}
          >
            {Object.entries(rackGroups).map(([rackUbicacion, tars]) => (
              <Box key={rackUbicacion}>
                <MenuItem disabled>
                  <Typography variant="caption" fontWeight="bold">
                    {rackUbicacion}
                  </Typography>
                </MenuItem>

                <Divider />

                {tars.map((t) => (
                  <MenuItem key={t.id} value={t.id}>
                    Tarima {t.numeroReferencia}
                  </MenuItem>
                ))}
              </Box>
            ))}
          </Select>
        </FormControl>
      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose}>Cancelar</Button>

        <Button
          variant="contained"
          color="error"
          onClick={handleMover}
          disabled={!tarimaDestinoId || loading}
        >
          {loading ? <CircularProgress size={20} /> : "Mover"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}