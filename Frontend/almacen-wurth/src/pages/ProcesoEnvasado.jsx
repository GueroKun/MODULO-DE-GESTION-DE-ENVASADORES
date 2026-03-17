import React, { useState, useEffect, useMemo } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Stack,
  Chip,
  Paper,
} from "@mui/material";
import { Square } from "lucide-react";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import AppButton from "../components/AppButton";
import ProcesoDialog from "../components/ProcesoDialog";

import { useProceso } from "../hooks/useProceso";
import { useEnvasadores } from "../hooks/useEnvasadores";
import { useProductos } from "../hooks/useProductos";

function TiempoTranscurrido({ fechaInicio }) {
  const [tiempo, setTiempo] = useState("");

  useEffect(() => {
    const interval = setInterval(() => {
      const inicio = new Date(fechaInicio);
      const ahora = new Date();
      const diff = ahora - inicio;

      const horas = Math.floor(diff / 3600000);
      const minutos = Math.floor((diff % 3600000) / 60000);
      const segundos = Math.floor((diff % 60000) / 1000);

      setTiempo(
        `${horas.toString().padStart(2, "0")}:${minutos
          .toString()
          .padStart(2, "0")}:${segundos.toString().padStart(2, "0")}`
      );
    }, 1000);

    return () => clearInterval(interval);
  }, [fechaInicio]);

  return (
    <Chip
      label={tiempo}
      size="small"
      sx={{
        fontFamily: "monospace",
        bgcolor: "#fff7ed",
        color: "#c2410c",
      }}
    />
  );
}

export default function ProcesoEnvasado() {
  const { items: procesos, iniciar, finalizar } = useProceso();
  const { items: envasadores } = useEnvasadores();
  const { items: articulos } = useProductos();

  const [filters, setFilters] = useState({});
  const [dialogOpen, setDialogOpen] = useState(false);
  const [finalizarDialog, setFinalizarDialog] = useState(false);
  const [procesoSeleccionado, setProcesoSeleccionado] = useState(null);

  const [formData, setFormData] = useState({
    envasador_id: "",
    codigo_articulo: "",
    nombre_articulo: "",
  });

  const tareasEnProceso = procesos;
  const envasadoresActivos = envasadores;

  const envasadoresConTarea = tareasEnProceso.map((t) => t.envasadorId);

  const handleSubmit = async (e) => {
    e.preventDefault();

    await iniciar({
      envasadorId: formData.envasador_id,
      codigoProducto: formData.codigo_articulo,
    });

    setDialogOpen(false);

    setFormData({
      envasador_id: "",
      codigo_articulo: "",
      nombre_articulo: "",
    });
  };

  const handleFinalizar = async () => {
    await finalizar(procesoSeleccionado.id);
    setFinalizarDialog(false);
  };

  const columns = [
    { header: "Código", accessor: "codigoProducto" },

    { header: "Descripción", accessor: "nombreProducto" },

    {
      header: "Presentación",
      accessor: "minimoEnvasado",
      cell: (row) => <span>{row.minimoEnvasado} pzs</span>,
    },

    { header: "Envasador", accessor: "envasadorNombre" },

    {
      header: "Inicio",
      accessor: "horaInicio",
      cell: (row) => new Date(row.horaInicio).toLocaleTimeString(),
    },

    {
      header: "Tiempo",
      cell: (row) => <TiempoTranscurrido fechaInicio={row.horaInicio} />,
    },

    {
      header: "Acciones",
      cell: (row) => (
        <Button
          variant="contained"
          color="success"
          size="small"
          startIcon={<Square size={14} />}
          onClick={() => {
            setProcesoSeleccionado(row);
            handleFinalizar();
          }}
        >
          Finalizar
        </Button>
      ),
    },
  ];

  const filteredData = useMemo(() => {
    return tareasEnProceso.filter((p) => {
      return Object.keys(filters).every((key) => {
        const value = filters[key]?.toLowerCase() || "";
        return (p[key] || "").toString().toLowerCase().includes(value);
      });
    });
  }, [tareasEnProceso, filters]);

  return (
    <Box>
      <PageHeader title="PROCESO DE ENVASADO">
        <AppButton variant="contained" onClick={() => setDialogOpen(true)}>
          Nuevo registro
        </AppButton>
      </PageHeader>

      <DataTable
        columns={columns}
        data={filteredData}
        filters={filters}
        onFilterChange={setFilters}
        emptyMessage="No hay artículos en proceso"
      />

      <ProcesoDialog
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        onSubmit={handleSubmit}
        formData={formData}
        setFormData={setFormData}
        envasadoresActivos={envasadoresActivos}
        envasadoresConTarea={envasadoresConTarea}
        articulos={articulos}
      />

      <Dialog open={finalizarDialog} onClose={() => setFinalizarDialog(false)}>
        <DialogTitle>Finalizar Envasado</DialogTitle>

        <DialogContent>
          {procesoSeleccionado && (
            <Stack spacing={2}>
              <Paper sx={{ p: 2 }}>
                <Typography variant="body2">
                  <strong>Artículo:</strong>{" "}
                  {procesoSeleccionado.codigoProducto}
                </Typography>

                <Typography variant="body2">
                  <strong>Envasador:</strong>{" "}
                  {procesoSeleccionado.envasadorNombre}
                </Typography>

                <Typography variant="body2">
                  <strong>Inicio:</strong>{" "}
                  {new Date(procesoSeleccionado.horaInicio).toLocaleString()}
                </Typography>
              </Paper>
            </Stack>
          )}
        </DialogContent>

        <DialogActions>
          <Button onClick={() => setFinalizarDialog(false)}>Cancelar</Button>
          <Button variant="contained" color="error">
            Finalizar
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
