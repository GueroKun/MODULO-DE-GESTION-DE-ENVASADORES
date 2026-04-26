import React, { useMemo, useState } from "react";
import {
  Box,
  Button,
  Chip,
  Tooltip,
  Typography,
  Tabs,
  Tab,
} from "@mui/material";
import {
  Pencil,
  Trash2,
  History,
  Plus,
  Minus,
} from "lucide-react";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import AppButton from "../components/AppButton";
import ConfirmDialog from "../components/ConfirmDialog";
import CajaDialog from "../components/CajaDialog";
import MovimientoCajaDialog from "../components/MovimientoCajaDialog";
import HistorialCajaDialog from "../components/HistorialCajaDialog";
import { useAlert } from "../components/AlertProvider";
import { useCajas } from "../hooks/useCajas";

export default function Cajas() {
  const {
    cajas,
    stock,
    entradas,
    salidas,
    createCaja,
    updateCaja,
    deleteCaja,
    registrarEntrada,
    registrarSalida,
    historialPorCaja,
  } = useCajas();

  const { showAlert } = useAlert();
  const usuario = localStorage.getItem("usuario") || "";

  const [tab, setTab] = useState(0);
  const [filters, setFilters] = useState({});
  const [page, setPage] = useState(1);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const [dialogCajaOpen, setDialogCajaOpen] = useState(false);
  const [editingCaja, setEditingCaja] = useState(null);

  const [movDialogOpen, setMovDialogOpen] = useState(false);
  const [movType, setMovType] = useState("ENTRADA");

  const [historialOpen, setHistorialOpen] = useState(false);
  const [historialData, setHistorialData] = useState([]);
  const [historialCajaCodigo, setHistorialCajaCodigo] = useState("");

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedCaja, setSelectedCaja] = useState(null);

  const [cajaForm, setCajaForm] = useState({
    codigo: "",
    activo: true,
  });

  const [movForm, setMovForm] = useState({
    cajaId: "",
    cantidad: "",
    ubicacion: "",
    usuario,
    observaciones: "",
  });

  const openNewCaja = () => {
    setEditingCaja(null);
    setCajaForm({ codigo: "", activo: true });
    setDialogCajaOpen(true);
  };

  const openEditCaja = (caja) => {
    setEditingCaja(caja);
    setCajaForm({
      codigo: caja.codigo || "",
      activo: !!caja.activo,
    });
    setDialogCajaOpen(true);
  };

  const handleSaveCaja = async () => {
    try {
      const payload = {
        codigo: cajaForm.codigo.trim(),
        activo: !!cajaForm.activo,
      };

      if (!payload.codigo) {
        showAlert("El código es obligatorio", "warning");
        return;
      }

      if (editingCaja) {
        await updateCaja(editingCaja.id, payload);
        showAlert("Caja actualizada correctamente", "success");
      } else {
        await createCaja(payload);
        showAlert("Caja creada correctamente", "success");
      }

      setDialogCajaOpen(false);
    } catch (err) {
      showAlert(err.message || "No se pudo guardar la caja", "error");
    }
  };

  const openEntradaGlobal = () => {
    setMovType("ENTRADA");
    setMovForm({
      cajaId: "",
      cantidad: "",
      ubicacion: "",
      usuario,
      observaciones: "",
    });
    setMovDialogOpen(true);
  };

  const openSalida = (row) => {
    setMovType("SALIDA");
    setMovForm({
      cajaId: row.cajaId,
      cantidad: "",
      ubicacion: row.ubicacion || "",
      usuario,
      observaciones: "",
    });
    setMovDialogOpen(true);
  };

  const openEntradaPorFila = (row) => {
    setMovType("ENTRADA");
    setMovForm({
      cajaId: row.cajaId,
      cantidad: "",
      ubicacion: row.ubicacion || "",
      usuario,
      observaciones: "",
    });
    setMovDialogOpen(true);
  };

  const handleSaveMovimiento = async () => {
    try {
      const payload = {
        cajaId: Number(movForm.cajaId),
        cantidad: Number(movForm.cantidad),
        ubicacion: movForm.ubicacion.trim(),
        usuarioNombre: movForm.usuario.trim(),
        observaciones: movForm.observaciones.trim(),
      };

      if (!payload.cajaId) {
        showAlert("Selecciona una caja", "warning");
        return;
      }

      if (!payload.cantidad || payload.cantidad <= 0) {
        showAlert("La cantidad debe ser mayor que 0", "warning");
        return;
      }

      if (!payload.ubicacion) {
        showAlert("La ubicación es obligatoria", "warning");
        return;
      }

      if (movType === "ENTRADA") {
        await registrarEntrada(payload);
        showAlert("Entrada registrada correctamente", "success");
      } else {
        await registrarSalida(payload);
        showAlert("Salida registrada correctamente", "success");
      }

      setMovDialogOpen(false);
    } catch (err) {
      showAlert(err.message || "No se pudo registrar el movimiento", "error");
    }
  };

  const handleDeleteCaja = (caja) => {
    setSelectedCaja(caja);
    setConfirmOpen(true);
  };

  const confirmDeleteCaja = async () => {
    try {
      await deleteCaja(selectedCaja.id);
      showAlert("Caja eliminada correctamente", "success");
      setConfirmOpen(false);
      setSelectedCaja(null);
    } catch (err) {
      showAlert(err.message || "No se pudo eliminar la caja", "error");
    }
  };

  const openHistorial = async (row) => {
    try {
      const data = await historialPorCaja(row.cajaId || row.id);
      setHistorialData(data || []);
      setHistorialCajaCodigo(row.codigoCaja || row.codigo || "");
      setHistorialOpen(true);
    } catch (err) {
      showAlert("No se pudo cargar el historial", "error");
    }
  };

  const estadoChip = (activo) => (
    <Chip
      label={activo ? "activo" : "Inactivo"}
      size="small"
      sx={{
        fontWeight: 700,
        bgcolor: activo ? "#dcfce7" : "#fee2e2",
        color: activo ? "#15803d" : "#b91c1c",
      }}
    />
  );

  const currentData = useMemo(() => {
    if (tab === 0) return stock;
    if (tab === 1) return entradas;
    if (tab === 2) return salidas;
    return cajas;
  }, [tab, stock, entradas, salidas, cajas]);

  const filteredData = useMemo(() => {
    return currentData.filter((row) =>
      Object.keys(filters).every((key) => {
        const value = filters[key]?.toLowerCase() || "";
        return (row[key] ?? "").toString().toLowerCase().includes(value);
      })
    );
  }, [currentData, filters]);

  const totalPages = Math.ceil(filteredData.length / rowsPerPage) || 1;

  const paginatedData = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    return filteredData.slice(start, start + rowsPerPage);
  }, [filteredData, page, rowsPerPage]);

  const cajasById = useMemo(() => {
  return new Map(cajas.map((caja) => [caja.id, caja]));
}, [cajas]);

const getCajaActiva = (row) => {
  const caja = cajasById.get(row.cajaId);
  return caja ? caja.activo : false;
};

  const stockColumns = [
    { header: "Código", accessor: "codigoCaja" },
    { header: "Ubicación", accessor: "ubicacion" },
    { header: "Stock", accessor: "stockActual" },
    {
      header: "Estado",
      accessor: "activo",
      cell: (row) => estadoChip(getCajaActiva(row)),
    },
    {
      header: "Acciones",
      cell: (row) => (
        <Box sx={{ display: "flex", gap: 1 }}>
          <Tooltip title="Registrar entrada">
            <Button
              size="small"
              variant="outlined"
              onClick={() => openEntradaPorFila(row)}
              sx={{
                border: "1px solid #86efac",
                color: "#15803d",
                minWidth: 0,
                height: "2rem",
                "&:hover": { bgcolor: "#f0fdf4", color: "black" },
              }}
            >
              <Plus size={16} />
            </Button>
          </Tooltip>

          <Tooltip title="Registrar salida">
            <Button
              size="small"
              variant="outlined"
              onClick={() => openSalida(row)}
              sx={{
                border: "1px solid #fecaca",
                color: "#dc2626",
                minWidth: 0,
                height: "2rem",
                "&:hover": { bgcolor: "#fef2f2", color: "black" },
              }}
            >
              <Minus size={16} />
            </Button>
          </Tooltip>

          <Tooltip title="Ver historial">
            <Button
              size="small"
              variant="outlined"
              onClick={() => openHistorial(row)}
              sx={{
                border: "1px solid #93c5fd",
                color: "#2563eb",
                minWidth: 0,
                height: "2rem",
                "&:hover": { bgcolor: "#eff6ff", color: "black" },
              }}
            >
              <History size={16} />
            </Button>
          </Tooltip>
        </Box>
      ),
    },
  ];

  const movimientosColumns = [
    { header: "Código", accessor: "codigoCaja" },
    { header: "Cantidad", accessor: "cantidad" },
    { header: "Ubicación", accessor: "ubicacion" },
    { header: "Usuario", accessor: "usuarioNombre" },
    { header: "Observaciones", accessor: "observaciones" },
    { header: "Fecha", accessor: "fechaMovimiento" },
  ];

  

  const cajasColumns = [
    { header: "Código", accessor: "codigo" },
    {
      header: "Estado",
      accessor: "activo",
      cell: (row) => estadoChip(row.activo),
    },
    {
      header: "Acciones",
      cell: (row) => (
        <Box sx={{ display: "flex", gap: 1 }}>
          <Tooltip title="Editar">
            <Button
              variant="outlined"
              size="small"
              onClick={() => openEditCaja(row)}
              sx={{
                border: "1px solid #fde68a",
                color: "#d97706",
                minWidth: 0,
                height: "2rem",
                "&:hover": { bgcolor: "#fffbeb", color: "black" },
              }}
            >
              <Pencil size={16} />
            </Button>
          </Tooltip>

          <Tooltip title="Eliminar">
            <Button
              size="small"
              variant="outlined"
              onClick={() => handleDeleteCaja(row)}
              sx={{
                border: "1px solid #fecaca",
                color: "#dc2626",
                minWidth: 0,
                height: "2rem",
                "&:hover": { bgcolor: "#fef2f2", color: "black" },
              }}
            >
              <Trash2 size={16} />
            </Button>
          </Tooltip>
        </Box>
      ),
    },
  ];

  const columns =
    tab === 0 ? stockColumns : tab === 1 ? movimientosColumns : tab === 2 ? movimientosColumns : cajasColumns;

  return (
    <Box>
      <PageHeader title="GESTOR DE CAJAS">
        {tab === 0 && (
          <AppButton variant="contained" onClick={openEntradaGlobal}>
            Registrar entrada
          </AppButton>
        )}

        {tab === 3 && (
          <AppButton variant="contained" onClick={openNewCaja}>
            Nueva caja
          </AppButton>
        )}
      </PageHeader>

      <Box
        sx={{
          mb: 2,
          bgcolor: "#fff",
          borderRadius: 3,
          border: "1px solid #e2e8f0",
          overflow: "hidden",
        }}
      >
        <Tabs
          value={tab}
          onChange={(_, newValue) => {
            setTab(newValue);
            setFilters({});
            setPage(1);
          }}
          variant="scrollable"
          scrollButtons="auto"
        >
          <Tab label="Stock" />
          <Tab label="Entradas" />
          <Tab label="Salidas" />
          <Tab label="Catálogo" />
        </Tabs>
      </Box>

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
        emptyMessage="No hay datos registrados"
      />

      <CajaDialog
        open={dialogCajaOpen}
        onClose={() => setDialogCajaOpen(false)}
        onSubmit={handleSaveCaja}
        formData={cajaForm}
        setFormData={setCajaForm}
        editingCaja={editingCaja}
      />

      <MovimientoCajaDialog
        open={movDialogOpen}
        onClose={() => setMovDialogOpen(false)}
        onSubmit={handleSaveMovimiento}
        title={movType === "ENTRADA" ? "Registrar entrada" : "Registrar salida"}
        cajas={cajas}
        formData={movForm}
        setFormData={setMovForm}
        lockCaja={!!movForm.cajaId}
        lockUbicacion={movType === "SALIDA" && !!movForm.ubicacion}
      />

      <HistorialCajaDialog
        open={historialOpen}
        onClose={() => setHistorialOpen(false)}
        historial={historialData}
        cajaCodigo={historialCajaCodigo}
      />

      <ConfirmDialog
        open={confirmOpen}
        onClose={() => setConfirmOpen(false)}
        onConfirm={confirmDeleteCaja}
        title="Confirmar eliminación"
        message={`¿Estás seguro de eliminar la caja "${selectedCaja?.codigo}"?`}
        confirmText="Eliminar"
      />
    </Box>
  );
}