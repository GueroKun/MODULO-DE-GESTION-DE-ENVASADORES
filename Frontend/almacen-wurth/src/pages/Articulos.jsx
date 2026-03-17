import React, { useMemo, useState } from "react";
import {
  Box,
  Chip,
  Typography,
} from "@mui/material";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import ProductoDialog from "../components/ProductoDialog";
import { useProductos } from "../hooks/useProductos";
import AppButton from "../components/AppButton";

export default function Productos() {

  const { items: productos, create } = useProductos();

  const [search, setSearch] = useState("");
  const [prioridadFilter, setPrioridadFilter] = useState("todas");
  const [dialogOpen, setDialogOpen] = useState(false);

  const [filters, setFilters] = useState({});

  // PAGINACIÓN
  const [page, setPage] = useState(1);
  const rowsPerPage = 5;

  const initialForm = {
    codigo: "",
    nombre: "",
    totalUnidades: 0,
    stockActual: 0,
    minimoEnvasado: 10,
    ubicacionArticulo: "M01-001-1",
    estado: "PENDIENTE",
    prioridad: "MEDIA",
  };

  const [form, setForm] = useState(initialForm);

  const openNew = () => {
    setForm(initialForm);
    setDialogOpen(true);
  };

  const closeDialog = () => setDialogOpen(false);

  const handleSubmit = async () => {
    try {

      await create({
        ...form,
        codigo: form.codigo.trim(),
        nombre: form.nombre.trim(),
        ubicacionArticulo: form.ubicacionArticulo.trim(),
      });

      closeDialog();

    } catch (err) {
      console.error(err);
      alert(err.message || "No se pudo crear el producto");
    }
  };

  const filteredData = useMemo(() => {

    const s = search.toLowerCase();

    return productos
      .filter((p) => {

        const matchSearch =
          (p.codigo || "").toLowerCase().includes(s) ||
          (p.nombre || "").toLowerCase().includes(s);

        const matchPrioridad =
          prioridadFilter === "todas" || p.prioridad === prioridadFilter;

        return matchSearch && matchPrioridad;

      })
      .filter((p) => {

        return Object.keys(filters).every((key) => {
          const value = filters[key]?.toLowerCase() || "";
          return (p[key] || "").toString().toLowerCase().includes(value);
        });

      });

  }, [productos, search, prioridadFilter, filters]);

  // TOTAL DE PÁGINAS
  const totalPages = Math.ceil(filteredData.length / rowsPerPage);

  // DATOS DE LA PÁGINA ACTUAL
  const paginatedData = useMemo(() => {

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    return filteredData.slice(start, end);

  }, [filteredData, page]);

  const prioridadChip = (nivel = "MEDIA") => {

    const styles = {
      ALTA: { bgcolor: "#fee2e2", color: "#b91c1c" },
      MEDIA: { bgcolor: "#fef3c7", color: "#b45309" },
      BAJA: { bgcolor: "#dcfce7", color: "#15803d" },
    };

    return (
      <Chip
        label={nivel}
        size="small"
        sx={{ fontWeight: 700, ...(styles[nivel] || styles.MEDIA) }}
      />
    );
  };

  const estadoChip = (estado = "PENDIENTE") => {

    const styles = {
      PENDIENTE: { bgcolor: "#e2e8f0", color: "#334155" },
      EN_PROCESO: { bgcolor: "#dbeafe", color: "#1d4ed8" },
      FINALIZADO: { bgcolor: "#dcfce7", color: "#15803d" },
    };

    return (
      <Chip
        label={estado}
        size="small"
        sx={{ fontWeight: 700, ...(styles[estado] || styles.PENDIENTE) }}
      />
    );
  };

  const columns = [

    { header: "Código", accessor: "codigo" },
    {
      header: "Descripción",
      accessor: "nombre",
      cell: (row) => (
        <Box>
          <Typography fontWeight={600} fontSize={13}>
            {row.nombre}
          </Typography>

          <Typography variant="caption" color="text.secondary">
            Ubicación: {row.ubicacionArticulo}
          </Typography>
        </Box>
      ),
    },

    {
      header: "Venta",
      cell: (row) => (
        <Typography>
          {row.totalUnidades ?? 0}
        </Typography>
      ),
    },

    {
      header: "Stock",
      cell: (row) => (
        <Typography>
          {row.stockActual ?? 0}
        </Typography>
      ),
    },

    {
      header: "Mín. Envasado",
      accessor: "minimoEnvasado",
      cell: (row) => (
        <Typography>
          {row.minimoEnvasado ?? 0}
        </Typography>
      ),
    },

    {
      header: "Prioridad",
      accessor: "prioridad",
      cell: (row) => prioridadChip(row.prioridad),
    },

    {
      header: "Estado",
      accessor: "estado",
      cell: (row) => estadoChip(row.estado),
    },

  ];

  return (
    <Box>

      <PageHeader title="GESTIÓN DE ARTICULOS">
        <AppButton
          variant="contained"
          onClick={openNew}
        >
          Nuevo producto
        </AppButton>
      </PageHeader>

      <DataTable
        columns={columns}
        data={paginatedData}
        filters={filters}
        onFilterChange={setFilters}
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        emptyMessage="No hay productos registrados"
      />

      <ProductoDialog
        open={dialogOpen}
        onClose={closeDialog}
        onSubmit={handleSubmit}
        formData={form}
        setFormData={setForm}
      />

    </Box>
  );
}