import React, { useState } from "react";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    MenuItem,
    IconButton,
    Box,
    Stack,
} from "@mui/material";

import CloseIcon from "@mui/icons-material/Close";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";

export default function ProcesoDialog({
    open,
    onClose,
    onSubmit,
    formData,
    setFormData,
    envasadoresActivos,
    envasadoresConTarea,
    articulos,
}) {
    const [errorCodigo, setErrorCodigo] = useState("");
    const handleSubmit = (e) => {
  e.preventDefault();

  const art = articulos.find(
    (a) =>
      a.codigo?.trim().toLowerCase() ===
      formData.codigo_articulo?.trim().toLowerCase()
  );

  if (!art) {
    setErrorCodigo("Este código no está registrado");
    return;
  }

  setErrorCodigo("");

  setFormData({
    ...formData,
    nombre_articulo: art.nombre,
    presentacion: art.minimoEnvasado,
  });

  onSubmit(e);
};

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">

            <Box component="form" onSubmit={handleSubmit}>

                <DialogTitle
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 1,
                    }}
                >
                    <PlayArrowIcon />
                    Iniciar envasado

                    <IconButton
                        onClick={onClose}
                        sx={{ position: "absolute", right: 10, top: 10 }}
                    >
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                <DialogContent>

                    <Stack spacing={2} sx={{ mt: 1 }}>

                        <TextField
                            select
                            label="Envasador"
                            required
                            value={formData.envasador_id}
                            onChange={(e) =>
                                setFormData({
                                    ...formData,
                                    envasador_id: e.target.value,
                                })
                            }
                            fullWidth
                        >
                            {envasadoresActivos
                                .filter(
                                    (e) =>
                                        !envasadoresConTarea.includes(e.id)
                                )
                                .map((emp) => (
                                    <MenuItem
                                        key={emp.id}
                                        value={emp.id}
                                    >
                                        {emp.nombre}
                                    </MenuItem>
                                ))}
                        </TextField>

                        <TextField
                            label="Código del artículo"
                            value={formData.codigo_articulo}
                            onChange={(e) => {
                                const codigo = e.target.value;

                                const art = articulos.find(
                                    (a) =>
                                        a.codigo?.trim().toLowerCase() ===
                                        codigo.trim().toLowerCase()
                                );

                                if (art) {
                                    setFormData({
                                        ...formData,
                                        codigo_articulo: codigo,
                                        nombre_articulo: art.nombre,
                                        presentacion: art.minimoEnvasado,
                                    });
                                } else {
                                    setFormData({
                                        ...formData,
                                        codigo_articulo: codigo,
                                        nombre_articulo: "",
                                    });
                                }
                            }}
                            fullWidth
                            required
                            error={!!errorCodigo}
                            helperText={errorCodigo}
                        />

                        <TextField
                            label="Nombre del artículo"
                            value={formData.nombre_articulo}
                            disabled
                            fullWidth
                        />

                    </Stack>

                </DialogContent>

                <DialogActions>

                    <Button onClick={onClose}
                        variant="outlined"
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
                        type="submit"
                        variant="contained"
                        sx={{
                            textTransform: "none",
                            backgroundColor: "#b91c1c",
                            "&:hover": {
                                backgroundColor: "#991b1b",
                            },
                        }}
                    >
                        Iniciar envasado
                    </Button>

                </DialogActions>

            </Box>

        </Dialog>
    );
}