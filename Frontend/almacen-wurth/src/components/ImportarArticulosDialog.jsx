import React, { useRef, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogActions,
  Box,
  Typography,
  Button,
  CircularProgress,
  IconButton,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from "@mui/material";

import { useAlert } from "./AlertProvider";

import CloseIcon from "@mui/icons-material/Close";

import {
  Upload,
  FileSpreadsheet,
  CheckCircle2,
  Save,
  X,
} from "lucide-react";

import { useAnalisis } from "../hooks/useAnalisis";

export default function ImportarArticulosDialog({ open, onClose, onSuccess }) {

  const { generarPreview, confirmarPreview, cancelarPreview, preview, loading } = useAnalisis();

  const { showAlert } = useAlert();
  const [step, setStep] = useState("upload");
  const [nombreArchivo, setNombreArchivo] = useState("");
  const fileInputRef = useRef(null);

  // SUBIR ARCHIVO Y GENERAR PREVIEW
  const handleFile = async (file) => {
    if (!file) return;

    setNombreArchivo(file.name);

    try {
      await generarPreview(file);
      setStep("preview");
    } catch (error) {
      console.error(error);
      showAlert("Error al procesar archivo", "error");
    }
  };

  const handleGuardar = async () => {
  try {
    await confirmarPreview(preview.previewId);

    showAlert("Artículos importados correctamente", "success");

    // 🔥 avisar al padre
    if (onSuccess) {
      onSuccess();
    }

    handleClose();

  } catch (e) {
    showAlert("Error al guardar", "error");
  }
};

  const handleCancelar = async () => {
    await cancelarPreview(preview?.previewId);
    handleClose();
  };

  const handleClose = () => {
    setStep("upload");
    setNombreArchivo("");
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle
        sx={{
          m: 0,
          p: 3,
          pb: 2,
          display: "flex",
          alignItems: "center",
          gap: 1.5,
        }}
      >
        <Box sx={{ color: "#b91c1c", display: "flex", alignItems: "center" }}>
          <FileSpreadsheet size={24} />
        </Box>

        <Typography
          sx={{
            fontSize: "1.25rem",
            fontWeight: 600,
            color: "#111827",
            flex: 1,
          }}
        >
          Importar Artículos desde Excel
        </Typography>

        <IconButton onClick={handleClose}>
          <CloseIcon sx={{ fontSize: 20 }} />
        </IconButton>
      </DialogTitle>

      <DialogContent sx={{ display: "flex", flexDirection: "column" }}>

        {/* STEP UPLOAD */}
        {step === "upload" && (
          <Box
            sx={{
              flex: 1,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <Box
              onClick={() => !loading && fileInputRef.current?.click()}
              sx={{
                width: "100%",
                border: "2px dashed #cbd5e1",
                borderRadius: 3,
                p: 6,
                textAlign: "center",
                cursor: "pointer",
                "&:hover": { borderColor: "#dc2626" },
              }}
            >
              <input
                ref={fileInputRef}
                type="file"
                accept=".xlsx,.xls,.csv"
                hidden
                onChange={(e) => handleFile(e.target.files[0])}
              />

              {loading ? (
                <Box>
                  <CircularProgress sx={{ color: "#dc2626", mb: 2 }} />
                  <Typography fontWeight={500}>
                    Leyendo archivo {nombreArchivo}...
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Esto puede tardar unos segundos
                  </Typography>
                </Box>
              ) : (
                <Box>
                  <Box
                    sx={{
                      width: 70,
                      height: 70,
                      borderRadius: "50%",
                      backgroundColor: "#fef2f2",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      margin: "0 auto",
                      mb: 2,
                    }}
                  >
                    <FileSpreadsheet size={32} color="#dc2626" />
                  </Box>

                  <Typography fontWeight={600} fontSize={18}>
                    Arrastra tu archivo aquí
                  </Typography>

                  <Typography variant="body2" color="text.secondary" mt={1}>
                    o haz clic para seleccionar
                  </Typography>

                  <Typography
                    variant="caption"
                    color="text.secondary"
                    display="block"
                    mt={2}
                  >
                    Archivos: .xlsx, .xls, .csv
                  </Typography>

                  <Button
                    variant="contained"
                    sx={{
                      mt: 3,
                      bgcolor: "#b91c1c",
                      "&:hover": { bgcolor: "#991b1b" },
                      textTransform: "none",
                    }}
                    startIcon={<Upload size={16} />}
                  >
                    Seleccionar archivo
                  </Button>
                </Box>
              )}
            </Box>
          </Box>
        )}

        {/* STEP PREVIEW */}
        {step === "preview" && preview && (
          <Box>
            <Typography fontWeight={600}>
              Vista previa del archivo:
            </Typography>

            <Typography color="text.secondary" mb={2}>
              {nombreArchivo}
            </Typography>

            <Typography variant="body2">
              Registros válidos: {preview.totalRegistrosValidos}
            </Typography>

            <Typography variant="body2" mb={2}>
              Registros descartados: {preview.totalRegistrosDescartados}
            </Typography>

            {/* TABLA */}
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Código</TableCell>
                  <TableCell>Descripción</TableCell>
                  <TableCell>Cantidad</TableCell>
                  <TableCell>ABC</TableCell>
                </TableRow>
              </TableHead>

              <TableBody>
                {preview.registros.slice(0, 50).map((row, i) => (
                  <TableRow key={i}>
                    <TableCell>{row.codigo}</TableCell>
                    <TableCell>{row.descripcion}</TableCell>
                    <TableCell>{row.cantidad}</TableCell>
                    <TableCell>{row.abc}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Box>
        )}

        {/* STEP DONE */}
        {step === "done" && (
          <Box
            sx={{
              flex: 1,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              flexDirection: "column",
              gap: 2,
            }}
          >
            <Box
              sx={{
                width: 80,
                height: 80,
                borderRadius: "50%",
                backgroundColor: "#dcfce7",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <CheckCircle2 size={40} color="#16a34a" />
            </Box>

            <Typography fontWeight={700} fontSize={20}>
              ¡Importación completada!
            </Typography>
          </Box>
        )}
      </DialogContent>

      {/* FOOTER */}
      {step === "preview" && (
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button
            variant="outlined"
            onClick={handleCancelar}
            startIcon={<X size={16} />}
            sx={{ textTransform: "none", fontWeight: 500,
                            color: "#374151",
                            borderColor: "#e5e7eb",
                            px: 3,
                            py: 1,
                            "&:hover": {
                                borderColor: "#d1d5db",
                                backgroundColor: "#ebebeb",
                            }, }}
          >
            Cancelar
          </Button>

          <Button
            variant="contained"
            startIcon={<Save size={16} />}
            sx={{
              bgcolor: "#b91c1c",
              "&:hover": { bgcolor: "#991b1b" },
              textTransform: "none",
            }}
            onClick={handleGuardar}
          >
            Guardar artículos
          </Button>
        </DialogActions>
      )}
    </Dialog>
  );
}