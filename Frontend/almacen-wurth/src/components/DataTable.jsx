import React from "react";
import {
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  TableContainer,
  Typography,
  Box,
  IconButton,
  TextField,
} from "@mui/material";

import { ChevronLeft, ChevronRight } from "lucide-react";

export default function DataTable({
  columns,
  data,
  filters = {},
  onFilterChange,
  currentPage = 1,
  totalPages = 1,
  onPageChange,
  emptyMessage = "No hay datos disponibles",
}) {

  const handleFilterChange = (key, value) => {

    if (onFilterChange) {

      onFilterChange({
        ...filters,
        [key]: value,
      });

    }
  };

  return (
    <Paper sx={{ borderRadius: 3, overflow: "hidden", boxShadow: 1 }}>

      <TableContainer>

        <Table>

          {/* HEADER */}
          <TableHead sx={{ bgcolor: "#f8fafc" }}>

            <TableRow>
              {columns.map((col, idx) => (
                <TableCell
                  key={idx}
                  sx={{
                    fontSize: 12,
                    fontWeight: 700,
                    textTransform: "uppercase",
                    color: "#64748b",
                  }}
                >
                  {col.header}
                </TableCell>
              ))}
            </TableRow>

            <TableRow>
              {columns.map((col, idx) => (
                <TableCell key={idx} sx={{ py: 1 }}>
                  {col.accessor && (
                    <TextField
                      variant="outlined"
                      size="small"
                      fullWidth
                      placeholder="Buscar..."
                      value={filters[col.accessor] || ""}
                      onChange={(e) =>
                        handleFilterChange(col.accessor, e.target.value)
                      }
                    />
                  )}
                </TableCell>
              ))}
            </TableRow>

          </TableHead>

          {/* BODY */}
          <TableBody>

            {data.length === 0 ? (

              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  align="center"
                  sx={{ py: 6 }}
                >
                  <Typography color="text.secondary">
                    {emptyMessage}
                  </Typography>
                </TableCell>
              </TableRow>

            ) : (

              data.map((row, rowIdx) => (

                <TableRow
                  key={rowIdx}
                  hover
                  sx={{ "&:last-child td": { borderBottom: 0 } }}
                >

                  {columns.map((col, colIdx) => (
                    <TableCell key={colIdx}>
                      {col.cell ? col.cell(row) : row[col.accessor]}
                    </TableCell>
                  ))}

                </TableRow>

              ))

            )}

          </TableBody>

        </Table>

      </TableContainer>

      {/* PAGINACIÓN */}
      {totalPages > 1 && (

        <Box
          sx={{
            px: 2,
            py: 1.5,
            borderTop: "1px solid #e2e8f0",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            bgcolor: "#f8fafc",
          }}
        >

          <Typography variant="body2" color="text.secondary">
            Página {currentPage} de {totalPages}
          </Typography>

          <Box>

            <IconButton
              onClick={() => onPageChange(currentPage - 1)}
              disabled={currentPage === 1}
            >
              <ChevronLeft size={18} />
            </IconButton>

            <IconButton
              onClick={() => onPageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
            >
              <ChevronRight size={18} />
            </IconButton>

          </Box>

        </Box>

      )}

    </Paper>
  );
}