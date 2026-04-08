import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  IconButton,
  Typography,
  Box,
  InputAdornment,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { Forklift, Eye, EyeOff } from "lucide-react";
import { useAlert } from "../components/AlertProvider";

export default function MontacargasDialog({
  open,
  onClose,
  onSubmit,
  formData,
  setFormData,
}) {
  const [showPass, setShowPass] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    const limits = {
      nombre: 20,
      password: 10,
    };

    if (limits[name] && value.length > limits[name]) return;

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
        <DialogTitle sx={headerStyle}>
          <Box sx={{ color: "#b91c1c", display: "flex", alignItems: "center" }}>
            <Forklift size={22} />
          </Box>

          <Typography sx={titleStyle}>
            {formData.id ? "Editar operador" : "Agregar nuevo operador de montacargas"}
          </Typography>

          <IconButton onClick={onClose} sx={closeBtn}>
            <CloseIcon sx={{ fontSize: 20 }} />
          </IconButton>
        </DialogTitle>

        {/* Content */}
        <DialogContent sx={{ p: 3, pt: 0 }}>
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
            
            {/* Usuario */}
            <Box>
              <Typography sx={labelStyle}>
                Usuario <span style={{ color: "#b91c1c" }}>*</span>
              </Typography>
              <TextField
                name="nombre"
                value={formData.nombre || ""}
                onChange={handleChange}
                fullWidth
                placeholder="Nombre de usuario"
                required
                size="small"
                sx={inputStyle}
              />
            </Box>

            {/* Contraseña */}
            <Box>
              <Typography sx={labelStyle}>
                Contraseña <span style={{ color: "#b91c1c" }}>*</span>
              </Typography>
              <TextField
                name="password"
                type={showPass ? "text" : "password"}
                value={formData.password || ""}
                onChange={handleChange}
                fullWidth
                placeholder="Contraseña"
                required
                size="small"
                sx={inputStyle}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={() => setShowPass(!showPass)}>
                        {showPass ? <EyeOff size={18} /> : <Eye size={18} />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Box>

          </Box>
        </DialogContent>

        {/* Footer */}
        <DialogActions sx={{ p: 3, pt: 0, gap: 1.5 }}>
          <Button variant="outlined" onClick={onClose} sx={cancelBtn}>
            Cancelar
          </Button>

          <Button variant="contained" type="submit" sx={saveBtn}>
            {formData.id ? "Actualizar" : "Crear operador"}
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}

/* ====== ESTILOS ====== */

const headerStyle = {
  m: 0,
  p: 3,
  pb: 2,
  display: "flex",
  alignItems: "center",
  gap: 1.5,
};

const titleStyle = {
  fontSize: "1.25rem",
  fontWeight: 600,
  color: "#111827",
  flex: 1,
};

const closeBtn = {
  color: "#9ca3af",
  p: 0.5,
  "&:hover": {
    color: "#6b7280",
    backgroundColor: "transparent",
  },
};

const labelStyle = {
  display: "block",
  fontSize: "0.875rem",
  fontWeight: 500,
  color: "#374151",
  marginBottom: "4px",
};

const inputStyle = {
  "& .MuiOutlinedInput-root": {
    backgroundColor: "#fff",
    "& fieldset": { borderColor: "#e5e7eb" },
    "&:hover fieldset": { borderColor: "#d1d5db" },
    "&.Mui-focused fieldset": {
      borderColor: "#b91c1c",
      borderWidth: "1px",
    },
  },
};

const cancelBtn = {
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
};

const saveBtn = {
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
};