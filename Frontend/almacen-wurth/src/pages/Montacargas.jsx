import React, { useMemo, useState } from "react";
import {
  Box,
  Button,
  Tooltip,
  Chip,
} from "@mui/material";
import { Pencil, Trash2 } from "lucide-react";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import MontacargasDialog from "../components/MontacargasDialog";
import AppButton from "../components/AppButton";
import ConfirmDialog from "../components/ConfirmDialog";

import { useUsuarios } from "../hooks/useUsuarios";
import { useAlert } from "../components/AlertProvider";

export default function Montacargas() {
  const { items, create, update, remove } = useUsuarios();
  const { showAlert } = useAlert();

  const [search, setSearch] = useState("");
  const [filters, setFilters] = useState({});

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState(null);

  const [form, setForm] = useState({
    id: null,
    nombre: "",
    password: "",
    rol: "MONTACARGAS",
  });

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedId, setSelectedId] = useState(null);

  // =========================
  // FILTRAR SOLO MONTACARGAS
  // =========================
  const montacargas = items.filter((u) => u.rol === "MONTACARGAS");

  const filteredData = useMemo(() => {
    const s = search.toLowerCase();

    return montacargas
      .filter((e) => {
        const matchSearch =
          (e.nombre || "").toLowerCase().includes(s) ||
          String(e.id ?? "").toLowerCase().includes(s);

        return matchSearch;
      })
      .filter((e) => {
        return Object.keys(filters).every((key) => {
          const value = filters[key]?.toLowerCase() || "";
          return (e[key] || "").toString().toLowerCase().includes(value);
        });
      });
  }, [montacargas, search, filters]);

  // =========================
  // CRUD
  // =========================
  const openNew = () => {
    setEditing(null);
    setForm({ id: null, nombre: "", password: "", rol: "MONTACARGAS" });
    setDialogOpen(true);
  };

  const openEdit = (row) => {
    setEditing(row);
    setForm({
      id: row.id,
      nombre: row.nombre,
      password: "",
      rol: "MONTACARGAS",
    });
    setDialogOpen(true);
  };

  const closeDialog = () => setDialogOpen(false);

  const handleSubmit = async () => {
    try {
      if (!form.nombre?.trim()) {
        showAlert("El nombre es obligatorio", "warning");
        return;
      }

      if (!editing && !form.password) {
        showAlert("La contraseña es obligatoria", "warning");
        return;
      }

      if (editing) {
        await update(form.id, {
          nombre: form.nombre,
          password: form.password,
          rol: form.rol,
        });
        showAlert("Operador actualizado correctamente", "success");
      } else {
        await create({
          nombre: form.nombre,
          password: form.password,
          rol: form.rol,
        });
        showAlert("Operador creado correctamente", "success");
      }

      closeDialog();

    } catch (err) {
      console.error(err);
      showAlert(err.message || "Ocurrió un error", "error");
    }
  };

  const handleDelete = async () => {
    try {
      await remove(selectedId);
      showAlert("Operador eliminado correctamente", "success");
    } catch (err) {
      showAlert("Error al eliminar", "error");
    } finally {
      setConfirmOpen(false);
      setSelectedId(null);
    }
  };

  // =========================
  // COLUMNAS
  // =========================
  const columns = [
    { header: "Usuario", accessor: "nombre" },
    {
      header: "Rol",
      cell: () => <Chip label="Montacargas" size="small" color="primary" />,
    },
    {
      header: "Acciones",
      cell: (row) => (
        <Box sx={{ display: "flex", gap: 1 }}>
          {/* EDITAR */}
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

          {/* ELIMINAR */}
          <Tooltip title="Eliminar">
            <Button
              size="small"
              variant="outlined"
              onClick={() => {
                setSelectedId(row.id);
                setConfirmOpen(true);
              }}
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
      ),
    },
  ];

  // =========================
  // RENDER
  // =========================
  return (
    <Box>
      <PageHeader title="GESTIÓN DE OPERADORES DE MONTACARGAS">
        <AppButton variant="contained" onClick={openNew}>
          Nuevo operador
        </AppButton>
      </PageHeader>

      <DataTable
        columns={columns}
        data={filteredData}
        filters={{ search, ...filters }}
        onFilterChange={(f) => {
          setSearch(f.search || "");
          setFilters(f);
        }}
        emptyMessage="No hay operadores registrados"
      />

      <MontacargasDialog
        open={dialogOpen}
        onClose={closeDialog}
        onSubmit={handleSubmit}
        formData={form}
        setFormData={setForm}
      />

      <ConfirmDialog
        open={confirmOpen}
        title="Eliminar operador"
        message="¿Seguro que deseas eliminar este operador?"
        onConfirm={handleDelete}
        onClose={() => setConfirmOpen(false)}
      />
    </Box>
  );
}