import React, { useEffect, useMemo, useRef, useState } from "react";
import {
  Box,
  Typography,
  CircularProgress,
  Button,
  Chip
} from "@mui/material";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import UploadFileIcon from "@mui/icons-material/UploadFile";

import { useAnalisis } from "../hooks/useAnalisis";

export default function AnalisisABC() {

  const fileRef = useRef();

  const {
    upload,
    getEmbarques,
    getDetalles,
    embarques,
    detalles,
    loading
  } = useAnalisis();

  const [selectedEmbarque, setSelectedEmbarque] = useState(null);

  // 🔥 FILTROS
  const [searchEmbarques, setSearchEmbarques] = useState("");
  const [filtersEmbarques, setFiltersEmbarques] = useState({});

  const [searchDetalles, setSearchDetalles] = useState("");
  const [filtersDetalles, setFiltersDetalles] = useState({});

  // 🚀 Cargar embarques
  useEffect(() => {
    getEmbarques();
  }, []);

  // 📤 SUBIR
  const handleFile = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      await upload(file);
    } catch (err) {
      console.error(err);
      alert("Error al subir archivo");
    }
  };

  // 👁️ VER DETALLE
  const handleVerDetalle = async (row) => {
    setSelectedEmbarque(row);
    await getDetalles(row.id);
  };

  // ==============================
  // FILTROS EMBARQUES
  // ==============================

  const filteredEmbarques = useMemo(() => {

    const s = searchEmbarques.toLowerCase();

    return embarques
      .filter((e) => {

        const matchSearch =
          (e.nombreArchivoOriginal || "").toLowerCase().includes(s) ||
          (e.estatus || "").toLowerCase().includes(s);

        return matchSearch;

      })
      .filter((e) => {

        return Object.keys(filtersEmbarques).every((key) => {
          const value = filtersEmbarques[key]?.toLowerCase() || "";
          return (e[key] || "").toString().toLowerCase().includes(value);
        });

      });

  }, [embarques, searchEmbarques, filtersEmbarques]);

  // ==============================
  // FILTROS DETALLE
  // ==============================

  const filteredDetalles = useMemo(() => {

    const s = searchDetalles.toLowerCase();

    return detalles
      .filter((d) => {

        const matchSearch =
          (d.codigo || "").toLowerCase().includes(s) ||
          (d.descripcion || "").toLowerCase().includes(s) ||
          (d.abc || "").toLowerCase().includes(s);

        return matchSearch;

      })
      .filter((d) => {

        return Object.keys(filtersDetalles).every((key) => {
          const value = filtersDetalles[key]?.toLowerCase() || "";
          return (d[key] || "").toString().toLowerCase().includes(value);
        });

      });

  }, [detalles, searchDetalles, filtersDetalles]);

  // ==============================
  // CHIP ABC
  // ==============================

  const abcChip = (abc) => {
    const styles = {
      A: { bgcolor: "#dcfce7", color: "#15803d" },
      B: { bgcolor: "#fef3c7", color: "#b45309" },
      C: { bgcolor: "#fee2e2", color: "#b91c1c" },
    };

    return (
      <Chip
        label={abc}
        size="small"
        sx={{ fontWeight: 700, ...(styles[abc] || {}) }}
      />
    );
  };

  // ==============================
  // COLUMNAS
  // ==============================

  const embarqueColumns = [
    { header: "Archivo", accessor: "nombreArchivoOriginal" },
    { header: "Filas", accessor: "totalFilas" },
    { header: "Columnas", accessor: "totalColumnas" },
    { header: "Estatus", accessor: "estatus" },
    {
      header: "Acciones",
      cell: (row) => (
        <Button
          variant="contained"
          size="small"
          onClick={() => handleVerDetalle(row)}
        >
          Ver detalle
        </Button>
      ),
    },
  ];

  const detalleColumns = [
    { header: "Código", accessor: "codigo" },
    { header: "Descripción", accessor: "descripcion" },
    { header: "Cantidad", accessor: "cantidad" },
    {
      header: "ABC",
      accessor: "abc",
      cell: (row) => abcChip(row.abc),
    },
  ];

  // ==============================
  // RENDER
  // ==============================

  return (
    <Box>

      <PageHeader title="ANÁLISIS ABC" />

      {/* SUBIR */}
      <Box
        onClick={() => fileRef.current.click()}
        sx={{
          backgroundColor: "white",
          borderRadius: 3,
          border: "2px dashed #cbd5f5",
          p: 6,
          mb: 4,
          textAlign: "center",
          cursor: "pointer",
          transition: "0.3s",
          "&:hover": {
            borderColor: "#b91c1c"
          }
        }}
      >

        <input
          ref={fileRef}
          type="file"
          accept=".xlsx,.xls"
          hidden
          onChange={handleFile}
        />

        {loading ? (
          <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 2 }}>
            <CircularProgress size={30} />
            <Typography>Procesando archivo...</Typography>
          </Box>
        ) : (
          <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 2 }}>
            <UploadFileIcon sx={{ fontSize: 50, color: "#f87171" }} />
            <Typography sx={{ fontSize: 18, fontWeight: 500 }}>
              Haz clic para cargar un archivo
            </Typography>
            <Typography fontSize={14}>
              Formatos aceptados: .xlsx, .xls
            </Typography>
          </Box>
        )}

      </Box>

      {/* EMBARQUES */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h6" mb={2}>
          Archivos subidos
        </Typography>

        <DataTable
          columns={embarqueColumns}
          data={filteredEmbarques}
          filters={{ search: searchEmbarques, ...filtersEmbarques }}
          onFilterChange={(f) => {
            setSearchEmbarques(f.search || "");
            setFiltersEmbarques(f);
          }}
          emptyMessage="No hay archivos"
        />
      </Box>

      {/* DETALLE */}
      {selectedEmbarque && (
        <Box>
          <Typography variant="h6" mb={2}>
            Detalle de: {selectedEmbarque.nombreArchivoOriginal}
          </Typography>

          <DataTable
            columns={detalleColumns}
            data={filteredDetalles}
            filters={{ search: searchDetalles, ...filtersDetalles }}
            onFilterChange={(f) => {
              setSearchDetalles(f.search || "");
              setFiltersDetalles(f);
            }}
            emptyMessage="No hay detalles"
          />
        </Box>
      )}

    </Box>
  );
}