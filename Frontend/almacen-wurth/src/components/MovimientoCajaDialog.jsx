import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  MenuItem,
} from "@mui/material";

export default function MovimientoCajaDialog({
  open,
  onClose,
  onSubmit,
  title,
  cajas,
  formData,
  setFormData,
  lockCaja = false,
  lockUbicacion = false,
}) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{title}</DialogTitle>

      <DialogContent>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
          <TextField
            select
            label="Caja"
            fullWidth
            value={formData.cajaId}
            disabled={lockCaja}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, cajaId: e.target.value }))
            }
          >
            {cajas.map((caja) => (
              <MenuItem key={caja.id} value={caja.id}>
                {caja.codigo}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="Cantidad"
            type="number"
            fullWidth
            value={formData.cantidad}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, cantidad: e.target.value }))
            }
          />

          <TextField
            label="Ubicación"
            fullWidth
            value={formData.ubicacion}
            disabled={lockUbicacion}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, ubicacion: e.target.value }))
            }
          />

          <TextField
            label="Usuario"
            fullWidth
            value={formData.usuario}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, usuario: e.target.value }))
            }
          />

          <TextField
            label="Observaciones"
            fullWidth
            multiline
            minRows={3}
            value={formData.observaciones}
            onChange={(e) =>
              setFormData((prev) => ({
                ...prev,
                observaciones: e.target.value,
              }))
            }
          />
        </Box>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button variant="contained" onClick={onSubmit}>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}