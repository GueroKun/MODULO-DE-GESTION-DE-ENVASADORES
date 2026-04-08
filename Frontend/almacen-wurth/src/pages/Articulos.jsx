import React, { useMemo, useState } from "react";
import {
  Box,
  Chip,
  Typography,
  Button,
  Tooltip,
} from "@mui/material";

import { useNavigate } from "react-router-dom";

import { Pencil, Trash2, ClipboardList } from "lucide-react";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import ProductoDialog from "../components/ProductoDialog";
import ImportarArticulosDialog from "../components/ImportarArticulosDialog";
import ConfirmDialog from "../components/ConfirmDialog";
import { useAlert } from "../components/AlertProvider";

import { useProductos } from "../hooks/useProductos";
import AppButton from "../components/AppButton";
import UploadExcelButton from "../components/UploadExcelButton";

export default function Productos() {
  const { items: productos, create, update, remove, reload } = useProductos();
  const navigate = useNavigate();
  const rol = localStorage.getItem("rol");
  const { showAlert } = useAlert();
  const [search, setSearch] = useState("");
  const [prioridadFilter, setPrioridadFilter] = useState("todas");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [excelOpen, setExcelOpen] = useState(false);
  const [editingProducto, setEditingProducto] = useState(null);

  // 🔴 Confirmación eliminar
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedProducto, setSelectedProducto] = useState(null);

  const [filters, setFilters] = useState({});

  // PAGINACIÓN
  const [page, setPage] = useState(1);
  const [rowsPerPage, setRowsPerPage] = useState(10);
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
  const totalPages = Math.ceil(filteredData.length / rowsPerPage);

  const paginatedData = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    return filteredData.slice(start, end);
  }, [filteredData, page, rowsPerPage]);

  const initialForm = {
    codigo: "",
    nombre: "",
    totalUnidades: 0,
    stockActual: 0,
    minimoEnvasado: 0,
    ubicacionArticulo: "",
    estado: "PENDIENTE",
    prioridad: "MEDIA",
  };

  const [form, setForm] = useState(initialForm);

  const openNew = () => {
    setEditingProducto(null);
    setForm(initialForm);
    setDialogOpen(true);
  };

  const openEdit = (producto) => {
    setEditingProducto(producto);
    setForm(producto);
    setDialogOpen(true);
  };

  const closeDialog = () => setDialogOpen(false);

  const handleSubmit = async () => {
    try {
      if (editingProducto) {
        await update(editingProducto.id, form);
        showAlert("Artículo actualizado correctamente", "success");
      } else {
        await create({
          ...form,
          codigo: form.codigo.trim(),
          nombre: form.nombre.trim(),
          ubicacionArticulo: form.ubicacionArticulo.trim(),
        });
        showAlert("Artículo creado correctamente", "success");
      }

      closeDialog();
    } catch (err) {
      showAlert(err.message || "No se pudo guardar", "error");
    }
  };
  // 🔴 ABRIR CONFIRMACIÓN
  const handleDelete = (producto) => {
    setSelectedProducto(producto);
    setConfirmOpen(true);
  };

  // 🔴 CONFIRMAR ELIMINACIÓN
  const confirmDelete = async () => {
    try {
      await remove(selectedProducto.id);
      showAlert("Artículo eliminado correctamente", "success");
      setConfirmOpen(false);
      setSelectedProducto(null);
    } catch (err) {
      showAlert("No se pudo eliminar", "error");
    }
  };

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
      header: "Cantidad",
      cell: (row) => <Typography>{row.totalUnidades ?? 0}</Typography>,
    },
    {
      header: "Stock",
      cell: (row) => <Typography>{row.stockActual ?? 0}</Typography>,
    },
    {
      header: "Mín. Envasado",
      accessor: "minimoEnvasado",
      cell: (row) => <Typography>{row.minimoEnvasado ?? 0}</Typography>,
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
    {
      header: "Acciones",
      cell: (row) => {
        return (
          <Box sx={{ display: "flex", gap: 1 }}>
            <Tooltip title={row.estado === "FINALIZADO"
              ? "Artículo finalizado"
              : row.estado === "EN_PROCESO"
                ? "Artículo en proceso"
                : "Asignar tarea"
            }>
              <span>
                <Button
                  size="small"
                  variant="outlined"
                  disabled={
                    row.estado === "FINALIZADO" ||
                    row.estado === "EN_PROCESO"
                  }
                  onClick={() =>
                    navigate("/proceso-envasado", {
                      state: {
                        codigo: row.codigo,
                        nombre: row.nombre,
                      },
                    })
                  }
                  sx={{
                    border: "1px solid #93c5fd",
                    color: "#2563eb",
                    minWidth: 0,
                    height: "2rem",
                    "&:hover": {
                      bgcolor: "#eff6ff",
                      color: "black",
                    },
                  }}
                >
                  <ClipboardList size={16} />
                </Button>
              </span>
            </Tooltip>
            <Tooltip title="Editar">
              <Button
                variant="outlined"
                size="small"
                onClick={() => openEdit(row)}
                sx={{
                  border: "1px solid #fde68a",
                  color: "#d97706",
                  minWidth: 0,
                  height: "2rem",
                  "&:hover": {
                    bgcolor: "#fffbeb",
                    color: "black",
                  },
                }}
              >
                <Pencil size={16} />
              </Button>
            </Tooltip>

            <Tooltip title="Eliminar">
              <Button
                size="small"
                variant="outlined"
                onClick={() => handleDelete(row)}
                sx={{
                  border: "1px solid #fecaca",
                  color: "#dc2626",
                  minWidth: 0,
                  height: "2rem",
                  "&:hover": {
                    bgcolor: "#fef2f2",
                    color: "black",
                  },
                }}
              >
                <Trash2 size={16} />
              </Button>
            </Tooltip>
          </Box>
        );
      },
    },
  ];

  return (
    <Box>
      <PageHeader title="GESTIÓN DE ARTICULOS">
        {rol === "ADMIN" && (
          <UploadExcelButton
            variant="contained"
            onClick={() => setExcelOpen(true)}
          >
            Importar Excel
          </UploadExcelButton>
        )}

        <AppButton variant="contained" onClick={openNew}>
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
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={(value) => {
          setRowsPerPage(value);
          setPage(1);
        }}
        emptyMessage="No hay productos registrados"
      />

      <ProductoDialog
        open={dialogOpen}
        onClose={closeDialog}
        onSubmit={handleSubmit}
        formData={form}
        setFormData={setForm}
      />

      <ImportarArticulosDialog
        open={excelOpen}
        onClose={() => setExcelOpen(false)}
        onSuccess={async () => {
          await reload();
        }}
      />

      <ConfirmDialog
        open={confirmOpen}
        onClose={() => setConfirmOpen(false)}
        onConfirm={confirmDelete}
        title="Confirmar eliminación"
        message={`¿Estás seguro de que deseas eliminar el artículo "${selectedProducto?.nombre}"? Esta acción no se puede deshacer.`}
        confirmText="Eliminar"
      />
    </Box>
  );
}