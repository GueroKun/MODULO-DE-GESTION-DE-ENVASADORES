import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Box,
  Typography,
  Chip,
  Divider,
} from "@mui/material";

function tipoChip(tipo) {
  const styles = {
    ENTRADA: { bgcolor: "#dcfce7", color: "#15803d" },
    SALIDA: { bgcolor: "#fee2e2", color: "#b91c1c" },
  };

  return (
    <Chip
      label={tipo}
      size="small"
      sx={{ fontWeight: 700, ...(styles[tipo] || {}) }}
    />
  );
}

export default function HistorialCajaDialog({
  open,
  onClose,
  historial = [],
  cajaCodigo,
}) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="md">
      <DialogTitle>Historial de caja {cajaCodigo}</DialogTitle>

      <DialogContent>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
          {historial.length === 0 && (
            <Typography color="text.secondary">
              No hay movimientos registrados.
            </Typography>
          )}

          {historial.map((mov) => (
            <Box
              key={mov.id}
              sx={{
                border: "1px solid #e2e8f0",
                borderRadius: 2,
                p: 2,
                bgcolor: "#fff",
              }}
            >
              <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
                {tipoChip(mov.tipo)}
                <Typography variant="caption" color="text.secondary">
                  {mov.fecha || "Sin fecha"}
                </Typography>
              </Box>

              <Typography fontWeight={600}>
                Cantidad: {mov.cantidad}
              </Typography>
              <Typography variant="body2">
                Ubicación: {mov.ubicacion || "Sin ubicación"}
              </Typography>
              <Typography variant="body2">
                Usuario: {mov.usuario || "Sin usuario"}
              </Typography>
              <Typography variant="body2">
                Observaciones: {mov.observaciones || "Sin observaciones"}
              </Typography>
            </Box>
          ))}
        </Box>
      </DialogContent>
    </Dialog>
  );
}