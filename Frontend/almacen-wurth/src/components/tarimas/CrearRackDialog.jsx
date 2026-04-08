import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  IconButton,
  Box,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { LayoutGrid } from "lucide-react";

export default function CrearRackDialog({ open, onClose, onCrear }) {
  const [formData, setFormData] = useState({
    ubicacion: "",
    limiteTarimas: 10,
  });

  useEffect(() => {
    if (open) {
      setFormData({
        ubicacion: "",
        limiteTarimas: 10,
      });
    }
  }, [open]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onCrear({
      ubicacion: formData.ubicacion,
      limiteTarimas: parseInt(formData.limiteTarimas),
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <Box component="form" onSubmit={handleSubmit}>
        {/* Título */}
        <DialogTitle sx={{ m: 0, p: 2 }}>
          <Box display="flex" alignItems="center" gap={1}>
            <LayoutGrid size={18} />
            Nuevo Rack
          </Box>

          <IconButton
            onClick={onClose}
            sx={{ position: "absolute", right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>

        {/* Contenido */}
        <DialogContent dividers>
          <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
            <Box>
              <Typography sx={{ mb: 1, fontWeight: 500 }}>
                Ubicación
              </Typography>
              <TextField
                required
                size="small"
                name="ubicacion"
                value={formData.ubicacion}
                onChange={handleChange}
                fullWidth
                placeholder="Ejemplo: Rack A - Fila 1"
              />
            </Box>

            <Box>
              <Typography sx={{ mb: 1, fontWeight: 500 }}>
                Capacidad de tarimas
              </Typography>
              <TextField
                required
                size="small"
                type="number"
                name="limiteTarimas"
                value={formData.limiteTarimas}
                onChange={handleChange}
                fullWidth
              />
            </Box>
          </Box>
        </DialogContent>

        {/* Botones */}
        <DialogActions sx={{ p: 2 }}>
          <Button
            variant="outlined"
            onClick={onClose}
            sx={{
              textTransform: "none",
              fontWeight: 500,
              color: "#374151",
              borderColor: "#e5e7eb",
              px: 3,
              py: 1,
              "&:hover": {
                borderColor: "#d1d5db",
                backgroundColor: "#ebebeb",
              },
            }}
          >
            Cancelar
          </Button>

          <Button
            variant="contained"
            type="submit"
            sx={{
              textTransform: "none",
              fontWeight: 500,
              backgroundColor: "#b91c1c",
              px: 3,
              py: 1,
              boxShadow: "none",
              "&:hover": {
                backgroundColor: "#991b1b",
                boxShadow: "none",
              },
            }}
          >
            Crear Rack
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}