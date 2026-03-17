import React, { useMemo, useState } from "react";
import {
  Box,
  Button,
  Tooltip,
} from "@mui/material";
import { Pencil, Trash2, ClipboardList } from "lucide-react";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import MuiDialog from "../components/Dialog";
import ProcesoDialog from "../components/ProcesoDialog";
import AppButton from "../components/AppButton";

import { useEnvasadores } from "../hooks/useEnvasadores";
import { useProceso } from "../hooks/useProceso";
import { useProductos } from "../hooks/useProductos";

export default function Empacadores() {

  const { items: envasadores, create, update, remove } = useEnvasadores();
  const { items: procesos, iniciar } = useProceso();
  const { items: articulos } = useProductos();

  // 🔥 FILTROS (igual que productos)
  const [search, setSearch] = useState("");
  const [filters, setFilters] = useState({});

  // DIALOGS
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState(null);

  const [form, setForm] = useState({
    id: null,
    nombre: "",
  });

  const [procesoDialogOpen, setProcesoDialogOpen] = useState(false);

  const [formProceso, setFormProceso] = useState({
    envasador_id: "",
    codigo_articulo: "",
    nombre_articulo: "",
  });

  // ==============================
  // CRUD ENVASADORES
  // ==============================

  const openNew = () => {
    setEditing(null);
    setForm({ id: null, nombre: "" });
    setDialogOpen(true);
  };

  const openEdit = (row) => {
    setEditing(row);
    setForm({ id: row.id, nombre: row.nombre ?? "" });
    setDialogOpen(true);
  };

  const closeDialog = () => setDialogOpen(false);

  const handleSubmit = async () => {
    try {

      if (!form.nombre?.trim()) {
        alert("El nombre es obligatorio");
        return;
      }

      if (editing) {
        await update(editing.id, {
          id: editing.id,
          nombre: form.nombre,
        });
      } else {
        await create({
          nombre: form.nombre,
        });
      }

      closeDialog();

    } catch (err) {
      console.error(err);
      alert(err.message || "Ocurrió un error");
    }
  };

  // ==============================
  // PROCESOS
  // ==============================

  const abrirAsignarTarea = (row) => {
    setFormProceso({
      envasador_id: row.id,
      codigo_articulo: "",
      nombre_articulo: "",
    });
    setProcesoDialogOpen(true);
  };

  const handleAsignarTarea = async (e) => {
    e.preventDefault();

    await iniciar({
      envasadorId: formProceso.envasador_id,
      codigoProducto: formProceso.codigo_articulo,
    });

    setProcesoDialogOpen(false);
  };

  // ==============================
  // FILTROS (IGUAL QUE PRODUCTOS 🔥)
  // ==============================

  const filteredData = useMemo(() => {

    const s = search.toLowerCase();

    return envasadores
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

  }, [envasadores, search, filters]);

  // ==============================
  // COLUMNAS
  // ==============================

  const columns = [

    { header: "Nombre", accessor: "nombre" },

    {
      header: "Acciones",
      cell: (row) => {

        const tieneTarea = procesos?.some(
          (p) => p.envasadorId === row.id
        );

        return (
          <Box sx={{ display: "flex", gap: 1 }}>

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
                onClick={async () => {
                  if (window.confirm("¿Seguro que deseas eliminar este envasador?")) {
                    await remove(row.id);
                  }
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

            <Tooltip title={tieneTarea ? "Este envasador ya está trabajando" : "Asignar tarea"}>
              <span>
                <Button
                  size="small"
                  variant="outlined"
                  disabled={tieneTarea}
                  onClick={() => abrirAsignarTarea(row)}
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

          </Box>
        );
      },
    },
  ];

  // ==============================
  // RENDER
  // ==============================

  return (
    <Box>

      <PageHeader title="GESTIÓN DE ENVASADORES">
        <AppButton variant="contained" onClick={openNew}>
          Nuevo registro
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
        emptyMessage="No hay envasadores registrados"
      />

      <MuiDialog
        open={dialogOpen}
        onClose={closeDialog}
        onSubmit={handleSubmit}
        editing={!!editing}
        formData={form}
        setFormData={setForm}
      />

      <ProcesoDialog
        open={procesoDialogOpen}
        onClose={() => setProcesoDialogOpen(false)}
        onSubmit={handleAsignarTarea}
        formData={formProceso}
        setFormData={setFormProceso}
        envasadoresActivos={envasadores}
        envasadoresConTarea={[]}
        articulos={articulos}
      />

    </Box>
  );
}