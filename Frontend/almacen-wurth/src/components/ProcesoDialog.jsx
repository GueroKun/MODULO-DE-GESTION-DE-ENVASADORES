import React, { useState, useEffect } from "react";
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
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Stack,
  Grid
} from "@mui/material";

import CloseIcon from "@mui/icons-material/Close";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";

import { useAlert } from "../components/AlertProvider";

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
  const { showAlert } = useAlert();

  useEffect(() => {
    if (open && (!formData.asignaciones || formData.asignaciones.length === 0)) {
      setFormData({
        ...formData,
        asignaciones: [
          { envasador_id: "", cantidad: "", presentacion: "" },
        ],
      });
    }
  }, [open]);

  const initialForm = {
  codigo_articulo: "",
  nombre_articulo: "",
  asignaciones: [
    { envasador_id: "", cantidad: "", presentacion: "" },
  ],
};


const handleClose = () => {
  setFormData(initialForm);
  onClose();
};


  const asignaciones = formData.asignaciones || [];

  const setAsignaciones = (newAsignaciones) => {
    setFormData({ ...formData, asignaciones: newAsignaciones });
  };

  const addAsignacion = () => {
    setAsignaciones([
      ...asignaciones,
      { envasador_id: "", cantidad: "", presentacion: "" },
    ]);
  };

  const removeAsignacion = (index) => {
    setAsignaciones(asignaciones.filter((_, i) => i !== index));
  };

  const updateAsignacion = (index, field, value) => {
    const updated = [...asignaciones];
    updated[index][field] = value;
    setAsignaciones(updated);
  };

  const envasadoresUsados = asignaciones.map((a) => a.envasador_id);

  const total = asignaciones.reduce(
    (sum, a) => sum + (parseInt(a.cantidad) || 0),
    0
  );

   const handleSubmit = (e) => {
    e.preventDefault();

    const art = articulos.find(
      (a) =>
        a.codigo?.trim().toLowerCase() ===
        formData.codigo_articulo?.trim().toLowerCase()
    );

    if (!art) {
      showAlert("El código no existe", "error");
      return;
    }

    if (asignaciones.length === 0) {
      showAlert("Debes agregar al menos un envasador", "warning");
      return;
    }
    onSubmit(e);
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
      <Box component="form" onSubmit={handleSubmit}>

        {/* HEADER */}
        <DialogTitle sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <PlayArrowIcon sx={{ color: "#16a34a" }} />
          Iniciar Envasado

          <IconButton
             onClick={handleClose}
            sx={{ position: "absolute", right: 12, top: 12 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>

        <DialogContent sx={{ pt: 3 }}>

          {/* ARTICULO */}
          <Grid container spacing={2}>
            <Grid size={{ xs: 6, md: 6 }}>
              <Typography sx={{ fontSize: "0.875rem", fontWeight: 500, mb: 0.5 }}>
                Código del Artículo
              </Typography>

              <TextField
                value={formData.codigo_articulo}
                size="small"
                onChange={(e) => {
                  const codigo = e.target.value;

                  const art = articulos.find(
                    (a) =>
                      a.codigo?.trim().toLowerCase() ===
                      codigo.trim().toLowerCase()
                  );

                  if (art) {
                    const nuevasAsignaciones = asignaciones.map((a) => ({
                      ...a,
                      presentacion: art.minimoEnvasado || "",
                    }));

                    setFormData({
                      ...formData,
                      codigo_articulo: codigo,
                      nombre_articulo: art.nombre,
                      asignaciones: nuevasAsignaciones,
                    });
                  } else {
                    setFormData({
                      ...formData,
                      codigo_articulo: codigo,
                      nombre_articulo: "",
                    });
                  }
                }}
                required
                fullWidth
              />
            </Grid>

            <Grid size={{ xs: 6, md: 6 }}>
              <Typography sx={{ fontSize: "0.875rem", fontWeight: 500, mb: 0.5 }}>
                Nombre del Artículo
              </Typography>

              <TextField
                value={formData.nombre_articulo}
                size="small"
                disabled
                fullWidth
                sx={{ backgroundColor: "#f8fafc" }}
              />
            </Grid>
          </Grid>

          {/* TABLA */}
          <Stack spacing={3} mt={2}>
            <Box>
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography fontWeight={600} fontSize=".85rem">
                  Envasadores asignados
                </Typography>

                <Button
                  size="small"
                  variant="outlined"
                  startIcon={<AddIcon />}
                  onClick={addAsignacion}
                   sx={{
              textTransform: "none",
              fontWeight: 600,
              color: "#374151",
              borderColor: "#e5e7eb",
              px: 3,
              
              "&:hover": {
                borderColor: "#d1d5db",
                backgroundColor: "#ebebeb",
              },
            }}
                >
                  Agregar envasador
                </Button>
              </Box>

              <Box
                sx={{
                  border: "1px solid #e5e7eb",
                  borderRadius: 2,
                  overflow: "hidden",
                }}
              >
                <Table size="small">
                  <TableHead sx={{ backgroundColor: "#f9fafb" }}>
                    <TableRow>
                      <TableCell>Envasador</TableCell>
                      <TableCell>Cantidad</TableCell>
                      <TableCell>Min.Envasado</TableCell>
                      <TableCell width={10}></TableCell>
                    </TableRow>
                  </TableHead>

                  <TableBody>
                    {asignaciones.map((a, idx) => (
                      <TableRow key={idx}>
                        <TableCell>
                          <TextField
                            select
                            size="small"
                            fullWidth
                            required
                            value={a.envasador_id}
                            onChange={(e) =>
                              updateAsignacion(idx, "envasador_id", e.target.value)
                            }
                          >
                            {envasadoresActivos
                              .filter(
                                (e) =>
                                  !envasadoresConTarea.includes(e.id) &&
                                  (!envasadoresUsados.includes(e.id) ||
                                    e.id === a.envasador_id)
                              )
                              .map((emp) => (
                                <MenuItem key={emp.id} value={emp.id}>
                                {emp.nombre}
                                </MenuItem>
                              ))}
                          </TextField>
                        </TableCell>

                        <TableCell>
                          <TextField
                            type="number"
                            size="small"
                            value={a.cantidad}
                            required
                            onChange={(e) =>
                              updateAsignacion(idx, "cantidad", e.target.value)
                            }
                            placeholder="Ej: 500"
                            inputProps={{ min: 1 }}
                          />
                        </TableCell>

                        <TableCell>
                          <TextField
                            type="number"
                            size="small"
                            value={a.presentacion}
                            required
                            onChange={(e) =>
                              updateAsignacion(idx, "presentacion", e.target.value)
                            }
                            placeholder="Ej: 100"
                            inputProps={{ min: 1 }}
                          />
                        </TableCell>

                        <TableCell>
                          {asignaciones.length > 1 && (
                            <IconButton
                              onClick={() => removeAsignacion(idx)}
                              color="error"
                            >
                              <DeleteIcon fontSize="small" />
                            </IconButton>
                          )}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </Box>

              {/* TOTAL */}
              {asignaciones.length > 0 && (
                <Typography
                  variant="caption"
                  display="block"
                  textAlign="right"
                  mt={1}
                >
                  Total piezas: <b>{total}</b>
                </Typography>
              )}
            </Box>
          </Stack>
        </DialogContent>

        {/* FOOTER */}
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={handleClose}  variant="outlined"
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
            startIcon={<PlayArrowIcon />}
            sx={{
              backgroundColor: "#16a34a",textTransform: "none",
              "&:hover": { backgroundColor: "#15803d" },
            }}
          >
            Iniciar envasado
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}