import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControlLabel,
  Switch,
  Button,
  Box,
} from "@mui/material";

export default function CajaDialog({
  open,
  onClose,
  onSubmit,
  formData,
  setFormData,
  editingCaja,
}) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {editingCaja ? "Editar caja" : "Nueva caja"}
      </DialogTitle>

      <DialogContent>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
          <TextField
            label="Código"
            fullWidth
            value={formData.codigo}
            onChange={(e) =>
              setFormData((prev) => ({ ...prev, codigo: e.target.value }))
            }
          />

          <FormControlLabel
            control={
              <Switch
                checked={!!formData.activo}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, activo: e.target.checked }))
                }
              />
            }
            label="Caja activa"
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