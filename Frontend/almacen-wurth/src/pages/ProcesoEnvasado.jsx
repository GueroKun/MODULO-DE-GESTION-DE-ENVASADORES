import React, { useState, useEffect, useMemo } from "react";
import {
  Box,
  Button,
  Chip,
} from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";
import { Square } from "lucide-react";
import { useLocation } from "react-router-dom";

import { useAlert } from "../components/AlertProvider";

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

  const [loadingFinalizarId, setLoadingFinalizarId] = useState(null);
  const { showAlert } = useAlert();

  const location = useLocation();
  const [filters, setFilters] = useState({});
  const [dialogOpen, setDialogOpen] = useState(false);

  const [formData, setFormData] = useState({
    codigo_articulo: "",
    nombre_articulo: "",
    asignaciones: [],
  });

  const tareasEnProceso = procesos;
  const envasadoresActivos = envasadores;
  const envasadoresConTarea = tareasEnProceso.map((t) => t.envasadorId);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.asignaciones || formData.asignaciones.length === 0) {
      showAlert("Debes agregar al menos un envasador", "warning");
      return;
    }

    try {
      for (const asignacion of formData.asignaciones) {
        if (
          !asignacion.envasador_id ||
          !asignacion.cantidad ||
          !asignacion.presentacion
        ) {
          showAlert("Completa todos los campos", "warning");
          return;
        }

        await iniciar({
          envasadorId: asignacion.envasador_id,
          codigoProducto: formData.codigo_articulo,
          cantidadAsignada: parseInt(asignacion.cantidad),
          minimoEnvasado: parseInt(asignacion.presentacion),
        });
      }

      showAlert("Articulo iniciado correctamente", "success");

      setDialogOpen(false);

      setFormData({
        codigo_articulo: "",
        nombre_articulo: "",
        asignaciones: [
          { envasador_id: "", cantidad: "", presentacion: "" },
        ],
      });

    } catch (err) {
      showAlert(err.message || "Error al asignar tarea", "error");
    }
  };

  const handleFinalizar = async (row) => {
    try {
      setLoadingFinalizarId(row.id); // 🔒 bloquea este botón

      await finalizar(row.id);

      showAlert("Articulo finalizado correctamente", "success");

    } catch (error) {
      showAlert(
        error?.response?.data?.message || "Error al finalizar",
        "error"
      );
    } finally {
      setLoadingFinalizarId(null); // 🔓 desbloquea
    }
  };

  useEffect(() => {
    if (location.state?.codigo) {
      setFormData({
        codigo_articulo: location.state.codigo,
        nombre_articulo: location.state.nombre,
        asignaciones: [
          { envasador_id: "", cantidad: "", presentacion: "" },
        ],
      });

      setDialogOpen(true);
    }
  }, [location.state]);

  const columns = [
    { header: "Código", accessor: "codigoProducto" },
    { header: "Descripción", accessor: "nombreProducto" },
    {
      header: "Min.Envasado",
      accessor: "minimoEnvasado",
      cell: (row) => <span>{row.minimoEnvasado} pzs</span>,
    },
    { header: "Envasador", accessor: "envasadorNombre" },
    {
      header: "Inicio",
      cell: (row) => new Date(row.horaInicio).toLocaleTimeString(),
    },
    {
      header: "Tiempo",
      cell: (row) => (
        <TiempoTranscurrido fechaInicio={row.horaInicio} />
      ),
    },
    {
      header: "Acciones",
      cell: (row) => (
        <Button
          variant="contained"
          color="success"
          size="small"
          disabled={loadingFinalizarId === row.id}
          onClick={() => handleFinalizar(row)}
          startIcon={
            loadingFinalizarId === row.id
              ? <CircularProgress size={14} color="inherit" />
              : <Square size={14} />
          }
        >
          {loadingFinalizarId === row.id ? "Finalizando..." : "Finalizar"}
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
    </Box>
  );
}
