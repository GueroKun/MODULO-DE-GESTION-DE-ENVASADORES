import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  IconButton,
  Typography,
  Box,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import WarningAmberIcon from "@mui/icons-material/WarningAmber";

export default function ConfirmDialog({
  open,
  onClose,
  onConfirm,
  title = "Confirmar acción",
  message = "¿Estás seguro de realizar esta acción?",
  confirmText = "Confirmar",
  cancelText = "Cancelar",
}) {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      
      {/* 🔴 TÍTULO */}
      <DialogTitle sx={{ m: 0, p: 2, fontWeight: 600 }}>
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <WarningAmberIcon sx={{ color: "#dc2626" }} />
          <Typography fontWeight={600} color="#dc2626">
            {title}
          </Typography>
        </Box>

        <IconButton
          onClick={onClose}
          sx={{ position: "absolute", right: 8, top: 8 }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Typography color="text.secondary">
          {message}
        </Typography>
      </DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <Button
          variant="outlined"
          onClick={onClose}
          sx={{
            textTransform: "none",
            fontWeight: 500,
            color: "#374151",
            borderColor: "#e5e7eb",
            px: 3,
            py: 1,
            "&:hover": {
              borderColor: "#d1d5db",
              backgroundColor: "#ebebeb",
            },
          }}
        >
          {cancelText}
        </Button>

        <Button
          variant="contained"
          onClick={onConfirm}
          sx={{
            textTransform: "none",
            fontWeight: 500,
            backgroundColor: "#dc2626",
            px: 3,
            py: 1,
            boxShadow: "none",
            "&:hover": {
              backgroundColor: "#b91c1c",
              boxShadow: "none",
            },
          }}
        >
          {confirmText}
        </Button>
      </DialogActions>
    </Dialog>
  );
}