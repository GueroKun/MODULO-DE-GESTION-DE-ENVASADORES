import React, { useState } from "react";
import {
  Box,
  Typography,
  IconButton,
  Button,
  Collapse,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";

import {
  KeyboardArrowDown,
  KeyboardArrowRight,
  Delete,
  Edit,
  SwapHoriz,
  Add,
  Check,
  Close,
} from "@mui/icons-material";

import { Package } from "lucide-react";

import MoverArticuloDialog from "./MoverArticuloDialog";
import ArticulosTarimaDialog from "./ArticulosTarimaDialog";

// =====================================
// FILA TARIMA
// =====================================
function FilaTarima({
  tarima,
  todosArticulos,
  racks,
  todasTarimas,
  onEliminarTarima,
  onAgregarArticulo,
  onEditarCantidad,
  onEliminarArticulo,
}) {
  const [expandida, setExpandida] = useState(false);
  const [moverArticulo, setMoverArticulo] = useState(null);

  const [dialogOpen, setDialogOpen] = useState(false);

  const [editandoId, setEditandoId] = useState(null);
  const [editCantidad, setEditCantidad] = useState(1);

  const articulosDeTarima = todosArticulos.filter(
    (a) => a.tarimaId === tarima.id
  );

  const conteo = articulosDeTarima.length;

  return (
    <>
      {/* ================= TARIMA ================= */}
      <TableRow hover>
        <TableCell width={40}>
          <IconButton size="small" onClick={() => setExpandida(!expandida)}>
            {expandida ? <KeyboardArrowDown /> : <KeyboardArrowRight />}
          </IconButton>
        </TableCell>

        <TableCell>
          <Typography fontWeight={600}>
            {tarima.numeroReferencia}
          </Typography>
        </TableCell>

        <TableCell>
          <Typography color="text.secondary">
            {conteo} artículo{conteo !== 1 ? "s" : ""}
          </Typography>
        </TableCell>

        <TableCell>
          <Box display="flex" gap={1}>
            <Button
              size="small"
              variant="outlined"
              startIcon={<Add />}
              onClick={() => setDialogOpen(true)}
            >
              Agregar
            </Button>

            <Button
              size="small"
              color="error"
              startIcon={<Delete />}
              onClick={() => onEliminarTarima(tarima)}
            >
              Eliminar
            </Button>
          </Box>
        </TableCell>
      </TableRow>

      {/* ================= ARTICULOS ================= */}
      <TableRow sx={{borderTop: "2px solid #575757"}}>
        <TableCell colSpan={4} sx={{ p: 0, border: 0 }}>
          <Collapse in={expandida}>
            <Box
              sx={{
                p: 2,
                bgcolor: "#f8fafc",
                pl: 6,
              }}
            >
              {articulosDeTarima.length === 0 ? (
                <Box display="flex" alignItems="center" gap={1}>
                  <Package color="disabled" />
                  <Typography color="text.secondary">
                    No hay artículos en esta tarima
                  </Typography>
                </Box>
              ) : (
                <Table size="small" sx={{ borderLeft: "4px solid #ea1616",}}>
                  <TableHead>
                    <TableRow sx={{ backgroundColor: "#f1f5f9" }}>
                      <TableCell>Código artículo</TableCell>
                      <TableCell>Cantidad</TableCell>
                      <TableCell>Acciones</TableCell>
                    </TableRow>
                  </TableHead>

                  <TableBody>
                    {articulosDeTarima.map((a) => (
                      <TableRow key={a.id}>
                        {/* CODIGO */}
                        <TableCell>{a.codigo}</TableCell>

                        {/* CANTIDAD EDITABLE */}
                        <TableCell>
                          {editandoId === a.id ? (
                            <Box display="flex" alignItems="center" gap={1}>
                              <Box
                                component="input"
                                type="number"
                                value={editCantidad}
                                onChange={(e) =>
                                  setEditCantidad(e.target.value)
                                }
                                style={{
                                  width: 60,
                                  padding: "4px",
                                  borderRadius: 4,
                                  border: "1px solid #cbd5e1",
                                }}
                              />

                              <IconButton
                                size="small"
                                sx={{
                                  bgcolor: "#22c55e",
                                  color: "white",
                                  "&:hover": { bgcolor: "#16a34a" },
                                }}
                                onClick={() => {
                                  onEditarCantidad(
                                    a.id,
                                    parseInt(editCantidad) || 1
                                  );
                                  setEditandoId(null);
                                }}
                              >
                                <Check fontSize="small" />
                              </IconButton>

                              <IconButton
                                size="small"
                                onClick={() => setEditandoId(null)}
                              >
                                <Close fontSize="small" />
                              </IconButton>
                            </Box>
                          ) : (
                            <Typography fontWeight={600}>
                              {a.cantidad}
                            </Typography>
                          )}
                        </TableCell>

                        {/* ACCIONES */}
                        <TableCell>
                          <Box display="flex" gap={1}>
                            <Button
                              size="small"
                              startIcon={<Edit />}
                              onClick={() => {
                                setEditandoId(a.id);
                                setEditCantidad(a.cantidad);
                              }}
                            >
                              Editar
                            </Button>

                            <Button
                              size="small"
                              startIcon={<SwapHoriz />}
                              onClick={() => setMoverArticulo(a)}
                            >
                              Mover
                            </Button>

                            <Button
                              size="small"
                              color="error"
                              startIcon={<Delete />}
                              onClick={() => onEliminarArticulo(a.id)}
                            >
                              Eliminar
                            </Button>
                          </Box>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              )}
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>

      {/* ================= DIALOG AGREGAR ================= */}
      <ArticulosTarimaDialog
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        tarima={tarima}
        modo="agregar"
        articulos={articulosDeTarima}
        onAgregar={onAgregarArticulo}
        onEditarCantidad={onEditarCantidad}
        onEliminar={onEliminarArticulo}
      />

      {/* ================= DIALOG MOVER ================= */}
      {moverArticulo && (
        <MoverArticuloDialog
          open={!!moverArticulo}
          onClose={() => setMoverArticulo(null)}
          articulo={moverArticulo}
          racks={racks}
          tarimas={todasTarimas}
          tarimaActualId={tarima.id}
        />
      )}
    </>
  );
}

// =====================================
// TABLA TARIMAS
// =====================================
export default function TarimasTable({
  tarimas,
  todosArticulos,
  racks,
  todasTarimas,
  onEliminarTarima,
  onAgregarArticulo,
  onEditarCantidad,
  onEliminarArticulo,
  onAgregarTarima,
}) {
  if (!tarimas || tarimas.length === 0) {
    return (
      <Paper
        sx={{
          p: 6,
          textAlign: "center",
          border: "2px dashed #cbd5e1",
        }}
      >
        <Package size={40} color="#94a3b8" />

        <Typography variant="h6" color="text.secondary" mt={2}>
          Este rack está vacío
        </Typography>
      </Paper>
    );
  }

  return (
    <Paper>
      <Table>
        <TableHead sx={{ bgcolor: "#f1f5f9" }}>
          <TableRow>
            <TableCell />
            <TableCell>Tarima</TableCell>
            <TableCell>Artículos</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {tarimas.map((t) => (
            <FilaTarima
              key={t.id}
              tarima={t}
              todosArticulos={todosArticulos}
              racks={racks}
              todasTarimas={todasTarimas}
              onEliminarTarima={onEliminarTarima}
              onAgregarArticulo={onAgregarArticulo}
              onEditarCantidad={onEditarCantidad}
              onEliminarArticulo={onEliminarArticulo}
            />
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}