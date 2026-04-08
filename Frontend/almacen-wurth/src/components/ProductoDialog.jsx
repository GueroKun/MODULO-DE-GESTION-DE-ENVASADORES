import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Grid,
  FormControl,
  Select,
  MenuItem,
  IconButton,
  Typography,
  Box,
  OutlinedInput,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { Package } from "lucide-react";

export default function ProductoDialog({
  open,
  onClose,
  onSubmit,
  formData,
  setFormData,
}) {
  const handleChange = (e) => {
    const { name, value } = e.target;

    // Limites de caracteres 
    const limits = {
      codigo: 20,
      nombre: 50,
      ubicacionArticulo: 10, 
    };

    if (limits[name] && value.length > limits[name]) {
      return;
    }

    // Campos numéricos
    const numericFields = ["totalUnidades", "stockActual", "minimoEnvasado"];
    if (numericFields.includes(name)) {
      setFormData((prev) => ({ ...prev, [name]: Number(value) || 0 }));
      return;
    }

    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit();
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullWidth
      maxWidth="sm"
      PaperProps={{
        sx: {
          borderRadius: 2,
          boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.25)",
        },
      }}
    >
      <Box component="form" onSubmit={handleSubmit}>
        {/* Header */}
        <DialogTitle
          sx={{
            m: 0,
            p: 3,
            pb: 2,
            display: "flex",
            alignItems: "center",
            gap: 1.5,
          }}
        >
          <Box
            sx={{
              color: "#b91c1c",
              display: "flex",
              alignItems: "center",
            }}
          >
            <Package sx={{ fontSize: 24 }} />
          </Box>
          <Typography
            sx={{
              fontSize: "1.25rem",
              fontWeight: 600,
              color: "#111827",
              flex: 1,
            }}
          >
            {formData.id ? "Editar Artículo" : "Nuevo Artículo"}
          </Typography>
          <IconButton
            onClick={onClose}
            sx={{
              color: "#9ca3af",
              p: 0.5,
              "&:hover": {
                color: "#6b7280",
                backgroundColor: "transparent",
              },
            }}
          >
            <CloseIcon sx={{ fontSize: 20 }} />
          </IconButton>
        </DialogTitle>

        <DialogContent sx={{ p: 3, pt: 0 }}>
          <Grid container spacing={2}>
            {/* ===== Fila 1 ===== */}
            <Grid container size={12} spacing={2}>
              <Grid size={{ xs: 6, md: 8 }}>
                {/* Código */}
                <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                  Código del Artículo <span style={{ color: "#b91c1c" }}>*</span>
                </Typography>
                <TextField
                  name="codigo"
                  value={formData.codigo}
                  onChange={handleChange}
                  fullWidth
                  placeholder="Ej: 00501 111"
                  required
                  variant="outlined"
                  inputProps={{ maxLength: 20 }}
                  size="small"
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#fff",
                      "& fieldset": { borderColor: "#e5e7eb" },
                      "&:hover fieldset": { borderColor: "#d1d5db" },
                      "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                    },
                  }}
                />
              </Grid>

              <Grid size={{ xs: 6, md: 4 }}>
                {/* Prioridad */}
                <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                  Prioridad
                </Typography>
                <FormControl fullWidth size="small">
                  <Select
                    name="prioridad"
                    value={formData.prioridad || "MEDIA"}
                    onChange={handleChange}
                    displayEmpty
                    input={
                      <OutlinedInput
                        sx={{
                          backgroundColor: "#fff",
                          "& fieldset": { borderColor: "#e5e7eb" },
                          "&:hover fieldset": { borderColor: "#d1d5db" },
                          "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                        }}
                      />
                    }
                  >
                    <MenuItem value="BAJA">Baja</MenuItem>
                    <MenuItem value="MEDIA">Media</MenuItem>
                    <MenuItem value="ALTA">Alta</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>

            {/* ===== Fila 2 (Nombre FULL) ===== */}
            <Grid size={12}>
              <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                Nombre del Artículo <span style={{ color: "#b91c1c" }}>*</span>
              </Typography>
              <TextField
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                inputProps={{ maxLength: 50 }}
                fullWidth
                placeholder="Nombre descriptivo del artículo"
                required
                variant="outlined"
                size="small"
                sx={{
                  "& .MuiOutlinedInput-root": {
                    backgroundColor: "#fff",
                    "& fieldset": { borderColor: "#e5e7eb" },
                    "&:hover fieldset": { borderColor: "#d1d5db" },
                    "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                  },
                }}
              />
            </Grid>
            {/* ===== NUEVA FILA: UBICACIÓN ===== */}
            <Grid size={12}>
              <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                Ubicación del Artículo
              </Typography>
              <TextField
                name="ubicacionArticulo"
                value={formData.ubicacionArticulo || ""}
                onChange={handleChange}
                fullWidth
                placeholder="Ej: M01-001-1"
                variant="outlined"
                size="small"
                sx={{
                  "& .MuiOutlinedInput-root": {
                    backgroundColor: "#fff",
                    "& fieldset": { borderColor: "#e5e7eb" },
                    "&:hover fieldset": { borderColor: "#d1d5db" },
                    "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                  },
                }}
              />
            </Grid>
            {/* ===== Fila 3 (3 inputs en una fila) ===== */}
            <Grid container size={12} spacing={2}>
              <Grid size={{ xs: 12, sm: 4 }}>
                <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                  Cantidad Total
                </Typography>
                <TextField
                  name="totalUnidades"
                  type="number"
                  value={formData.totalUnidades}
                  onChange={handleChange}
                  fullWidth
                  placeholder="0"
                  variant="outlined"
                  size="small"
                  inputProps={{ min: 1 }}
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#fff",
                      "& fieldset": { borderColor: "#e5e7eb" },
                      "&:hover fieldset": { borderColor: "#d1d5db" },
                      "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                    },
                  }}
                />
              </Grid>

              <Grid size={{ xs: 12, sm: 4 }}>
                <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                  Stock Actual
                </Typography>
                <TextField
                  name="stockActual"
                  type="number"
                  value={formData.stockActual}
                  onChange={handleChange}
                  fullWidth
                  placeholder="0"
                  variant="outlined"
                  size="small"
                  inputProps={{ min: 0 }}
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#fff",
                      "& fieldset": { borderColor: "#e5e7eb" },
                      "&:hover fieldset": { borderColor: "#d1d5db" },
                      "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                    },
                  }}
                />
              </Grid>

              <Grid size={{ xs: 12, sm: 4 }}>
                <Typography component="label" sx={{ display: "block", fontSize: "0.875rem", fontWeight: 500, color: "#374151", mb: 0.5 }}>
                  Mín. Envasado
                </Typography>
                <TextField
                  name="minimoEnvasado"
                  type="number"
                  value={formData.minimoEnvasado}
                  onChange={handleChange}
                  fullWidth
                  placeholder="10"
                  variant="outlined"
                  size="small"
                  inputProps={{ min: 1 }}
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#fff",
                      "& fieldset": { borderColor: "#e5e7eb" },
                      "&:hover fieldset": { borderColor: "#d1d5db" },
                      "&.Mui-focused fieldset": { borderColor: "#b91c1c", borderWidth: "1px" },
                    },
                  }}
                />
              </Grid>
            </Grid>
          </Grid>

        </DialogContent>

        {/* Footer */}
        <DialogActions
          sx={{
            p: 3,
            pt: 0,
            gap: 1.5,
          }}
        >
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
            {formData.id ? "Actualizar" : "Crear artículo"}
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}