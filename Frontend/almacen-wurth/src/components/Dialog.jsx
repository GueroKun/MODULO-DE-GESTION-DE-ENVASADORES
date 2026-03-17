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

export default function MuiDialog({
  open,
  onClose,
  editing,
  onSubmit,
  formData,
  setFormData,
}) {
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

   const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(); //
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">

     <Box component="form" onSubmit={handleSubmit}>
      {/* 🔹 Título */}
      <DialogTitle sx={{ m: 0, p: 2 }}>
        {editing ? "Editar Envasador" : "Nuevo Envasador"}

        <IconButton
          onClick={onClose}
          sx={{ position: "absolute", right: 8, top: 8 }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      {/* 🔹 Contenido */}
      <DialogContent dividers>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
          {/* Nombre */}
          <Box>
            <Typography sx={{ mb: 1, fontWeight: 500 }}>
              Nombre
            </Typography>
            <TextField
            required
            size="small"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              fullWidth
              placeholder="Ingrese el nombre"
            />
          </Box>
        </Box>
      </DialogContent>

      {/* 🔹 Botones */}
      <DialogActions sx={{ p: 2 }}>
        <Button variant="outlined" onClick={onClose}
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
          }}>
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
          {editing ? "Guardar cambios" : "Crear envasador"}
        </Button>
      </DialogActions>
      </Box>
    </Dialog>
  );
}
