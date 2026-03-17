import React, { useMemo, useState } from "react";
import {
  Box,
  Typography,
} from "@mui/material";

import PageHeader from "../components/PageHeader";
import DataTable from "../components/DataTable";
import { useProcesoFinalizado } from "../hooks/useProcesoFinalizado";

export default function ArticulosEnvasados() {

  const { items, loading } = useProcesoFinalizado();

  // 🔥 filtros
  const [search, setSearch] = useState("");
  const [filters, setFilters] = useState({});

  // 🔥 paginación
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  // ==============================
  // FILTROS
  // ==============================

  const filteredData = useMemo(() => {

    const s = search.toLowerCase();

    return items
      .filter((p) => {

        const matchSearch =
          (p.codigoProducto || "").toLowerCase().includes(s) ||
          (p.nombreProducto || "").toLowerCase().includes(s) ||
          (p.envasadorNombre || "").toLowerCase().includes(s);

        return matchSearch;

      })
      .filter((p) => {

        return Object.keys(filters).every((key) => {
          const value = filters[key]?.toLowerCase() || "";
          return (p[key] || "").toString().toLowerCase().includes(value);
        });

      });

  }, [items, search, filters]);

  // ==============================
  // PAGINACIÓN (DESPUÉS DEL FILTRO)
  // ==============================

  const totalPages = Math.ceil(filteredData.length / itemsPerPage);

  const paginatedData = useMemo(() => {

    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    return filteredData.slice(start, end);

  }, [filteredData, currentPage]);

  // ==============================
  // HELPERS
  // ==============================

  const formatTiempo = (segundos) => {
    if (!segundos) return "-";

    const horas = Math.floor(segundos / 3600);
    const mins = Math.floor((segundos % 3600) / 60);

    return `${horas}h ${mins}m`;
  };

  // ==============================
  // COLUMNAS
  // ==============================

  const columns = [
    {
      header: "Código",
      accessor: "codigoProducto",
    },
    {
      header: "Descripción",
      accessor: "nombreProducto",
      cell: (row) => (
        <Box>
          <Typography fontWeight={500}>
            {row.nombreProducto}
          </Typography>
          <Typography variant="caption" color="text.secondary">
            Min: {row.minimoEnvasado} pzs
          </Typography>
        </Box>
      ),
    },
    {
      header: "Envasador",
      accessor: "envasadorNombre",
    },
    {
      header: "Inicio",
      cell: (row) =>
        new Date(row.horaInicio).toLocaleString(),
    },
    {
      header: "Fin",
      cell: (row) =>
        new Date(row.horaFin).toLocaleString(),
    },
    {
      header: "Tiempo",
      cell: (row) => (
        <Box
          sx={{
            bgcolor: "grey.100",
            px: 1,
            py: 0.5,
            borderRadius: 1,
            fontSize: 13,
          }}
        >
          {formatTiempo(row.tiempoTranscurridoSegundos)}
        </Box>
      ),
    },
  ];

  return (
    <Box>

      <PageHeader title="ARTÍCULOS ENVASADOS" />

      <DataTable
        columns={columns}
        data={paginatedData}
        currentPage={currentPage}
        totalPages={totalPages}
        filters={{ search, ...filters }}
        onFilterChange={(f) => {
          setSearch(f.search || "");
          setFilters(f);
          setCurrentPage(1); 
        }}

        onPageChange={setCurrentPage}
        loading={loading}
        emptyMessage="No hay artículos envasados"
      />

    </Box>
  );
}